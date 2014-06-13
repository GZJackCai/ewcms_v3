package com.ewcms.site.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.site.model.FileType;
import com.ewcms.site.model.Template;
import com.ewcms.site.model.TemplateEntity;
import com.ewcms.site.service.TemplateService;
import com.ewcms.util.ConvertUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.util.TreeNodeConvert;
import com.ewcms.web.vo.TreeNode;

/**
 * 
 * @author wu_zhijun
 *
 */
@Controller
@RequestMapping(value = "/site/template")
public class TemplateController {
	
	@Autowired
	private TemplateService templateService;
	
	@RequestMapping(value = "/index")
	public String index() {
		return "site/template/index";
	}
	
	@RequestMapping(value = "/import/{templateParentId}")
	public String importTemplate(@PathVariable(value = "templateParentId") Long templateParentId, Model model){
		Template template = templateService.getTemplate(templateParentId);
		model.addAttribute("template", template);
		return "site/template/import";
	}
	
	@RequestMapping(value = "/saveImport")
	public String saveImportTemplate(@ModelAttribute(value="template") Template template, @RequestParam(value = "templateFile", required = false) MultipartFile templateFile, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String message = "导入模板文件";
		InputStream in = null;
		File file = null;
		try {
			request.setCharacterEncoding("UTF-8");
			if (templateFile != null && templateFile.getSize() > 0) {
				if (templateFile.getContentType() != null
						&& "application/octet-stream,application/zip,application/x-zip-compressed,application/x-download".indexOf(templateFile.getContentType()) != -1) {
					file = new File("temp.zip");
					FileCopyUtils.copy(templateFile.getBytes(), file);
					templateFile.transferTo(file);
					templateService.unZipTemplateFile(template.getId(), file);
				} else {
					template.setSite(EwcmsContextUtil.getCurrentSite());
					template.setName(templateFile.getOriginalFilename());
					template.setSize(ConvertUtil.kb(templateFile.getSize()));
					TemplateEntity tplEntityVo = new TemplateEntity();
					
					byte[] buffer = new byte[Integer.parseInt(String.valueOf(templateFile.getSize()))];
					in = new BufferedInputStream(templateFile.getInputStream(), Integer.parseInt(String.valueOf(templateFile.getSize())));
					in.read(buffer);
					tplEntityVo.setTplEntity(buffer);
					template.setTemplateEntity(tplEntityVo);
					if (template.getId() == null) {
						template.setParent(null);
					} else {
						template.setParent(templateService.getTemplate(template.getId()));
					}
					templateService.addTemplate(template);
					in.close();
				}
			} else {
				if (template != null && template.getId() != null)
					template.setPath(templateService.getTemplate(template.getId()).getPath());
			}
			message += "成功";
		} catch (Exception e) {
			message += "失败";
		}finally {
			try {
				if (in != null){
					in.close();
					in = null;
				}
				if (file != null){
					file.delete();
					file = null;
				}
			} catch (IOException e) {}
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/template/import/" + template.getId();
	}

	@RequestMapping(value = "/input",method = RequestMethod.GET)
	public String input(@RequestParam(value="templateId", required = false) Long templateId, Model model){
		Template template = new Template();
		if (templateId != null){
			template = templateService.getTemplate(templateId);
		}
		model.addAttribute("template", template);
		return "site/channel/templateedit";
	}
	
	@RequestMapping(value = "/save")
	public String save(@ModelAttribute(value="template") Template template, @RequestParam(value = "templateFile", required = false) MultipartFile templateFile, HttpServletRequest request, RedirectAttributes redirectAttributes){
		String message = "修改模板信息";
		TemplateEntity tplEntityVo = new TemplateEntity();
		InputStream in = null;
		try {
			request.setCharacterEncoding("UTF-8");
			if (templateFile != null && templateFile.getSize() > 0) {
				template.setSize(ConvertUtil.kb(templateFile.getSize()));

				byte[] buffer = new byte[Integer.parseInt(String.valueOf(templateFile.getSize()))];
				in = new BufferedInputStream(templateFile.getInputStream(), Integer.parseInt(String
						.valueOf(templateFile.getSize())));
				in.read(buffer);
				tplEntityVo.setTplEntity(buffer);
				template.setTemplateEntity(tplEntityVo);
				
				in.close();

			}
			if (template.getId() != null) {
				Template oldvo = templateService.getTemplate(template.getId());
				oldvo.setDescribe(template.getDescribe());
				oldvo.setType(template.getType());
				oldvo.setUriPattern(template.getUriPattern());
				if (templateFile != null && templateFile.getSize() > 0) {
					oldvo.getTemplateEntity().setTplEntity(template.getTemplateEntity().getTplEntity());
					oldvo.setName(templateFile.getOriginalFilename());
				}
				templateService.updTemplate(oldvo);
			} else {
				template.setTemplateEntity(tplEntityVo);
				template.setSite(EwcmsContextUtil.getCurrentSite());
				template.setParent(templateService.channelTemplate(template.getChannelId().toString()));
				if (templateFile != null && templateFile.getSize() > 0) {
					template.setName(templateFile.getOriginalFilename());
				} else {
					String fileName = "new" + (int) (Math.random() * 100) + ".htm";
					template.setName(fileName);
				}
				templateService.addTemplate(template);
			}
			message += "成功";
		} catch (Exception e) {
			message += "失败";
		} finally {
			try {
				if (in != null){
					in.close();
					in = null;
				}
			} catch (IOException e) {}
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/template/input?templateId=" + template.getId();
	}
	
	@RequestMapping(value = "/tree")
	public @ResponseBody List<TreeNode> templateTree(@RequestParam(value = "id", required = false) Long templateId){
		List<TreeNode> result = new ArrayList<TreeNode>();
		if (templateId == null) {
			TreeNode treeNode = new TreeNode();
			treeNode.setText(EwcmsContextUtil.getCurrentSite().getSiteName());
			treeNode.setState("open");
			treeNode.setChildren(TreeNodeConvert.templateConvert(templateService.getTemplaeTreeList(false)));
			result.add(treeNode);
		}else{
			result = TreeNodeConvert.templateConvert(templateService.getTemplaeTreeList(templateId, false));
		}
		return result;
	}
	
	@RequestMapping(value = "/treeChannel")
	public @ResponseBody List<TreeNode> templateChannelTree(@RequestParam(value = "templateId", required = false) Long templateId, @RequestParam(value = "channelId", required = false)Integer channelId ){
		List<TreeNode> result = new ArrayList<TreeNode>();
		if (templateId == null) {
			TreeNode treeNode = new TreeNode();
			treeNode.setText(EwcmsContextUtil.getCurrentSite().getSiteName());
			treeNode.setState("open");
			treeNode.setChildren(TreeNodeConvert.templateConvert(templateService.getTemplaeTreeList(true)));
			result.add(treeNode);
		}else{
			result = TreeNodeConvert.templateConvert(templateService.getTemplaeTreeList(templateId, channelId.toString()));
		}
		return result;
	}

	@RequestMapping(value = "/add")
	public @ResponseBody Long addTemplate(@RequestParam(value = "templateParentId", required = false)Long templateParentId, @RequestParam(value = "templateName")String templateName) {
		Long templateId = null;
		try {
			Template template = new Template();
			template.setName(templateName);
			template.setSite(EwcmsContextUtil.getCurrentSite());
			template.setFileType(FileType.FILE);
			template.setSize("0 KB");
			template.setTemplateEntity(new TemplateEntity());
			if (templateParentId == null) {
				template.setParent(null);
			} else {
				template.setParent(templateService.getTemplate(templateParentId));
			}
			templateId = templateService.addTemplate(template);
		} catch (Exception e) {
		}
		return templateId;
	}
	
	@RequestMapping(value = "/rename")
	public @ResponseBody Long renameTemplate(@RequestParam(value = "templateId") Long templateId,
			@RequestParam(value = "templateName") String templateName) {
		try {
			Template template = templateService.getTemplate(templateId);
			template.setName(templateName);
			templateService.updTemplate(template);
			return templateId;
		} catch (Exception e) {
			return null;
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Boolean delOrgan(@RequestParam(value = "templateId") Long templateId){
		Boolean result = false;
		try{
			templateService.delTemplate(templateId);
			result = true;
		} catch(Exception e){
		}
		return result;
	}
	
	/**
	 * 新建模板文件夹.
	 */
	@RequestMapping(value = "/addFolder")
	public @ResponseBody Long addFolder(@RequestParam(value = "templateParentId", required = false) Long templateParentId, @RequestParam(value = "templateName")String templateName) {
		Long templateId = null;
		try {
			Template template = new Template();
			template.setName(templateName);
			template.setFileType(FileType.DIRECTORY);
			template.setSite(EwcmsContextUtil.getCurrentSite());
			if (templateParentId == null) {
				template.setParent(null);
			} else {
				template.setParent(templateService.getTemplate(templateParentId));
			}
			templateId = templateService.addTemplate(template);
		} catch (Exception e) {
		}
		return templateId;
	}
	
	/**
	 * 移动模板.
	 */
	@RequestMapping(value = "/moveto")
	public @ResponseBody Boolean movetoTemplate(@RequestParam(value = "templateId") Long templateId, @RequestParam(value = "templateParentId", required = false)Long templateParentId) {
		Boolean result = false;
		try {
			Template template = templateService.getTemplate(templateId);
			if (templateParentId == null) {
				template.setParent(null);
			} else {
				template.setParent(templateService.getTemplate(templateParentId));
			}
			templateService.updTemplate(template);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}
	
	@RequestMapping(value = "/edit")
	public String edit(@RequestParam(value = "templateId", required = false)Long templateId, Model model){
		if (templateId != null){
			Template template = templateService.getTemplate(templateId);
			model.addAttribute("template", template);
		}
		return "site/template/edit";
	}
	
	/**
	 * 编辑模板.
	 */
	@RequestMapping(value = "/editInfo/{templateId}")
	public String editInfo(@PathVariable(value = "templateId")Long templateId, Model model) {
		Template template = templateService.getTemplate(templateId);
		try {
			model.addAttribute("templateContent", new String(template.getTemplateEntity().getTplEntity(), "UTF-8"));
		} catch (Exception e) {
		}
		model.addAttribute("template", template);	
		return "site/template/info";
	}
	
	@RequestMapping(value = "/editContent/{templateId}")
	public String editContent(@PathVariable(value = "templateId")Long templateId, Model model) {
		Template template = templateService.getTemplate(templateId);
		try {
			model.addAttribute("templateContent", new String(template.getTemplateEntity().getTplEntity(), "UTF-8"));
		} catch (Exception e) {
		}
		model.addAttribute("template", template);	
		return "site/template/content";
	}
	
	@RequestMapping(value = "/saveInfo")
	public String saveInfo(@ModelAttribute(value = "template") Template template, @RequestParam(value = "templateFile", required = false) MultipartFile templateFile, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		String message = "模板信息保存";
		InputStream in = null;
		try {
			request.setCharacterEncoding("UTF-8");
			Template vo = templateService.getTemplate(template.getId());
			vo.setDescribe(template.getDescribe());
			if (templateFile != null && templateFile.getSize() > 0) {
				vo.setSize(ConvertUtil.kb(templateFile.getSize()));
				TemplateEntity tplEntityVo = new TemplateEntity();
				byte[] buffer = new byte[Integer.parseInt(String.valueOf(templateFile.getSize()))];
				in = new BufferedInputStream(templateFile.getInputStream(), Integer.parseInt(String
						.valueOf(templateFile.getSize())));
				in.read(buffer);
				tplEntityVo.setTplEntity(buffer);
				vo.setTemplateEntity(tplEntityVo);
				
				in.close();
			}
			templateService.updTemplate(vo);
			message += "成功";
		} catch (Exception e) {
			message += "失败";
		} finally {
			try{
				if (in != null){
					in.close();
					in = null;
				}
			} catch(IOException e){}
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/template/editInfo/" + template.getId();
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
		} catch (Exception e) {
			message = "失败";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/template/editContent/" + template.getId();
	}
	
	@RequestMapping(value = "/exportzip")
	public void exportZip(@RequestParam(value = "templateId", required = false) Long templateId, HttpServletResponse response){
		ZipOutputStream zos = null;
		try{
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/x-download;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=template" + templateId + ".zip");
			zos = new ZipOutputStream(response.getOutputStream());
			zos.setEncoding("UTF-8");
			
			if (templateId != null){
				templateService.exportTemplateZip(templateId, zos, "");
			}else{
				List<Template> templates = templateService.getTemplaeTreeList(false);
				for (Template template : templates){
					templateService.exportTemplateZip(template.getId(), zos, EwcmsContextUtil.getCurrentSite().getSiteName() + "/");
				}
			}
			zos.flush();
			zos.close();
		}catch(Exception e){
			
		}finally {
			if (zos != null){
				try{
					zos.close();
					zos = null;
				}catch(Exception e){
				}
			}
		}
	}
	
}
