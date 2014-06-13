package com.ewcms.visit.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.ewcms.visit.vo.loyalty.BackVo;
import com.ewcms.visit.vo.loyalty.StickTimeVo;

/**
 * @author 吴智俊
 */
public class LoyaltyDaoImpl implements LoyaltyDaoCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<BackVo> findBackTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page) {
		String hql = "select new com.ewcms.visit.vo.loyalty.BackVo(" +
				"visitDate, (select count(distinct rvFlag) from Visit where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId And rvFlag=false And rvFlag is not null)," +
				"(select count(distinct rvFlag) from Visit where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId And rvFlag=true And rvFlag is not null)) " +
				"from Visit " +
				"where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId " +
				"group by visitDate " +
				"order by visitDate desc";
				
		TypedQuery<BackVo> query = em.createQuery(hql, BackVo.class);
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
	public List<StickTimeVo> findStickTimeTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page) {
		String hql = "select new com.ewcms.visit.vo.loyalty.StickTimeVo(" +
				"visitDate, sum(stickTime), avg(stickTime)) " +
				"from Visit " +
				"where visitDate>=:startDate and visitDate<=:endDate and siteId=:siteId " +
				"group by visitDate " +
				"order by visitDate desc";
		
		TypedQuery<StickTimeVo> query = em.createQuery(hql, StickTimeVo.class);
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
