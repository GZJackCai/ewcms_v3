package com.ewcms.site.service;

import static com.ewcms.util.EmptyUtil.isNotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ewcms.security.acl.annotation.AclEnum;
import com.ewcms.security.acl.model.AclEntry;
import com.ewcms.security.acl.model.AclIdEntity;
import com.ewcms.security.acl.service.AclService;
import com.ewcms.security.acl.util.AclUtil;
import com.ewcms.site.dao.ChannelDao;
import com.ewcms.site.dao.TemplateDao;
import com.ewcms.site.dao.TemplateSourceDao;
import com.ewcms.site.model.Channel;
import com.ewcms.site.model.Site;
import com.ewcms.site.model.Template;
import com.ewcms.site.model.TemplateSource;
import com.ewcms.util.Collections3;
import com.ewcms.util.ConvertUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;
import com.ewcms.web.util.ChannelNode;
import com.ewcms.publication.PublishException;
import com.ewcms.publication.PublishServiceable;

/**
 * 栏目管理
 * 
 * @author wu_zhijun
 * 
 */
@Component
@Transactional(readOnly = true)
public class ChannelService {
	
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private TemplateSourceDao templateSourceDao;
	@Autowired
	private PublishServiceable publishService;
	@Autowired
	private AclService aclService;
	
	public List<ChannelNode> getChannelChildren(Long id, Boolean publicenable, Boolean isArticle) {
		List<ChannelNode> nodes = new ArrayList<ChannelNode>();
		List<Channel> channels = channelDao.getChannelChildren(id);
		String loginName = EwcmsContextUtil.getLoginName();
		List<String> roleNames = EwcmsContextUtil.getRoleNames(); 
		for (Channel channel : channels) {
			if (publicenable && !channel.getPublicenable()) continue;
			
			Long channelId = channel.getId();
			
			String permission = "";
			if (isArticle){
				permission = AclUtil.getPermissionExpression(AclEnum.VIEW_ARTICLE, channelId);
			}else{
				permission = AclUtil.getPermissionExpression(AclEnum.VIEW_CHANNEL, channelId);
			}
			
			Integer mask = 0;
			if (loginName.equals("admin") || Collections3.convertToString(roleNames, ",").indexOf("ROLE_ADMIN") > 0){
				mask = 9;
			}else{
				if (SecurityUtils.getSubject().isPermitted(permission)){
					mask = aclService.findByMaxMask(channelId, Channel.class.getName().toString(), loginName, roleNames);
				}
			}
			ChannelNode node = new ChannelNode(channel, mask);
			nodes.add(node);
		}
		return nodes;
	}
	
	@Transactional(readOnly = false)
	public void saveAcl(Channel channel, String sid, Boolean principal, Integer mask){
		aclService.saveAclEntry(channel, Channel.class.getName().toString(), sid, principal, mask);
	}
	
	@Transactional(readOnly = false)
	public void deleteAcl(Long channelId, String sid, Boolean principal){
		aclService.deleteAclEntry(Channel.class.getName().toString(), channelId, sid, principal);
	}
	
	@Transactional(readOnly = false)
	public void inheritingAcl(Long channelId, Boolean inheriting){
		aclService.inheriting(Channel.class.getName().toString(), channelId, inheriting);
	}
	
	public List<AclEntry> findByAclChannel(Long channelId){
		return aclService.findByObjectId(channelId);
	}

	/**
	 * 初始站点专栏访问权限
	 * 
	 * @param channel
	 *            站点专栏
	 * @param createCommon
	 *            创建通用权限(true:创建通用权限)
	 * @param inherit
	 *            继承父站点专栏权限
	 */
	private void initAclOfChannel(Channel channel, boolean createCommon, boolean inherit) {
		String className = Channel.class.getName().toString();
		
		if (createCommon){
			aclService.saveAclEntry(channel, className, "ROLE_USER", false, 1);
			aclService.saveAclEntry(channel, className, "ROLE_WRITER", false, 2);
			aclService.saveAclEntry(channel, className, "ROLE_EDITOR", false, 5);
			aclService.saveAclEntry(channel, className, "admin", true, 9);
		}
	}

	@Transactional(readOnly = false)
	public ChannelNode channelNodeRoot() {
		Channel channel = channelDao.getChannelRoot(getCurSite().getId());
		if (channel == null) {
			channel = new Channel();
			channel.setDir("");
			channel.setPubPath("");
			channel.setAbsUrl("");
			channel.setUrl("");
			channel.setName(getCurSite().getSiteName());
			channel.setSite(getCurSite());
			channelDao.save(channel);
			initAclOfChannel(channel, true, true);
			//aclEntityDao.save(aclEntity);
		}
		return new ChannelNode(channel, 1);
	}

	
	public Channel getChannelRoot() {
		return channelDao.getChannelRoot(getCurSite().getId());
	}

	@Transactional(readOnly = false)
	public Long addChannel(Long parentId, String name) {
		Assert.notNull(parentId, "parentId is null");
		Channel channel = new Channel();
		channel.setName(name);
		channel.setParent(channelDao.findOne(parentId));
		channel.setSite(getCurSite());
		channel.setDir(ConvertUtil.pinYin(name));
		channel.setSort(channelDao.findMaxSiblingChannel(parentId) + 1);
		channelDao.save(channel);
		initAclOfChannel(channel, true, true);
		return channel.getId();
	}

