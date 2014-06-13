package com.ewcms.plugin.externalds.manager.web;

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
import com.ewcms.plugin.externalds.model.BaseDs;
import com.ewcms.plugin.externalds.model.CustomDs;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/externalds/custom")
public class CustomDsController {
	
	@Autowired
	private BaseDsService baseDsService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "plugin/externalds/custom/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return baseDsService.searchCustomDs(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		BaseDs customDs = new CustomDs();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			customDs = baseDsService.findByBaseDs(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("customDs", customDs);
		return "plugin/externalds/custom/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("beanDs")CustomDs customDs, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (customDs.getId() != null && StringUtils.hasText(customDs.getId().toString())){
				baseDsService.saveOrUpdateBaseDs(customDs);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					customDs = (CustomDs)baseDsService.findByBaseDs(selections.get(0));
					model.addAttribute("customDs", customDs);
					model.addAttribute("selections", selections);
				}
			}else{
				baseDsService.saveOrUpdateBaseDs(customDs);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, customDs.getId());
				model.addAttribute("customDs", new CustomDs());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "plugin/externalds/custom/edit";	
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		for (Long id : selections){
			try{
				baseDsService.deletedBaseDs(id);
			}catch(ServiceException e){
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
    	}
		return "redirect:/plugin/externalds/custom/index";
	}
}
