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
import com.ewcms.plugin.externalds.model.BeanDs;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/externalds/bean")
public class BeanDsController {
	
	@Autowired
	private BaseDsService baseDsService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "plugin/externalds/bean/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return baseDsService.searchBeanDs(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		BaseDs beanDs = new BeanDs();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			beanDs = baseDsService.findByBaseDs(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("beanDs", beanDs);
		return "plugin/externalds/bean/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("beanDs")BeanDs beanDs, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (beanDs.getId() != null && StringUtils.hasText(beanDs.getId().toString())){
				baseDsService.saveOrUpdateBaseDs(beanDs);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					beanDs = (BeanDs)baseDsService.findByBaseDs(selections.get(0));
					model.addAttribute("beanDs", beanDs);
					model.addAttribute("selections", selections);
				}
			}else{
				baseDsService.saveOrUpdateBaseDs(beanDs);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, beanDs.getId());
				model.addAttribute("beanDs", new BeanDs());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "plugin/externalds/bean/edit";	
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
		return "redirect:/plugin/externalds/bean/index";
	}
}
