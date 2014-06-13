package com.ewcms.plugin.report.manage.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.plugin.externalds.manager.service.BaseDsService;
import com.ewcms.plugin.report.manage.service.ChartReportService;
import com.ewcms.plugin.report.manage.util.ChartUtil;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/report/chart")
public class ChartReportConroller {
	@Autowired
	private ChartReportService chartReportService;
	@Autowired
	private BaseDsService baseDsService;

	@ModelAttribute
	public void init(Model model) {
		model.addAttribute("typeMap", ChartReport.Type.values());
		model.addAttribute("baseDsList", baseDsService.findAllBaseDs());
		model.addAttribute("fontNameMap", ChartUtil.getFontNameMap());
		model.addAttribute("fontStyleMap", ChartUtil.getFontStyleMap());
		model.addAttribute("fontSizeMap", ChartUtil.getFontSizeMap());
		model.addAttribute("rotateMap", ChartUtil.getRotateMap());
		model.addAttribute("positionMap", ChartUtil.getPositionMap());
		model.addAttribute("alignmentMap", ChartUtil.getAlignmentMap());
	}
	
	@RequestMapping(value = "/index")
	public String index() {
		return "plugin/report/chart/index";
	}

	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return chartReportService.search(params);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		ChartReport chartReport = new ChartReport();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			chartReport = chartReportService.findChartReportById(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("chartReport", chartReport);
		return "plugin/report/chart/edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("chartReport") ChartReport chartReport, @RequestParam(required = false) List<Long> selections, Model model,
			RedirectAttributes redirectAttributes) {
		InputStream in = null;
		try {
			Boolean close = Boolean.FALSE;
			if (chartReport.getId() != null && StringUtils.hasText(chartReport.getId().toString())) {
				chartReportService.updChartReport(chartReport);
				selections.remove(0);
				if (selections == null || selections.isEmpty()) {
					close = Boolean.TRUE;
				} else {
					chartReport = chartReportService.findChartReportById(selections.get(0));
					model.addAttribute("chartReport", chartReport);
					model.addAttribute("selections", selections);
				}
			} else {
				chartReportService.addChartReport(chartReport);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, chartReport.getId());
				model.addAttribute("chartReport", new ChartReport());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close", close);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
				}
				in = null;
			}
		}
		return "plugin/report/chart/edit";
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes) {
		try {
			for (Long id : selections) {
				chartReportService.delChartReport(id);
			}
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/plugin/report/chart/index";
	}
	
	@RequestMapping(value = "/sqlHelp")
	public String sqlHelp(){
		return "plugin/report/chart/sql_help";
	}
}
