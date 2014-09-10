/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.service;

import static com.ewcms.util.EmptyUtil.isNotNull;
import static com.ewcms.util.EmptyUtil.isNull;
import static com.ewcms.util.EmptyUtil.isStringEmpty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.content.document.dao.ArticleDao;
import com.ewcms.content.document.dao.ArticleMainDao;
import com.ewcms.content.document.dao.ReviewProcessDao;
import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.model.ArticleMain;
import com.ewcms.content.document.model.Article.Status;
import com.ewcms.content.document.model.Article.Genre;
import com.ewcms.content.document.model.Content;
import com.ewcms.content.document.model.ReviewRole;
import com.ewcms.content.document.model.ReviewProcess;
import com.ewcms.content.document.model.ReviewUser;
import com.ewcms.content.document.util.search.ExtractKeywordAndSummary;
import com.ewcms.content.history.History;
import com.ewcms.content.history.model.HistoryModel;
import com.ewcms.content.history.service.HistoryModelService;
import com.ewcms.content.history.util.HistoryUtil;
import com.ewcms.content.message.push.PushApiService;
import com.ewcms.plugin.BaseException;
import com.ewcms.publication.PublishException;
import com.ewcms.publication.PublishServiceable;
import com.ewcms.publication.deploy.operator.FileOperatorable;
import com.ewcms.security.model.User;
import com.ewcms.security.service.AccountService;
import com.ewcms.site.dao.ChannelDao;
import com.ewcms.site.dao.TemplateDao;
import com.ewcms.site.model.Channel;
import com.ewcms.site.model.Template;
import com.ewcms.util.Collections3;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.util.HtmlStringUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author 吴智俊
 */
@Component
public class ArticleMainService{

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ArticleMainDao articleMainDao;
	@Autowired
	private ReviewProcessDao reviewProcessDao;
	@Autowired
	private OperateTrackService operateTrackService;
	@Autowired
	private PublishServiceable publishService;
	@Autowired
	private FileOperatorable localFileOperator;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private TemplateDao templateDao;
	@Autowired
	private AccountService accountService;
	@Autowired
	private HistoryModelService historyModelService;
	@Autowired
	private PushApiService pushApiService;
//	@Autowired
//	private RelationService relationService;
	
	public ArticleMain findArticleMainByArticleMainAndChannel(Long articleMainId, Long channelId) {
		return articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
	}

	public void delArticleMainToRecycleBin(Long articleMainId, Long channelId) {
		ArticleMain articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
		Assert.notNull(articleMain);
		Article article = articleMain.getArticle();
		Assert.notNull(article);
		Status oldStatus = article.getStatus();
		
		if (isNotNull(articleMain.getIsReference()) && articleMain.getIsReference()){
			articleMain.setArticle(null);
			articleMainDao.delete(articleMain);
		}else{
			if (isNotNull(articleMain.getIsShare()) && articleMain.getIsShare()){
				List<ArticleMain> articleMains = articleMainDao.findArticleMainByArticleIdAndReference(article.getId(), true);
				if (articleMains != null && !articleMains.isEmpty()){
					for (ArticleMain vo : articleMains){
						if (isNotNull(vo.getIsReference()) && vo.getIsReference()){
							vo.setArticle(null);
							articleMainDao.delete(vo);
						}
					}
				}
			}
			
			operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(), "放入内容回收站。", "");
				
			article.setStatus(Status.REEDIT);
			article.setIsDelete(true);
			articleMain.setArticle(article);
			articleMainDao.save(articleMain);
		}
		
