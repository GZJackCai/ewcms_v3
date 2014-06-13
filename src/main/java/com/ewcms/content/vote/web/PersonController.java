package com.ewcms.content.vote.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.content.vote.service.PersonService;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/vote/person")
public class PersonController {
	
	@Autowired
	private PersonService personService;
	
	@RequestMapping(value = "/index/{questionnaireId}")
	public String index(@PathVariable(value = "questionnaireId") Long questionnaireId, Model model){
		model.addAttribute("questionnaireId", questionnaireId);
		return "content/vote/person/index";
	}
	
	@RequestMapping(value = "/query/{questionnaireId}")
	public @ResponseBody
	Map<String, Object> query(@PathVariable(value = "questionnaireId") Long questionnaireId, @ModelAttribute QueryParameter params) {
		return personService.search(params, questionnaireId);
	}
}
