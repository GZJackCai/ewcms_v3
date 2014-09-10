package com.ewcms.site.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.content.history.History;
import com.ewcms.content.history.model.HistoryModel;
import com.ewcms.content.history.service.HistoryModelService;
import com.ewcms.content.history.util.HistoryUtil;
import com.ewcms.publication.preview.PreviewServiceable;
import com.ewcms.site.dao.ChannelDao;
import com.ewcms.site.dao.TemplateDao;
import com.ewcms.site.model.Channel;
import com.ewcms.site.model.FileType;
import com.ewcms.site.model.Site;
import com.ewcms.site.model.Template;
import com.ewcms.site.model.TemplateEntity;
import com.ewcms.site.util.TemplateUtil;
import com.ewcms.util.ConvertUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author wu_zhijun
 *
 */
@Component
@Transactional(readOnly = true)
public class TemplateService {
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private PreviewServiceable previewService;
	@Autowired
	private HistoryModelService historyModelService;
	
	public Template getTemplate(Long id){
		return templateDao.findOne(id);
	}
	
	@Transactional(readOnly = false)
	@History(modelObjectIndex = 0)	
	public Long addTemplate(Template template){
		if (template.getSite() == null) template.setSite(getCurSite());
		templateDao.save(template);
		return template.getId();
	}
	
	@Transactional(readOnly = false)
	@History(modelObjectIndex = 0)	
	public Long updTemplate(Template template){
		templateDao.save(template);	
		updPubPath(template);
		return template.getId();
	}
	
	/**
	 * 模板目录发生修改，需要更新模板发布路径，并且包括其子模板路径
	 * 
	 * @param channel 模板
	 */
	private void updPubPath(final Template vo) {
		for (Template child : templateDao.getTemplateChildren(vo.getId(), getCurSite().getId(), null)) {
			child.setPath(null);
			updPubPath(child);
		}
		templateDao.save(vo);
	}
	
	@Transactional(readOnly = false)
	public void delTemplateBatch(List<Long> idList){
		for(Long id :idList){
			delTemplate(id);
		}		
	}
	
	@Transactional(readOnly = false)
	public void delTemplate(Long id){
		delTemplateChildren(id);
		templateDao.delete(id);
	}
	
	@Transactional(readOnly = false)
	private void delTemplateChildren(Long id){
		List<Template> templates = templateDao.getTemplateChildren(id, getCurSite().getId(), null);
		if (templates != null && !templates.isEmpty()){
			for (Template template : templates){
				List<Template> templateChildrens = templateDao.getTemplateChildren(template.getId(), getCurSite().getId(), null);
				if (templateChildrens != null && !templateChildrens.isEmpty()){
					delTemplateChildren(template.getId());
				}else{
					templateDao.delete(template);
				}
			}
		}
	}
	
	public List<Template> getTemplateList(){
		return templateDao.getTemplateList(getCurSite().getId());
	}
	/**
	 * 获取跟模板集
	 * 
	 */ 	
	public List<Template> getTemplaeTreeList(Boolean channelEnable){
		return getTemplateChildren(null,channelEnable,null);
	}
	
	/**
	 * 获取模板子模板集
	 * 
	 */ 	
	public List<Template> getTemplaeTreeList(Long parentId,Boolean channelEnable){
		return getTemplateChildren(parentId,channelEnable,null);
	}

	/**
	 * 获取模板子模板集及 某个专栏模板
	 * 
	 */ 	
	public List<Template> getTemplaeTreeList(Long parentId,String channelName){
		return getTemplateChildren(parentId,true,channelName);
	}
	
	/**
	 * 获取模板子模板
	 * 
	 */ 	
    private List<Template> getTemplateChildren(Long parentId,Boolean channelEnable,String channelName){
        List<Template> tplList = getTemplateChildren(parentId,channelName);
        List<Template> validateList = new ArrayList<Template>();
        for(Template vo:tplList){
        	if(!channelEnable&&vo.getName().equals(getSiteTplName())){//屏蔽所有专栏模板
        		continue;
        	}
        	if(channelName!=null && parentId!=null){//屏蔽其它专栏模板,只显示某个专栏模板
        		if(getTemplate(parentId).getName().equals(getSiteTplName())&&!vo.getName().equals(channelName)){
        			continue;
        		}
        	}
        	validateList.add(vo);
        }
        return validateList;
    }
	
