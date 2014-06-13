package com.ewcms.content.vote.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.ewcms.content.vote.service.QuestionnaireService;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/vote/questionnaire")
public class QuestionnaireController {
	
	@Autowired
	private QuestionnaireService questionnaireService;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("statusMap", Questionnaire.Status.values());
	}
	
	@RequestMapping(value = "/index/{channelId}")
	public String index(@PathVariable(value = "channelId") Long channelId, Model model){
		model.addAttribute("channelId", channelId);
		return "content/vote/questionnaire/index";
	}
	
	@RequestMapping(value = "/query/{channelId}")
	public @ResponseBody
	Map<String, Object> query(@PathVariable(value = "channelId") Long channelId, @ModelAttribute QueryParameter params) {
		return questionnaireService.search(params, channelId);
	}

	@RequestMapping(value = "/resultVote/{questionnaireId}")
	public void resultVote(@PathVariable(value = "questionnaireId") Long questionnaireId, HttpServletRequest request, HttpServletResponse response){
		ServletOutputStream out = null;
		StringBuffer output = new StringBuffer();
		try{
			output = questionnaireService.getQuestionnaireResultToHtml(questionnaireId, request.getContextPath(), null, false);
			out = response.getOutputStream();
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html; charset=utf-8");
	    	out.write(output.toString().getBytes());
	    	out.flush();
		}catch(IOException e){
		}finally{
			if (out != null){
    			try {
					out.close();
				} catch (IOException e) {
				}
    		}
		}
	}
	
	@RequestMapping(value = "/edit/{channelId}",method = RequestMethod.GET)
	public String edit(@PathVariable(value = "channelId") Long channelId, @RequestParam(required = false) List<Long> selections, Model model) {
		Questionnaire questionnaire = new Questionnaire();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
			questionnaire.setChannelId(channelId);
		} else {
			questionnaire = questionnaireService.findQuestionnaire(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("questionnaire", questionnaire);
		return "content/vote/questionnaire/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "questionnaire") Questionnaire questionnaire, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (questionnaire.getId() != null && StringUtils.hasText(questionnaire.getId().toString())){
				questionnaireService.updQuestionnaire(questionnaire);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					questionnaire = questionnaireService.findQuestionnaire(selections.get(0));
					model.addAttribute("questionnaire", questionnaire);
					model.addAttribute("selections", selections);
				}
			}else{
				questionnaireService.addQuestionnaire(questionnaire);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, questionnaire.getId());
				model.addAttribute("questionnaire", new Questionnaire());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(Exception e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "content/vote/questionnaire/edit";	
	}
	
	@RequestMapping(value = "/delete/{channelId}", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long id : selections){
				questionnaireService.delQuestionnaire(id);
	    	}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/vote/questionnaire/index/" + channelId;
	}
}
