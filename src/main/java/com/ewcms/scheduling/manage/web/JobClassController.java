package com.ewcms.scheduling.manage.web;

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

import com.ewcms.plugin.externalds.model.BeanDs;
import com.ewcms.scheduling.manage.service.JobClassService;
import com.ewcms.scheduling.model.JobClass;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/scheduling/jobclass")
public class JobClassController {

	@Autowired
	private JobClassService jobClassService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "scheduling/jobclass/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return jobClassService.search(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		JobClass jobClass = new JobClass();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			jobClass = jobClassService.findByJobClass(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("jobClass", jobClass);
		return "scheduling/jobclass/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("jobClass")JobClass jobClass, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (jobClass.getId() != null && StringUtils.hasText(jobClass.getId().toString())){
				jobClassService.saveJobClass(jobClass);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					jobClass = jobClassService.findByJobClass(selections.get(0));
					model.addAttribute("jobClass", jobClass);
					model.addAttribute("selections", selections);
				}
			}else{
				jobClassService.updateJobClass(jobClass);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, jobClass.getId());
				model.addAttribute("beanDs", new BeanDs());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(Exception e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "plugin/externalds/bean/edit";	
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long id : selections){
				jobClassService.deletedJobClass(id);
	    	}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/plugin/externalds/bean/index";
	}
}
