package com.ewcms.content.resource.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ewcms.content.resource.model.Resource;
import com.ewcms.content.resource.service.ResourceService;
import com.ewcms.publication.PublishException;
import com.ewcms.publication.PublishServiceable;
import com.ewcms.util.ConvertUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.util.JSONUtil;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/resource")
public class ResourceController {
	@Autowired
	private ResourceService resourceService;
	@Autowired
	private PublishServiceable publishService;

	@RequestMapping(value = "/index")
	public String index() {
		return "content/resource/index";
	}

	@RequestMapping(value = "/manage/{type}")
	public String manage(@PathVariable(value = "type") String type, Model model) {
		model.addAttribute("type", type);
		return "content/resource/manage";
	}

	@RequestMapping(value = "/manage/delete/{type}")
	public @ResponseBody
	String softDelete(@PathVariable(value = "type") String type, @RequestParam(required = false) List<Long> selections) {
		resourceService.softDelete(selections);
		return "已把资源文件放入到回收站";
	}

	@RequestMapping(value = "/manage/publish/{type}")
	public @ResponseBody
	String publish(@PathVariable(value = "type") String type, @RequestParam(required = false) List<Long> selections) {
		String message = "资源发布";
		try {
			publishService.pubResource(EwcmsContextUtil.getCurrentSiteId(), selections);
			message += "成功";
		} catch (PublishException e) {
			message = "失败";
		}
		return message;
	}

	@RequestMapping(value = "/resource")
	public String resource(@RequestParam(value = "type") String type, @RequestParam(value = "resourceId", required = false) Long resourceId,
			@RequestParam(value = "multi", required = false) Boolean multi, Model model) {
		Resource.Type resType = Resource.Type.valueOf(StringUtils.upperCase(type));
		if (resourceId == null) {
			resourceId = Long.MIN_VALUE;
		}
		if (multi == null) {
			multi = true;
		}
		model.addAttribute("resourceId", resourceId);
		model.addAttribute("type", type);
		model.addAttribute("multi", multi);
		model.addAttribute("fileDesc", resType.getFileDesc());
		model.addAttribute("fileExt", resType.getFileExt());
		return "content/resource/resource";
	}

	@RequestMapping(value = "/receive")
	public @ResponseBody
	String receive(@RequestParam(value = "resourceId", required = false) Long resourceId, @RequestParam(value = "type", required = false) String type, @RequestParam(value = "myUpload", required = false) MultipartFile myUpload) {
		try {
			Resource.Type resType = Resource.Type.valueOf(StringUtils.upperCase(type));
			Resource resource = null;
			if (resourceId == Long.MIN_VALUE) {
				resource = resourceService.upload(myUpload, resType);
			} else {
				resource = resourceService.update(resourceId, myUpload, resType);
			}
			return JSONUtil.toJSON(resource);
		} catch (IOException e) {
			return null;
		}
	}

	@RequestMapping(value = "/save")
	public @ResponseBody String save(@RequestBody String descriptions) {
		try{
			Map<Long, String> map = ConvertUtil.resource(descriptions, "descriptions");
			List<Resource> resources = resourceService.save(map);
			return JSONUtil.renderSuccess(resources);
		}catch(Exception e){
			return null;
		}
	}

	@RequestMapping(value = "/query/{annexType}")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params, @PathVariable(value = "annexType") String annexType) {
		return resourceService.search(params, annexType);
	}

	@RequestMapping(value = "/insert/{annexType}")
	public String insert(@PathVariable(value = "annexType") String annexType, @RequestParam(value = "multi", required = false)Boolean multi, Model model) {
		model.addAttribute("type", annexType);
		if (multi != null){
			model.addAttribute("multi", multi);
		}
		return "content/resource/insert";
	}
}