	private List<Template> getTemplateChildren(Long parentId,String channelName){
		if(channelName!=null && getTemplate(parentId).getName().equals(channelName))
			return templateDao.getTemplateChildren(parentId,EwcmsContextUtil.getCurrentSite().getId(),Long.valueOf(channelName));
		return templateDao.getTemplateChildren(parentId,EwcmsContextUtil.getCurrentSite().getId(),null);
	}  
	/**
	 * 获取站点专栏模板根目录
	 * 
	 */    
    public Template channelTPLRoot(){
    	return channelTemplate(null);
    }
	/**
	 * 获取模板唯一路径
	 * 
	 */      
    public String getTemplateUniquePath(Long siteId,Long channelId,String templateName){
    	return siteId.toString()+"/"+ siteId.toString()+"tpl/"+channelId.toString()+"/"+templateName;
    }
    
	/**
	 * 获取站点专栏模板目录
	 * 
	 */     
	@Transactional(readOnly = false)
    public Template channelTemplate(String tplName){
    	if(tplName==null||tplName.length()==0){
        	Template template = templateDao.getChannelTemplate(getSiteTplName(),getCurSite().getId(),null);
        	if(template == null){//没有站点专栏模板节点，就创建
        		template = new Template();
        		template.setDescribe(getCurSite().getSiteName()+"专栏模板目录");
        		template.setName(getSiteTplName());
        		template.setSite(getCurSite());
        		template.setPath(getSiteTplName());
        		template.setSize("0KB");
        		templateDao.save(template);
        	} 
        	return template;
    	}else{
    		Long parentId = channelTPLRoot().getId();
    		Template template = templateDao.getChannelTemplate(tplName,getCurSite().getId(),parentId);
        	if(template == null){//没有站点专栏模板节点，就创建
        		template = new Template();
        		template.setDescribe(tplName+"专栏模板目录");
        		template.setName(tplName);
        		template.setSite(getCurSite());
        		template.setPath(getSiteTplName()+"/"+tplName);
        		template.setSize("0KB");
        		template.setParent(channelTPLRoot());
        		templateDao.save(template);
        	} 
        	return template;
    	}
    }
    
	private Site getCurSite(){
		return EwcmsContextUtil.getCurrentSite();
	}
	
	private String getSiteTplName(){
		return getCurSite().getId()+"tpl";
	}
	
	public List<Template> getTemplatesInChannel(Long id) {
		return templateDao.getTemplatesInChannel(id);
	}
	
	public Template getTemplateByUniquePath(String path) {
		return templateDao.getTemplateByPath(path);
	}
	
	public String getUniquePathOfChannelTemplate(Long siteId, Long channelId, String name) {
		return siteId.toString()+"/"+siteId.toString()+"tpl/"+channelId.toString()+"/"+name;
	}
	
	public void saveAppChild(Long channelId, List<Long> templateIds, Boolean cover) {
		List<Template> templates = new ArrayList<Template>();
		for (Long templateId : templateIds){
			Template template = templateDao.findOne(templateId);
			templates.add(template);
		}
		templateChild(channelId, channelId, templates, cover);
	}
	
	private void templateChild(Long initChannelId, Long channelId, List<Template> templates, Boolean cover){
		List<Channel> channels = channelDao.getChannelChildren(channelId);
		if (channels != null && !channels.isEmpty()){
			for (Channel channel : channels){
				for (Template template : templates){
					Template dbTemplate = templateDao.findTemplateByChannelIdAndTemplateType(channel.getId(), template.getType());

					TemplateEntity templateEntity = template.getTemplateEntity();
					TemplateEntity newTemplateEntity = new TemplateEntity();
					if (dbTemplate != null){
						if (!cover) continue;
						if (templateEntity != null && templateEntity.getTplEntity() != null && templateEntity.getTplEntity().length != 0){
							newTemplateEntity.setTplEntity(templateEntity.getTplEntity());
							dbTemplate.setSize(ConvertUtil.kb(templateEntity.getTplEntity().length));
							dbTemplate.setTemplateEntity(newTemplateEntity);
							try{
								updTemplate(dbTemplate);
							}catch(Exception e){
							}
						}
					}else{
						Template newTemplate = new Template();
						newTemplate.setUpdTime(new Date(Calendar.getInstance().getTime().getTime()));
						newTemplate.setChannelId(channel.getId());
						if (templateEntity != null && templateEntity.getTplEntity() != null && templateEntity.getTplEntity().length != 0){
							newTemplateEntity.setTplEntity(templateEntity.getTplEntity());
							newTemplate.setSize(ConvertUtil.kb(templateEntity.getTplEntity().length));
						}else{
							newTemplate.setSize(ConvertUtil.kb(0));
						}
						
						newTemplate.setTemplateEntity(newTemplateEntity);
						newTemplate.setSite(template.getSite());
						newTemplate.setDescribe(template.getDescribe());
						newTemplate.setType(template.getType());
						
						String name = template.getName();
						String newName = "app_" + initChannelId + "_" + name;
						
						newTemplate.setName(newName);
						newTemplate.setParent(channelTemplate(channel.getId().toString()));
						try{
							addTemplate(newTemplate);
						}catch(Exception e){
						}
					}
				}
				templateChild(initChannelId, channel.getId(), templates, cover);
			}
		}
	}

