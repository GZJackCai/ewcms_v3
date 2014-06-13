package com.ewcms.plugin.report.manage.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.plugin.report.model.CategoryReport;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;

/**
 * @author 吴智俊
 */
@Component
public class TextReportDaoImpl implements TextReportDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<CategoryReport> findCategoryReportByTextReportId(final Long textReportId){
		String hql = "Select c From CategoryReport As c Left Join c.texts As t Where t.id=:textReportId";
		
		TypedQuery<CategoryReport> query = em.createQuery(hql, CategoryReport.class);
		query.setParameter("textReportId", textReportId);
		
		return query.getResultList();
	}
	
	@Override
	public List<EwcmsJobReport> findEwcmsJobReportByTextReportId(final Long textReportId){
		String hql = "Select e From EwcmsJobReport As e Where e.textReport.id=:textReportId";
		
		TypedQuery<EwcmsJobReport> query = em.createQuery(hql, EwcmsJobReport.class);
		query.setParameter("textReportId", textReportId);
		
		return query.getResultList();
	}

}
