package com.ewcms.plugin.report.manage.web;

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

import com.ewcms.plugin.report.manage.service.CategoryReportService;
import com.ewcms.plugin.report.model.CategoryReport;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/report/category")
public class CategoryReportController {
	@Autowired
	private CategoryReportService categoryReportService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "plugin/report/category/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return categoryReportService.queryCategoryReport(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		CategoryReport categoryReport = new CategoryReport();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			categoryReport = categoryReportService.findCategoryReportById(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("categoryReport", categoryReport);
		return "plugin/report/category/edit";
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("categoryReport")CategoryReport categoryReport, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (categoryReport.getId() != null && StringUtils.hasText(categoryReport.getId().toString())){
				categoryReportService.addOrUpdCategoryReport(categoryReport);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					categoryReport = (CategoryReport)categoryReportService.findCategoryReportById(selections.get(0));
					model.addAttribute("beanDs", categoryReport);
					model.addAttribute("selections", selections);
				}
			}else{
				categoryReportService.addOrUpdCategoryReport(categoryReport);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, categoryReport.getId());
				model.addAttribute("beanDs", new CategoryReport());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "plugin/report/category/edit";	
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		for (Long id : selections){
			try{
				categoryReportService.delCategoryReport(id);
			}catch(ServiceException e){
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
    	}
		return "redirect:/plugin/externalds/bean/index";
	}
}
