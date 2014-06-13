package com.ewcms.content.vote.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ewcms.content.vote.service.PersonService;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/vote/record")
public class RecordController {
	
	@Autowired
	private PersonService personService;
	
	@RequestMapping(value = "/index/{questionnaireId}_{personId}")
	public String index(@PathVariable(value = "questionnaireId") Long questionnaireId, @PathVariable(value = "personId") Long personId, Model model){
		model.addAttribute("results", personService.getRecordToHtml(questionnaireId, personId));
		return "content/vote/record";
	}
}
