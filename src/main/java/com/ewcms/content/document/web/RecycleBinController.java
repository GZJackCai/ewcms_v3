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

import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.service.RecycleBinService;
import com.ewcms.security.acl.annotation.AclEnum;
import com.ewcms.security.acl.annotation.ChannelAcl;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * 文章回收站
 * 
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/document/recyclebin")
public class RecycleBinController {
	
	@Autowired
	private RecycleBinService recycleBinService;
	
	@ModelAttribute
	public void init(Model model) {
		model.addAttribute("statusMap", Article.Status.values());
		model.addAttribute("genreMap", Article.Genre.values());
	}

	@RequestMapping(value = "/tree")
	public String tree(){
		return "content/document/recyclebin/tree";
	}
	
	@RequestMapping(value = "/index/{channelId}")
	public String index(@PathVariable(value = "channelId") Long channelId, Model model){
		model.addAttribute("channelId", channelId);
		return "content/document/recyclebin/index";
	}

	@RequestMapping(value = "/query/{channelId}")
	public @ResponseBody Map<String, Object> query(@PathVariable(value = "channelId") Long channelId, @ModelAttribute QueryParameter params) {
		return recycleBinService.search(channelId, params);
	}

	@ChannelAcl(acl = { AclEnum.DELETE_ARTICLE }, position = 0)
	@RequestMapping(value = "/delete/{channelId}", method = {RequestMethod.POST,RequestMethod.GET})
	public String delete(@PathVariable(value = "channelId") Long channelId, @RequestParam(value = "selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long selection : selections){
				recycleBinService.delArticleMain(selection, channelId);
			}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/document/recyclebin/index/" + channelId;
	}
	
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/restore/{channelId}", method = {RequestMethod.POST,RequestMethod.GET})
	public String restore(@PathVariable(value = "channelId") Long channelId, @RequestParam(value = "selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long selection : selections){
				recycleBinService.restoreArticleMain(selection, channelId);
			}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/document/recyclebin/index/" + channelId;
	}
	
}