	@Transactional(readOnly = false)
	public void renameChannel(Long id, String name) {
		Channel vo = getChannel(id);
		vo.setName(name);
		channelDao.save(vo);
	}

	@Transactional(readOnly = false)
	public Long updChannel(Channel channel) {
		channelDao.save(channel);
		updAbsUrlAndPubPath(channel.getId(), getCurSite().getId());
		return channel.getId();
	}

	/**
	 * 更新子专栏的AbsUrl和PubPath
	 * 
	 * 专栏的目录和链接地址发生改变,则子站点专栏的absUrl和pubPath也要发生改变。
	 * 
	 * @param channelId
	 *            专栏编号
	 * @param siteId
	 *            站点编号
	 */
	private void updAbsUrlAndPubPath(Long channelId, Long siteId) {
		Channel channel = channelDao.findOne(channelId);
		if (channel == null || channel.getSite().getId() != siteId) {
			return;
		}
		List<Channel> children = channelDao.getChannelChildren(channelId);
		for (Channel child : children) {
			child.setAbsUrl(null);
			child.setPubPath(null);
			channelDao.save(child);
			updAbsUrlAndPubPath(child.getId(), siteId);
		}
	}

	@Transactional(readOnly = false)
	public void delChannel(Long id) {
		channelDao.delete(id);
	}

	public Channel getChannel(Long id) {
		if (id == null){
			return getChannelRoot();
		}else{
			return channelDao.findOne(id);
		}
	}

	private Site getCurSite() {
		return EwcmsContextUtil.getCurrentSite();
	}

	public Channel getChannelRoot(Long siteId) {
		return channelDao.getChannelRoot(siteId);
	}

	public Channel getChannel(Long siteId, Long id) {
		return this.getChannel(id);
	}

	public List<Channel> getChannelChildren(Long id) {
		return channelDao.getChannelChildren(id);
	}

	public Channel getChannelByUrlOrPath(Long siteId, String path) {
		return channelDao.getChannelByURL(siteId, path);
	}

	public void forceRelease(Long siteId, Long channelId, Boolean children) throws PublishException {
		if (isNotNull(channelId)) {
			publishService.pubChannel(siteId, channelId, true, children);
		}
	}

	@Transactional(readOnly = false)
	public void downChannel(Long channelId, Long parentId) {
		if (channelId != null && parentId != null) {
			Long maxSort = channelDao.findMaxSiblingChannel(parentId);
			Channel channel = channelDao.findOne(channelId);
			if (channel.getSort().longValue() != maxSort.longValue()) {
				Long sort = channel.getSort();
				sort = sort + 1;

				Channel channel_prev = channelDao.findChannelByParentIdAndSort(parentId, sort);

				channel.setSort(sort);
				channel_prev.setSort(sort - 1);

				channelDao.save(channel);
				channelDao.save(channel_prev);
			}
		}
	}

	@Transactional(readOnly = false)
	public void upChannel(Long channelId, Long parentId) {
		if (channelId != null && parentId != null) {
			Channel channel = channelDao.findOne(channelId);
			if (channel.getSort().longValue() > 1) {
				Long sort = channel.getSort();
				sort = sort - 1;
				Channel channel_prev = channelDao.findChannelByParentIdAndSort(parentId, sort);

				channel.setSort(sort);
				channel_prev.setSort(sort + 1);

				channelDao.save(channel);
				channelDao.save(channel_prev);
			}
		}
	}

	@Transactional(readOnly = false)
	public void moveSortChannel(Long channelId, Long parentId, Long sort) {
		if (channelId != null && parentId != null) {
			Channel moveChannel = channelDao.findOne(channelId);

			Long oldSort = moveChannel.getSort();
			if (sort.longValue() == oldSort.longValue())
				return;

			moveChannel.setSort(sort);

			if (sort.longValue() > oldSort.longValue()) {
				List<Channel> channels = channelDao.findChannelByParentIdAndLtSort(channelId, parentId, sort, oldSort);
				if (channels == null || channels.isEmpty())
					return;

				for (Channel channel : channels) {
					sort--;
					channel.setSort(sort);
					channelDao.save(channel);
				}
			} else {
				List<Channel> channels = channelDao.findChannelByParentIdAndGtSort(channelId, parentId, sort, oldSort);
				if (channels == null || channels.isEmpty())
					return;

				for (Channel channel : channels) {
					sort++;
					channel.setSort(sort);
					channelDao.save(channel);
				}
			}

			channelDao.save(moveChannel);
		}
	}