	public void exportTemplateZip(Long templateId, ZipOutputStream zos, String templatePath) {
		try{
			Template template = getTemplate(templateId);
			if (template == null) return;

			TemplateEntity templateEntity = template.getTemplateEntity();
			
			String filePath = templatePath + template.getName();
			ZipEntry zipEntry;
			if (templateEntity == null){
				filePath += "/";
				
				//创建栏目目录
				zipEntry = new ZipEntry(filePath);
				zipEntry.setUnixMode(755);
				zos.putNextEntry(zipEntry);
				
				List<Template> templateChildrens = templateDao.getTemplateChildren(template.getId(), getCurSite().getId(), null);
				
				for (Template templateChildren : templateChildrens){
					exportTemplateZip(templateChildren.getId(), zos, filePath);
				}
			}else{
				zipEntry = new ZipEntry(filePath);
				zipEntry.setUnixMode(644);
				zos.putNextEntry(zipEntry);
				
				BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(templateEntity.getTplEntity()));
				int b;
				while((b = bis.read())!=-1){  
					zos.write(b);  
		        }  
				
				zos.closeEntry();
				bis.close();
			}
		}catch(Exception e){
			
		}
	}

	@Transactional(readOnly = false)
	public Boolean restoreTemplate(Long templateId, Long historyId) {
		Boolean result = false;
		try{
			HistoryModel historyModel = historyModelService.findByHistoryModel(historyId);
			String className = Template.class.getPackage().getName() + "." + Template.class.getSimpleName();
			if (className.equals(historyModel.getClassName())){
				Template oldTemplate = templateDao.findOne(templateId);
				TemplateEntity oldTemplateEntity = oldTemplate.getTemplateEntity();
				Object obj = HistoryUtil.conversion(historyModel.getModelObject());
				Template template = (Template) obj;
				TemplateEntity templateEntity = template.getTemplateEntity();
				if (templateEntity == null) return result;
				if (oldTemplateEntity == null) oldTemplateEntity = new TemplateEntity();
				oldTemplateEntity.setTplEntity(templateEntity.getTplEntity());
				templateDao.save(oldTemplate);
				result = true;
			}
		}catch (Exception e){
		}
		return result;
	}
	
	public StringBuffer findUseChannel(Long channelId, Long siteId){
		HashSet<Long> appChannelIdSets = new HashSet<Long>();
		List<Long> appChannelIds = new ArrayList<Long>();
		
		Channel channel = channelDao.findOne(channelId);
		String absUrl = channel.getAbsUrl();
		
		List<Template> templates = templateDao.getTemplateList(siteId);
		for (Template template : templates){
			String tplEntity = new String(template.getTemplateEntity().getTplEntity());
			List<String> results = TemplateUtil.associate(tplEntity);
			for (String result : results){
				if (result.indexOf(TemplateUtil.CHILD_ATTRIB) != -1){
					String[] expression = result.split(":");
					try{
						String channelResult = expression[0];
						if (channelResult == null || channelResult.length() == 0) continue; 
						if (channelResult.indexOf("[") == 0 && channelResult.indexOf("]") == channelResult.length() - 1){
							channelResult = channelResult.substring(1, channelResult.length() - 1);
							String[] childExpressions = channelResult.split(",");
							for (String childExpression : childExpressions){
								if (TemplateUtil.isNumeric(childExpression)){
									List<Channel> childChannels = channelDao.getChannelChildren(Long.valueOf(childExpression));
									for (Channel childChannel : childChannels){
										if (appChannelIdSets.add(childChannel.getId())) appChannelIds.add(childChannel.getId());
									}
								}else{
									Channel childChannel = channelDao.getChannelByURL(siteId, childExpression.substring(1));
									if (appChannelIdSets.add(childChannel.getId())) appChannelIds.add(childChannel.getId());
								}
							}
						}else{
							if (result.equals(absUrl) || result.equals(String.valueOf(channelId.intValue()))){
								if (appChannelIdSets.add(template.getChannelId())) appChannelIds.add(template.getChannelId());
							}
						}
					}catch(Exception e){
						
					}
				}else{
					if (result.equals(absUrl) || result.equals(String.valueOf(channelId.intValue()))){
						if (appChannelIdSets.add(template.getChannelId())) appChannelIds.add(template.getChannelId());
					}
				}
			}
		}
		
		StringBuffer appChannelId = new StringBuffer();
		
		if (appChannelIds.size() > 0){
			Collections.sort(appChannelIds, new ChannelIdAscComparator());
			int appChannelIdSize = appChannelIds.size();
			for (int i = 0; i < appChannelIdSize - 1; i++){
				if (channelId.intValue() == appChannelIds.get(i).intValue()) continue;
				appChannelId.append(appChannelIds.get(i) + ",");
			}
			appChannelId.append(appChannelIds.get(appChannelIdSize - 1));
		}
		
		return appChannelId;
	}
	
	@Transactional(readOnly = false)
	public void connectChannel(Long channelId){
		updAppChannel(channelId);
	}
	
	@Transactional(readOnly = false)
	public void disConnectChannel(Long channelId){
		Channel channel = channelDao.findOne(channelId);
		channel.setAppChannel(null);
		channelDao.save(channel);
	}
	
	@Transactional(readOnly = false)
	public Boolean verify(Long templateId){
		Template template = templateDao.findOne(templateId);
		if (template.getType() == null) return false;
		return previewService.verifyTemplate(template.getSite().getId(), template.getChannelId(), template.getId());
//		template.setIsVerify(result);
//		templateDao.save(template);
	}
	
	private void updAppChannel(Long channelId){
		StringBuffer channelIds = findUseChannel(channelId, getCurSite().getId());
		Channel channel = channelDao.findOne(channelId);
		channel.setAppChannel(channelIds.toString());
		channelDao.save(channel);
	}
	
	class ChannelIdAscComparator implements Comparator<Long>{
		@Override
		public int compare(Long o1, Long o2) {
			if (o1 > o2){
				return 1;
			}else{
				if (o1 == o2)
					return 0;
				else
					return -1;
			}
		}
	}
	
	public Map<String, Object> searchTemplate(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, templateDao, Template.class);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void unZipTemplateFile(Long templateId, File file){
		try {
			ZipFile zfile = new ZipFile(file);
			Enumeration<ZipEntry> zList = zfile.getEntries();
			Map<String, Long> dirMap = new HashMap<String, Long>();
			ZipEntry ze = null;
			String[] pathArr;
			while (zList.hasMoreElements()) {
				try {
					ze = (ZipEntry) zList.nextElement();
					Template template = new Template();
					
					template.setSite(EwcmsContextUtil.getCurrentSite());
					
					pathArr = ze.getName().split("/");
					template.setFileType(FileType.FILE);
					template.setName(pathArr[pathArr.length - 1]);
					String pathKey = ze.getName().substring(0, ze.getName().lastIndexOf(pathArr[pathArr.length - 1]));
					if (pathKey == null || pathKey.length() == 0) {
						if (templateId == null) {
							template.setParent(null);
						} else {
							template.setParent(getTemplate(templateId));
						}
					} else {
						template.setParent(getTemplate(dirMap.get(pathKey)));
					}

					if (ze.isDirectory()) {
						template.setFileType(FileType.DIRECTORY);
						dirMap.put(ze.getName(), addTemplate(template));
						continue;
					}

					InputStream in = new BufferedInputStream(zfile.getInputStream(ze));
					byte[] buffer = new byte[Integer.parseInt(String.valueOf(ze.getSize()))];
					in.read(buffer);
					TemplateEntity tplEntityVo = new TemplateEntity();
					tplEntityVo.setTplEntity(buffer);
					template.setTemplateEntity(tplEntityVo);
					addTemplate(template);
					in.close();
				} catch (Exception e) {
				}
			}
			zfile.close();
		} catch (Exception e) {
		}
	}
	
	public Template findByIdAndChannelId(Long templateId, Long channelId){
		return templateDao.findByIdAndChannelId(templateId, channelId);
	}
	
	public Map<String, Object> searchTemplateHistory(Long templateId, QueryParameter params){
		Map<String, Object> result = historyModelService.searchTemplate(params, templateId);
    	return HistoryUtil.resolve(result);
	}
}
