package com.ewcms.content.document.dao;

import java.util.List;

import com.ewcms.content.document.model.Article;

/**
 * @author 吴智俊
 */
public interface ArticleDaoCustom {

	Long findArticleReleseCount(Long channelId);
	
	List<Article> findPublishArticles(final Long channelId,final Boolean forceAgain,final Integer limit);
	
	List<Article> findArticleReleasePage(final Long channelId, final Integer page, final Integer row, final Boolean top);
	
	List<Article> findChildChannelArticleReleasePage(final List<Long> channelIds, final Integer page, final Integer row, final Boolean top);
}
