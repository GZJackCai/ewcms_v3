/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.scheduling.generate.job.report.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.plugin.BaseException;
import com.ewcms.plugin.report.manage.dao.ChartReportDao;
import com.ewcms.plugin.report.manage.dao.TextReportDao;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.TextReport;
import com.ewcms.scheduling.generate.job.JobClassEntity;
import com.ewcms.scheduling.generate.job.report.dao.EwcmsJobReportDao;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobParameter;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;
import com.ewcms.scheduling.manage.SchedulingFac;
import com.ewcms.scheduling.manage.dao.JobClassDao;
import com.ewcms.scheduling.manage.dao.JobInfoDao;
import com.ewcms.scheduling.manage.util.ConversionUtil;
import com.ewcms.scheduling.manage.vo.PageDisplay;
import com.ewcms.scheduling.model.JobClass;
import com.ewcms.scheduling.model.JobInfo;

/**
 * 
 * @author wu_zhijun
 *
 */
@Component
public class EwcmsJobReportService {

	@Autowired
	private EwcmsJobReportDao ewcmsJobReportDAO;
	@Autowired
	private ChartReportDao chartReportDao;
	@Autowired
	private TextReportDao textReportDao;
	@Autowired
	private JobInfoDao jobInfoDao;
	@Autowired
	private JobClassDao jobClassDao;
	@Autowired
	private SchedulingFac schedulingFac;
	
	public Long saveOrUpdateJobReport(Long reportId, PageDisplay vo, String reportType, Set<EwcmsJobParameter> ewcmsJobParameters) throws BaseException {
		TextReport textReport = null;
		ChartReport chartReport = null;
		if (reportType.equals("text")){
			textReport = textReportDao.findOne(reportId);
		}else if (reportType.equals("chart")){
			chartReport = chartReportDao.findOne(reportId);
		}
		
		if (textReport != null || chartReport != null){
			JobInfo jobInfo = new JobInfo();
			if (vo.getJobId() != null && vo.getJobId().intValue() > 0){
				jobInfo = jobInfoDao.findOne(vo.getJobId());
			}
			
			if (jobInfo == null) {
				throw new BaseException("定时任务已经被删除,请重新操作!","定时任务已经被删除,请重新操作!");
			}
			
			jobInfo = ConversionUtil.constructJobInfo(jobInfo,vo);

			EwcmsJobReport jobReport = new EwcmsJobReport();
			if (vo.getJobId() != null && vo.getJobId().intValue() > 0) {
				jobReport.setId(vo.getJobId());
				jobReport.setJobClass(jobInfo.getJobClass());
			}else{
				JobClass jobClass = jobClassDao.findByClassEntity(JobClassEntity.JOB_REPORT);
				if (jobClass == null) {
					jobClass = new JobClass();
					jobClass.setClassEntity(JobClassEntity.JOB_REPORT);
					jobClass.setClassName("报表定时器类");
					jobClass.setDescription("报表定时器类");
					jobClassDao.save(jobClass);
				}
				jobReport.setJobClass(jobClass);
			}
			
			if (reportType.equals("text")){
				if (vo.getOutputFormats() != null && vo.getOutputFormats().length > 0){
					jobReport.setOutputFormat(ConversionUtil.arrayToString(vo.getOutputFormats()));
				}else{
					jobReport.setOutputFormat(String.valueOf(EwcmsJobReport.OUTPUT_FORMAT_PDF));
				}
			}
			jobReport.setEwcmsJobParameters(ewcmsJobParameters);
			jobReport.setDescription(jobInfo.getDescription());
			jobReport.setLabel(jobInfo.getLabel());
			jobReport.setNextFireTime(jobInfo.getNextFireTime());
			jobReport.setOutputLocale(jobInfo.getOutputLocale());
			jobReport.setPreviousFireTime(jobInfo.getPreviousFireTime());
			jobReport.setState(jobInfo.getState());
			jobReport.setTrigger(jobInfo.getTrigger());
			jobReport.setUserName(jobInfo.getUserName());
			jobReport.setVersion(jobInfo.getVersion());
			jobReport.setTextReport(textReport);
			jobReport.setChartReport(chartReport);
			if (jobReport.getId() == null) {
				return schedulingFac.saveScheduleJob(jobReport);
			} else {
				return schedulingFac.updateScheduledJob(jobReport);
			}
		}
		return null;
	}

	public EwcmsJobReport getScheduledJobReport(Long jobId) {
		return ewcmsJobReportDAO.findOne(jobId);
	}

	public EwcmsJobReport getSchedulingByReportId(Long reportId, String reportType) {
		return ewcmsJobReportDAO.findJobReportByReportId(reportId, reportType);
	}

	public List<EwcmsJobParameter> findByJobReportParameterById(Long jobReportId) {
		return ewcmsJobReportDAO.findByJobReportParameterById(jobReportId);
	}
}
