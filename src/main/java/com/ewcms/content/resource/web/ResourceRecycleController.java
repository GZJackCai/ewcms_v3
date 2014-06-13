package com.ewcms.content.resource.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.content.resource.model.Resource;
import com.ewcms.content.resource.service.ResourceService;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/resource/recycle")
public class ResourceRecycleController {
	@Autowired
	private ResourceService resourceService;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("typeMap", Resource.Type.values());
	}

	
	@RequestMapping(value = "/index")
	public String recycle(){
		return "content/resource/recycle";
	}
	
	@RequestMapping(value = "/revert")
	public String revert(@RequestParam(value = "selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try {
			resourceService.revert(selections.toArray(new Long[0]));
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "资源还原失败");
		}
		return "redirect:/content/resource/recycle/index";
	}
	
	@RequestMapping(value = "/delete")
	public String delete(@RequestParam(value = "selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try {
			resourceService.delete(selections);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "资源删除失败");
		}
		return "redirect:/content/resource/recycle/index";
	}
	
	@RequestMapping(value = "/clear")
	public String clear(RedirectAttributes redirectAttributes){
		try {
			resourceService.clearSoftDelete();
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", "资源清空失败");
		}
		return "redirect:/content/resource/recycle/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return resourceService.searchRecycle(params);
	}
}
