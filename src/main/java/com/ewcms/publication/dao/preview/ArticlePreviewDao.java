package com.ewcms.publication.dao.preview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import com.ewcms.publication.dao.ArticlePublishDaoable;
import com.ewcms.publication.dao.publish.ArticlePublishDao;
import com.ewcms.publication.module.Article;
import com.ewcms.publication.module.ArticleInfo;

public class ArticlePreviewDao implements ArticlePublishDaoable {
	private static final int DEFAULT_COUNT = 20;
	private final ArticlePublishDaoable articlePublishDao;
	private final boolean mock;

	public ArticlePreviewDao(DataSource dataSource, boolean mock) {
		ArticlePublishDao dao = new ArticlePublishDao();
		dao.setDataSource(dataSource);
		this.articlePublishDao = dao;
		this.mock = mock;
	}

	@Override
	public List<Article> findPrePublish(List<Long> ids) {
		if(!mock){
			return articlePublishDao.findPrePublish(ids);
		}
		
		List<Article> list = new ArrayList<Article>(ids.size());
		for(Long id : ids){
			list.add(newMockArticle(id));
		}
		return list;
	}

	@Override
	public int findPrePublishCount(Long channelId, Boolean forceAgain) {
		return mock ? DEFAULT_COUNT : articlePublishDao.findPrePublishCount(channelId, forceAgain);
	}

	@Override
	public List<Article> findPrePublish(Long channelId, Boolean forceAgain, Long startId, Integer limit) {
		if(!mock){
			return articlePublishDao.findPrePublish(channelId, forceAgain, startId, limit);
		}
		List<Article> articles = new ArrayList<Article>(limit);
		for(int i = 0 ; i < limit ; i++){
			long id = 1000l + Long.valueOf(i);
			articles.add(newMockArticle(id));
		}
		return articles;
	}

	@Override
	public List<ArticleInfo> findPublish(Long channelId, Integer page, Integer row, Boolean top) {
		if(!mock){
			return articlePublishDao.findPublish(channelId, page, row, top);
		}
		List<ArticleInfo> infos = new ArrayList<ArticleInfo>(row);
	    for(int i = 0 ; i < row ; i++){
			long id = 1000l + Long.valueOf(i);
			infos.add(newMockArticleInfo(id));
		}
		return infos;
	}

	@Override
	public List<ArticleInfo> findPublish(List<Long> ids) {
		if(!mock){
			return articlePublishDao.findPublish(ids);
		}
		List<ArticleInfo> infos = new ArrayList<ArticleInfo>(ids.size());
		for(Long id : ids){
			infos.add(newMockArticleInfo(id));
		}
		return infos;
	}

	@Override
	public int findPublishCount(Long channelId) {
		return mock ? DEFAULT_COUNT : articlePublishDao.findPublishCount(channelId);
	}

	/**
	 * 创建缺省的模拟文章
	 * 
	 * @return
	 */
	public static Article newMockArticle(Long id) {
		Date now = new Date();
		Article article = new Article();

		article.setId(id);
		article.setAuthor("模拟测试");
		article.setKeyword("文章 测试");
		article.setOrigin("模拟测试");
		article.setCreated("测试用户");
		article.setShortTitle("文章模拟测试");
		article.setSubTitle("文章模拟测试子标题");
		article.setSummary("测试生成模板是否正确");
		article.setTag("测试 模拟 文章");
		article.setTotalPage(3);
		article.setInside(false);
		article.setComment(true);
		article.setCreateTime(now);
		article.setModified(now);
		article.setPublished(now);
		article.setPage(1);
		article.setUrl("#");
		article.setContent("静态网页生成系统，快速");
		String title = String.format("%s(%s)", "模拟文章测试", id);
		article.setTitle(title);

		return article;
	}
	
	/**
	 * 创建缺省的模拟文章信息
	 * 
	 * @return
	 */
	public static ArticleInfo newMockArticleInfo(Long id) {
		Date now = new Date();
		
		ArticleInfo article = new ArticleInfo();
		article.setId(id);
		article.setAuthor("模拟测试");
		article.setKeyword("文章 测试");
		article.setOrigin("模拟测试");
		article.setOwner("测试用户");
		article.setShortTitle("文章模拟测试");
		article.setSubTitle("文章模拟测试子标题");
		article.setSummary("测试生成模板是否正确");
		article.setTag("测试 模拟 文章");
		article.setComment(true);
		article.setCreateTime(now);
		article.setModified(now);
		article.setPublished(now);
		article.setUrl("#");
		String title = String.format("%s(%s)", "模拟文章测试", id);
		article.setTitle(title);

		return article;
	}
}
