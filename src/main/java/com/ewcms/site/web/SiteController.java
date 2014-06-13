package com.ewcms.site.web;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.publication.deploy.DeployOperatorable;
import com.ewcms.site.model.Organ;
import com.ewcms.site.model.Site;
import com.ewcms.site.model.SiteServer;
import com.ewcms.site.service.OrganService;
import com.ewcms.site.service.SiteService;
import com.ewcms.web.vo.TreeNode;

@Controller
@RequestMapping(value = "/site/setup")
public class SiteController {
	
	@Autowired
	private OrganService organService;
	@Autowired
	private SiteService siteService;
	
	@RequiresPermissions("site:server")
	@RequestMapping(value = "/serverSite/{siteId}")
	public String serverSite(@PathVariable(value = "siteId") Long siteId, Model model){
		model.addAttribute("site", siteService.getSite(siteId));
		return "site/organ/server";
	}
	
	@RequiresPermissions("site:config")
	@RequestMapping(value = "/configSite/{siteId}")
	public String configSite(@PathVariable(value = "siteId") Long siteId, Model model){
		model.addAttribute("site", siteService.getSite(siteId));
		return "site/organ/config";
	}
	
	@RequiresPermissions("site:parse")
	@RequestMapping(value = "/moveSite")
	public @ResponseBody Boolean moveSite(@RequestParam(value="siteId") Long siteId, @RequestParam(value="siteParentId") Long siteParentId){
		Boolean result = true;
		try {
			Site site = siteService.getSite(siteId);
			Site siteParent = siteService.getSite(siteParentId);
			site.setParent(siteParent);
			siteService.updSite(site);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	@RequiresPermissions("site:delete")
	@RequestMapping(value = "/delete")
	public @ResponseBody Boolean delSite(@RequestParam(value = "siteId") Long siteId){
		Boolean result = true;
		try {
			siteService.delSite(siteId);
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
	
	@RequiresPermissions("site:rename")
	@RequestMapping(value = "/renameSite")
	public @ResponseBody Boolean renameSite(@RequestParam(value = "siteId") Long siteid, @RequestParam(value = "siteName") String siteName){
		Boolean result = true;
		try{
			Site site = siteService.getSite(siteid);
			site.setSiteName(siteName);
			siteService.updSite(site);
		}catch(Exception e){
			result = false;
		}
		return result;
	}
	
	@RequiresPermissions("site:set")
	@RequestMapping(value = "/setSite")
	public @ResponseBody Boolean setSite(@RequestParam(value = "siteId", required = false) Long siteId, @RequestParam(value = "organId") Long organId){
		Boolean result = true;
		try{
			Organ organ = organService.getOrgan(organId);
			if (siteId != null){
				siteService.updSiteParent(organId, siteId, organ.getHomeSiteId());
			}
			organ.setHomeSiteId(siteId);
			organService.updOrgan(organ);
		}catch(Exception e){
			result = false;
		}
		return result;
	}
	
	@RequiresPermissions("site:add")
	@RequestMapping(value = "/addSite")
	public @ResponseBody Long addSite(@RequestParam(value = "siteId", required = false) Long siteId, @RequestParam(value = "siteName") String siteName, @RequestParam(value = "organId") Long organId){
		Long result = null;
		try{
			result = siteService.addSite(siteId, siteName, organId);
		}catch (Exception e){
		}
		return result;
	}
	
	@RequiresPermissions("site:config")
	@RequestMapping(value = "/saveConfig")
	public String saveInfo(@ModelAttribute(value="site") Site site, RedirectAttributes redirectAttributes){
		String message = "修改站点属性设置";
		try{
			Site oldSite = siteService.getSite(site.getId());
			site.setOrgan(oldSite.getOrgan());
			site.setParent(oldSite.getParent());
			site.setSiteServer(oldSite.getSiteServer());
			siteService.updSite(site);
			message += "成功";
		}catch(Exception e){
			message += "失败";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/setup/configSite/" + site.getId();
	}
	
	@RequiresPermissions("site:server")
	@RequestMapping(value = "/saveServer")
	public String saveServer(@ModelAttribute(value="site") Site site, RedirectAttributes redirectAttributes){
		String message = "修改站点发布设置";
		try{
			siteService.saveSiteServer(site);
			message += "成功";
		}catch(Exception e){
			message += "失败";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/setup/serverSite/" + site.getId();
	}
	
	@RequiresPermissions("site:server")
	@RequestMapping(value = "/siteServerTest")
	public @ResponseBody String siteServerTest(@ModelAttribute(value = "site") Site site) {
		try {
			SiteServer siteServer = site.getSiteServer();
			DeployOperatorable output = siteServer.getOutputType().deployOperator(siteServer);
			if(siteServer.getPassword() == null || siteServer.getPassword().length() == 0){
				if(siteServer.getId() != null){
					siteServer.setPassword(siteService.getSite(site.getId()).getSiteServer().getPassword());
				}
			}
			output.test();
			return "连接发布服务器成功";
		} catch (Exception e) {
			String errorMSG = "连接异常";
			if(e.getMessage().equals("error.output.notconnect"))errorMSG += "服务器连接失败\n请核对端口、服务器地址以及用户名密码的正确性";
			if(e.getMessage().equals("error.output.nodir"))errorMSG += "目录不存在";
			if(e.getMessage().equals("error.output.notwrite"))errorMSG += "没有可写权限";
			return errorMSG;
		}
	}
	
	@RequiresPermissions("site:view")
	@RequestMapping(value = "/tree")
	public @ResponseBody List<TreeNode> siteTree(@RequestParam(required=false, value="id") Long id, @RequestParam(required=false, value="siteId") Long siteId, @RequestParam(required=false, value="organId") Long organId) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		if (id == null) {
			TreeNode treeNode = new TreeNode();
			if(siteId != null){
				if(organId != null){
					treeNode.setChildren(siteService.getOrganSiteTreeList(siteId,organId));
				}else{
					treeNode.setChildren(siteService.getCustomerSiteTreeList(siteId));
				}
				treeNode.setId(siteId.toString());
			}else{
				if(organId != null){
					treeNode.setChildren(siteService.getOrganSiteTreeList(organId));
				}else{
					treeNode.setChildren(siteService.getCustomerSiteTreeList());
				}
			}
			treeNode.setText("机构站群管理");
			treeNode.setState("open");
		
			treeNodes.add(treeNode);
		}else{
			if (organId != null){
				treeNodes = siteService.getOrganSiteTreeList(id, organId);
			}else{
				treeNodes = siteService.getCustomerSiteTreeList(id);
			}
		}
		return treeNodes;
	}
}
