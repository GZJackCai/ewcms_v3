package com.ewcms.plugin.citizen.web;

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

import com.ewcms.plugin.citizen.model.Citizen;
import com.ewcms.plugin.citizen.service.CitizenService;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/citizen")
public class CitizenController {
	
	@Autowired
	private CitizenService citizenService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "plugin/citizen/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return citizenService.search(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		Citizen citizen = new Citizen();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			citizen = citizenService.getCitizen(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("citizen", citizen);
		return "plugin/citizen/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "citizen")Citizen citizen, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (citizen.getId() != null && StringUtils.hasText(citizen.getId().toString())){
				citizenService.updCitizen(citizen);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					citizen = citizenService.getCitizen(selections.get(0));
					model.addAttribute("citizen", citizen);
					model.addAttribute("selections", selections);
				}
			}else{
				citizenService.addCitizen(citizen);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, citizen.getId());
				model.addAttribute("citizen", new Citizen());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "plugin/citizen/edit";	
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long id : selections){
				citizenService.delCitizen(id);
	    	}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/plugin/citizen/index";
	}
}
