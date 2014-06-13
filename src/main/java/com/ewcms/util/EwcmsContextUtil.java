/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.ewcms.site.model.Site;
import com.ewcms.security.model.Role;
import com.ewcms.security.service.ShiroDbRealm.ShiroUser;

/**
 *
 * @author wu_zhijun
 */
public class EwcmsContextUtil {
	
	private static Subject getSubject(){
		return SecurityUtils.getSubject();
	}

    public static Site getCurrentSite() {
        return getShiroUser().getSite();
    }
    
    public static void setCurrentSite(Site site){
    	ShiroUser shiroUser = getShiroUser();
    	shiroUser.setSite(site);
    	shiroUser.setSiteId(site.getId());
    }
    
    public static Long getCurrentSiteId(){
    	return getShiroUser().getSiteId();
    }
    
    public static ShiroUser getShiroUser(){
    	return (ShiroUser) getSubject().getPrincipal();
    }
    
    public static String getLoginName(){
    	return getShiroUser().getLoginName();
    }
    
    public static String getRealName(){
    	return getShiroUser().getRealName();
    }
    
    public static List<String> getRoleNames(){
    	List<String> roleNames = new ArrayList<String>();
    	Set<Role> roles = getShiroUser().getRoles();
    	for (Role role : roles){
    		roleNames.add(role.getRoleName());
    	}
    	return roleNames;
    }
    
    public static Boolean isPermitted(String expression){
    	return getSubject().isPermitted(expression);
    }
    
    public static Boolean hasRole(String expression){
    	return getSubject().hasRole(expression);
    }
    
}
