package com.ewcms.scheduling.generate.job.report.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.scheduling.generate.job.report.model.EwcmsJobParameter;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;

/**
 * @author 吴智俊
 */
@Component
public class EwcmsJobReportDaoImpl implements EwcmsJobReportDaoCustom {
	
	@PersistenceContext
	private EntityManager em;
	
	public EwcmsJobReport findJobReportByReportId(final Long reportId, final String reportType) {
		String hql = "Select o From EwcmsJobReport o Inner Join ";
		if (reportType.equals("text")){
			hql += " o.textReport c ";
		}else if (reportType.equals("chart")){
			hql += " o.chartReport c ";
		}
		hql += " Where c.id=:reportId ";

		TypedQuery<EwcmsJobReport> query = em.createQuery(hql, EwcmsJobReport.class);
    	query.setParameter("reportId", reportId);

    	EwcmsJobReport ewcmsJobReport = null;
    	try{
    		ewcmsJobReport = (EwcmsJobReport) query.getSingleResult();
    	}catch(Exception e){
    		
    	}
    	return ewcmsJobReport;
	}

	public List<EwcmsJobParameter> findByJobReportParameterById(final Long jobReportId) {
		String hql = "Select p From EwcmsJobReport o Join o.ewcmsJobParameters p Where o.id=:jobReportId";
		
		TypedQuery<EwcmsJobParameter> query = em.createQuery(hql, EwcmsJobParameter.class);
		query.setParameter("jobReportId", jobReportId);

		return query.getResultList();
	}

}
