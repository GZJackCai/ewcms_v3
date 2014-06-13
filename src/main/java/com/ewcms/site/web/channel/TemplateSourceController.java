package com.ewcms.site.web.channel;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.site.model.TemplateSource;
import com.ewcms.site.model.TemplatesrcEntity;
import com.ewcms.site.service.TemplateSourceService;
import com.ewcms.util.ConvertUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;

@Controller(value = "channel.templatesource")
@RequestMapping(value = "/site/channel/templatesource")
public class TemplateSourceController {
	
	@Autowired
	private TemplateSourceService templateSourceService;
	
	@RequestMapping(value = "/index")
	public String index(@RequestParam(value = "channelId") Long channelId, Model model) {
		model.addAttribute("channelId", channelId);
		return "site/channel/templatesource/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params, @RequestParam(value = "channelId") Long channelId) {
		Map<String, Object> map = params.getParameters();
		map.put("EQ_channelId", channelId);
		params.setParameters(map);
		return templateSourceService.searchTemplate(params);
	}
	
	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String editChannelTemplate(@RequestParam(value = "channelId") Long channelId, @RequestParam(required = false) List<Long> selections, Model model) {
		TemplateSource templateSource = new TemplateSource();
		templateSource.setChannelId(channelId);
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			templateSource = templateSourceService.findByIdAndChannelId(selections.get(0), channelId);
			model.addAttribute("selections", selections);
		}
		model.addAttribute("templateSource", templateSource);
		return "site/channel/templatesource/edit";
	}

	@RequestMapping(value = "/save")
	public String save(@ModelAttribute(value = "templateSource") TemplateSource templateSource, @RequestParam(required = false) List<Long> selections,
			Model model, @RequestParam(value = "templateSourceFile", required = false) MultipartFile templateSourceFile, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		
		InputStream in = null;
		try {
			Boolean annexFlag = Boolean.FALSE;
			request.setCharacterEncoding("UTF-8");
			if (templateSourceFile != null && templateSourceFile.getSize() > 0) {
				templateSource.setSize(ConvertUtil.kb(templateSourceFile.getSize()));

				byte[] buffer = new byte[Integer.parseInt(String.valueOf(templateSourceFile.getSize()))];
				in = new BufferedInputStream(templateSourceFile.getInputStream(), Integer.parseInt(String.valueOf(templateSourceFile.getSize())));
				in.read(buffer);
				
				TemplatesrcEntity templatesrcEntity = new TemplatesrcEntity();
				templatesrcEntity.setSrcEntity(buffer);
				templateSource.setSourceEntity(templatesrcEntity);

				in.close();
				
				annexFlag = Boolean.TRUE;
			}
			
			Boolean close = Boolean.FALSE;
			if (templateSource.getId() != null) {
				TemplateSource oldvo = templateSourceService.getTemplateSource(templateSource.getId());
				oldvo.setDescribe(templateSource.getDescribe());
				if (annexFlag) {
					oldvo.getSourceEntity().setSrcEntity(templateSource.getSourceEntity().getSrcEntity());
					oldvo.setName(templateSourceFile.getOriginalFilename());
					oldvo.setRelease(false);
				}
				templateSourceService.updTemplateSource(oldvo);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					templateSource = templateSourceService.findByIdAndChannelId(selections.get(0), templateSource.getChannelId());
					model.addAttribute("templateSource", templateSource);
					model.addAttribute("selections", selections);
				}
			} else {
				templateSource.setSite(EwcmsContextUtil.getCurrentSite());
				templateSource.setParent(templateSourceService.channelTemplateSource(templateSource.getChannelId().toString()));
				if (annexFlag) {
					templateSource.setName(templateSourceFile.getOriginalFilename());
				} else {
					String fileName = "new" + (int) (Math.random() * 100) + ".htm";
					templateSource.setName(fileName);
				}
				templateSourceService.addTemplateSource(templateSource);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, templateSource.getId());
				model.addAttribute("templateSource", new TemplateSource());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
			}
		}
		return "site/channel/templatesource/edit";
	}

	@RequestMapping(value = "/delete")
	public String delete(@RequestParam(value = "selections") List<Long> selections, @RequestParam(value = "channelId") Long channelId, RedirectAttributes redirectAttributes){
		for (Long templateSourceId : selections){
			templateSourceService.delTemplateSource(templateSourceId);
		}
		return "redirect:/site/channel/templatesource/index?channelId=" + channelId;
	}
	
	@RequestMapping(value = "/editContent/{templateSourceId}")
	public String editContent(@PathVariable(value = "templateSourceId")Long templateSourceId, Model model) {
		TemplateSource templateSource = templateSourceService.getTemplateSource(templateSourceId);
		try {
			model.addAttribute("templateSourceContent", new String(templateSource.getSourceEntity().getSrcEntity(), "UTF-8"));
		} catch (Exception e) {
		}
		model.addAttribute("templateSource", templateSource);	
		return "site/channel/templatesource/content";
	}
	
	@RequestMapping(value = "/saveContent")
	public String saveContent(@ModelAttribute(value = "templateSource") TemplateSource templateSource, @RequestParam(value = "templateSourceContent") String templateSourceContent, RedirectAttributes redirectAttributes) {
		String message = "模板内容保存";
		try {
			TemplateSource vo = templateSourceService.getTemplateSource(templateSource.getId());
			TemplatesrcEntity templatesrcEntity = new TemplatesrcEntity();
			templatesrcEntity.setSrcEntity(templateSourceContent.getBytes("UTF-8"));
			vo.setSourceEntity(templatesrcEntity);
			templateSourceService.updTemplateSource(vo);
			message += "成功";
		} catch (Exception e) {
			message = "失败";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/channel/templatesource/editContent/" + templateSource.getId();
	}

}
