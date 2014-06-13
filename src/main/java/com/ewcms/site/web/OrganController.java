package com.ewcms.site.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.site.model.Organ;
import com.ewcms.site.service.OrganService;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.vo.TreeNode;

@Controller
@RequestMapping(value = "/site/organ")
public class OrganController {

	private static Logger logger = LoggerFactory.getLogger(OrganController.class);
	
	@Autowired
	private OrganService organService;

	@RequiresPermissions("organ:view")
	@RequestMapping(value = "/index")
	public String index() {
		return "site/organ/index";
	}

	@RequiresPermissions("organ:edit")
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = false, value = "organId") Long organId, Model model) {
		if (organId != null) {
			model.addAttribute("organ", organService.getOrgan(organId));
		}
		return "site/organ/edit";
	}
	
	@RequestMapping(value = "/download/{organId}")
	public ModelAndView download(@PathVariable(value = "organId") Long organId, HttpServletResponse response) throws IOException{
		response.setContentType("text/html;charset=utf-8");
		ByteArrayInputStream bis = null;
		BufferedOutputStream bos = null;
		try{
			Organ organ = organService.getOrgan(organId);
			if (organ != null && organ.getIcon() != null && organ.getIcon().length > 0){
				response.setContentType(organ.getIconType());  
				bis = new ByteArrayInputStream(organ.getIcon());
				bos = new BufferedOutputStream(response.getOutputStream());
				byte[] buffer = new byte[2048];
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buffer, 0, buffer.length))){
					bos.write(buffer, 0 , bytesRead);
				}
			}
		}catch(Exception e){
			logger.warn("文件不是有效的图片");
			byte[] buffer = "文件不是有效的图片".getBytes();
			bos = new BufferedOutputStream(response.getOutputStream());
			bos.write(buffer);
		}finally{
			if (bis != null){
				bis.close();
			}
			if (bos != null){
				bos.close();
			}
		}
		return null;
	}

	@RequiresPermissions("organ:edit")
	@RequestMapping(value = "/editInfo", method = RequestMethod.GET)
	public String editOrganInfo(@RequestParam(required = false, value = "organId") Long organId, Model model) {
		if (organId != null) {
			model.addAttribute("organ", organService.getOrgan(organId));
		}
		return "site/organ/info";
	}

	@RequiresPermissions("site:edit")
	@RequestMapping(value = "/editSite", method = RequestMethod.GET)
	public String editSite(@RequestParam(required = false, value = "organId") Long organId, Model model) {
		if (organId != null) {
			model.addAttribute("organ", organService.getOrgan(organId));
		}
		return "site/organ/site";
	}

	@RequiresPermissions("organ:add")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public @ResponseBody Long addOrgan(@RequestParam(value = "organId") Long organId,
			@RequestParam(value = "organName") String organName) {
		Long newOrganId = null;
		try{
			newOrganId = organService.addOrgan(organId, organName);
		}catch(Exception e){
			
		}
		return newOrganId;
	}
	
	@RequiresPermissions("organ:rename")
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	public @ResponseBody Long renameOrgan(@RequestParam(value = "organId") Long organId,
			@RequestParam(value = "organName") String organName){
		if (organId != null) {
			Organ organ = organService.getOrgan(organId);
			organ.setName(organName);
			organService.updOrgan(organ);
			return organId;
		}else{
			return null;
		}
	}
	
	@RequiresPermissions("organ:delete")
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public @ResponseBody Boolean delOrgan(@RequestParam(value = "organId") Long organId){
		Boolean result = false;
		try{
			organService.delOrgan(organId);
			result = true;
		} catch(Exception e){
		}
		return result;
	}
	
	@RequiresPermissions("organ:parse")
	@RequestMapping(value = "/move", method = RequestMethod.POST)
	public @ResponseBody Boolean moveOrgan(@RequestParam(value = "organId") Long organId, @RequestParam(value="organParentId", required=false)Long organParentId){
		Boolean result = false;
		try{
			Organ organ = organService.getOrgan(organId);
			if (organ != null){
				if (organParentId == null){
					organ.setParent(null);
				}else{
					organ.setParent(organService.getOrgan(organParentId));
				}
				organService.updOrgan(organ);
				result = true;
			}
		}catch(Exception e){
		}
		return result;
	}

	@RequiresPermissions("organ:edit")
	@RequestMapping(value = "/saveInfo", method = RequestMethod.POST)
	public String saveOrganInfo(@ModelAttribute(value="organ") Organ organ, @RequestParam(value = "iconFile", required = false) MultipartFile image, RedirectAttributes redirectAttributes){
		String message = "修改机构信息";
		try {
			if (image != null && image.getBytes().length > 0){
				organ.setIconType(image.getContentType());
				organ.setIcon(image.getBytes());
			}else{
				Organ oldOrgan = organService.getOrgan(organ.getId());
				organ.setIconType(oldOrgan.getIconType());
				organ.setIcon(oldOrgan.getIcon());
			}
			organService.updOrgan(organ);
			message += "成功";
		} catch (IOException e) {
			logger.warn("上传文件错误");
			message += "失败";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/organ/editInfo?organId=" + organ.getId();
	}
	
	@RequiresPermissions("organ:view")
	@RequestMapping(value = "/query")
	public @ResponseBody List<TreeNode> searchOrgan(@ModelAttribute QueryParameter params, @RequestParam(required = false, value = "id") Long organId) {
		if (organId == null) {
			TreeNode treeRoot = new TreeNode();
			treeRoot.setText("客户机构");
			treeRoot.setState("open");
			treeRoot.setChildren(organService.getOrganTreeList());

			List<TreeNode> result = new ArrayList<TreeNode>();
			result.add(treeRoot);

			return result;
		}
		List<TreeNode> treeNodes = organService.getOrganTreeList(organId);
		if (treeNodes.isEmpty()) {
			return null;
		} else {
			return treeNodes;
		}
	}
}
