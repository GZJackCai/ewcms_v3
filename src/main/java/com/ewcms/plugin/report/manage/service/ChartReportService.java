/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.report.manage.service;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.plugin.BaseException;
import com.ewcms.plugin.externalds.manager.dao.BaseDsDao;
import com.ewcms.plugin.externalds.model.BaseDs;
import com.ewcms.plugin.report.manage.dao.CategoryReportDao;
import com.ewcms.plugin.report.manage.dao.ChartReportDao;
import com.ewcms.plugin.report.manage.util.ChartAnalysisUtil;
import com.ewcms.plugin.report.manage.util.ParameterSetValueUtil;
import com.ewcms.plugin.report.model.CategoryReport;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.Parameter;
import com.ewcms.scheduling.generate.job.report.dao.EwcmsJobReportDao;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author wu_zhijun
 *
 */
@Component
public class ChartReportService {

	@Autowired
	private ChartReportDao chartReportDao;
	@Autowired
	private CategoryReportDao categorReportDao;
	@Autowired
	private EwcmsJobReportDao ewcmsJobReportDao;
	@Autowired
	private BaseDsDao baseDsDao;
	
	public Long addChartReport(ChartReport chartReport){
		Assert.notNull(chartReport);
		Assert.hasLength(chartReport.getChartSql());

		Set<Parameter> parameters = ChartAnalysisUtil.analysisSql(chartReport.getChartSql());
		chartReport.setParameters(parameters);
		
		if (chartReport.getBaseDs().getId() == 0L){
			chartReport.setBaseDs(null);
		}else{
			BaseDs baseDs = baseDsDao.findOne(chartReport.getBaseDs().getId());
			chartReport.setBaseDs(baseDs);
		}
		chartReportDao.save(chartReport);
		return chartReport.getId();
	}

	public Long updChartReport(ChartReport chartReport){
		Assert.notNull(chartReport);
		Assert.hasLength(chartReport.getChartSql());
		
		ChartReport entity = chartReportDao.findOne(chartReport.getId());
		
		entity.setName(chartReport.getName());
		if (chartReport.getBaseDs().getId() == 0L){
			entity.setBaseDs(null);
		}else{
			BaseDs baseDs = baseDsDao.findOne(chartReport.getBaseDs().getId());
			entity.setBaseDs(baseDs);
		}
		entity.setType(chartReport.getType());
		entity.setShowTooltips(chartReport.getShowTooltips());
		entity.setChartTitle(chartReport.getChartTitle());
		entity.setFontName(chartReport.getFontName());
		entity.setFontSize(chartReport.getFontSize());
		entity.setFontStyle(chartReport.getFontStyle());
		entity.setHorizAxisLabel(chartReport.getHorizAxisLabel());
		entity.setVertAxisLabel(chartReport.getVertAxisLabel());
		entity.setDataFontName(chartReport.getDataFontName());
		entity.setDataFontSize(chartReport.getDataFontSize());
		entity.setDataFontStyle(chartReport.getDataFontStyle());
		entity.setAxisFontName(chartReport.getAxisFontName());
		entity.setAxisFontSize(chartReport.getAxisFontSize());
		entity.setAxisFontStyle(chartReport.getAxisFontStyle());
		entity.setAxisTickFontName(chartReport.getAxisTickFontName());
		entity.setAxisTickFontSize(chartReport.getAxisTickFontSize());
		entity.setAxisTickFontStyle(chartReport.getAxisTickFontStyle());
		entity.setTickLabelRotate(chartReport.getTickLabelRotate());
		entity.setShowLegend(chartReport.getShowLegend());
		entity.setLegendPosition(chartReport.getLegendPosition());
		entity.setLegendFontName(chartReport.getLegendFontName());
		entity.setLegendFontSize(chartReport.getLegendFontSize());
		entity.setLegendFontStyle(chartReport.getLegendFontStyle());
		entity.setChartHeight(chartReport.getChartHeight());
		entity.setChartWidth(chartReport.getChartWidth());
		entity.setBgColorB(chartReport.getBgColorB());
		entity.setBgColorG(chartReport.getBgColorG());
		entity.setBgColorR(chartReport.getBgColorR());
		entity.setRemarks(chartReport.getRemarks());
		entity.setUpdateDate(new Date(Calendar.getInstance().getTime().getTime()));
		
		if (!entity.getChartSql().equals(chartReport.getChartSql())) {
			entity.setChartSql(chartReport.getChartSql());
			Set<Parameter> icNewList = new LinkedHashSet<Parameter>();
			
			Set<Parameter> oldParameters = entity.getParameters();
			Set<Parameter> newParameters = ChartAnalysisUtil.analysisSql(chartReport.getChartSql());
			for (Parameter newParameter : newParameters){
				Parameter ic = findListEntity(oldParameters,newParameter);
				if (ic == null){
					ic = newParameter;
				}
				icNewList.add(ic);
			}
			entity.setParameters(icNewList);
		}
		chartReportDao.save(entity);
		return chartReport.getId();
	}

