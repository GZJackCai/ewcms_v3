package com.ewcms.site.web.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.security.acl.annotation.AclEnum;
import com.ewcms.security.acl.annotation.ChannelAcl;
import com.ewcms.site.model.Channel;
import com.ewcms.site.service.ChannelService;
import com.ewcms.site.service.TemplateService;
import com.ewcms.util.Collections3;
import com.ewcms.util.ConvertUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.util.ChannelNode;
import com.ewcms.web.util.TreeNodeConvert;
import com.ewcms.web.vo.TreeNode;

@Controller(value = "channel.index")
@RequestMapping(value = "/site/channel")
public class ChannelController {

	@Autowired
	private ChannelService channelService;
	@Autowired
	private TemplateService templateService;

	@RequestMapping(value = "/index")
	public String index() {
		return "site/channel/index";
	}

	/**
	 * 获取站点已发布专栏树.
	 */
	@RequestMapping(value = "/tree")
	public @ResponseBody
	List<TreeNode> channelTree(@RequestParam(value = "id", required = false) Long channelId) {
		return constructTree(channelId, true, true);
	}

	/**
	 * 获取站点所有专栏树.
	 */
	@RequestMapping(value = "/treePub")
	public @ResponseBody
	List<TreeNode> channelTreePub(@RequestParam(value = "id", required = false) Long channelId) {
		return constructTree(channelId, false, false);
	}

	private List<TreeNode> constructTree(Long channelId, Boolean isPub, Boolean isArticle) {
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		try {
			if (channelId == null) {
				ChannelNode rootVo = channelService.channelNodeRoot();
				if (isPub && !rootVo.isPublicable()) {
					return treeNodes;
				}
				TreeNode treeFile = new TreeNode();
				treeFile.setId(rootVo.getId().toString());
				treeFile.setText(rootVo.getName());
				treeFile.setState("open");
				treeFile.setIconCls("icon-channel-site");
				Map<String, String> attributes = new HashMap<String, String>();
				treeFile.setAttributes(attributes);
				String loginName = EwcmsContextUtil.getLoginName();
				List<String> roleNames = EwcmsContextUtil.getRoleNames(); 
				if (loginName.equals("admin") || Collections3.convertToString(roleNames, ",").indexOf("ROLE_ADMIN") > 0)
					TreeNodeConvert.treeNodeMask(attributes, 9);
				else
					TreeNodeConvert.treeNodeMask(attributes, 1);
				treeFile.setChildren(TreeNodeConvert.channelNodeConvert(channelService.getChannelChildren(rootVo.getId(), isPub, isArticle)));
				treeNodes.add(treeFile);
			} else {
				treeNodes = TreeNodeConvert.channelNodeConvert(channelService.getChannelChildren(channelId, isPub, isArticle));
			}
		} catch (Exception e) {
		}
		return treeNodes;
	}

