package com.ewcms.content.vote.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.content.vote.model.Subject;
import com.ewcms.content.vote.model.SubjectItem;
import com.ewcms.content.vote.service.SubjectItemService;
import com.ewcms.content.vote.service.SubjectService;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/vote/subjectitem")
public class SubjectItemController {
	
	@Autowired
	private SubjectService subjectService;
	@Autowired
	private SubjectItemService subjectItemService;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("statusMap", SubjectItem.Status.values());
		
		Map<String, String> inputMap = new HashMap<String, String>();
		inputMap.put(SubjectItem.Status.SINGLETEXT.name(), SubjectItem.Status.SINGLETEXT.getDescription());
		inputMap.put(SubjectItem.Status.MULTITEXT.name(), SubjectItem.Status.MULTITEXT.getDescription());
		model.addAttribute("inputMap", inputMap);
	}

	@RequestMapping(value = "/index/{subjectId}")
	public String index(@PathVariable(value = "subjectId") Long subjectId, Model model){
		model.addAttribute("subjectId", subjectId);
		return "content/vote/subjectitem/index";
	}

	@RequestMapping(value = "/query/{subjectId}")
	public @ResponseBody
	Map<String, Object> query(@PathVariable(value = "subjectId") Long subjectId, @ModelAttribute QueryParameter params) {
		return subjectItemService.search(params, subjectId);
	}

	@RequestMapping(value = "/edit/{subjectId}",method = RequestMethod.GET)
	public String edit(@PathVariable(value = "subjectId") Long subjectId, @RequestParam(required = false) List<Long> selections, Model model) {
		SubjectItem subjectItem = new SubjectItem();
		if (selections == null || selections.isEmpty()) {
			Subject subject = subjectService.findSubject(subjectId);
			subjectItem.setSubject(subject);
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			subjectItem = subjectItemService.findSubjectItem(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("subjectItem", subjectItem).addAttribute("subjectId", subjectId);
		return "content/vote/subjectitem/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "subjectItem") SubjectItem subjectItem, @RequestParam(required = false) List<Long> selections, @RequestParam(value = "subjectId") Long subjectId, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (subjectItem.getId() != null && StringUtils.hasText(subjectItem.getId().toString())){
				subjectItemService.addSubjectItem(subjectId, subjectItem);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					subjectItem = subjectItemService.findSubjectItem(selections.get(0));
					model.addAttribute("subjectItem", subjectItem);
					model.addAttribute("selections", selections);
				}
			}else{
				subjectItemService.addSubjectItem(subjectId, subjectItem);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, subjectItem.getId());
				
				SubjectItem newSubjectItem = new SubjectItem();
				Subject subject = subjectService.findSubject(subjectId);
				newSubjectItem.setSubject(subject);
				model.addAttribute("subjectItem", newSubjectItem);
				model.addAttribute("selections", selections);
			}
			model.addAttribute("subjectId", subjectId);
			model.addAttribute("close",close);
		}catch(Exception e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "content/vote/subjectitem/edit";	
	}
	
	@RequestMapping(value = "/delete/{subjectId}", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@PathVariable(value = "subjectId") Long subjectId, @RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long id : selections){
				subjectItemService.delSubjectItem(id);
	    	}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/vote/subjectitem/index/" + subjectId;
	}
	
	@RequestMapping(value = "/up/{subjectItemId}")
	public @ResponseBody String up(@RequestParam(value = "subjectItemId") Long subjectItemId){
		String result = "上移";
		try{
			subjectItemService.upSubjectItem(subjectItemId, subjectItemId);
			result += "成功";
		}catch(Exception e){
			result += "失败";
		}
		return result;
	}
	
	@RequestMapping(value = "/down/{subjectItemId}")
	public @ResponseBody String down(@RequestParam(value = "subjectItemId") Long subjectItemId){
		String result = "下移";
		try{
			subjectItemService.downSubjectItem(subjectItemId, subjectItemId);
			result += "成功";
		}catch(Exception e){
			result += "失败";
		}
		return result;
	}
	
	@RequestMapping(value = "/editopt/{subjectId}")
	public String editopt(@PathVariable(value = "subjectId")Long subjectId, Model model){
		SubjectItem subjectItem = subjectItemService.findSubjectItemBySubjectAndInputStatus(subjectId);
		if (subjectItem == null) {
			subjectItem = new SubjectItem();
			
			Subject subject = subjectService.findSubject(subjectId);
			subjectItem.setSubject(subject);
		}
		
		model.addAttribute("subjectItem", subjectItem);
		return "content/vote/subjectitem/editopt";
	}
	
	@RequestMapping(value = "/saveopt")
	public String saveopt(@ModelAttribute(value = "subjectItem") SubjectItem subjectItem, @RequestParam(value = "subjectId") Long subjectId, RedirectAttributes redirectAttributes){
		try{
			if (subjectItem.getId() == null){
				subjectItemService.addSubjectItem(subjectId, subjectItem);
			}else{
				subjectItemService.updSubjectItem(subjectId, subjectItem);
			}
			redirectAttributes.addFlashAttribute("message", "保存成功");
		}catch(Exception e){
			redirectAttributes.addFlashAttribute("message", "保存失败");
		}
		return "redirect:/content/vote/subjectitem/editopt/" + subjectId;
	}
}
