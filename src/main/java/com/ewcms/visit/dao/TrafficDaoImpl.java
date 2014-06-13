package com.ewcms.visit.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.visit.vo.traffic.ArticleClickVo;
import com.ewcms.visit.vo.traffic.UrlClickVo;

/**
 * @author 吴智俊
 */
@Component
public class TrafficDaoImpl implements TrafficDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<ArticleClickVo> findArticleClickTable(Date startDate, Date endDate, List<Long> channelIds, Long siteId, Integer rows, Integer page) {
		String hql = "select new com.ewcms.visit.vo.traffic.ArticleClickVo(" + "c.name, a.title, v.url, a.created, sum(v.pageView), avg(v.stickTime)) "
				+ "from Article a, Channel c, Visit v "
				+ "where a.id=v.articleId and c.id=v.channelId and v.visitDate>=:startDate and v.visitDate<=:endDate @channelIds@ and v.siteId=:siteId "
				+ "group by c.name, a.title, v.url, a.created " + "order by sum(v.pageView) desc";
		if (channelIds != null && !channelIds.isEmpty()) {
			hql = hql.replaceAll("@channelIds@", "and v.channelId in :channelIds");
		} else {
			hql = hql.replaceAll("@channelIds@", "");
		}

		TypedQuery<ArticleClickVo> query = em.createQuery(hql, ArticleClickVo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if (channelIds != null && !channelIds.isEmpty()) {
			query.setParameter("channelIds", channelIds);
		}
		query.setParameter("siteId", siteId);
		if (page <= 0)
			page = 0;
		else
			page = page - 1;
		query.setFirstResult(page * rows);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	public Long findArticleClickCount(Date startDate, Date endDate, List<Long> channelIds, Long siteId) {
		String hql = "select count(c.name) " 
	            + "from Article a, Channel c, Visit v "
				+ "where a.id=v.articleId and c.id=v.channelId and v.visitDate>=:startDate and v.visitDate<=:endDate @channelIds@ and v.siteId=:siteId ";
				
		if (channelIds != null && !channelIds.isEmpty()) {
			hql = hql.replaceAll("@channelIds@", "and v.channelId in :channelIds");
		} else {
			hql = hql.replaceAll("@channelIds@", "");
		}

		TypedQuery<Long> query = em.createQuery(hql, Long.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		if (channelIds != null && !channelIds.isEmpty()) {
			query.setParameter("channelIds", channelIds);
		}
		query.setParameter("siteId", siteId);
		return query.getSingleResult();
	}

	@Override
	public List<UrlClickVo> findUrlClickTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page) {
		String hql = "select new com.ewcms.visit.vo.traffic.UrlClickVo("
				+ "url, sum(pageView), (select sum(pageView) from Visit where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId)) "
				+ "from Visit " + "where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId " + "group by url " + "order by sum(pageView) desc";
		TypedQuery<UrlClickVo> query = em.createQuery(hql, UrlClickVo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("siteId", siteId);
		if (page <= 0)
			page = 0;
		else
			page = page - 1;
		query.setFirstResult(page * rows);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@Override
	public Long findUrlClickCount(Date startDate, Date endDate, Long siteId){
		String hql = "select count(url) "
				+ "from Visit "
				+ "where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId ";
		TypedQuery<Long> query = em.createQuery(hql, Long.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("siteId", siteId);
		return query.getSingleResult();
	}
}
