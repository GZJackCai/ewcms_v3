package com.ewcms.content.document.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.service.ShareService;
import com.ewcms.web.QueryParameter;

/**
 * 文章共享
 * 
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/document/share")
public class ShareController {
	
	@Autowired
	private ShareService shareService;
	
	@ModelAttribute
	public void init(Model model) {
		model.addAttribute("statusMap", Article.Status.values());
		model.addAttribute("genreMap", Article.Genre.values());
	}
	
	@RequestMapping(value = "/index")
	public String index(){
		return "content/document/share/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return shareService.search(params);
	}
	
	@RequestMapping(value = "/copy")
	public @ResponseBody Boolean copy(@RequestParam(value = "channelIds")List<Long> channelIds, @RequestParam(value = "selections")List<Long> selections){
		Boolean result = false;
		try{
			if (selections != null && selections.size() > 0 && channelIds != null && channelIds.size() > 0) {
				shareService.copyArticleMainFromShare(selections, channelIds);
				result = true;
			}
		}catch(Exception e){
		}
		return result;
	}
	
	@RequestMapping(value = "/refence")
	public @ResponseBody Boolean refence(@RequestParam(value = "channelIds")List<Long> channelIds, @RequestParam(value = "selections")List<Long> selections){
		Boolean result = false;
		try{
			if (selections != null && selections.size() > 0 && channelIds != null && channelIds.size() > 0) {
				shareService.refenceArticleMainFromShare(selections, channelIds);
				result = true;
			}
		}catch(Exception e){
		}
		return result;
	}
}
