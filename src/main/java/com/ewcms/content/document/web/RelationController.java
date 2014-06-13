package com.ewcms.content.document.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.content.document.service.RelationService;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/document/relation")
public class RelationController {
	
	@Autowired
	private RelationService relationService;
	
	@RequestMapping(value = "/index/{articleId}")
	public String index(@PathVariable(value = "articleId") Long articleId, Model model){
		model.addAttribute("articleId", articleId);
		return "content/document/relation/index";
	}
	
	@RequestMapping(value = "/query/{articleId}")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params, @PathVariable(value = "articleId") Long articleId){
		return relationService.searchArticleRelation(articleId, params);
	}

	@RequestMapping(value = "/add")
	public String add(){
		return "content/document/relation/article";
	}
	
	@RequestMapping(value = "/save/{articleId}", method = RequestMethod.POST)
	public @ResponseBody Boolean save(@PathVariable(value = "articleId") Long articleId, @RequestParam("selections") List<Long> selections) {
		Boolean result = false;
		try{
			relationService.saveRelation(articleId, selections);
			result = true;
		}catch(Exception e){
			System.out.println(e.toString());
		}
		return result;
	}
	
	@RequestMapping(value = "/delete/{articleId}", method = { RequestMethod.POST, RequestMethod.GET })
	public String remove(@PathVariable(value = "articleId") Long articleId, @RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes) {
		try {
			relationService.deleteRelation(articleId, selections);
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/document/relation/index/" + articleId;
	}

	@RequestMapping(value = "/up/{articleId}_{relationArticleId}")
	public String up(@PathVariable(value = "articleId") Long articleId, @PathVariable(value = "relationArticleId") Long relationArticleId, RedirectAttributes redirectAttributes) {
		try {
			relationService.upRelation(articleId, relationArticleId);
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/document/relation/index/" + articleId;
	}
	
	@RequestMapping(value = "/down/{articleId}_{relationArticleId}")
	public String down(@PathVariable(value = "articleId") Long articleId, @PathVariable(value = "relationArticleId") Long relationArticleId, RedirectAttributes redirectAttributes){
		try{
			relationService.downRelation(articleId, relationArticleId);
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/document/relation/index/" + articleId;
	}
}
