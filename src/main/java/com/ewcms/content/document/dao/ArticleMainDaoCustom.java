package com.ewcms.content.document.dao;

import java.util.List;
import java.util.Map;

import com.ewcms.content.document.model.ArticleMain;

/**
 * @author 吴智俊
 */
public interface ArticleMainDaoCustom {
	
	ArticleMain findArticleMainByArticleMainAndChannel(final Long articleMainId, final Long channelId);
	
	List<ArticleMain> findArticleMainByChannel(final Long channelId);
	
	ArticleMain findArticleMainByChannelAndEqualSort(final Long channelId, final Long sort, final Boolean top);
	
	List<ArticleMain> findArticleMainByChannelAndThanSort(final Long channelId, final Long sort, final Boolean top);
	
	List<ArticleMain> findArticleMainByChannelIdAndUserName(final Long channelId, final String userName);
	
	Boolean findArticleIsEntityByArticleAndCategory(final Long articleId, final Long categoryId);
	
	Map<Long, Long> findBeApprovalArticleMain(final String userName, final List<String> groupNames);
	
	Boolean findArticleTitleIsEntityByCrawler(final String title, final Long channelId, final String userName);
	
	Long findArticleMainCountByCrawler(final Long channelId, final String userName);
	
	Map<Integer, Long> findCreateArticleFcfChart(final Integer year, final Long siteId);
	
	Map<Integer, Long> findReleaseArticleFcfChart(final Integer year, final Long siteId);
	
	Map<String, Long> findReleaseArticlePersonFcfChart(final Integer year, final Long siteId);
	
	List<ArticleMain> findArticleMainByTitlePrerelease();
	
	List<ArticleMain> findArticleMainByArticleIdAndReference(Long articleId, Boolean reference);
}
