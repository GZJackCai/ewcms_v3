package com.ewcms.scheduling.manage.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.plugin.BaseException;
import com.ewcms.plugin.report.generate.factory.ChartFactory;
import com.ewcms.plugin.report.generate.factory.TextFactory;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.TextReport;
import com.ewcms.scheduling.generate.job.JobClassEntity;
import com.ewcms.scheduling.generate.job.channel.model.EwcmsJobChannel;
import com.ewcms.scheduling.generate.job.crawler.model.EwcmsJobCrawler;
import com.ewcms.scheduling.generate.job.report.model.EwcmsJobReport;
import com.ewcms.scheduling.generate.job.report.service.EwcmsJobReportService;
import com.ewcms.scheduling.manage.SchedulingFac;
import com.ewcms.scheduling.manage.util.ConversionUtil;
import com.ewcms.scheduling.manage.vo.PageDisplay;
import com.ewcms.scheduling.model.JobClass;
import com.ewcms.scheduling.model.JobInfo;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/scheduling/jobinfo")
public class JobInfoController {

	@Autowired
	private SchedulingFac schedulingFac;
	@Autowired
	private EwcmsJobReportService ewcmsJobReportService;
	@Autowired
	private TextFactory textFactory;
	@Autowired
	private ChartFactory chartFactory;

	@ModelAttribute
	public void init(Model model){
		
	}
	
	@RequestMapping(value = "/index")
	public String index() {
		return "scheduling/jobinfo/index";
	}

	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return schedulingFac.search(params);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
			model.addAttribute("pageDisplay", new PageDisplay());
		} else {
			JobInfo jobInfo = schedulingFac.getScheduledJob(selections.get(0));
			PageDisplay pageDisplay = initPageDisplay(jobInfo, model);
			model.addAttribute("selections", selections);
			model.addAttribute("pageDisplay", pageDisplay);
		}
		return "scheduling/jobinfo/edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("pageDisplay") PageDisplay pageDisplay, @RequestParam(required = false) List<Long> selections, Model model,
			RedirectAttributes redirectAttributes) {
		JobInfo jobInfo = new JobInfo();
		try {
			if (pageDisplay.getJobId() != null && pageDisplay.getJobId().intValue() > 0) {
				jobInfo = schedulingFac.getScheduledJob(pageDisplay.getJobId());
			}
			if (pageDisplay.getJobClassId() != null && pageDisplay.getJobClassId().intValue() > 0) {
				JobClass alqcJobClass = schedulingFac.findByJobClass(pageDisplay.getJobClassId());
				jobInfo.setJobClass(alqcJobClass);
			}
			jobInfo = ConversionUtil.constructJobInfo(jobInfo, pageDisplay);

			Boolean close = Boolean.FALSE;
			if (jobInfo.getId() != null && StringUtils.hasText(jobInfo.getId().toString())) {
				schedulingFac.updateScheduledJob(jobInfo);
				selections.remove(0);
				if (selections == null || selections.isEmpty()) {
					close = Boolean.TRUE;
				} else {
					jobInfo = schedulingFac.getScheduledJob(selections.get(0));
					pageDisplay = initPageDisplay(jobInfo, model);
					model.addAttribute("pageDisplay", pageDisplay);
					model.addAttribute("selections", selections);
				}
			} else {
				schedulingFac.saveScheduleJob(jobInfo);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, jobInfo.getId());
				model.addAttribute("pageDisplay", new PageDisplay());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close", close);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "scheduling/jobinfo/edit";
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes) {
		try {
			for (Long id : selections) {
				schedulingFac.deletedScheduledJob(id);
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/scheduling/jobinfo/index";
	}

	@RequestMapping(value = "/pause/{jobId}")
	public String pauseJob(@PathVariable(value = "jobId") Long jobId, RedirectAttributes redirectAttributes) {
		try {
			schedulingFac.pauseJob(jobId);
		} catch (BaseException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/scheduling/jobinfo/index";
	}

	@RequestMapping(value = "/resumed/{jobId}s")
	public String resumedJob(@PathVariable(value = "jobId") Long jobId, RedirectAttributes redirectAttributes) {
		try {
			schedulingFac.resumedJob(jobId);
		} catch (BaseException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/scheduling/jobinfo/index";
	}
	
	private PageDisplay initPageDisplay(JobInfo jobInfo, Model model) {
		PageDisplay pageDisplay = ConversionUtil.constructPage(jobInfo);

		if (jobInfo instanceof EwcmsJobChannel) {
			pageDisplay.setSubChannel(((EwcmsJobChannel) jobInfo).getSubChannel());
			pageDisplay.setIsJobChannel(true);
		} else if (jobInfo instanceof EwcmsJobReport) {
			TextReport textReport = ((EwcmsJobReport) jobInfo).getTextReport();
			ChartReport chartReport = ((EwcmsJobReport) jobInfo).getChartReport();
			pageDisplay = ConversionUtil.constructPage((EwcmsJobReport) jobInfo);
			if (textReport != null) {
				pageDisplay.setReportId(textReport.getId());
				pageDisplay.setReportName(textReport.getName());
				pageDisplay.setReportType("text");
				pageDisplay.setPageShowParams(ConversionUtil.coversionParameterFromPage(ewcmsJobReportService.findByJobReportParameterById(jobInfo.getId()),
						textFactory.textParameters(textReport)));
				pageDisplay.setOutputFormats(ConversionUtil.stringToArray(((EwcmsJobReport) jobInfo).getOutputFormat()));
			} else if (chartReport != null) {
				pageDisplay.setReportId(chartReport.getId());
				pageDisplay.setReportName(chartReport.getName());
				pageDisplay.setReportType("chart");
				pageDisplay.setPageShowParams(ConversionUtil.coversionParameterFromPage(ewcmsJobReportService.findByJobReportParameterById(jobInfo.getId()),
						chartFactory.chartParameters(chartReport)));
			}
			pageDisplay.setIsJobReport(true);
			model.addAttribute("pageShowParams", pageDisplay.getPageShowParams());
		} else if (jobInfo instanceof EwcmsJobCrawler) {
			pageDisplay.setIsJobCrawler(true);
		}

		List<JobClass> jobClassList = schedulingFac.findByAllJobClass();
		if (!pageDisplay.getIsJobChannel()) {
			JobClass jobClass = schedulingFac.findByJobClassByClassEntity(JobClassEntity.JOB_CHANNEL);
			jobClassList.remove(jobClass);
		}
		if (!pageDisplay.getIsJobReport()) {
			JobClass jobClass = schedulingFac.findByJobClassByClassEntity(JobClassEntity.JOB_REPORT);
			jobClassList.remove(jobClass);
		}
		if (!pageDisplay.getIsJobCrawler()) {
			//JobClass jobClass = schedulingFac.findByJobClassByClassEntity(JobClassEntity.JOB_CRAWLER);
			//jobClassList.remove(jobClass);
		}
		model.addAttribute("jobClassList", jobClassList);
		return pageDisplay;
	}
	
	
}
