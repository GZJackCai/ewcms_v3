package com.ewcms.scheduling.manage.web;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ewcms.plugin.BaseException;
import com.ewcms.plugin.report.generate.factory.ChartFactory;
import com.ewcms.plugin.report.generate.factory.TextFactory;
import com.ewcms.plugin.report.manage.service.ChartReportService;
import com.ewcms.plugin.report.manage.service.TextReportService;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.TextReport;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobParameter;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;
import com.ewcms.scheduling.generate.job.report.service.EwcmsJobReportService;
import com.ewcms.scheduling.manage.SchedulingFac;
import com.ewcms.scheduling.manage.util.ConversionUtil;
import com.ewcms.scheduling.manage.vo.PageDisplay;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/scheduling/job/report")
public class JobReportController {

	@Autowired
	private SchedulingFac schedulingFac;
	@Autowired
	private EwcmsJobReportService ewcmsJobReportService;
	@Autowired
	private TextFactory textFactory;
	@Autowired
	private ChartFactory chartFactory;
	@Autowired
	private TextReportService textReportService;
	@Autowired
	private ChartReportService chartReportService;

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(value = "reportType") String reportType, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes) {
		if (selections == null || selections.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "不能对报表新增定时任务");
			return "redirect:/plugin/report/" + reportType + "/index";
		} else {
			PageDisplay pageDisplay = initPageDisplay(selections.get(0), reportType);
			model.addAttribute("selections", selections);
			model.addAttribute("pageDisplay", pageDisplay);
			return "scheduling/jobinfo/edit_report";
		}
	}

	private PageDisplay initPageDisplay(Long reportId, String reportType) {
		PageDisplay pageDisplay = new PageDisplay();
		
		EwcmsJobReport ewcmsJobReport = ewcmsJobReportService.getSchedulingByReportId(reportId, reportType);
		if (ewcmsJobReport != null) {
			pageDisplay = ConversionUtil.constructPage(ewcmsJobReport);
			if (reportType.equals("text")) {
				TextReport text = ewcmsJobReport.getTextReport();
				pageDisplay.setReportId(text.getId());
				pageDisplay.setReportName(text.getName());
				pageDisplay.setPageShowParams(ConversionUtil.coversionParameterFromPage(
						ewcmsJobReportService.findByJobReportParameterById(ewcmsJobReport.getId()), textFactory.textParameters(text)));
				pageDisplay.setOutputFormats(ConversionUtil.stringToArray(((EwcmsJobReport) ewcmsJobReport).getOutputFormat()));
			} else if (reportType.equals("chart")) {
				ChartReport chart = ewcmsJobReport.getChartReport();
				pageDisplay.setReportId(chart.getId());
				pageDisplay.setReportName(chart.getName());
				pageDisplay.setPageShowParams(ConversionUtil.coversionParameterFromPage(
						ewcmsJobReportService.findByJobReportParameterById(ewcmsJobReport.getId()), chartFactory.chartParameters(chart)));
			}
		} else {
			if (reportType.equals("text")) {
				TextReport text = textReportService.findTextReportById(reportId);
				pageDisplay.setLabel(text.getName());
				pageDisplay.setReportName(text.getName());
				pageDisplay.setPageShowParams(textFactory.textParameters(text));
			} else if (reportType.equals("chart")) {
				ChartReport chart = chartReportService.findChartReportById(reportId);
				pageDisplay.setLabel(chart.getName());
				pageDisplay.setReportName(chart.getName());
				pageDisplay.setPageShowParams(chartFactory.chartParameters(chart));
			}
		}
		pageDisplay.setReportId(reportId);
		pageDisplay.setReportType(reportType);
		
		return pageDisplay;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("pageDisplay") PageDisplay pageDisplay, @RequestParam(required = false) List<Long> selections, Model model,
			HttpServletRequest request, RedirectAttributes redirectAttributes) {
		Long reportId = pageDisplay.getReportId();
		String reportType = pageDisplay.getReportType();
		try {
			Set<EwcmsJobParameter> ewcmsJobParameters = new LinkedHashSet<EwcmsJobParameter>();
			if (reportType.equals("text")) {
				TextReport text = textReportService.findTextReportById(reportId);
				ewcmsJobParameters = ConversionUtil.pageToJob(new LinkedHashSet<EwcmsJobParameter>(), text.getParameters(), request);
			} else if (reportType.equals("chart")) {
				ChartReport chart = chartReportService.findChartReportById(reportId);
				ewcmsJobParameters = ConversionUtil.pageToJob(new LinkedHashSet<EwcmsJobParameter>(), chart.getParameters(), request);
			}

			Boolean close = Boolean.FALSE;
			if (pageDisplay.getJobId() != null && StringUtils.hasText(pageDisplay.getJobId().toString())) {
				ewcmsJobReportService.saveOrUpdateJobReport(reportId, pageDisplay, reportType, ewcmsJobParameters);
				selections.remove(0);
				if (selections == null || selections.isEmpty()) {
					close = Boolean.TRUE;
				} else {
					pageDisplay = initPageDisplay(selections.get(0), reportType);
					model.addAttribute("pageDisplay", pageDisplay);
					model.addAttribute("selections", selections);
				}
				model.addAttribute("close", close);
				return "scheduling/jobinfo/edit_report";
			} else {
				ewcmsJobReportService.saveOrUpdateJobReport(reportId, pageDisplay, reportType, ewcmsJobParameters);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, reportId);
				pageDisplay = initPageDisplay(reportId, reportType);
				model.addAttribute("pageDisplay", pageDisplay);
				model.addAttribute("selections", selections);
				
				redirectAttributes.addAttribute("reportType", reportType).addAttribute("selections", selections).addFlashAttribute("message", "保存成功");
				return "redirect:/scheduling/job/report/edit";
			}
		} catch (BaseException e) {
			redirectAttributes.addAttribute("reportType", reportType).addAttribute("selections", selections).addFlashAttribute("message", e.getMessage());
			return "redirect:/scheduling/job/report/edit";
		}
	}
	
}
