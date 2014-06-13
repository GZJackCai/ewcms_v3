package com.ewcms.plugin.report.manage.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.plugin.report.model.CategoryReport;

/**
 * @author 吴智俊
 */
@Component
public class CategoryReportDaoImpl implements CategoryReportDaoCustom {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public Boolean findTextIsEntityByTextAndCategory(final Long textReportId, final Long categoryReportId){
    	String hql = "Select c From CategoryReport As c Left Join c.texts As t Where t.id=:textReportId And c.id=:categoryReportId";
    	
    	TypedQuery<CategoryReport> query = em.createQuery(hql, CategoryReport.class);
    	query.setParameter("textReportId", textReportId);
    	query.setParameter("categoryReportId", categoryReportId);
    	
    	List<CategoryReport> list = query.getResultList();
    	return list.isEmpty() ? false : true;
    }
	
	@Override
	public Boolean findChartIsEntityByChartAndCategory(final Long chartReportId, final Long categoryReportId){
    	String hql = "Select c From CategoryReport As c Left Join c.charts As t Where t.id=:chartReportId And c.id=:categoryReportId";
    	
    	TypedQuery<CategoryReport> query = em.createQuery(hql, CategoryReport.class);
    	query.setParameter("chartReportId", chartReportId);
    	query.setParameter("categoryReportId", categoryReportId);
    	
    	List<CategoryReport> list = query.getResultList();
    	return list.isEmpty() ? false : true;
    }
}
