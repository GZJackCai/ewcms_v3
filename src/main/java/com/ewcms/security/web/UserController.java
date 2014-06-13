/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.security.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.security.model.Role;
import com.ewcms.security.model.User;
import com.ewcms.security.service.AccountService;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.vo.ComboBox;

@Controller
@RequestMapping(value = "/security/user")
public class UserController {

	@Autowired
	private AccountService accountService;
	
	@RequiresPermissions("user:view")
	@RequestMapping(value = "/index")
	public String index(){
		return "security/user/index";
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false)List<Long> selections, Model model){
		if(selections == null || selections.isEmpty()){
			model.addAttribute("user", new User());
			model.addAttribute("selections", new ArrayList<Long>(0));
		}else{
			User user = accountService.getUser(selections.get(0));
			user = (user == null ? new User() : user);
			model.addAttribute("user", user);
			model.addAttribute("selections", selections);
		}
		model.addAttribute("allRoles", accountService.getAllRole());
		return "security/user/edit";
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute("user")User user, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (user.getId() != null && StringUtils.hasText(user.getId().toString())){
				User oldUser = accountService.getUser(user.getId());
				
				user.setPermissions(oldUser.getPermissions());
				user.setRoles(oldUser.getRoles());
				
				accountService.saveUser(user, true);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					user = accountService.getUser(selections.get(0));
					model.addAttribute("user", user);
					model.addAttribute("selections", selections);
				}
			}else{
				accountService.saveUser(user, true);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, user.getId());
				model.addAttribute("user", new User());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "security/user/edit";
	}
	
	@RequiresPermissions("user:delete")
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		for (Long id : selections){
			try{
				accountService.deleteUser(id);
			}catch(ServiceException e){
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
    	}
		return "redirect:/security/user/index";
    	//return "security/user/index";
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/checkLoginName")
	public @ResponseBody String checkLoginName(@RequestParam("oldLoginName") String oldLoginName,	@RequestParam("loginName") String loginName) {
		if (loginName.equals(oldLoginName) || accountService.findUserByLoginName(loginName) == null) {
			return "true";
		}
		return "false";
	}
	
	@RequiresPermissions("user:edit")
	@RequestMapping(value = "/findAllRole")
	public @ResponseBody List<ComboBox> findAllRole(@RequestParam("id") Long id){
		List<Role> roles = accountService.getAllRole();
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();
		if (roles == null || roles.isEmpty()) return comboBoxs;
		for (Role role : roles){
			ComboBox comboBox = new ComboBox();
			Role entity = accountService.findSelectedRole(id, role.getId());
			if (entity != null) comboBox.setSelected(true);
			comboBox.setId(role.getId());
			comboBox.setText(role.getRoleName());
			comboBoxs.add(comboBox);
		}
		return comboBoxs;
	}
	
	@RequestMapping(value = "/password")
	public String password(){
		return "/security/account/password";
	}
	
	@RequestMapping(value = "/savePassword")
	public String savePassword(@RequestParam(value = "loginName") String loginName, @RequestParam(value = "newPassword") String newPassword, RedirectAttributes redirectAttributes){
		try{
			accountService.updatePassword(loginName, newPassword);
			return "redirect:/logout";
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/security/user/password";
		}
	}
	
	@RequestMapping(value = "/info/{loginName}")
	public String info(@PathVariable(value = "loginName") String loginName, Model model){
		User user = accountService.findUserByLoginName(loginName);
		model.addAttribute("user", user);
		return "/security/account/user";
	}
	
	@RequestMapping(value = "/saveUserInfo")
	public String updUserInfo(@ModelAttribute("user")User user, RedirectAttributes redirectAttributes){
		try{
			accountService.updUserInfo(user);
			return "redirect:/logout";
		}catch(Exception e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/security/user/info/" + user.getLoginName();
		}
	}
	
	@RequiresPermissions("user:view")
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return accountService.searchUser(params);
	}
	
}
