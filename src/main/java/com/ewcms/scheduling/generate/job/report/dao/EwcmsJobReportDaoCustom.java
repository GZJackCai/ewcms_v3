package com.ewcms.scheduling.generate.job.report.dao;

import java.util.List;

import com.ewcms.scheduling.generate.job.report.model.EwcmsJobParameter;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;

/**
 * @author 吴智俊
 */
public interface EwcmsJobReportDaoCustom {
	public EwcmsJobReport findJobReportByReportId(final Long reportId, final String reportType);
	public List<EwcmsJobParameter> findByJobReportParameterById(final Long jobReportId);
}
