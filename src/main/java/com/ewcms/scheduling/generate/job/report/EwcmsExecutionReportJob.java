/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.scheduling.generate.job.report;

import java.util.Calendar;
import java.util.LinkedHashMap;

import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.plugin.report.generate.factory.ChartFactory;
import com.ewcms.plugin.report.generate.factory.TextFactory;
import com.ewcms.plugin.report.manage.service.RepositoryService;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.Repository;
import com.ewcms.plugin.report.model.TextReport;
import com.ewcms.plugin.report.model.TextReport.Type;
import com.ewcms.scheduling.generate.job.BaseEwcmsExecutionJob;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobParameter;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;
import com.ewcms.scheduling.generate.job.report.service.EwcmsJobReportService;

/**
 * 执行报表Job
 * 
 * @author 吴智俊
 */
public class EwcmsExecutionReportJob extends BaseEwcmsExecutionJob {

	private static final Logger logger = LoggerFactory.getLogger(EwcmsExecutionReportJob.class);
    
    public static final String JOB_REPORT_SERVICE = "ewcmsJobReportFac";
    public static final String REPOSITORY_SERVICE = "repositoryService";
    public static final String CHART_FACTORY = "chartFactory";
    public static final String TEXT_FACTORY = "textFactory";

    private EwcmsJobReport ewcmsJobReport;

    protected void jobExecute(Long jobId) throws Exception {
        ewcmsJobReport = getEwcmsJobReportService().getScheduledJobReport(jobId);
        if (ewcmsJobReport != null){
	        String outputFormat = ewcmsJobReport.getOutputFormat();
	        String label = ewcmsJobReport.getLabel();
	        String description = ewcmsJobReport.getDescription();
	        
	        logger.info("生成报表开始...");
	        if (outputFormat != null && outputFormat.length() > 0) {
	            StringTokenizer tokenizerOutput = new StringTokenizer(outputFormat, ",", false);
	            while (tokenizerOutput.hasMoreElements()) {
	                Integer iOutput = Integer.valueOf(tokenizerOutput.nextToken());
	                attachOutput(iOutput, label, description);
	            }
	        }else{
	        	attachOutput(null, label, description);
	        }
	        logger.info("生成报表结束.");
        }
    }

	protected void jobClear() {
		ewcmsJobReport = null;		
	}
	
	private void attachOutput(Integer iOutput, String label, String description) throws Exception {
        Set<EwcmsJobParameter> jobParams = ewcmsJobReport.getEwcmsJobParameters();
        Map<String, String> parameters = new LinkedHashMap<String, String>();
        for (EwcmsJobParameter jobParameter : jobParams) {
            String enName = jobParameter.getParameter().getEnName();
            String value = jobParameter.getParameterValue();
            parameters.put(enName, value);
        }

        TextReport textReport = ewcmsJobReport.getTextReport();
        ChartReport chartReport = ewcmsJobReport.getChartReport();

        String fileType = "";
        byte[] bytes = null;
        if (textReport != null) {
            TextFactory reportEngine = getTextFactory();
            Type textEnum = conversionToEnum(iOutput);
            bytes = reportEngine.export(parameters, textReport, textEnum, null, null);
            fileType = textEnum.getDescription();
        }

        if (chartReport != null) {
            fileType = "PNG";
            ChartFactory chartEngine = getChartFactory();
            bytes = chartEngine.export(chartReport, parameters);
        }

        if (bytes != null){
        	RepositoryService repositoryService = getRepositoryService();
	        Repository repository = new Repository();
	        
	        repository.setEntity(bytes);
	        repository.setType(fileType);
	        repository.setName(label);
	        repository.setDescription(description);
	        repository.setUpdateDate(new java.sql.Date(Calendar.getInstance().getTime().getTime()));
	        repositoryService.addRepository(repository);
        }
    }

    private EwcmsJobReportService getEwcmsJobReportService(){
    	return (EwcmsJobReportService) applicationContext.getBean(JOB_REPORT_SERVICE);
    }
    
    private RepositoryService getRepositoryService(){
    	return (RepositoryService) applicationContext.getBean(REPOSITORY_SERVICE);
    }
    
    private ChartFactory getChartFactory() {
        return (ChartFactory) applicationContext.getBean(CHART_FACTORY);
    }

    private TextFactory getTextFactory() {
        return (TextFactory) applicationContext.getBean(TEXT_FACTORY);
    }
    
    private Type conversionToEnum(Integer iOutput) {
        if (iOutput.intValue() == Type.HTML.ordinal()) {
            return Type.HTML;
        } else if (iOutput.intValue() == Type.PDF.ordinal()) {
            return Type.PDF;
        } else if (iOutput.intValue() == Type.XLS.ordinal()) {
            return Type.XLS;
        } else if (iOutput.intValue() == Type.RTF.ordinal()) {
            return Type.RTF;
        } else if (iOutput.intValue() == Type.XML.ordinal()) {
            return Type.XML;
        } else {
            return Type.PDF;
        }
    }
}
