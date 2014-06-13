package com.ewcms.content.vote.web;

import java.util.ArrayList;
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

import com.ewcms.content.vote.model.Questionnaire;
import com.ewcms.content.vote.model.Subject;
import com.ewcms.content.vote.service.QuestionnaireService;
import com.ewcms.content.vote.service.SubjectService;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/vote/subject")
public class SubjectController {
	
	@Autowired
	private QuestionnaireService questionnaireService;
	@Autowired
	private SubjectService subjectService;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("statusMap", Subject.Status.values());
	}
	
	@RequestMapping(value = "/index/{questionnaireId}")
	public String index(@PathVariable(value = "questionnaireId") Long questionnaireId, Model model){
		model.addAttribute("questionnaireId", questionnaireId);
		return "content/vote/subject/index";
	}

	@RequestMapping(value = "/query/{questionnaireId}")
	public @ResponseBody
	Map<String, Object> query(@PathVariable(value = "questionnaireId") Long questionnaireId, @ModelAttribute QueryParameter params) {
		return subjectService.search(params, questionnaireId);
	}

	@RequestMapping(value = "/edit/{questionnaireId}",method = RequestMethod.GET)
	public String edit(@PathVariable(value = "questionnaireId") Long questionnaireId, @RequestParam(required = false) List<Long> selections, Model model) {
		Subject subject = new Subject();
		if (selections == null || selections.isEmpty()) {
			Questionnaire questionnaire = questionnaireService.findQuestionnaire(questionnaireId);
			subject.setQuestionnaire(questionnaire);
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			subject = subjectService.findSubject(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("subject", subject);
		return "content/vote/subject/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "subject") Subject subject, @RequestParam(required = false) List<Long> selections, @RequestParam(value = "questionnaireId") Long questionnaireId, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (subject.getId() != null && StringUtils.hasText(subject.getId().toString())){
				subjectService.addSubject(questionnaireId, subject);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					subject = subjectService.findSubject(selections.get(0));
					model.addAttribute("subject", subject);
					model.addAttribute("selections", selections);
				}
			}else{
				subjectService.addSubject(questionnaireId, subject);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, subject.getId());
				
				Subject newSubject = new Subject();
				Questionnaire questionnaire = questionnaireService.findQuestionnaire(questionnaireId);
				newSubject.setQuestionnaire(questionnaire);
				
				model.addAttribute("subject", newSubject);
				model.addAttribute("selections", selections);
			}
			model.addAttribute("questionnaireId", questionnaireId);
			model.addAttribute("close",close);
		}catch(Exception e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "content/vote/subject/edit";	
	}
	
	@RequestMapping(value = "/delete/{questionnaireId}", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@PathVariable(value = "questionnaireId") Long questionnaireId, @RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long id : selections){
				subjectService.delSubject(id);
	    	}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/vote/subject/index/" + questionnaireId;
	}
	
	@RequestMapping(value = "/up/{questionnaireId}_{subjectId}")
	public @ResponseBody String up(@PathVariable(value = "questionnaireId") Long questionnaireId, @RequestParam(value = "subjectId") Long subjectId){
		String result = "上移";
		try{
			subjectService.upSubject(questionnaireId, subjectId);
			result += "成功";
		}catch(Exception e){
			result += "失败";
		}
		return result;
	}
	
	@RequestMapping(value = "/down/{questionnaireId}_{subjectId}")
	public @ResponseBody String down(@PathVariable(value = "questionnaireId") Long questionnaireId, @RequestParam(value = "subjectId") Long subjectId){
		String result = "下移";
		try{
			subjectService.downSubject(questionnaireId, subjectId);
			result += "成功";
		}catch(Exception e){
			result += "失败";
		}
		return result;
	}
}
