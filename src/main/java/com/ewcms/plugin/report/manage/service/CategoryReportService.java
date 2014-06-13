/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.report.manage.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.plugin.report.manage.dao.CategoryReportDao;
import com.ewcms.plugin.report.manage.dao.ChartReportDao;
import com.ewcms.plugin.report.manage.dao.TextReportDao;
import com.ewcms.plugin.report.model.CategoryReport;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.TextReport;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author wu_zhijun
 * 
 */
@Component
@Transactional(readOnly = true)
public class CategoryReportService {

	@Autowired
	private CategoryReportDao categoryReportDao;
	@Autowired
	private TextReportDao textReportDao;
	@Autowired
	private ChartReportDao chartReportDao;

	@Transactional(readOnly = false)
	public Long addOrUpdCategoryReport(CategoryReport categoryReport){
		if (categoryReport.getId() != null){
			Long categoryReportId = categoryReport.getId();
			CategoryReport source = categoryReportDao.findOne(categoryReportId);
			if (source.getCharts() != null && !source.getCharts().isEmpty()){
				categoryReport.setCharts(source.getCharts());
			}
			if (source.getTexts() != null && !source.getTexts().isEmpty()){
				categoryReport.setTexts(source.getTexts());
			}
		}
		categoryReportDao.save(categoryReport);
		return categoryReport.getId();
	}

	@Transactional(readOnly = false)
	public void delCategoryReport(Long categoryReportId){
		categoryReportDao.delete(categoryReportId);
	}

	public CategoryReport findCategoryReportById(Long categoryReportId) {
		return categoryReportDao.findOne(categoryReportId);
	}

	public Iterable<CategoryReport> findAllCategoryReport(){
		return categoryReportDao.findAll();
	}

	@Transactional(readOnly = false)
	public void addReportToCategories(Long categoryReportId, List<Long> textReportIds, List<Long> chartReportIds){
		CategoryReport categoryReport = findCategoryReportById(categoryReportId);
		Set<TextReport> textReports = new HashSet<TextReport>();
		if (textReportIds != null && !textReportIds.isEmpty()){
			for (Long textReportId : textReportIds){
				TextReport text = textReportDao.findOne(textReportId);
				textReports.add(text);
			}
		}
		categoryReport.setTexts(textReports);
		
		Set<ChartReport> chartList = new HashSet<ChartReport>();
		if (chartReportIds != null && !chartReportIds.isEmpty()){
			for (Long chartReportId : chartReportIds){
				ChartReport chart = chartReportDao.findOne(chartReportId);
				chartList.add(chart);
			}
		}
		categoryReport.setCharts(chartList);
		
		categoryReportDao.save(categoryReport);
	}

	public Boolean findTextIsEntityByTextAndCategory(Long textId, Long categoryId) {
		return categoryReportDao.findTextIsEntityByTextAndCategory(textId, categoryId);
	}

	public Boolean findChartIsEntityByChartAndCategory(Long chartId, Long categoryId) {
		return categoryReportDao.findChartIsEntityByChartAndCategory(chartId, categoryId);
	}
	
	public Map<String, Object> queryCategoryReport(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, categoryReportDao, CategoryReport.class);
	}
}
