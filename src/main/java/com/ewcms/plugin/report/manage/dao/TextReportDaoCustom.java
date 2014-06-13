package com.ewcms.plugin.report.manage.dao;

import java.util.List;

import com.ewcms.plugin.report.model.CategoryReport;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;

/**
 * @author 吴智俊
 */
public interface TextReportDaoCustom {
	public List<CategoryReport> findCategoryReportByTextReportId(final Long textReportId);
	public List<EwcmsJobReport> findEwcmsJobReportByTextReportId(final Long textReportId);
}
