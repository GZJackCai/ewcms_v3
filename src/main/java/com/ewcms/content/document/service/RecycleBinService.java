package com.ewcms.content.document.service;

import static com.ewcms.util.EmptyUtil.isNotNull;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.content.document.dao.ArticleMainDao;
import com.ewcms.content.document.dao.RelationDao;
import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.model.ArticleMain;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * @author 吴智俊
 */
@Component
public class RecycleBinService {
	
	@Autowired
	private ArticleMainDao articleMainDao;
	@Autowired
	private RelationDao relationDao;
	@Autowired
	private OperateTrackService operateTrackService;
	
	public Map<String, Object> search(Long channelId, QueryParameter params){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_article.isDelete", true);
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
	
	public void delArticleMain(Long articleMainId, Long channelId) {
		ArticleMain articleMain = articleMainDao.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
		Assert.notNull(articleMain);
		if (articleMain.getArticle() != null && articleMain.getArticle().getId() != null){
			relationDao.delete(relationDao.findRelationById(articleMain.getArticle().getId()));
		}
		if (isNotNull(articleMain.getIsReference()) && articleMain.getIsReference()){
			articleMain.setArticle(null);
		}
		articleMainDao.delete(articleMain);
		operateTrackService.delOperateTrack(articleMainId);
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
}
