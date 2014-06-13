package com.ewcms.plugin.report.manage.dao;

/**
 * @author 吴智俊
 */
public interface CategoryReportDaoCustom {
	public Boolean findTextIsEntityByTextAndCategory(final Long textReportId, final Long categoryReportId);
	public Boolean findChartIsEntityByChartAndCategory(final Long chartReportId, final Long categoryReportId);
}
