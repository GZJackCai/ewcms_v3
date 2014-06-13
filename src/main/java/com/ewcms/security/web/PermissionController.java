package com.ewcms.security.web;

import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.security.service.AccountService;
import com.ewcms.web.QueryParameter;

@Controller
@RequestMapping(value = "/security/permission")
public class PermissionController {

	@Autowired
	private AccountService accountService;
	
	@RequiresPermissions("permission:view")
	@RequestMapping(value = "/index")
	public String index(){
		return "security/permission/index";
	}
	
	@RequiresPermissions("permission:view")
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return accountService.searchPermission(params);
	}

}