	public void delChartReport(Long chartReportId){
		ChartReport chartReport = chartReportDao.findOne(chartReportId);
		Assert.notNull(chartReport);
		List<CategoryReport> categories = chartReportDao.findCategoryReportByChartReportId(chartReportId);
		if (categories != null && !categories.isEmpty()){
			for (CategoryReport categoryReport : categories){
				Set<ChartReport> chartReports = categoryReport.getCharts();
				if (chartReports.isEmpty()) continue;
				chartReports.remove(chartReport);
				categoryReport.setCharts(chartReports);
				categorReportDao.save(categoryReport);
			}
		}
		List<EwcmsJobReport> ewcmsJobReports = chartReportDao.findEwcmsJobReportByChartReportId(chartReportId);
		if (ewcmsJobReports != null && !ewcmsJobReports.isEmpty()){
			for (EwcmsJobReport ewcmsJobReport : ewcmsJobReports){
				if (ewcmsJobReport.getTextReport() == null) {
					ewcmsJobReportDao.delete(ewcmsJobReport);
				}else{
					ewcmsJobReport.setChartReport(null);
					ewcmsJobReportDao.save(ewcmsJobReport);
				}
			}
		}
		chartReportDao.delete(chartReportId);
	}

	public ChartReport findChartReportById(Long chartReportId){
		return chartReportDao.findOne(chartReportId);
	}

	public Iterable<ChartReport> findAllChartReport() {
		return chartReportDao.findAll();
	}
	
	public Long updChartReportParameter(Long chartReportId, Parameter parameter) throws BaseException {
		if (chartReportId == null || chartReportId.intValue() == 0)
			throw new BaseException("", "图型编号不存在，请重新选择！");
		ChartReport chart = chartReportDao.findOne(chartReportId);
		if (chart == null)
			throw new BaseException("", "图型不存在，请重新选择！");
		
		parameter = ParameterSetValueUtil.setParametersValue(parameter);
		
		Set<Parameter> parameters = chart.getParameters();
		parameters.remove(parameter);
		parameters.add(parameter);
		chart.setParameters(parameters);
		
		chartReportDao.save(chart);	
		
		return parameter.getId();
	}
	
	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, chartReportDao, ChartReport.class);
	}

	
	/**
	 * 根据参数名查询数据库中的参数集合
	 * 
	 * @param oldParameters
	 *            数据库中的报表参数集合
	 * @param newParameter 新参数
	 *            
	 * @return Parameter
	 */
	private Parameter findListEntity(Set<Parameter> oldParameters, Parameter newParameter) {
		for (Parameter parameter : oldParameters) {
			String rpEnName = parameter.getEnName();
			if (newParameter.getEnName().trim().equals(rpEnName.trim())) {
				parameter.setClassName(newParameter.getClassName());
				parameter.setDefaultValue(newParameter.getDefaultValue());
				parameter.setDescription(newParameter.getDescription());
				return parameter;
			}
		}
		return null;
	}
}