		if (oldStatus == Status.RELEASE){
			try {
				localFileOperator.delete(article.getUrl());
			} catch (PublishException e) {
			}
			try {
				associateRelease(channelId);
			} catch (PublishException e) {
			}
		}
	}

	public void restoreArticleMain(Long articleMainId, Long channelId) {
		ArticleMain articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
		Assert.notNull(articleMain);
		Article article = articleMain.getArticle();
		Assert.notNull(article);
		article.setIsDelete(false);
		articleMain.setArticle(article);
		articleMainDao.save(articleMain);
		
		operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(), "从内容回收站还原。", "");
	}

	public void submitReviewArticleMain(String userName, List<Long> articleMainIds, Long channelId) throws BaseException {
		for (Long articleMainId : articleMainIds){
			ArticleMain articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
			Assert.notNull(articleMain);
			Article article = articleMain.getArticle();
			Assert.notNull(article);
			if (article.getStatus() == Status.DRAFT || article.getStatus() == Status.REEDIT) {
				ReviewProcess reviewProcess = reviewProcessDao.findFirstReviewProcessByChannel(channelId);
				if (reviewProcess == null ){
					operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(), "发布版。", "");
	
					article.setStatus(Status.PRERELEASE);
					article.setReviewProcess(null);
				}else{
					List<String> roleNameList = EwcmsContextUtil.getRoleNames();
					Boolean isAdmin = false;
					String roleNames = Collections3.convertToString(roleNameList, ",");
					
					if (roleNames.toUpperCase().indexOf("ROLE_ADMIN") > 0){
						operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(), "管理员直接跳过流程进入发布版。", "");
							
						article.setStatus(Status.PRERELEASE);
						article.setReviewProcess(null);
						isAdmin = true;
					}

					if (!isAdmin){
						operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(),"已提交到【" + reviewProcess.getName() + "】进行审核。", "");
		
						article.setStatus(Status.REVIEW);
						article.setReviewProcess(reviewProcess);
					}
				}
				if (article.getPublished() == null) {
					article.setPublished(new Date(Calendar.getInstance().getTime().getTime()));
				}
				articleMain.setArticle(article);
				articleMainDao.save(articleMain);
				
				pushApiService.pushTodo(userName, findBeApprovalArticleMain(userName));
