package com.ewcms.security.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.security.model.Permission;
import com.ewcms.security.model.Role;
import com.ewcms.security.model.User;
import com.ewcms.security.service.AccountService;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.vo.PropertyGrid;

@Controller
@RequestMapping(value = "/security/userdetail")
public class UserDetailController {
	
	private static final String ROLE_TITLE = "角色";
	private static final String PERMISSION_TITLE = "权限";
	
	@Autowired
	private AccountService accountService;
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/index")
	public String index(@RequestParam("id") Long id, Model model){
		model.addAttribute("id", id);
		return "security/user/detail";
	}

	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params, @RequestParam("id") Long id){
		User user = accountService.getUser(id);
		List<PropertyGrid> propertyGrids = new ArrayList<PropertyGrid>();
		if (user != null){
			Set<Role> roles = user.getRoles(); 
			for (Role role : roles){
				PropertyGrid propertyGrid = new PropertyGrid(role.getRoleName(), role.getCaption(), ROLE_TITLE, null);
				propertyGrids.add(propertyGrid);
			}
			Set<Permission> permissions = user.getPermissions();
			for (Permission permission : permissions){
				PropertyGrid propertyGrid = new PropertyGrid(permission.getName(), permission.getCaption(), PERMISSION_TITLE, null);
				propertyGrids.add(propertyGrid);
			}
		}
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("total", propertyGrids.size());
		resultMap.put("rows", propertyGrids);
		return resultMap;
	}
	
	@RequestMapping(value = "/editRoleAndPermission")
	public @ResponseBody String editRoleAndPermission(@RequestParam(required = false) List<String> roleNames, @RequestParam(required = false) List<String> permissionNames, @RequestParam("id") Long id, @RequestParam("isRemove") Boolean isRemove){
		try{
			if (id == null) return "用户未选择";
			
			User user = accountService.getUser(id);
			if (user == null) return "用户不存在";
			
			if (roleNames != null && !roleNames.isEmpty()){
				Set<Role> roles = user.getRoles();
				if (isRemove){
					if (roles == null || roles.isEmpty()) return "未有移除的角色";
					for (String groupName : roleNames){
						Role role = accountService.findRoleByRoleName(groupName);
						roles.remove(role);
					}
				}else{
					for (String groupName : roleNames){
						Role role = accountService.findRoleByRoleName(groupName);
						roles.add(role);
					}
				}
				user.setRoles(roles);
			}
			if (permissionNames != null && !permissionNames.isEmpty()){
				Set<Permission> permissions = user.getPermissions();
				if (isRemove){
					if (permissions == null || permissions.isEmpty()) return "未有移除的权限";
					for (String permissionName : permissionNames){
						Permission permission = accountService.findPermissionByName(permissionName);
						permissions.remove(permission);
					}
				}else{
					for (String permissionName : permissionNames){
						Permission permission = accountService.findPermissionByName(permissionName);
						permissions.add(permission);
					}
				}
				user.setPermissions(permissions);
			}
			accountService.saveUser(user, false);
			if (isRemove){
				return "移除成功";
			}else{
				return "修改成功";
			}
		}catch(Exception e){
			return e.getMessage();
		}
	}
}
