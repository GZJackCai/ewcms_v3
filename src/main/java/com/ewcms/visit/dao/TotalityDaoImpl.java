package com.ewcms.visit.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.visit.vo.totality.EntranceVo;
import com.ewcms.visit.vo.totality.SiteClickVo;

/**
 * @author 吴智俊
 */
@Component
public class TotalityDaoImpl implements TotalityDaoCustom{

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<EntranceVo> findEntrance(Date startDate, Date endDate, Long siteId, Integer rows, Integer page) {
		String hql = "select new com.ewcms.visit.vo.totality.EntranceVo(" +
				"url, count(url), (select count(url) from Visit where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId and url is not null)) " +
				"from Visit " +
				"where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId and url is not null " +
				"group by url " +
				"order by count(url) desc";
		TypedQuery<EntranceVo> query = em.createQuery(hql, EntranceVo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("siteId", siteId);
		if (page <= 0 ) page = 0;
		else page = page - 1; 
		query.setFirstResult(page * rows);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	public List<EntranceVo> findExit(Date startDate, Date endDate, Long siteId, Integer rows, Integer page) {
		String hql = "select new com.ewcms.visit.vo.totality.EntranceVo(" +
			"url, count(url), (select count(url) from Visit where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId and url is not null and event=com.ewcms.visit.util.VisitUtil.UNLOAD_EVENT)) " +
			"from Visit " +
			"where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId and url is not null and event=com.ewcms.visit.util.VisitUtil.UNLOAD_EVENT " +
			"group by url order by " +
			"count(url) desc";
		TypedQuery<EntranceVo> query = em.createQuery(hql, EntranceVo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("siteId", siteId);
		if (page <= 0 ) page = 0;
		else page = page - 1; 
		query.setFirstResult(page * rows);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	public List<SiteClickVo> findSiteClickTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page) {
		String hql = "select new com.ewcms.visit.vo.totality.SiteClickVo(" +
			"visitDate, count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
			"from Visit where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId " +
			"group by visitDate " +
			"order by visitDate desc";
		TypedQuery<SiteClickVo> query = em.createQuery(hql, SiteClickVo.class);
		query.setParameter("startDate", startDate);
		query.setParameter("endDate", endDate);
		query.setParameter("siteId", siteId);
		if (page <= 0 ) page = 0;
		else page = page - 1; 
		query.setFirstResult(page * rows);
		query.setMaxResults(rows);
		return query.getResultList();
	}

}
