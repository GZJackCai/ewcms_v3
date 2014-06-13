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

import com.ewcms.content.particular.model.IndustryCode;
import com.ewcms.content.particular.service.IndustryCodeService;
import com.ewcms.plugin.BaseException;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/particular/industrycode")
public class IndustryCodeController {
	@Autowired
	private IndustryCodeService industryCodeService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "content/particular/industrycode/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return industryCodeService.search(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		IndustryCode industryCode = new IndustryCode();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			industryCode = industryCodeService.findIndustryCodeById(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("industryCode", industryCode);
		return "content/particular/industrycode/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "industryCode")IndustryCode industryCode, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (industryCode.getId() != null && StringUtils.hasText(industryCode.getId().toString())){
				industryCodeService.updIndustryCode(industryCode);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					industryCode = industryCodeService.findIndustryCodeById(selections.get(0));
					model.addAttribute("industryCode", industryCode);
					model.addAttribute("selections", selections);
				}
			}else{
				industryCodeService.addIndustryCode(industryCode);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, industryCode.getId());
				model.addAttribute("industryCode", new IndustryCode());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(BaseException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "content/particular/industrycode/edit";	
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long id : selections){
				industryCodeService.delIndustryCode(id);
	    	}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/particular/industrycode/index";
	}
}
