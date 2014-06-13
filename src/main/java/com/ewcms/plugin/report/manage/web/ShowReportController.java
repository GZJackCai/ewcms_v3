package com.ewcms.plugin.report.manage.web;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ewcms.plugin.report.generate.factory.ChartFactory;
import com.ewcms.plugin.report.generate.factory.TextFactory;
import com.ewcms.plugin.report.generate.util.ParamConversionPage;
import com.ewcms.plugin.report.generate.vo.PageShowParam;
import com.ewcms.plugin.report.generate.vo.ParameterBuilder;
import com.ewcms.plugin.report.manage.service.CategoryReportService;
import com.ewcms.plugin.report.manage.service.ChartReportService;
import com.ewcms.plugin.report.manage.service.TextReportService;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.TextReport;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/report/show")
public class ShowReportController {
	@Autowired
	private CategoryReportService categoryReportService;
	@Autowired
	private TextReportService textReportService;
	@Autowired
	private TextFactory textFactory;
	@Autowired
	private ChartReportService chartReportService;
	@Autowired
	private ChartFactory chartFactory;

	@ModelAttribute
	public void init(Model model) {
		model.addAttribute("categoryReportList", categoryReportService.findAllCategoryReport());
		model.addAttribute("textReportTypeMap", TextReport.Type.values());
	}

	@RequestMapping(value = "/index")
	public String index() {
		return "plugin/report/show/index";
	}

	@RequestMapping(value = "/paraset/{reportType}_{reportId}")
	public String parameterSet(@PathVariable(value = "reportId") Long reportId, @PathVariable(value = "reportType") String reportType, Model model) {
		List<PageShowParam> pageShowParams = new ArrayList<PageShowParam>();
		if (reportType.toLowerCase().trim().equals("text")) {
			pageShowParams = ParamConversionPage.conversion(textReportService.findTextReportById(reportId).getParameters());
		} else if (reportType.toLowerCase().trim().equals("chart")) {
			pageShowParams = ParamConversionPage.conversion(chartReportService.findChartReportById(reportId).getParameters());
		}
		model.addAttribute("pageShowParams", pageShowParams);
		ParameterBuilder parameterBuilder = new ParameterBuilder();
		parameterBuilder.setReportId(reportId);
		parameterBuilder.setReportType(reportType);
		model.addAttribute("parameterBuilder", parameterBuilder);
		return "plugin/report/show/paraset";
	}

	@RequestMapping(value = "/build")
	public void build(@ModelAttribute(value = "parameterBuilder") ParameterBuilder parameterBuilder, HttpServletRequest request, HttpServletResponse response) {
		if (parameterBuilder.getReportType().equals("text")){
			buildText(parameterBuilder.getParamMap(), parameterBuilder.getReportId(), parameterBuilder.getTextType(), request, response);
		}else if (parameterBuilder.getReportType().equals("chart")){
			buildChart(parameterBuilder.getParamMap(), parameterBuilder.getReportId(), response);
		}
	}
	
	private void buildText(Map<String, String> paramMap, Long reportId, TextReport.Type textReportTypeMap, HttpServletRequest request, HttpServletResponse response) {
		PrintWriter pw = null;
		InputStream in = null;
		try {
			TextReport report = textReportService.findTextReportById(reportId);

			pw = response.getWriter();
			response.reset();// 清空输出
			response.setContentLength(0);
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html; charset=utf-8");
			byte[] bytes = textFactory.export(paramMap, report, textReportTypeMap, response, request);
			in = new ByteArrayInputStream(bytes);
			int len = 0;
			while ((len = in.read()) > -1) {
				pw.write(len);
			}
			pw.flush();
		} catch (Exception e) {
			// log.error(e.toString());
		} finally {
			if (pw != null) {
				try {
					pw.close();
					pw = null;
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception e) {
				}
			}
		}
	}

	private void buildChart(Map<String, String> paramMap, Long reportId, HttpServletResponse response) {
		PrintWriter pw = null;
		InputStream in = null;
		try {
			ChartReport chart = chartReportService.findChartReportById(reportId);

			pw = response.getWriter();

			response.reset();// 清空输出
			response.setContentLength(0);
			byte[] bytes = chartFactory.export(chart, paramMap);
			response.setContentLength(bytes.length);
			response.setHeader("Content-Type", "image/png");
			in = new ByteArrayInputStream(bytes);
			int len = 0;
			while ((len = in.read()) > -1) {
				pw.write(len);
			}
			pw.flush();
		} catch (Exception e) {
			// log.error(e.toString());
		} finally {
			if (pw != null) {
				try {
					pw.close();
					pw = null;
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception e) {
				}
			}
		}
	}
}