	@ChannelAcl(acl = { AclEnum.WRITER_CHANNEL }, position = 0)
	@RequestMapping(value = "/add")
	public @ResponseBody
	Long addChannel(@RequestParam(value = "channelParentId", required = false) Long channelParentId, @RequestParam(value = "channelName") String channelName) {
		Long channelId = null;
		try {
			channelId = channelService.addChannel(channelParentId, channelName);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return channelId;
	}

	/**
	 * 重命名专栏.
	 */
	@ChannelAcl(acl = { AclEnum.WRITER_CHANNEL }, position = 0)
	@RequestMapping(value = "/rename")
	public @ResponseBody
	Long renameChannel(@RequestParam(value = "channelId") Long channelId, @RequestParam(value = "channelName") String channelName) {
		try {
			Channel vo = channelService.getChannel(channelId);
			vo.setName(channelName);
			channelService.updChannel(vo);
			return channelId;
		} catch (Exception e) {
			return null;
		}
	}

	@ChannelAcl(acl = { AclEnum.DELETE_CHANNEL }, position = 0)
	@RequestMapping(value = "/delete")
	public @ResponseBody
	Boolean delChannel(@RequestParam(value = "channelId") Long channelId) {
		Boolean result = false;
		try {
			channelService.delChannel(channelId);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	@ChannelAcl(acl = { AclEnum.WRITER_CHANNEL }, position = 1)
	@RequestMapping(value = "/moveto")
	public @ResponseBody
	Boolean movetoChannel(@RequestParam(value = "channelParentId", required = false) Long channelParentId, @RequestParam(value = "channelId") Long channelId) {
		Boolean result = false;
		try {
			channelService.moveToChannel(channelId, channelParentId);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	@ChannelAcl(acl = { AclEnum.VIEW_CHANNEL }, position = 0)
	@RequestMapping(value = "/edit")
	public String edit(@RequestParam(value = "channelId", required = false) Long channelId, Model model) {
		if (channelId != null) {
			Channel channel = channelService.getChannel(channelId);
			model.addAttribute("channel", channel);
		}
		return "site/channel/edit";
	}

	@ChannelAcl(acl = { AclEnum.VIEW_CHANNEL }, position = 0)
	@RequestMapping(value = "/editInfo")
	public String editInfo(@RequestParam(value = "channelId", required = false) Long channelId, Model model) {
		Channel channel = channelService.getChannel(channelId);
		model.addAttribute("channel", channel);
		return "site/channel/info";
	}

	@ChannelAcl(acl = { AclEnum.WRITER_CHANNEL }, position = 0, className = "Channel")
	@RequestMapping(value = "/saveInfo")
	public String saveInfo(@ModelAttribute(value = "channel") Channel channel, RedirectAttributes redirectAttributes) {
		String message = "栏目保存";
		try {
			Channel vo = channelService.getChannel(channel.getId());

			if (vo.getParent() != null) {
				vo.setDir(channel.getDir());
				vo.setListSize(channel.getListSize());
				vo.setUrl(channel.getUrl());
				vo.setMaxSize(channel.getMaxSize());
				vo.setDescribe(channel.getDescribe());
				vo.setType(channel.getType());
				vo.setIconUrl(channel.getIconUrl());
			}

			vo.setPublicenable(channel.getPublicenable());
			channelService.updChannel(vo);
			message += "成功";
		} catch (Exception e) {
			message += "失败";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/site/channel/editInfo?channelId=" + channel.getId();
	}

	@ChannelAcl(acl = { AclEnum.WRITER_CHANNEL }, position = 0)
	@RequestMapping(value = "/upChannel")
	public @ResponseBody
	Boolean upChannel(@RequestParam(value = "channelId") Long channelId, @RequestParam(value = "channelParentId", required = false) Long channelParentId) {
		Boolean result = false;
		try {
			channelService.upChannel(channelId, channelParentId);
			result = true;
		} catch (Exception e) {

		}
		return result;
	}

	@ChannelAcl(acl = { AclEnum.WRITER_CHANNEL }, position = 0)
	@RequestMapping(value = "/downChannel")
	public @ResponseBody
	Boolean downChannel(@RequestParam(value = "channelId") Long channelId, @RequestParam(value = "channelParentId", required = false) Long channelParentId) {
		Boolean result = false;
		try {
			channelService.downChannel(channelId, channelParentId);
			result = true;
		} catch (Exception e) {

		}
		return result;
	}

	@ChannelAcl(acl = { AclEnum.WRITER_CHANNEL }, position = 0)
	@RequestMapping(value = "/moveSort")
	public @ResponseBody
	Boolean moveSortChannel(@RequestParam(value = "channelId") Long channelId, @RequestParam(value = "channelParentId", required = false) Long channelParentId,
			@RequestParam(value = "sort") Long sort) {
		Boolean result = false;
		try {
			channelService.moveSortChannel(channelId, channelParentId, sort);
			result = true;
		} catch (Exception e) {

		}
		return result;
	}

	@RequestMapping(value = "/exportzip")
	public void exportZip(@RequestParam(value = "channelId", required = false) Long channelId, HttpServletResponse response) {
		ZipOutputStream zos = null;
		try {
			if (channelId != null) {
				response.setCharacterEncoding("UTF-8");
				response.setContentType("application/x-download;charset=UTF-8");
				response.setHeader("Content-Disposition", "attachment; filename=channel" + channelId + ".zip");

				zos = new ZipOutputStream(response.getOutputStream());
				zos.setEncoding("UTF-8");

				channelService.exportChannelZip(channelId, zos, "");
				zos.flush();
				zos.close();
			}
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

	@RequestMapping(value = "/channelSource")
	public String channelTemplateSource(@RequestParam(value = "channelId") Long channelId, Model model) {
		model.addAttribute("channelId", channelId);
		return "site/channel/source";
	}

	@RequestMapping(value = "/connect/{channelId}")
	public @ResponseBody Boolean connect(@PathVariable(value = "channelId") Long channelId) {
		Boolean result = false;
		if (channelId != null) {
			templateService.connectChannel(channelId);
			result = true;
		}
		return result;
	}

	@RequestMapping(value = "/disConnect/{channelId}")
	public @ResponseBody Boolean disConnect(@PathVariable(value = "channelId") Long channelId) {
		Boolean result = false;
		if (channelId != null) {
			templateService.disConnectChannel(channelId);
			result = true;
		}
		return result;
	}

	@RequestMapping(value = "/pinYin")
	public @ResponseBody
	String pinYin(@RequestParam(value = "channelName") String channelName) {
		if (channelName.trim().length() > 0) {
			return ConvertUtil.pinYin(channelName);
		} else {
			return "";
		}
	}

	@RequestMapping(value = "/forceRelease")
	public @ResponseBody
	String forceRelease(@RequestParam(value = "channelId") Long channelId, @RequestParam(value = "children") Integer children) {
		try {
			if (children.intValue() == 1)
				channelService.forceRelease(EwcmsContextUtil.getCurrentSiteId(), channelId, true);
			else
				channelService.forceRelease(EwcmsContextUtil.getCurrentSiteId(), channelId, false);
			return "强制发布正在后台运行中...";
		} catch (Exception e) {
			return "强制发布失败";
		}
	}

	@RequestMapping(value = "/saveAcl/{channelId}")
	public @ResponseBody Boolean saveAcl(@PathVariable(value = "channelId") Long channelId, @RequestParam(value = "name") String name, @RequestParam(value = "type")String type, @RequestParam(value = "mask")Integer mask){
		try{
			Boolean principal = false;
			if (type.equals("user")){
				principal = true;
			}
			Channel channel = channelService.getChannel(channelId);
			channelService.saveAcl(channel, name, principal, mask);
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
	@RequestMapping(value = "/deleteAcl/{channelId}")
	public @ResponseBody Boolean deleteAcl(@PathVariable(value = "channelId") Long channelId, @RequestParam(value = "name") String name, @RequestParam(value = "type")String type){
		try{
			Boolean principal = false;
			if (type.equals("user")) principal = true;
			channelService.deleteAcl(channelId, name, principal);
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
	@RequestMapping(value = "/inheritingAcl/{channelId}")
	public @ResponseBody Boolean inheritingAcl(@PathVariable(value = "channelId") Long channelId, @RequestParam(value = "inherit") Boolean inherit){
		try{
			channelService.inheritingAcl(channelId, inherit);
			return true;
		} catch(Exception e){
			return false;
		}
	}
	
	@RequestMapping(value = "/apply/index/{channelId}")
	public String applyIndex(@PathVariable(value = "channelId") Long channelId, Model model) {
		model.addAttribute("channelId", channelId);
		return "site/channel/apply";
	}

	@RequestMapping(value = "/apply/query/{channelId}")
	public @ResponseBody Map<String, Object> applyQuery(@PathVariable(value = "channelId")Long channelId, @ModelAttribute QueryParameter params){
		return channelService.searchApply(channelId, params);
	}
	
	@RequestMapping(value = "/apply/delete/{channelId}")
	public @ResponseBody Boolean applyDelete(@PathVariable(value = "channelId")Long channelId,  @RequestParam(required = false) List<Long> selections){
		Boolean result = false;
		try{
			for (Long selection : selections){
				channelService.delAppChannel(channelId, selection);
			}
			result = true;
		}catch(Exception e){
		}
		return result;
	}
}
