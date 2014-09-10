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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.site.model.Template;
import com.ewcms.site.model.TemplateEntity;
import com.ewcms.site.service.TemplateService;
import com.ewcms.util.ConvertUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;

@Controller(value = "channel.template")
@RequestMapping(value = "/site/channel/template")
public class TemplateController {

	@Autowired
	private TemplateService templateService;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("typeMap", Template.TemplateType.values());
	}
	
	@RequestMapping(value = "/index")
	public String channelTemplate(@RequestParam(value = "channelId") Long channelId, Model model) {
		model.addAttribute("channelId", channelId);
		return "site/channel/template/index";
	}

	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params, @RequestParam(value = "channelId") Long channelId) {
		Map<String, Object> map = params.getParameters();
		map.put("EQ_channelId", channelId);
		params.setParameters(map);
		return templateService.searchTemplate(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String editChannelTemplate(@RequestParam(value = "channelId") Long channelId, @RequestParam(required = false) List<Long> selections, Model model) {
		Template template = new Template();
		template.setChannelId(channelId);
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			template = templateService.findByIdAndChannelId(selections.get(0), channelId);
			model.addAttribute("selections", selections);
		}
		model.addAttribute("template", template);
		return "site/channel/template/edit";
	}

	@RequestMapping(value = "/save", method=RequestMethod.POST)
	public String saveChannelTemplate(@ModelAttribute(value = "template") Template template, @RequestParam(required = false) List<Long> selections,
			Model model, @RequestParam(value = "templateFile", required = false) MultipartFile templateFile, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		InputStream in = null;
		try {
			Boolean annexFlag = Boolean.FALSE;
			request.setCharacterEncoding("UTF-8");
			if (templateFile != null && templateFile.getSize() > 0) {
				template.setSize(ConvertUtil.kb(templateFile.getSize()));

				byte[] buffer = new byte[Integer.parseInt(String.valueOf(templateFile.getSize()))];
				in = new BufferedInputStream(templateFile.getInputStream(), Integer.parseInt(String.valueOf(templateFile.getSize())));
				in.read(buffer);
				
				TemplateEntity templateEntity = new TemplateEntity();
				
				templateEntity.setTplEntity(buffer);
				template.setTemplateEntity(templateEntity);
				in.close();
				
				annexFlag = Boolean.TRUE;
			}
			
			Boolean close = Boolean.FALSE;
			if (template.getId() != null && StringUtils.hasText(template.getId().toString())) {
				Template oldTemplate = templateService.findByIdAndChannelId(selections.get(0), template.getChannelId());
				oldTemplate.setDescribe(template.getDescribe());
				oldTemplate.setType(template.getType());
				oldTemplate.setUriPattern(template.getUriPattern());
				if (annexFlag) {
					oldTemplate.getTemplateEntity().setTplEntity(template.getTemplateEntity().getTplEntity());
					oldTemplate.setName(templateFile.getOriginalFilename());
				}
				templateService.updTemplate(oldTemplate);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					template = templateService.findByIdAndChannelId(selections.get(0), template.getChannelId());
					model.addAttribute("template", template);
					model.addAttribute("selections", selections);
				}
			}else{
				template.setSite(EwcmsContextUtil.getCurrentSite());
				template.setParent(templateService.channelTemplate(template.getChannelId().toString()));
				if (annexFlag) {
					template.setName(templateFile.getOriginalFilename());
				} else {
					String fileName = "new" + (int) (Math.random() * 100) + ".htm";
					template.setName(fileName);
				}
				templateService.addTemplate(template);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, template.getId());
				model.addAttribute("template", new Template());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		} finally{
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
				}
				in = null;
			}
		}
		return "site/channel/template/edit";
	}
	
	@RequestMapping(value = "/delete")
	public String delChannelTemplate(@RequestParam(value = "selections") List<Long> selections, @RequestParam(value = "channelId") Long channelId, RedirectAttributes redirectAttributes){
		for (Long id : selections){
			templateService.delTemplate(id);
		}
		return "redirect:/site/channel/template/index?channelId=" + channelId;
	}

	@RequestMapping(value = "/import")
	public @ResponseBody
	Boolean importChannelTemplate(@RequestParam(value = "templateId") Long templateId, @RequestParam(value = "templateName") String templateName,
			@RequestParam(value = "channelId") Long channelId) {
		try {
			Template template = templateService.getTemplate(templateId);
			if (template.getChannelId() != null)
				return false;
			Template vo = new Template();
			vo.setSite(EwcmsContextUtil.getCurrentSite());
			vo.setName(template.getName());
			vo.setSize(template.getSize());
			TemplateEntity tplEntity = new TemplateEntity();
			tplEntity.setTplEntity(template.getTemplateEntity().getTplEntity());
			vo.setTemplateEntity(tplEntity);
			vo.setParent(templateService.channelTemplate(templateName));
			vo.setPath(templateService.getTemplate(vo.getParent().getId()).getPath() + "/" + template.getName());
			vo.setChannelId(channelId);
			templateService.addTemplate(vo);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	
	@RequestMapping(value = "/appChild")
	public @ResponseBody String appChildChannel(@RequestParam(value = "channelId")Long channelId, @RequestParam(value = "selections") List<Long> selections, @RequestParam(value="cover") Integer cover){
		String message = "模板应用于子栏目";
		try{
			if (cover.intValue() == 1)
				templateService.saveAppChild(channelId, selections, true);
			else
				templateService.saveAppChild(channelId, selections, false);
			message += "成功";
		}catch(Exception e){
			message += "失败";
		}
		return message;
	}
	
	@RequestMapping(value = "/verify")
	public @ResponseBody Boolean verifyTemplate(@RequestParam(value = "templateId")Long templateId){
		return templateService.verify(templateId);
	}
	
	@RequestMapping(value = "/editContent/{templateId}")
	public String editContent(@PathVariable(value = "templateId")Long templateId, Model model) {
		Template template = templateService.getTemplate(templateId);
		try {
			model.addAttribute("templateContent", new String(template.getTemplateEntity().getTplEntity(), "UTF-8"));
		} catch (Exception e) {
		}
		model.addAttribute("template", template);	
		return "site/channel/template/content";
	}
	
	@RequestMapping(value = "/saveContent")
	public String saveContent(@ModelAttribute(value = "template") Template template, @RequestParam(value = "templateContent") String templateContent, RedirectAttributes redirectAttributes) {
		String message = "模板内容保存";
		try {
			Template vo = templateService.getTemplate(template.getId());
			TemplateEntity tplEntityVo = new TemplateEntity();
			tplEntityVo.setTplEntity(templateContent.getBytes("UTF-8"));
			vo.setTemplateEntity(tplEntityVo);
			templateService.updTemplate(vo);
			message += "成功";
			Boolean verify = templateService.verify(template.getId());
			if (verify){
				message +="，模板校验通过";
			}else{
				message +="，模板校验不通过";
			}
		} catch (Exception e) {
			message = "失败";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/channel/template/editContent/" + template.getId();
	}
	
	@RequestMapping(value = "/history/index/{templateId}")
	public String history(@PathVariable(value="templateId")Long templateId, Model model){
		model.addAttribute("templateId", templateId);
		return "site/channel/template/history";
	}
	
	@RequestMapping(value="/history/restore/{templateId}")
	public @ResponseBody Boolean restoreHistory(@PathVariable(value="templateId")Long templateId, @RequestParam(value="historyId") Long historyId){
		return templateService.restoreTemplate(templateId, historyId);
	}
	
	@RequestMapping(value = "/history/query/{templateId}")
	public @ResponseBody Map<String, Object> queryHistory(@PathVariable(value="templateId")Long templateId, @ModelAttribute QueryParameter params){
		return templateService.searchTemplateHistory(templateId, params);
	}
}
