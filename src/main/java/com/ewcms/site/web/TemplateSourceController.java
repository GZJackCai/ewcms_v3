package com.ewcms.site.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.ewcms.site.model.TemplatesrcEntity;
import com.ewcms.site.model.TemplateSource;
import com.ewcms.site.service.TemplateSourceService;
import com.ewcms.util.ConvertUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.util.TreeNodeConvert;
import com.ewcms.web.vo.TreeNode;

/**
 * 
 * @author wu_zhijun
 * 
 */
@Controller
@RequestMapping(value = "/site/template/source")
public class TemplateSourceController {

	@Autowired
	private TemplateSourceService templateSourceService;

	@RequestMapping(value = "/index")
	public String index() {
		return "site/template/source/index";
	}

	@RequestMapping(value = "/delete")
	public @ResponseBody
	Boolean delTemplateSource(@RequestParam(value = "templateSourceId") Long templateSourceId) {
		Boolean result = false;
		try {
			templateSourceService.delTemplateSource(templateSourceId);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params, @RequestParam(value = "channelId") Long channelId) {
		Map<String, Object> map = params.getParameters();
		map.put("EQ_channelId", channelId);
		params.setParameters(map);
		return templateSourceService.searchTemplate(params);
	}

	@RequestMapping(value = "/input", method = RequestMethod.GET)
	public String input(@RequestParam(value = "templateSourceId", required = false) Long templateSourceId, Model model) {
		TemplateSource templateSource = new TemplateSource();
		if (templateSourceId != null) {
			templateSource = templateSourceService.getTemplateSource(templateSourceId);
		}
		model.addAttribute("templateSource", templateSource);
		return "site/channel/sourceedit";
	}

	@RequestMapping(value = "/save")
	public String save(@ModelAttribute(value = "templateSource") TemplateSource templateSource,
			@RequestParam(value = "templateSourceFile", required = false) MultipartFile templateSourceFile, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String message = "修改模板资源信息";
		TemplatesrcEntity tplEntityVo = new TemplatesrcEntity();
		InputStream in = null;
		try {
			request.setCharacterEncoding("UTF-8");
			if (templateSourceFile != null && templateSourceFile.getSize() > 0) {
				templateSource.setSize(ConvertUtil.kb(templateSourceFile.getSize()));

				byte[] buffer = new byte[Integer.parseInt(String.valueOf(templateSourceFile.getSize()))];
				in = new BufferedInputStream(templateSourceFile.getInputStream(), Integer.parseInt(String.valueOf(templateSourceFile.getSize())));
				in.read(buffer);
				tplEntityVo.setSrcEntity(buffer);
				templateSource.setSourceEntity(tplEntityVo);

				in.close();
			}
			if (templateSource.getId() != null) {
				TemplateSource oldvo = templateSourceService.getTemplateSource(templateSource.getId());
				oldvo.setDescribe(templateSource.getDescribe());
				if (templateSourceFile != null) {
					oldvo.getSourceEntity().setSrcEntity(templateSource.getSourceEntity().getSrcEntity());
					oldvo.setName(templateSourceFile.getOriginalFilename());
					oldvo.setRelease(false);
				}
				templateSourceService.updTemplateSource(oldvo);
			} else {
				templateSource.setSourceEntity(tplEntityVo);
				templateSource.setSite(EwcmsContextUtil.getCurrentSite());
				templateSource.setParent(templateSourceService.channelTemplateSource(templateSource.getChannelId().toString()));
				if (templateSourceFile != null && templateSourceFile.getSize() > 0) {
					templateSource.setName(templateSourceFile.getOriginalFilename());
				} else {
					String fileName = "new" + (int) (Math.random() * 100) + ".htm";
					templateSource.setName(fileName);
				}
				templateSourceService.addTemplateSource(templateSource);
			}
			message += "成功";
		} catch (Exception e) {
			message += "失败";
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
			}
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/template/source/input?templateId=" + templateSource.getId();
	}

	@RequestMapping(value = "/import/{templateSourceParentId}")
	public String importTemplate(@PathVariable(value = "templateSourceParentId") Long templateSourceParentId, Model model){
		TemplateSource templateSource = templateSourceService.getTemplateSource(templateSourceParentId);
		model.addAttribute("templateSource", templateSource);
		return "site/template/source/import";
	}
	
	@RequestMapping(value = "/saveImport")
	public String saveImportTemplateSource(@ModelAttribute(value = "templateSource") TemplateSource templateSource,
			@RequestParam(value = "templateSourceFile", required = false) MultipartFile templateSourceFile, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String message = "导入资源文件";
		InputStream in = null;
		File file = null;
		try {
			request.setCharacterEncoding("UTF-8");
			if (templateSourceFile != null) {
				if (templateSourceFile.getContentType() != null
						&& "application/octet-stream,application/zip,application/x-zip-compressed".indexOf(templateSourceFile.getContentType()) != -1) {
					file = new File("temp.zip");
					FileCopyUtils.copy(templateSourceFile.getBytes(), file);
					templateSourceFile.transferTo(file);
					templateSourceService.unZipTemplateSourceFile(templateSource.getId(), file);
				} else {
					templateSource.setSite(EwcmsContextUtil.getCurrentSite());
					templateSource.setName(templateSourceFile.getOriginalFilename());
					templateSource.setSize(ConvertUtil.kb(templateSourceFile.getSize()));
					TemplatesrcEntity tplEntityVo = new TemplatesrcEntity();
					byte[] buffer = new byte[Integer.parseInt(String.valueOf(templateSourceFile.getSize()))];
					try {
						in = new BufferedInputStream(templateSourceFile.getInputStream(), Integer.parseInt(String.valueOf(templateSourceFile.getSize())));
						in.read(buffer);

						tplEntityVo.setSrcEntity(buffer);
						templateSource.setSourceEntity(tplEntityVo);
						if (templateSource.getParent().getId() == null) {
							templateSource.setParent(null);

						} else {
							templateSource.setParent(templateSourceService.getTemplateSource(templateSource.getParent().getId()));
						}
						templateSourceService.addTemplateSource(templateSource);

						in.close();
					} catch (Exception e) {
					} finally {
						try {
							if (in != null) {
								in.close();
								in = null;
							}
						} catch (IOException e) {
						}
					}
				}
			} else {
				if (templateSource.getParent() != null && templateSource.getParent().getId() != null)
					templateSource.setPath(templateSourceService.getTemplateSource(templateSource.getParent().getId()).getPath());
			}
			message += "成功";
		} catch (Exception e) {
			message += "失败";
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
				if (file != null) {
					file.delete();
					file = null;
				}
			} catch (IOException e) {
			}
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/template/source/import/" + templateSource.getId();
	}

	@RequestMapping(value = "/pubsource")
	public @ResponseBody
	Boolean pubSource(@RequestParam(value = "templateSourceId") Long templateSourceId) {
		Boolean result = false;
		try {
			// webPublish.publishTemplateSources(new int[]{templateSourceId});
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	@RequestMapping(value = "/tree")
	public @ResponseBody
	List<TreeNode> templateSourceTree(@RequestParam(value = "id", required = false) Long templateSourceId) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		if (templateSourceId == null) {
			TreeNode treeNode = new TreeNode();
			treeNode.setText(EwcmsContextUtil.getCurrentSite().getSiteName());
			treeNode.setState("open");
			treeNode.setIconCls("icon-channel-site");
			treeNode.setChildren(TreeNodeConvert.templateSourceConvert(templateSourceService.getTemplaeSourceTreeList(false)));

			treeNodes.add(treeNode);
		} else {
			treeNodes = TreeNodeConvert.templateSourceConvert(templateSourceService.getTemplaeSourceTreeList(templateSourceId, false));
		}
		return treeNodes;
	}

	/**
	 * 新建资源文件.
	 */
	@RequestMapping(value = "/add")
	public @ResponseBody
	Long addTemplateSource(@RequestParam(value = "templateSourceParentId", required = false) Long templateSourceParentId,
			@RequestParam(value = "templateSourceName") String templateSourceName) {
		Long templateSourceId = null;
		try {
			TemplateSource templateSource = new TemplateSource();
			templateSource.setSite(EwcmsContextUtil.getCurrentSite());
			templateSource.setName(templateSourceName);
			templateSource.setSize("0 KB");
			templateSource.setFileType(FileType.FILE);
			templateSource.setSourceEntity(new TemplatesrcEntity());
			if (templateSourceParentId == null) {
				templateSource.setParent(null);
			} else {
				templateSource.setParent(templateSourceService.getTemplateSource(templateSourceParentId));
			}
			templateSourceId = templateSourceService.addTemplateSource(templateSource);
		} catch (Exception e) {
		}
		return templateSourceId;
	}

	/**
	 * 重命名资源.
	 */
	@RequestMapping(value = "/rename")
	public @ResponseBody
	Long renameTemplateSource(@RequestParam(value = "templateSourceId") Long templateSourceId,
			@RequestParam(value = "templateSourceName") String templateSourceName) {
		try {
			TemplateSource templateSource = templateSourceService.getTemplateSource(templateSourceId);
			templateSource.setName(templateSourceName);
			templateSourceService.updTemplateSource(templateSource);
			return templateSourceId;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 新建资源文件夹.
	 */
	@RequestMapping(value = "/addFolder")
	public @ResponseBody
	Long addTemplateSourceFolder(@RequestParam(value = "templateSourceParentId", required = false) Long templateSourceParentId,
			@RequestParam(value = "templateSourceName") String templateSourceName) {
		Long templateSourceId = null;
		try {
			TemplateSource templateSource = new TemplateSource();
			templateSource.setSite(EwcmsContextUtil.getCurrentSite());
			templateSource.setName(templateSourceName);
			templateSource.setFileType(FileType.DIRECTORY);
			if (templateSourceParentId == null) {
				templateSource.setParent(null);
			} else {
				templateSource.setParent(templateSourceService.getTemplateSource(templateSourceParentId));
			}
			templateSourceId = templateSourceService.addTemplateSource(templateSource);
		} catch (Exception e) {
		}
		return templateSourceId;
	}

	/**
	 * 移动资源.
	 */
	@RequestMapping(value = "/moveto")
	public @ResponseBody
	Boolean movetoTemplate(@RequestParam(value = "templateSourceId") Long templateSourceId,
			@RequestParam(value = "templateSourceParentId", required = false) Long templateSourceParentId) {
		Boolean result = false;
		try {
			TemplateSource templateSource = templateSourceService.getTemplateSource(templateSourceId);
			if (templateSourceParentId == null) {
				templateSource.setParent(null);
			} else {
				templateSource.setParent(templateSourceService.getTemplateSource(templateSourceParentId));
			}
			templateSourceService.updTemplateSource(templateSource);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	@RequestMapping(value = "/edit")
	public String edit(@RequestParam(value = "templateSourceId", required = false) Long templateSourceId, Model model) {
		if (templateSourceId != null) {
			TemplateSource templateSource = templateSourceService.getTemplateSource(templateSourceId);
			model.addAttribute("templateSource", templateSource);
		}
		return "site/template/source/edit";
	}

	/**
	 * 编辑模板.
	 */
	@RequestMapping(value = "/editInfo/{templateSourceId}")
	public String editInfo(@PathVariable(value = "templateSourceId") Long templateSourceId, Model model) {
		TemplateSource templateSource = templateSourceService.getTemplateSource(templateSourceId);
		try {
			model.addAttribute("templateContent", new String(templateSource.getSourceEntity().getSrcEntity(), "UTF-8"));
		} catch (Exception e) {
		}
		model.addAttribute("templateSource", templateSource);
		return "site/template/source/info";
	}

	@RequestMapping(value = "/editContent/{templateSourceId}")
	public String editContent(@PathVariable(value = "templateSourceId") Long templateSourceId, Model model) {
		TemplateSource templateSource = templateSourceService.getTemplateSource(templateSourceId);
		try {
			model.addAttribute("templateSourceContent", new String(templateSource.getSourceEntity().getSrcEntity(), "UTF-8"));
		} catch (Exception e) {
		}
		model.addAttribute("templateSource", templateSource);
		return "site/template/source/content";
	}

	@RequestMapping(value = "/saveInfo")
	public String saveInfo(@ModelAttribute(value = "templateSource") TemplateSource templateSource,
			@RequestParam(value = "templateSourceFile", required = false) MultipartFile templateSourceFile, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String message = "模板资源保存";
		InputStream in = null;
		try {
			request.setCharacterEncoding("UTF-8");
			TemplateSource vo = templateSourceService.getTemplateSource(templateSource.getId());
			if (templateSourceFile != null && templateSourceFile.getSize() > 0) {
				TemplatesrcEntity tplEntityVo = new TemplatesrcEntity();
				vo.setSize(ConvertUtil.kb(templateSourceFile.getSize()));
				byte[] buffer = new byte[Integer.parseInt(String.valueOf(templateSourceFile.getSize()))];
				in = new BufferedInputStream(templateSourceFile.getInputStream(), Integer.parseInt(String.valueOf(templateSourceFile.getSize())));
				in.read(buffer);
				tplEntityVo.setSrcEntity(buffer);
				vo.setSourceEntity(tplEntityVo);

				in.close();
			}
			vo.setDescribe(templateSource.getDescribe());
			templateSourceService.updTemplateSource(vo);
			message += "成功";
		} catch (Exception e) {
			message += "失败";
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
			}
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/template/source/editInfo/" + templateSource.getId();
	}

	@RequestMapping(value = "/saveContent")
	public String saveContent(@ModelAttribute(value = "templateSource") TemplateSource templateSource,
			@RequestParam(value = "templateSourceContent") String templateSourceContent, RedirectAttributes redirectAttributes) {
		String message = "资源内容保存";
		try {
			TemplateSource vo = templateSourceService.getTemplateSource(templateSource.getId());
			TemplatesrcEntity tplEntityVo = new TemplatesrcEntity();
			tplEntityVo.setSrcEntity(templateSourceContent.getBytes("UTF-8"));
			vo.setSourceEntity(tplEntityVo);
			vo.setRelease(false);
			templateSourceService.updTemplateSource(vo);
			message += "成功";
		} catch (Exception e) {
			message = "失败";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/template/source/editContent/" + templateSource.getId();
	}

	@RequestMapping(value = "/exportzip")
	public void exportZip(@RequestParam(value = "templateSourceId", required = false) Long templateSourceId, HttpServletResponse response) {
		ZipOutputStream zos = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("application/zip;charset=UTF-8");
			response.setHeader("Content-Disposition", "attachment; filename=souce" + templateSourceId + ".zip");

			zos = new ZipOutputStream(response.getOutputStream());
			zos.setEncoding("UTF-8");
			if (templateSourceId != null) {
				templateSourceService.exportTemplateSourceZip(templateSourceId, zos, "");
			} else {
				List<TemplateSource> templateSources = templateSourceService.getTemplaeSourceTreeList(false);
				for (TemplateSource source : templateSources) {
					templateSourceService.exportTemplateSourceZip(source.getId(), zos, EwcmsContextUtil.getCurrentSite().getSiteName() + "/");
				}
			}
			zos.flush();
			zos.close();
		} catch (Exception e) {

		} finally {
			if (zos != null) {
				try {
					zos.close();
					zos = null;
				} catch (Exception e) {
				}
			}
		}
	}
}
