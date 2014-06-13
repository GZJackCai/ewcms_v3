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
import com.ewcms.plugin.externalds.model.JndiDs;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/externalds/jndi")
public class JndiDsController {
	
	@Autowired
	private BaseDsService baseDsService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "plugin/externalds/jndi/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return baseDsService.searchJndiDs(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		BaseDs jndiDs = new JndiDs();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			jndiDs = baseDsService.findByBaseDs(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("jndiDs", jndiDs);
		return "plugin/externalds/jndi/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("jndiDs")JndiDs jndiDs, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (jndiDs.getId() != null && StringUtils.hasText(jndiDs.getId().toString())){
				baseDsService.saveOrUpdateBaseDs(jndiDs);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					jndiDs = (JndiDs)baseDsService.findByBaseDs(selections.get(0));
					model.addAttribute("jndiDs", jndiDs);
					model.addAttribute("selections", selections);
				}
			}else{
				baseDsService.saveOrUpdateBaseDs(jndiDs);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, jndiDs.getId());
				model.addAttribute("jndiDs", new JndiDs());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "plugin/externalds/jndi/edit";	
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
		return "redirect:/plugin/externalds/jndi/index";
	}
}
