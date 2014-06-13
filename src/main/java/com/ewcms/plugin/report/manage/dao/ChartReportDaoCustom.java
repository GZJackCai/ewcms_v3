package com.ewcms.plugin.report.manage.dao;

import java.util.List;

import com.ewcms.plugin.report.model.CategoryReport;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;

/**
 * @author 吴智俊
 */
public interface ChartReportDaoCustom {
	public List<CategoryReport> findCategoryReportByChartReportId(final Long chartReportId);
	public List<EwcmsJobReport> findEwcmsJobReportByChartReportId(final Long chartReportId);
}
