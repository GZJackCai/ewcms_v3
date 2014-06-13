package com.ewcms.security.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.security.model.Role;
import com.ewcms.security.model.Permission;
import com.ewcms.security.service.AccountService;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.vo.ComboBox;

@Controller
@RequestMapping(value = "/security/role")
public class RoleController {
	@Autowired
	private AccountService accountService;
	
	@RequiresPermissions("role:view")
	@RequestMapping(value = "/index")
	public String index(){
		return "security/role/index";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false)List<Long> selections, Model model){
		if(selections == null || selections.isEmpty()){
			model.addAttribute("role", new Role());
			model.addAttribute("selections", new ArrayList<Long>(0));
		}else{
			Role role = accountService.getRole(selections.get(0));
			role = (role == null ? new Role() : role);
			model.addAttribute("role", role);
			model.addAttribute("selections", selections);
		}
		return "security/role/edit";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("role")Role role, @RequestParam(required = false) List<Long> selections, @RequestParam(required = false) List<Long> permissionIds, Model model){
		Boolean close = Boolean.FALSE;
		Set<Permission> permissions = new HashSet<Permission>();
		if (permissionIds != null && !permissionIds.isEmpty()){
			for (Long permissionId : permissionIds){
				Permission permission = accountService.getPermission(permissionId);
				permissions.add(permission);
			}
		}
		role.setPermissions(permissions);
		if (role.getId() != null && StringUtils.hasText(role.getId().toString())){
			accountService.saveRole(role);
			selections.remove(0);
			if(selections == null || selections.isEmpty()){
				close = Boolean.TRUE;
			}else{
				role = accountService.getRole(selections.get(0));
				model.addAttribute("role", role);
				model.addAttribute("selections", selections);
			}
		}else{
			accountService.saveRole(role);
			selections = selections == null ? new ArrayList<Long>() : selections;
			selections.add(0, role.getId());
			model.addAttribute("role", new Role());
			model.addAttribute("selections", selections);
		}
		model.addAttribute("close",close);
		
		return "security/role/edit";
	}
	
	@RequiresPermissions("role:delete")
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections){
		for (Long id : selections){
			accountService.deleteRole(id);
    	}
    	return "security/role/index";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/checkRoleName")
	public @ResponseBody String checkRoleName(@RequestParam("oldRoleName") String oldRoleName, @RequestParam("roleName") String roleName) {
		if (roleName.equals(oldRoleName) || accountService.findRoleByRoleName(roleName) == null) {
			return "true";
		}
		return "false";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/checkCaption")
	public @ResponseBody String checkCaption(@RequestParam("oldCaption") String oldCaption, @RequestParam("caption") String caption){
		if (caption.equals(oldCaption) || accountService.findRoleByCaption(caption) == null){
			return "true";
		}
		return "false";
	}
	
	@RequiresPermissions("role:edit")
	@RequestMapping(value = "/findAllPermission")
	public @ResponseBody List<ComboBox> findAllPermission(@RequestParam("id") Long id){
		List<Permission> permissions = accountService.getAllPermission();				
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		if (permissions == null || permissions.isEmpty()) return comboBoxs;
		for (Permission permission : permissions){
			ComboBox comboBox = new ComboBox();
			Permission entity = accountService.findSelectedPermission(id, permission.getId());
			if (entity != null) comboBox.setSelected(true);
			comboBox.setId(permission.getId());
			comboBox.setText(permission.getCaption());
			comboBoxs.add(comboBox);
		}
		return comboBoxs;
	}

	
	@RequiresPermissions("role:view")
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return accountService.searchRole(params);
	}
}
