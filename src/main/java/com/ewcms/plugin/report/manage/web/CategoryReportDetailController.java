package com.ewcms.plugin.report.manage.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.ewcms.plugin.report.manage.service.CategoryReportService;
import com.ewcms.plugin.report.manage.service.ChartReportService;
import com.ewcms.plugin.report.manage.service.TextReportService;
import com.ewcms.plugin.report.model.CategoryReport;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.TextReport;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.vo.ComboBox;
import com.ewcms.web.vo.PropertyGrid;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/report/categorydetail")
public class CategoryReportDetailController {

	private static final String TEXT_GROUP_TITLE = "文字报表";
	private static final String CHART_GROUP_TITLE = "图型报表";

	@Autowired
	private CategoryReportService categoryReportService;
	@Autowired
	private TextReportService textReportService;
	@Autowired
	private ChartReportService chartReportService;

	@RequestMapping(value = "/index/{categoryId}")
	public String index(@PathVariable(value = "categoryId") Long categoryId, Model model) {
		model.addAttribute("categoryId", categoryId);
		return "plugin/report/category/detail/index";
	}

	@RequestMapping(value = "/query/{categoryId}")
	public @ResponseBody Map<String, Object> query(@PathVariable(value = "categoryId") Long categoryId, @ModelAttribute QueryParameter params) {
		CategoryReport categoryReport = categoryReportService.findCategoryReportById(categoryId);
		List<PropertyGrid> propertyGrids = new ArrayList<PropertyGrid>();
		if (categoryReport != null) {
			Set<TextReport> textReports = categoryReport.getTexts();
			for (TextReport textReport : textReports) {
				PropertyGrid propertyGrid = new PropertyGrid(textReport.getName(), textReport.getRemarks(), TEXT_GROUP_TITLE, null);
				propertyGrids.add(propertyGrid);
			}
			Set<ChartReport> chartReports = categoryReport.getCharts();
			for (ChartReport chartReport : chartReports) {
				PropertyGrid propertyGrid = new PropertyGrid(chartReport.getName(), chartReport.getRemarks(), CHART_GROUP_TITLE, null);
				propertyGrids.add(propertyGrid);
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", propertyGrids.size());
		resultMap.put("rows", propertyGrids);
		return resultMap;
	}

	@RequestMapping(value = "/edit/{categoryId}", method = RequestMethod.GET)
	public String edit(@PathVariable(value = "categoryId") Long categoryId, Model model) {
		CategoryReport categoryReport = categoryReportService.findCategoryReportById(categoryId);
		model.addAttribute("categoryReport", categoryReport);
		return "plugin/report/category/detail/edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam(required = false) List<Long> textReportIds, @RequestParam(required = false) List<Long> chartReportIds,
			@ModelAttribute("categoryReport") CategoryReport categoryReport, Model model, RedirectAttributes redirectAttributes) {
		try {
			Boolean close = Boolean.FALSE;
			if (categoryReport.getId() != null && StringUtils.hasText(categoryReport.getId().toString())) {
				categoryReportService.addReportToCategories(categoryReport.getId(), textReportIds, chartReportIds);
				close = Boolean.TRUE;
			}
			model.addAttribute("close", close);
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "plugin/report/category/detail/edit";
	}
	
	@RequestMapping(value = "/text/{categoryId}")
	public @ResponseBody List<ComboBox> findTextReport(@PathVariable("categoryId")Long categoryId){
		Iterable<TextReport> texts = textReportService.findAllTextReport();
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		if (texts != null) {
			ComboBox comboBox = null;
			for (TextReport text : texts) {
				comboBox = new ComboBox();
				comboBox.setId(text.getId());
				comboBox.setText(text.getName());
				Boolean isEntity = categoryReportService.findTextIsEntityByTextAndCategory(text.getId(),categoryId);
				if (isEntity)comboBox.setSelected(true);
				comboBoxs.add(comboBox);
			}
		}
		return comboBoxs;
	}
	
	@RequestMapping(value = "/chart/{categoryId}")
	public @ResponseBody List<ComboBox> findChartReport(@PathVariable("categoryId")Long categoryId) {
		Iterable<ChartReport> charts = chartReportService.findAllChartReport();
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		if (charts != null) {
			ComboBox comboBox = null;
			for (ChartReport chart : charts) {
				comboBox = new ComboBox();
				comboBox.setId(chart.getId());
				comboBox.setText(chart.getName());
				Boolean isEntity = categoryReportService.findChartIsEntityByChartAndCategory(chart.getId(), categoryId);
				if (isEntity) comboBox.setSelected(true);
				comboBoxs.add(comboBox);
			}
		}
		return comboBoxs;
	}
}
