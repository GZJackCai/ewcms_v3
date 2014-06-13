package com.ewcms.content.document.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.model.Article.Status;

/**
 * @author 吴智俊
 */
@Component
public class ArticleDaoImpl implements ArticleDaoCustom {

	private final static Logger logger = LoggerFactory.getLogger(ArticleDaoImpl.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Long findArticleReleseCount(final Long channelId) {
		String hql = "Select Count(m.id) From ArticleMain As m Where m.channelId=:channelId And m.article.status=:status";

		TypedQuery<Long> query = em.createQuery(hql, Long.class);
		query.setParameter("channelId", channelId);
		query.setParameter("status", Status.RELEASE);

		return query.getSingleResult();
	}

	@Override
	public List<Article> findPublishArticles(final Long channelId, final Boolean forceAgain, final Integer limit) {
		String hql = "Select m.article From ArticleMain As m Where m.reference=false And m.channelId=:channelId And m.article.status In (:status)";
		List<Status> status = new ArrayList<Status>();
		if (forceAgain) {
			status.add(Status.RELEASE);
		}
		status.add(Status.PRERELEASE);

		TypedQuery<Article> query = em.createQuery(hql, Article.class);
		query.setParameter("channelId", channelId);
		query.setParameter("status", status);
		query.setMaxResults(limit);

		return query.getResultList();
	}

	@Override
	public List<Article> findArticleReleasePage(final Long channelId, final Integer page, final Integer row, final Boolean top) {
		logger.debug("query channel is {}", channelId);
		String hql = "Select m.article From ArticleMain As m Where m.channelId=:channelId And m.article.status=:status And m.top In (:tops) Order By m.sort Asc, m.article.published Desc, m.id Desc";
		int startRow = page * row;
		List<Boolean> tops = new ArrayList<Boolean>();
		if (top == null) {
			tops.add(Boolean.FALSE);
			tops.add(Boolean.TRUE);
		} else {
			tops.add(top);
		}

		TypedQuery<Article> query = em.createQuery(hql, Article.class);
		query.setParameter("channelId", channelId);
		query.setParameter("status", Status.RELEASE);
		query.setParameter("tops", tops);
		query.setFirstResult(startRow);
		query.setMaxResults(row);
		// .setHint("org.hibernate.cacheable", true);

		return query.getResultList();
	}

	@Override
	public List<Article> findChildChannelArticleReleasePage(final List<Long> channelIds, final Integer page, final Integer row, final Boolean top) {
		String hql = "Select m.article From ArticleMain As m Where m.channelId In (:channelIds) And m.article.status=:status And m.top In (:tops)  Order By m.sort Asc, m.article.published Desc, m.id Desc";
		int startRow = page * row;
		List<Boolean> tops = new ArrayList<Boolean>();
		if (top == null) {
			tops.add(Boolean.FALSE);
			tops.add(Boolean.TRUE);
		} else {
			tops.add(top);
		}

		TypedQuery<Article> query = em.createQuery(hql, Article.class);
		query.setParameter("channelIds", channelIds);
		query.setParameter("status", Status.RELEASE);
		query.setParameter("tops", tops);
		query.setFirstResult(startRow);
		query.setMaxResults(row);
		// .setHint("org.hibernate.cacheable", true);

		return query.getResultList();
	}

}
