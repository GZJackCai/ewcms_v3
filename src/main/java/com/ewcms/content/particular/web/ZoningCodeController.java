package com.ewcms.content.particular.web;

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

import com.ewcms.content.particular.model.ZoningCode;
import com.ewcms.content.particular.service.ZoningCodeService;
import com.ewcms.plugin.BaseException;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/particular/zoningcode")
public class ZoningCodeController {
	@Autowired
	private ZoningCodeService zoningCodeService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "content/particular/zoningcode/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return zoningCodeService.search(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		ZoningCode zoningCode = new ZoningCode();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			zoningCode = zoningCodeService.findZoningCodeById(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("zoningCode", zoningCode);
		return "content/particular/zoningcode/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "zoningCode")ZoningCode zoningCode, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (zoningCode.getId() != null && StringUtils.hasText(zoningCode.getId().toString())){
				zoningCodeService.updZoningCode(zoningCode);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					zoningCode = zoningCodeService.findZoningCodeById(selections.get(0));
					model.addAttribute("zoningCode", zoningCode);
					model.addAttribute("selections", selections);
				}
			}else{
				zoningCodeService.addZoningCode(zoningCode);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, zoningCode.getId());
				model.addAttribute("zoningCode", new ZoningCode());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(BaseException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "content/particular/zoningcode/edit";	
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long id : selections){
				zoningCodeService.delZoningCode(id);
	    	}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/particular/zoningCode/index";
	}
}