//			}else{
//				throw new BaseException("","只有在初稿或重新编辑状态下才能提交审核");
			}
		}
	}

	public Boolean copyArticleMainToChannel(List<Long> articleMainIds, List<Long> targetChannelIds, Long channelId) {
		ArticleMain articleMain = null;
		Article article = null;
		Channel channel = channelDao.findOne(channelId);
		Assert.notNull(channel);
		String source_channelName = channel.getName();
		for (Long targetChannelId : targetChannelIds) {
			for (Long articleMainId : articleMainIds) {
				articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
				if (isNull(articleMain)) continue;
				if (isNotNull(articleMain.getIsReference()) && articleMain.getIsReference()) continue;
				article = articleMain.getArticle();
				if (isNull(article)) continue;
				if (targetChannelId != channelId) {
					Article target_article = new Article();

					target_article.setStatus(Status.DRAFT);
					target_article.setPublished(null);
					if (article.getGenre() == Genre.TITLE){
						target_article.setUrl(article.getUrl());
					}else{
						target_article.setUrl(null);
					}
					target_article.setIsDelete(article.getIsDelete());

					List<Content> contents = article.getContents();
					List<Content> contents_target = new ArrayList<Content>();
					for (Content content : contents) {
						Content content_target = new Content();
						content_target.setDetail(content.getDetail());
						content_target.setPage(content.getPage());

						contents_target.add(content_target);
					}
					target_article.setContents(contents_target);

					target_article.setTitle(article.getTitle());
					target_article.setShortTitle(article.getShortTitle());
					target_article.setSubTitle(article.getSubTitle());
					target_article.setAuthor(article.getAuthor());
					target_article.setOrigin(article.getOrigin());
					target_article.setKeyword(article.getKeyword());
					target_article.setTag(article.getTag());
					target_article.setSummary(article.getSummary());
					target_article.setImage(article.getImage());
					target_article.setIsComment(article.getIsComment());
					target_article.setGenre(article.getGenre());
					target_article.setModified(new Date(Calendar.getInstance().getTime().getTime()));
					target_article.setInside(article.getInside());
					target_article.setCreated(EwcmsContextUtil.getLoginName());
					target_article.setContentTotal(article.getContentTotal());

					
					ArticleMain articleMain_new = new ArticleMain();
					articleMain_new.setArticle(target_article);
					articleMain_new.setChannelId(targetChannelId);
					
					articleMainDao.save(articleMain_new);
//					articleMainDao.flush(articleMain_new);
					
					String target_channelName = channelDao.findOne(targetChannelId).getName();
					operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(), "复制到『" + target_channelName + "』栏目。", "");
					operateTrackService.addOperateTrack(articleMain_new.getId(), target_article.getStatusDescription(),"从『" + source_channelName + "』栏目复制。", "");
				}
			}
		}
		return true;
	}
	
	public Boolean moveArticleMainToChannel(List<Long> articleMainIds, List<Long> tagerChannelIds, Long channelId) {
		ArticleMain articleMain = null;
		Channel channel = channelDao.findOne(channelId);
		Assert.notNull(channel);
		String source_channelName = channel.getName();
		for (Long targetChannelId : tagerChannelIds) {
			for (Long articleMainId : articleMainIds) {
				articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
				if (isNull(articleMain)) continue;
				if (targetChannelId != channelId) {
					Article article = articleMain.getArticle();
					
					operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(), "从『" + source_channelName + "』栏目移动。", "");
					
					articleMain.setArticle(article);
					articleMain.setChannelId(targetChannelId);
					articleMainDao.save(articleMain);
				}
			}
		}
		return true;
	}

	public List<ArticleMain> findArticleMainByChannel(Long channelId) {
		return articleMainDao.findArticleMainByChannel(channelId);
	}

	public void pubArticleMainByChannel(Long channelId, Boolean again, Boolean children) throws PublishException {
		if (isNotNull(channelId)) {
			Channel channel = channelDao.findOne(channelId);
			if (!channel.getPublicenable()){
				throw new PublishException("栏目不允许发布文章");
			}
			List<ArticleMain> articleMains = articleMainDao.findArticleMainByTitlePrerelease();
			if (articleMains != null && !articleMains.isEmpty()){
				for (ArticleMain articleMain : articleMains){
					Article article = articleMain.getArticle();
					if (article != null && article.getUrl().trim().length() > 0){
						article.setStatus(Article.Status.RELEASE);
						articleMain.setArticle(article);
						articleMainDao.save(articleMain);
					}
				}
			}
			publishService.pubChannel(channel.getSite().getId(), channelId, again, children);
		}
	}
	
	public void associateRelease(Long channelId) throws PublishException{
		if (isNotNull(channelId)){
			pubArticleMainByChannel(channelId, false, false);
			
			List<Template> templates = templateDao.getTemplatesInChannel(channelId);
			if (templates != null && !templates.isEmpty()){
				for (Template template : templates){
					publishService.pubTemplate(template.getSite().getId(),channelId, template.getId(), false);
				}
			}
			List<Channel> channelChildrens = channelDao.getChannelChildren(channelId);
			if (channelChildrens != null && !channelChildrens.isEmpty()){
				for (Channel channelChildren : channelChildrens){
					if (channelChildren.getPublicenable())
						associateRelease(channelChildren.getId());
				}
			}
			
			Channel channel = channelDao.findOne(channelId);
			String appChannel = channel.getAppChannel();
			if (appChannel != null && appChannel.length() > 0){
				String[] appChannelIds = appChannel.split(",");
				for (String id : appChannelIds){
					try{
						Long tempId = Long.valueOf(id);
						pubArticleMainByChannel(tempId, false, false);
					}catch(Exception e){
					}
				}
			}
		}
	}
	
	public Boolean reviewArticleMainIsEffective(Long articleMainId, Long channelId){
		if (isNull(articleMainId) && isNull(channelId)) return false;
		ArticleMain articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
		if (isNull(articleMain)) return false;
		Article article = articleMain.getArticle();
		if (isNull(article)) return false;
		if (article.getStatus() == Status.REVIEW){
			ReviewProcess rp = reviewProcessDao.findReviewProcessByIdAndChannel(article.getReviewProcess().getId(), channelId);
			Set<ReviewUser> reviewUsers = rp.getReviewUsers();
			Set<ReviewRole> reviewGroups = rp.getReviewRoles();
			if (reviewUsers.isEmpty() && reviewGroups.isEmpty()){
				return false;
			}
			String userName = EwcmsContextUtil.getLoginName();
			if (isNull(userName) || userName.length() == 0) return false;
			for (ReviewUser reviewUser : reviewUsers){
				if (reviewUser.getUserName().equals(userName)){
					return true;
				}
			}
			Collection<String> grouptNames = EwcmsContextUtil.getRoleNames();
			if (isNull(grouptNames)) return false;
			for (ReviewRole reviewGroup : reviewGroups){
				for (String groupName : grouptNames){
					if (reviewGroup.getRoleName().equals(groupName)){
						return true;
					}
				}
			}
		}
		return false;
	}

	public void approveArticleMain(String userName, Long articleMainId, Long channelId, Integer review, String reason) {
		ArticleMain articleMain = null;
		Article article = null;
		Assert.notNull(articleMainId);
		articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
		if (isNull(articleMain)) return;
		article = articleMain.getArticle();
		if (isNull(article)) return;
		if (article.getStatus() == Status.REVIEW) {
			ReviewProcess rp = reviewProcessDao.findReviewProcessByIdAndChannel(article.getReviewProcess().getId(), channelId);
			String currentStatus = article.getStatusDescription();
			String caption = "";
			if (review == 0){// 通过
				if (rp != null){
					caption = "【" + rp.getName() + "】<span style='color:blue;'>通过</span>";
					if (rp.getNextProcess() != null) {
						article.setReviewProcess(rp.getNextProcess());
						caption += "，已提交到【" + rp.getNextProcess().getName() + "】进行审核。";
					}else{
						article.setStatus(Status.PRERELEASE);
						article.setReviewProcess(null);
						caption += "，可以进行发布。";
					}
				}else{
					article.setStatus(Status.REVIEWBREAK);
					caption = "审核流程已改变，不能再进行审核。请联系频道管理员恢复到重新编辑状态。";
				}
			}else if (review == 1){// 不通过
				if (rp != null){
					caption = "【" + rp.getName() + "】<span style='color:red;'>不通过</span>";
					if (rp.getPrevProcess() != null){
						article.setReviewProcess(rp.getPrevProcess());
						caption += "，已退回到【" + rp.getPrevProcess().getName() + "】进行重新审核。";
					}else{
						article.setStatus(Status.REEDIT);
						article.setReviewProcess(null);
						caption += "，已退回到重新编辑状态。";
					}
				}else{
					article.setStatus(Status.REVIEWBREAK);
					caption = "审核流程已改变，不能再进行审核。请联系频道管理员章恢复到重新编辑状态。";
				}
			}
			articleMain.setArticle(article);
			articleMainDao.save(articleMain);
			
			operateTrackService.addOperateTrack(articleMainId, currentStatus, caption, reason);
			
			pushApiService.pushTodo(userName, findBeApprovalArticleMain(userName));
		}
	}

	public void moveArticleMainSort(Long articleMainId, Long channelId, Long sort, Integer isInsert, Boolean isTop) {
		ArticleMain articleMain = articleMainDao.findArticleMainByChannelAndEqualSort(channelId, sort, isTop);
		String desc = "设置排序号为：" + sort + "。";
		if (articleMain == null){
			articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
			Assert.notNull(articleMain);
			articleMain.setSort(sort);
			articleMainDao.save(articleMain);
		}else{
			ArticleMain articleMain_new = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
			Assert.notNull(articleMain_new);
			articleMain_new.setSort(sort);
			if (isInsert == 0){//插入
				List<ArticleMain> articleMains = articleMainDao.findArticleMainByChannelAndThanSort(channelId, sort, isTop);
				if (!articleMains.isEmpty()){
					for (ArticleMain articleMain_old : articleMains){
						if (articleMain_new.getId() == articleMain_old.getId()) continue;
						articleMain_old.setSort(articleMain_old.getSort() + 1);
						articleMainDao.save(articleMain_old);
					}
				}
				desc = "插入排序号为：" + sort + "。";
				articleMainDao.save(articleMain_new);
			}else if (isInsert == 1){//替换
				if (articleMain_new.getId() != articleMain.getId()){
					articleMain.setSort(null);
					articleMainDao.save(articleMain);
					desc = "替换排序号为：" + sort + "。";
					articleMainDao.save(articleMain_new);
				}
			}
		}
		operateTrackService.addOperateTrack(articleMainId, articleMain.getArticle().getStatusDescription(), desc, "");
	}

	public Boolean findArticleMainByChannelAndEqualSort(Long channelId, Long sort, Boolean isTop) {
		ArticleMain articleMain = articleMainDao.findArticleMainByChannelAndEqualSort(channelId, sort, isTop);
		if (articleMain == null) return false;
		return true;
	}

	public void clearArticleMainSort(List<Long> articleMainIds, Long channelId) {
		Assert.notNull(articleMainIds);
		for (Long articleMainId : articleMainIds){
			ArticleMain articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
			Assert.notNull(articleMain);
			if (articleMain.getSort() != null){
				articleMain.setSort(null);
				articleMainDao.save(articleMain);

				operateTrackService.addOperateTrack(articleMainId, articleMain.getArticle().getStatusDescription(), "清除排序号。", "");
			}
		}
	}

	public void breakArticleMain(String userName, List<Long> articleMainIds, Long channelId) throws BaseException {
		for (Long articleMainId : articleMainIds){
			ArticleMain articleMain = articleMainDao.findOne(articleMainId);
			Assert.notNull(articleMain);
			Article article = articleMain.getArticle();
			Assert.notNull(article);
			if (article.getStatus() == Status.PRERELEASE || article.getStatus() == Status.RELEASE || article.getStatus() == Status.REVIEWBREAK){
	
				operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(), "已退回到重新编辑状态。", "");
				
				article.setStatus(Status.REEDIT);
				articleMain.setArticle(article);
				articleMainDao.save(articleMain);
				
				pushApiService.pushTodo(userName, findBeApprovalArticleMain(userName));
//			}else{
//				throw new BaseException("","只有在审核中断、发布版、已发布版状态下才能退回");
			}
		}
	}
	
	public void delArticleMainByCrawler(Long channelId, String userName) {
		List<ArticleMain> articleMains = articleMainDao.findArticleMainByChannelIdAndUserName(channelId, userName);
		for (ArticleMain articleMain : articleMains){
			if (articleMain.getArticle().getStatus() == Status.RELEASE) continue;
			articleMainDao.save(articleMain);
		}
	}

	public void topArticleMain(List<Long> articleMainIds, Boolean top) {
		Assert.notNull(articleMainIds);
		for (Long articleMainId : articleMainIds){
			ArticleMain articleMain = articleMainDao.findOne(articleMainId);
			if (articleMain == null) continue;
			if (articleMain.getTop() != top){
				articleMain.setTop(top);
				articleMainDao.save(articleMain);

				String desc = "设为置顶。";
				if (!top) desc = "取消置顶。";
				operateTrackService.addOperateTrack(articleMainId, articleMain.getArticle().getStatusDescription(), desc, "");
			}
		}
	}
	
	public Boolean findArticleIsEntityByArticleAndCategory(Long articleId, Long categoryId) {
		return articleMainDao.findArticleIsEntityByArticleAndCategory(articleId, categoryId);
	}

	@History(modelObjectIndex = 0)
	public Long addArticleMain(Article article, Long channelId, Date published) {
		Assert.notNull(article);
		if (isNotNull(published)) {
			article.setPublished(published);
		}
		if (isStringEmpty(article.getCreated())){
			article.setCreated(EwcmsContextUtil.getLoginName());
		}

		article.setModified(new Date(Calendar.getInstance().getTime().getTime()));
		if (article.getGenre() == Genre.TITLE){
			titleArticleContentNull(article);
		}else{
			keywordAndSummary(article);
		}
		
		ArticleMain articleMain = new ArticleMain();
		articleMain.setArticle(article);
		articleMain.setChannelId(channelId);
		
		articleMainDao.save(articleMain);
//		articleMainDao.flush(articleMain);
		
		operateTrackService.addOperateTrack(articleMain.getId(), article.getStatusDescription(), "创建。", "");
		
		return articleMain.getId();
	}
	
	public Long addArticleMainByCrawler(Article article, Long channelId, String userName){
		Assert.notNull(article);
		if (articleMainDao.findArticleTitleIsEntityByCrawler(article.getTitle(), channelId, userName))
			return null;
		
		article.setCreated(userName);
		article.setModified(new Date(Calendar.getInstance().getTime().getTime()));
		//keywordAndSummary(article);

		ArticleMain articleMain = new ArticleMain();
		
		articleMain.setArticle(article);
		articleMain.setChannelId(channelId);
		
		articleMainDao.save(articleMain);
//		articleMainDao.flush(articleMain);
		
		operateTrackService.addOperateTrack(articleMain.getId(), article.getStatusDescription(), "通过采集器创建。", "", userName, "网络爬虫");

		return articleMain.getId();
	}
	
	public Long findArticleMainCountByCrawler(Long channelId, String userName){
		return articleMainDao.findArticleMainCountByCrawler(channelId, userName);
	}

	@History(modelObjectIndex = 0)
	public Long updArticleMain(Article article, Long articleMainId, Long channelId, Date published) {
		ArticleMain articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
		Assert.notNull(articleMain);
		
		if (isNotNull(published)) {
			article.setPublished(published);
		}

//		if (article.getStatus() == Status.RELEASE || article.getStatus() == Status.PRERELEASE || article.getStatus() == Status.REVIEW) {
//			// throw new BaseException("error.document.article.notupdate","只能在初稿或重新编辑下才能修改");
//		} else {
			Article article_old = articleMain.getArticle();
			Assert.notNull(article_old);
			if (article.getGenre() == Genre.GENERAL) {
				article.setUrl(null);
				keywordAndSummary(article);
			} else if (article.getGenre() == Genre.TITLE) {
				article.setKeyword("");
				article.setSummary("");
				if (article_old.getContents() != null && !article_old.getContents().isEmpty()) {
					article.setContents(article_old.getContents());
				} else {
					titleArticleContentNull(article);
				}
			}
			
			Date modNow = new Date(Calendar.getInstance().getTime().getTime());
			
			article.setCreated(article_old.getCreated());
			article.setCreateTime(article_old.getCreateTime());
			article.setModified(modNow);
			article.setStatus(Article.Status.REEDIT);
			
			article.setRelations(article_old.getRelations());
			
			articleMain.setArticle(article);
			articleMainDao.save(articleMain);

			operateTrackService.addOperateTrack(articleMainId, article_old.getStatusDescription(), "修改。", "");
//		}
		return articleMain.getId();
	}
	
	private void keywordAndSummary(Article article){
		List<Content> contents = article.getContents();
		String title = article.getTitle();
		if (contents != null && !contents.isEmpty() && title != null && title.length()>0){
			String contentAll = "";
			for (Content content : contents){
				contentAll += content.getDetail();
			}
			if (article.getKeyword() == null || article.getKeyword().length() == 0){
				String keyword = HtmlStringUtil.join(ExtractKeywordAndSummary.getKeyword(title + " " + contentAll), " ");	
				article.setKeyword(keyword);
			}
			if (article.getSummary() == null || article.getSummary().length() == 0){
				String summary = ExtractKeywordAndSummary.getTextAbstract(title, contentAll);
				article.setSummary(summary);
			}
		}
	}
	
	private void titleArticleContentNull(Article article){
		Content content = new Content();
		content.setDetail(null);
		content.setPage(1);
		List<Content> contents = new ArrayList<Content>();
		contents.add(content);
		article.setContents(contents);
	}

	public List<Map<String, Object>> findBeApprovalArticleMain(String userName) {
		List<String> groupNames = new ArrayList<String>();
		User user = accountService.findUserByLoginName(userName);
		if (user != null){
			groupNames = user.getRoleNames();
		}
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		Map<Long, Long> map = articleMainDao.findBeApprovalArticleMain(userName, groupNames);
		if (!map.isEmpty()){
			Set<Long> keySets = map.keySet();
			for (Long key : keySets){
				Channel channel = channelDao.findOne(key);
				Long count = map.get(key);
				
				Map<String, Object> toDoMap = new HashMap<String, Object>();
				toDoMap.put("id", channel.getId());
	            toDoMap.put("name", channel.getName());
	            toDoMap.put("count", count);
	            dataList.add(toDoMap);
			}
		}
		return dataList;
	}

	public Map<Integer, Long> findCreateArticleFcfChart(Integer year, Long siteId) {
		return articleMainDao.findCreateArticleFcfChart(year, siteId);
	}

	public Map<Integer, Long> findReleaseArticleFcfChart(Integer year, Long siteId) {
		return articleMainDao.findReleaseArticleFcfChart(year, siteId);
	}

	public Map<String, Long> findReleaseArticlePersonFcfChart(Integer year, Long siteId) {
		return articleMainDao.findReleaseArticlePersonFcfChart(year, siteId);
	}

	public ArticleMain findArticleMainById(Long articleMainId) {
		return articleMainDao.findOne(articleMainId);
	}

	public void referArticleMain(Long channelId, Long[] articleMainIds) {
		Assert.notNull(channelId);
		Assert.notEmpty(articleMainIds);
		
		Channel channel = channelDao.findOne(channelId);
		Channel parentChannel = channel.getParent();
		if (parentChannel.getType() == Channel.Type.LEADER){
			for (Long articleMainId : articleMainIds){
				ArticleMain articleMain = articleMainDao.findOne(articleMainId);
				if (articleMain == null) continue;
				ArticleMain refArticleMain = new ArticleMain();
				refArticleMain.setArticle(articleMain.getArticle());
				refArticleMain.setChannelId(channelId);
				refArticleMain.setIsReference(true);
				refArticleMain.setSort(articleMain.getSort());
				refArticleMain.setTop(articleMain.getTop());
				
				articleMainDao.save(refArticleMain);
			}
		}
	}

	public void removeArticleMain(Long[] articleMainIds) {
		Assert.notNull(articleMainIds);
		Assert.notEmpty(articleMainIds);
		for (Long articleMainId : articleMainIds){
			ArticleMain articleMain = articleMainDao.findOne(articleMainId);
			Assert.notNull(articleMain);
			if (articleMain.getIsReference()){
				articleMain.setArticle(null);
				articleMainDao.save(articleMain);
				articleMainDao.delete(articleMain);
			}
		}
	}
	
	public void shareArticleMain(List<Long> articleMainIds, Boolean share) {
		Assert.notNull(articleMainIds);
		for (Long articleMainId : articleMainIds){
			ArticleMain articleMain = articleMainDao.findOne(articleMainId);
			if (articleMain == null) continue;
			if (isNotNull(articleMain.getIsReference()) && articleMain.getIsReference()) continue;
			if (articleMain.getIsShare() != share){
				articleMain.setIsShare(share);
				articleMainDao.save(articleMain);

				String desc = "设为共享。";
				if (!share) desc = "取消共享。";
				operateTrackService.addOperateTrack(articleMainId, articleMain.getArticle().getStatusDescription(), desc, "");
			}
		}
	}

	public Map<String, Object> search(Long channelId, QueryParameter params){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_article.isDelete", false);
		parameters.put("EQ_channelId", channelId);
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("top",  Direction.DESC);
		sorts.put("sort", Direction.ASC);
		sorts.put("article.modified", Direction.DESC);
		sorts.put("article.published", Direction.DESC);
		sorts.put("id", Direction.DESC);
		params.setSorts(sorts);
		
		return SearchMain.search(params, "IN_id", Long.class, articleMainDao, ArticleMain.class);
	}

	public Map<String, Object> searchArticleHistory(Long articleId, QueryParameter params){
		Map<String, Object> result = historyModelService.searchArticle(params, articleId);
    	return HistoryUtil.resolve(result);
	}

	public Article restoreArticleHistory(Long historyId) {
		HistoryModel historyModel = historyModelService.findByHistoryModel(historyId);
		Object obj = HistoryUtil.conversion(historyModel.getModelObject());
		if (obj != null) {
			Article article = (Article) obj;
			return article;
		}else{
			return null;
		}
	}
	
	public Article findArticle(Long articleId){
		return articleDao.findOne(articleId);
	}
}
