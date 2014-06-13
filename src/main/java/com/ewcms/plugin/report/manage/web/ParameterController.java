package com.ewcms.plugin.report.manage.web;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ewcms.plugin.report.manage.service.ChartReportService;
import com.ewcms.plugin.report.manage.service.ParameterService;
import com.ewcms.plugin.report.manage.service.TextReportService;
import com.ewcms.plugin.report.model.ChartReport;
import com.ewcms.plugin.report.model.Parameter;
import com.ewcms.plugin.report.model.TextReport;
import com.ewcms.security.service.ServiceException;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/report/parameter")
public class ParameterController {

	@Autowired
	private ParameterService parameterService;
	@Autowired
	private TextReportService textReportService;
	@Autowired
	private ChartReportService chartReportService;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("typeMap",Parameter.Type.values());
	}
	
	@RequestMapping(value = "/index/{reportType}_{reportId}")
	public String index(@PathVariable(value = "reportType") String reportType, @PathVariable(value = "reportId") Long reportId, Model model){
		model.addAttribute("reportType", reportType);
		model.addAttribute("reportId", reportId);
		return "/plugin/report/parameter/index";
	}
	
	@RequestMapping(value = "/query/{reportType}_{reportId}")
	public @ResponseBody Map<String, Object> query(@PathVariable(value = "reportType")String reportType, @PathVariable(value = "reportId")Long reportId){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Long count = 0L;
		Set<Parameter> parameters = new LinkedHashSet<Parameter>();
		if (reportType.toLowerCase().trim().equals("text")){
			TextReport textReport = textReportService.findTextReportById(reportId);
			parameters = textReport.getParameters();
		}else if (reportType.toLowerCase().trim().equals("chart")){
			ChartReport chartReport = chartReportService.findChartReportById(reportId);
			parameters = chartReport.getParameters();
		}
		resultMap.put("total", count);
		resultMap.put("rows", parameters);
		return resultMap;
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		Parameter parameter = new Parameter();
		if (selections != null && !selections.isEmpty()) {
			parameter = parameterService.findParameterById(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("parameter", parameter);
		return "plugin/report/parameter/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("parameter")Parameter parameter, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (parameter.getId() != null && StringUtils.hasText(parameter.getId().toString())){
				parameterService.updParameter(parameter);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					parameter = parameterService.findParameterById(selections.get(0));
					model.addAttribute("parameter", parameter);
					model.addAttribute("selections", selections);
				}
			}
			model.addAttribute("close",close);
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "plugin/report/parameter/edit";	
	}

}
