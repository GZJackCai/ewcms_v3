package com.ewcms.site.web.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.security.acl.model.AclEntry;
//import com.ewcms.security.acl.model.AclIdEntity;
import com.ewcms.security.acl.model.AclSid;
//import com.ewcms.site.model.Channel;
import com.ewcms.site.service.ChannelService;
import com.ewcms.web.vo.PropertyGrid;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/site/channel/acl")
public class ChannelAclController {

	private static final Map<String, Object> EDITOR = createEditor();
//	private static final Map<String, Object> CHECK = createCheck();
	
	private final static String ACL_USERS = "用户权限";
	private final static String ACL_ROLES = "角色权限";
//	private final static String ACL_OTHER = "其他";

	@Autowired
	private ChannelService channelService;

	@RequestMapping(value = "/index/{channelId}")
	public String index(@PathVariable(value = "channelId") Long channelId, Model model) {
		model.addAttribute("channelId", channelId);
		return "site/channel/acl";
	}

	@RequestMapping(value = "/query/{channelId}")
	public @ResponseBody
	Map<String, Object> query(@PathVariable(value = "channelId") Long channelId) {
		List<AclEntry> aclEntries = channelService.findByAclChannel(channelId);
		List<PropertyGrid> propertyGrids = new ArrayList<PropertyGrid>();
		if (aclEntries != null && !aclEntries.isEmpty()) {
			for (AclEntry aclEntry : aclEntries) {
				if (!aclEntry.getGranting()) continue;
				PropertyGrid propertyGrid = null;
				AclSid aclSid = aclEntry.getAclSid();
				if (aclSid != null) {
					Boolean principal = aclSid.getPrincipal();
					String sid = aclSid.getSid();
					if (principal) {
						propertyGrid = new PropertyGrid(sid, aclEntry.getMask(), ACL_USERS, EDITOR);
					} else {
						propertyGrid = new PropertyGrid(sid, aclEntry.getMask(), ACL_ROLES, EDITOR);
					}
					propertyGrids.add(propertyGrid);
				}
			}
		}

//		AclIdEntity aclIdEntity = channelService.findByClassNameAndObject(Channel.class.getName().toString(), channelId);
//		Boolean inheriting = false;
//		if (aclIdEntity != null && aclIdEntity.getInheriting() != null) {
//			inheriting = aclIdEntity.getInheriting();
//		}
//		PropertyGrid propertyGrid = new PropertyGrid("继承权限", inheriting, ACL_OTHER, CHECK);
//		propertyGrids.add(propertyGrid);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", propertyGrids.size());
		resultMap.put("rows", propertyGrids);
		return resultMap;
	}

	private static Map<String, Object> createEditor() {
		Map<String, Object> editor = new HashMap<String, Object>();
		editor.put("type", "combobox");
		Map<String, Object> options = new HashMap<String, Object>();
		editor.put("options", options);
		List<Map<String, Object>> comboboxData = new ArrayList<Map<String, Object>>();
		options.put("data", comboboxData);
		Integer[] values = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9};
		String[] texts = new String[] { "查看文章", "编辑文章", "删除文章", "审核文章", "发布文章", "查看栏目", "修改栏目", "删除栏目", "管理员" };
		for (int i = 0; i < values.length; i++) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("value", values[i]);
			data.put("text", texts[i]);
			comboboxData.add(data);
		}

		return editor;
	}
	
//	private static Map<String,Object> createCheck(){
//        Map<String,Object> editor = new HashMap<String,Object>();
//        
//        editor.put("type", "checkbox");
//        Map<String,Object> checkboxData = new HashMap<String,Object>();
//        editor.put("options",checkboxData);
//        checkboxData.put("on", Boolean.TRUE);
//        checkboxData.put("off", Boolean.FALSE);
//        
//        return editor;
//    }
}
