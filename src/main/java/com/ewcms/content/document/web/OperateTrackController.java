package com.ewcms.content.document.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.content.document.service.OperateTrackService;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/document/operatetrack")
public class OperateTrackController {
	
	@Autowired
	private OperateTrackService operateTrackService;
	
	@RequestMapping(value = "/index/{articleMainId}")
	public String index(@PathVariable(value = "articleMainId") Long articleMainId, Model model){
		model.addAttribute("articleMainId", articleMainId);
		return "content/document/operatetrack/index";
	}
	
	@RequestMapping(value = "/query/{articleMainId}")
	public @ResponseBody Map<String, Object> query(@PathVariable(value = "articleMainId")Long articleMainId, @ModelAttribute QueryParameter params) {
		return operateTrackService.search(articleMainId, params);
	}

	@RequestMapping(value = "/reason/{trackId}")
	public String reason(@PathVariable(value = "trackId") Long trackId, Model model){
		String result = operateTrackService.getArticleOperateTrack(trackId);
		model.addAttribute("result", result);
		return "content/document/operatetrack/reason";
	}
}
