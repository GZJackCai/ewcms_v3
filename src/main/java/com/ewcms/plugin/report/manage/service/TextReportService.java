/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.report.manage.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.JRParameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.plugin.BaseException;
import com.ewcms.plugin.externalds.manager.dao.BaseDsDao;
import com.ewcms.plugin.externalds.model.BaseDs;
import com.ewcms.plugin.report.manage.dao.CategoryReportDao;
import com.ewcms.plugin.report.manage.dao.TextReportDao;
import com.ewcms.plugin.report.manage.util.ParameterSetValueUtil;
import com.ewcms.plugin.report.manage.util.TextDesignUtil;
import com.ewcms.plugin.report.model.CategoryReport;
import com.ewcms.plugin.report.model.Parameter;
import com.ewcms.plugin.report.model.TextReport;
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
public class TextReportService {

	@Autowired
	private TextReportDao textReportDao;
	@Autowired
	private CategoryReportDao categorReportDao;
	@Autowired
	private EwcmsJobReportDao ewcmsJobReportDao;
	@Autowired
	private BaseDsDao baseDsDao;
	
	public Long addTextReport(TextReport textReport) throws BaseException {
		byte[] reportFile = textReport.getTextEntity();

		if (reportFile != null && reportFile.length > 0) {
			InputStream in = new ByteArrayInputStream(reportFile);
			TextDesignUtil rd = new TextDesignUtil(in);
			List<JRParameter> paramList = rd.getParameters();
			
			Set<Parameter> icSet = new LinkedHashSet<Parameter>();
			if (!paramList.isEmpty()) {
				for (JRParameter param : paramList) {
					Parameter ic = getParameterValue(param);
					icSet.add(ic);
				}
				textReport.setParameters(icSet);
			}
		}
		
		if (textReport.getBaseDs().getId() == 0L){
			textReport.setBaseDs(null);
		}else{
			BaseDs baseDs = baseDsDao.findOne(textReport.getBaseDs().getId());
			textReport.setBaseDs(baseDs);
		}
		textReportDao.save(textReport);
		return textReport.getId();
	}

	public Long updTextReport(TextReport textReport) throws BaseException {
		TextReport entity = textReportDao.findOne(textReport.getId());
		
		if (textReport.getBaseDs().getId() == 0L){
			entity.setBaseDs(null);
		}else{
			BaseDs baseDs = baseDsDao.findOne(textReport.getBaseDs().getId());
			entity.setBaseDs(baseDs);
		}
		entity.setName(textReport.getName());
		entity.setHidden(textReport.getHidden());
		entity.setRemarks(textReport.getRemarks());
		
		entity.setUpdateDate(new Date(Calendar.getInstance().getTime().getTime()));
		
		byte[] reportFile = textReport.getTextEntity();
		if (reportFile != null && reportFile.length > 0) {
			entity.setTextEntity(textReport.getTextEntity());
			
			InputStream in = new ByteArrayInputStream(reportFile);
			TextDesignUtil rd = new TextDesignUtil(in);

			List<JRParameter> paramList = rd.getParameters();
			Set<Parameter> icSet = entity.getParameters();

			Set<Parameter> icNewList = new LinkedHashSet<Parameter>();
			for (JRParameter param : paramList) {
				Parameter ic = findListEntity(icSet, param);
				if (ic == null) {
					ic = getParameterValue(param);
				}
				icNewList.add(ic);
			}
			entity.setParameters(icNewList);
		}
		
		
		textReportDao.save(entity);
		return entity.getId();
	}

	public void delTextReport(Long textReportId){
		TextReport textReport = textReportDao.findOne(textReportId);
		Assert.notNull(textReport);
		List<CategoryReport> categories = textReportDao.findCategoryReportByTextReportId(textReportId);
		if (categories != null && !categories.isEmpty()){
			for (CategoryReport categoryReport : categories){
				Set<TextReport> textReports = categoryReport.getTexts();
				if (textReports.isEmpty()) continue;
				textReports.remove(textReport);
				categoryReport.setTexts(textReports);
				categorReportDao.save(categoryReport);
			}
		}
		List<EwcmsJobReport> ewcmsJobReports = textReportDao.findEwcmsJobReportByTextReportId(textReportId);
		if (ewcmsJobReports != null && !ewcmsJobReports.isEmpty()){
			for (EwcmsJobReport ewcmsJobReport : ewcmsJobReports){
				if (ewcmsJobReport.getChartReport() == null){
					ewcmsJobReportDao.delete(ewcmsJobReport);
				}else{
					ewcmsJobReport.setTextReport(null);
					ewcmsJobReportDao.save(ewcmsJobReport);
				}
			}
		}
		textReportDao.delete(textReportId);
	}

	public TextReport findTextReportById(Long textReportId){
		return textReportDao.findOne(textReportId);
	}

	public Iterable<TextReport> findAllTextReport() {
		return textReportDao.findAll();
	}
	
	public Long updTextReportParameter(Long textReportId, Parameter parameter) throws BaseException {
		if (textReportId == null || textReportId.intValue() == 0)
			throw new BaseException("", "报表编号不存在，请重新选择！");
		TextReport text = textReportDao.findOne(textReportId);
		if (text == null)
			throw new BaseException("", "报表不存在，请重新选择！");
		
		parameter = ParameterSetValueUtil.setParametersValue(parameter);
		
		Set<Parameter> parameters = text.getParameters();
		parameters.remove(parameter);
		parameters.add(parameter);
		text.setParameters(parameters);
		
		textReportDao.save(text);
		
		return parameter.getId();
	}
	
	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, textReportDao, TextReport.class);
	}
	
	/**
	 * 把报表文件里的参数转换数据参数
	 * 
	 * @param jrParameter
	 *            报表参数对象
	 * @return Parameters
	 */
	private Parameter getParameterValue(JRParameter jrParameter) {
		Parameter ic = new Parameter();

		ic.setEnName(jrParameter.getName());
		ic.setClassName(jrParameter.getValueClassName());
		if (jrParameter.getDefaultValueExpression() == null){
			ic.setDefaultValue("");
		}else{
			ic.setDefaultValue(jrParameter.getDefaultValueExpression().getText());
		}
		ic.setDescription(jrParameter.getDescription());
		ic.setType(Conversion(jrParameter.getValueClassName()));

		return ic;
	}

	/**
	 * 根据报表参数名查询数据库中的报表参数集合
	 * 
	 * @param icSet
	 *            数据库中的报表参数集合
	 * @param JRParameter
	 *            报表参数
	 * @return ReportParameter
	 */
	private Parameter findListEntity(Set<Parameter> icSet, JRParameter param) {
		for (Parameter ic : icSet) {
			String rpEnName = ic.getEnName();
			String jrEnName = param.getName();
			if (jrEnName.trim().equals(rpEnName.trim())) {
				return ic;
			}
		}
		return null;
	}

	/**
	 * 把类型名转换成枚举
	 * 
	 * @param className
	 *            类型名
	 * @return InputControlEnum 枚举
	 */
	private Parameter.Type Conversion(String className) {
		if (className.toLowerCase().indexOf("boolean") > -1) {
			return Parameter.Type.BOOLEAN;
		}
		return Parameter.Type.TEXT;
	}

}
