package com.ewcms.content.resource.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ewcms.content.resource.model.Resource;
import com.ewcms.content.resource.service.ResourceService;
import com.ewcms.web.util.JSONUtil;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/resource/thumb")
public class ResourceThumbController {
	
	@Autowired
	private ResourceService resourceService;
	
	@RequestMapping(value = "/index/{resourceId}")
	public String thumb(@PathVariable(value = "resourceId") Long resourceId, Model model) {
		model.addAttribute("id", resourceId);
		return "content/resource/thumb";
	}
	
	@RequestMapping(value = "/receive")
	public @ResponseBody
	String thumb(@RequestParam(value = "resourceId") Long resourceId, @RequestParam(value = "myUpload") MultipartFile myUpload) {
		try {
			Resource resource = resourceService.updateThumb(resourceId, myUpload);
			return JSONUtil.toJSON(resource);
		} catch (IOException e) {
			return null;
		}
	}

}