	@Transactional(readOnly = false)
	public void moveToChannel(Long channelId, Long parentId) {
		Channel moveChannel = channelDao.findOne(channelId);
		Long sort = moveChannel.getSort();
		Long moveParentId = moveChannel.getParent().getId();

		Channel targetParentChannel = channelDao.findOne(parentId);
		Long maxSort = channelDao.findMaxSiblingChannel(parentId);
		moveChannel.setParent(targetParentChannel);
		moveChannel.setSort(maxSort + 1);

		List<Channel> channels = channelDao.findChannelByGreaterThanSort(moveParentId, sort);
		if (channels != null && !channels.isEmpty()) {
			for (Channel channel : channels) {
				channel.setSort(channel.getSort() - 1);
				channelDao.save(channel);
			}
		}

		updChannel(moveChannel);
	}

	@Transactional(readOnly = false)
	public void exportChannelZip(Long channelId, ZipOutputStream zos, String channelPath) {
		try {
			Channel channel = getChannel(channelId);
			if (channel == null)
				return;

			// 创建栏目目录
			String filePath = channelPath + channel.getName() + "/";
			ZipEntry zipEntry = new ZipEntry(filePath);
			zipEntry.setUnixMode(755);
			zos.putNextEntry(zipEntry);

			// 创建栏目目录下一级的“模板”目录
			String templatePath = filePath + "模板/";
			zipEntry = new ZipEntry(templatePath);
			zipEntry.setUnixMode(755);
			zos.putNextEntry(zipEntry);

			// 创建栏目目录下一级的“资源”目录
			String templateSourcePath = filePath + "资源/";
			zipEntry = new ZipEntry(templateSourcePath);
			zipEntry.setUnixMode(755);
			zos.putNextEntry(zipEntry);

			List<Template> templates = templateDao.getTemplatesInChannel(channelId);
			List<TemplateSource> templateSources = templateSourceDao.getTemplateSourceInChannel(channelId);

			for (Template template : templates) {
				if (template == null || template.getTemplateEntity() == null)
					continue;
				String fileName = template.getName();

				zipEntry = new ZipEntry(templatePath + fileName);
				zipEntry.setUnixMode(644);

				zos.putNextEntry(zipEntry);

				BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(template.getTemplateEntity().getTplEntity()));
				int b;
				while ((b = bis.read()) != -1) {
					zos.write(b);
				}

				zos.closeEntry();
				bis.close();
			}

			for (TemplateSource templateSource : templateSources) {
				if (templateSource == null || templateSource.getSourceEntity() == null)
					continue;
				String fileName = templateSource.getName();

				zipEntry = new ZipEntry(templateSourcePath + fileName);
				zipEntry.setUnixMode(644);
				zos.putNextEntry(zipEntry);

				BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(templateSource.getSourceEntity().getSrcEntity()));
				int b;
				while ((b = bis.read()) != -1) {
					zos.write(b);
				}

				zos.closeEntry();
				bis.close();
			}

			List<Channel> childrenChannels = getChannelChildren(channelId);
			if (childrenChannels != null) {
				for (Channel childrenChannel : childrenChannels) {
					exportChannelZip(childrenChannel.getId(), zos, filePath);
				}
			}
		} catch (Exception e) {
		}
	}

	public List<Long> findAppChannel(Long channelId) {
		List<Long> channelIds = new ArrayList<Long>();
		Channel channel = channelDao.findOne(channelId);
		String appChannel = channel.getAppChannel();
		if (appChannel != null && appChannel.length() > 0) {
			String[] appChannelIds = appChannel.split(",");
			for (String appChannelId : appChannelIds) {
				try {
					Channel vo = channelDao.findOne(Long.valueOf(appChannelId));
					if (vo != null && vo.getId() != null) {
						channelIds.add(vo.getId());
					}
				} catch (Exception e) {
				}
			}
		}

		return channelIds;
	}

	@Transactional(readOnly = false)
	public void delAppChannel(Long channelId, Long appChannelId) {
		List<Long> appChannelIds = findAppChannel(channelId);
		if (!appChannelIds.isEmpty()) {
			appChannelIds.remove(appChannelId);
			Channel channel = channelDao.findOne(channelId);
			if (appChannelIds.isEmpty()) {
				channel.setAppChannel(null);
			} else {
				StringBuffer sb = new StringBuffer();
				int appChannelIdSize = appChannelIds.size();
				for (int i = 0; i < appChannelIdSize - 1; i++) {
					if (channelId.intValue() == appChannelIds.get(i).intValue())
						continue;
					sb.append(appChannelIds.get(i) + ",");
				}
				sb.append(appChannelIds.get(appChannelIdSize - 1));
				channel.setAppChannel(sb.toString());
			}
			channelDao.save(channel);
		}
	}

	public Channel getChannelParent(Long id) {
		Channel channel = channelDao.findOne(id);
		if (channel != null && channel.getParent() != null)
			return channel.getParent();
		return null;
	}
	
	public AclIdEntity findByClassNameAndObject(String className, Long objectId){
		return aclService.findByClassNameAndObjectId(className, objectId);
	}
	
	public Map<String, Object> searchApply(Long channelId, QueryParameter params){
		List<Long> applys = findAppChannel(channelId);
		
		Map<String, Object> parameters = params.getParameters();
		parameters.put("IN_id", applys);
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("id",  Direction.ASC);
		params.setSorts(sorts);
		
		return SearchMain.search(params, "IN_id", Long.class, channelDao, Channel.class);
	}
}
