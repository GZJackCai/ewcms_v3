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
public class ChartReportDaoImpl implements ChartReportDaoCustom {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<CategoryReport> findCategoryReportByChartReportId(final Long chartReportId){
		String hql = "Select c From CategoryReport As c Left Join c.charts As t Where t.id=:chartReportId";
		
		TypedQuery<CategoryReport> query = em.createQuery(hql, CategoryReport.class);
		query.setParameter("chartReportId", chartReportId);
		
		return query.getResultList();
	}
	
	@Override
	public List<EwcmsJobReport> findEwcmsJobReportByChartReportId(final Long chartReportId){
		String hql = "Select e From EwcmsJobReport As e Where e.chartReport.id=:chartReportId";
		
		TypedQuery<EwcmsJobReport> query = em.createQuery(hql, EwcmsJobReport.class);
		query.setParameter("chartReportId", chartReportId);
		
		return query.getResultList();
	}

}
