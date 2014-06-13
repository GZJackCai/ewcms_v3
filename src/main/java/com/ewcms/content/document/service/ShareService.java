package com.ewcms.content.document.service;

import static com.ewcms.util.EmptyUtil.isNotNull;
import static com.ewcms.util.EmptyUtil.isNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.ewcms.content.document.dao.ArticleMainDao;
import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.model.ArticleMain;
import com.ewcms.content.document.model.Content;
import com.ewcms.content.document.model.Article.Status;
import com.ewcms.content.document.model.Article.Genre;
import com.ewcms.site.dao.ChannelDao;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * @author 吴智俊
 */
@Component
public class ShareService {
	
	@Autowired
	private ArticleMainDao articleMainDao;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private OperateTrackService operateTrackService;
	
	public void copyArticleMainFromShare(List<Long> articleMainIds, List<Long> channelIds){
		ArticleMain articleMain = null;
		Article article = null;
		for (Long target_channelId : channelIds) {
			for (Long articleMainId : articleMainIds) {
				articleMain = articleMainDao.findOne(articleMainId);
				if (isNull(articleMain)) continue;
				if (isNotNull(articleMain.getIsReference()) && articleMain.getIsReference()) continue;
				article = articleMain.getArticle();
				if (isNull(article)) continue;
				if (articleMain.getChannelId() != null && target_channelId != articleMain.getChannelId()) {
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
					articleMain_new.setChannelId(target_channelId);
					
					articleMainDao.save(articleMain_new);
//					articleMainDao.flush(articleMain_new);
					
					String target_channelName = channelDao.findOne(target_channelId).getName();
					operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(), "从共享库中复制到『" + target_channelName + "』栏目。", "");
					operateTrackService.addOperateTrack(articleMain_new.getId(), target_article.getStatusDescription(),"从共享库中复制。", "");
				}
			}
		}
	}
	
	public void refenceArticleMainFromShare(List<Long> articleMainIds, List<Long> channelIds){
		ArticleMain articleMain = null;
		Article article = null;
		for (Long target_channelId : channelIds) {
			for (Long articleMainId : articleMainIds) {
				articleMain = articleMainDao.findOne(articleMainId);
				if (isNull(articleMain)) continue;
				if (isNotNull(articleMain.getIsReference()) && articleMain.getIsReference()) continue;
				article = articleMain.getArticle();
				if (isNull(article)) continue;
				if (articleMain.getChannelId() != null && target_channelId != articleMain.getChannelId()) {
					ArticleMain articleMain_new = new ArticleMain();
					articleMain_new.setArticle(article);
					articleMain_new.setChannelId(target_channelId);
					articleMain_new.setIsReference(true);
					articleMain_new.setSort(articleMain.getSort());
					articleMain_new.setTop(articleMain.getTop());

					articleMainDao.save(articleMain_new);
//					articleMainDao.flush(articleMain_new);
					
					String target_channelName = channelDao.findOne(target_channelId).getName();
					operateTrackService.addOperateTrack(articleMainId, article.getStatusDescription(), "从共享库中引用到『" + target_channelName + "』栏目。", "");
					operateTrackService.addOperateTrack(articleMain_new.getId(), article.getStatusDescription(),"从共享库中引用。", "");
				}
			}
		}
	}
	
	public Map<String, Object> search(QueryParameter params){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_isShare", true);
		parameters.put("EQ_article.isDelete", false);
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
}
