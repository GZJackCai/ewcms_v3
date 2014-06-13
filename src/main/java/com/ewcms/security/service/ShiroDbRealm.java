package com.ewcms.security.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.ewcms.security.acl.model.AclEntry;
import com.ewcms.security.acl.model.AclIdEntity;
import com.ewcms.security.acl.service.AclService;
import com.ewcms.security.acl.util.AclUtil;
import com.ewcms.security.model.Role;
import com.ewcms.security.model.User;
import com.ewcms.security.util.Digests;
import com.ewcms.site.model.Site;
import com.ewcms.util.Collections3;
import com.ewcms.util.Encodes;
import com.ewcms.web.filter.CaptchaUsernamePasswordToken;
import com.octo.captcha.service.CaptchaService;

public class ShiroDbRealm extends AuthorizingRealm {
	
	private static final int INTERATIONS = 1024;
	private static final int SALT_SIZE = 8;
	private static final String ALGORITHM = "SHA-1";
	
	private AccountService accountService;
	private AclService aclService;
    private CaptchaService captchaService;
    
    @Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
    
    @Autowired
    public void setAclService(AclService aclService){
    	this.aclService = aclService;
    }
    
    @Autowired
    public void setCaptchaService(CaptchaService captchaService){
    	this.captchaService = captchaService;
    }
    
	/**
	 * 认证回调函数, 登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		CaptchaUsernamePasswordToken token = (CaptchaUsernamePasswordToken) authcToken;
		
		String captcha = token.getCaptcha().toUpperCase();
		String captchaId = SecurityUtils.getSubject().getSession().getId().toString();
		if (!captchaService.validateResponseForID(captchaId, captcha)) {
			 throw new AuthenticationException("bad checkecode");
		}
		
		User user = accountService.findUserByLoginName(token.getUsername());
		if (user != null) {
			if (user.getStatus()) throw new DisabledAccountException();
			byte[] salt = Encodes.decodeHex(user.getSalt());
			
			return new SimpleAuthenticationInfo(new ShiroUser(user.getLoginName(), user.getRealName(), user.getRoles()), user.getPassword(), ByteSource.Util.bytes(salt), getName());
		} else {
			return null;
		}
	}

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.fromRealm(getName()).iterator().next();
		User user = accountService.findUserByLoginName(shiroUser.getLoginName());
		if (user != null) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			List<String> permissionNames = new ArrayList<String>();
			List<String> roleNames = user.getRoleNames();
			
			if (Collections3.convertToString(roleNames, ",").startsWith("ROLE_ADMIN")){
				permissionNames.add("*");
			}else{
				for (Role role : user.getRoles()) {
					//把角色组放入
					List<String> expressions = role.getPermissionExpressions();
					for (String expression : expressions){
						permissionNames.add(expression);
					}
				}
				
				//把用户权限放入
				List<String> expressions = user.getPermissionExpressions();
				for (String expression : expressions){
					permissionNames.add(expression);
				}
				
				String loginName = user.getLoginName();
				
				List<AclEntry> aclEntries = aclService.findByPermission(loginName, roleNames);
				for (AclEntry aclEntry : aclEntries){
					if (aclEntry != null){
						AclIdEntity aclIdEntity = aclEntry.getAclIdEntity();
						if (aclIdEntity != null && aclEntry.getMask() != null) {
							Integer mask = aclEntry.getMask();
							Long objectId = aclIdEntity.getObjectId();
							if (aclEntry.getGranting()){
								permissionNames.add(AclUtil.getPermission(mask, objectId));
							}
//							if (aclIdEntity.getInheriting()){
//								String value = String.valueOf(objectId);
//								addPermissionNames(permissionNames, aclIdEntity.getParent(), value);
//							}
						}
						
					}
				}
			}
			
			info.addRoles(roleNames);
			//把数据级权限放入
			info.addStringPermissions(permissionNames);
			
			return info;
		} else {
			return null;
		}
	}

	
//	private void addPermissionNames(List<String> permissionNames, AclIdEntity parentAclIdEntity, String value){
//		if (parentAclIdEntity != null && parentAclIdEntity.getObjectId() != null){
//			Long objectId = parentAclIdEntity.getObjectId();
//			List<AclEntry> aclEntries = aclService.findByObjectId(objectId);
//			
//			for (AclEntry aclEntry : aclEntries){
//				if (aclEntry != null){
//					AclIdEntity aclIdEntity = aclEntry.getAclIdEntity();
//					if (aclIdEntity != null && aclEntry.getMask() != null) {
//						Integer mask = aclEntry.getMask();
//						if (aclEntry.getGranting()){
//							permissionNames.add(AclUtil.getPermission(mask, objectId));
//						}
//						if (aclIdEntity.getInheriting()){
//							value += "," + String.valueOf(objectId);
//							addPermissionNames(permissionNames, aclIdEntity.getParent(), value);
//						}
//					}
//					
//				}
//			}
//		}
//	}
	
	/**
	 * 更新用户授权信息缓存.
	 */
	public void clearCachedAuthorizationInfo(String principal) {
		SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());
		clearCachedAuthorizationInfo(principals);
	}
	
	/**
	 * 清除所有用户授权信息缓存.
	 */
	public void clearAllCachedAuthorizationInfo() {
		Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();
		if (cache != null) {
			for (Object key : cache.keys()) {
				cache.remove(key);
			}
		}
	}
	
	public HashPassword encrypt(String plainText) {
		HashPassword result = new HashPassword();
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		result.salt = Encodes.encodeHex(salt);

		byte[] hashPassword = Digests.sha1(plainText.getBytes(), salt, INTERATIONS);
		result.password = Encodes.encodeHex(hashPassword);
		return result;

	}

	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(ALGORITHM);
		matcher.setHashIterations(INTERATIONS);

		setCredentialsMatcher(matcher);
	}

	public static class HashPassword {
		public String salt;
		public String password;
	}

	/**
	 * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
	 */
	public static class ShiroUser implements Serializable {

		private static final long serialVersionUID = -4235825356126500405L;

		private String loginName;
		private String realName;
		private Set<Role> roles;
		private Site site;
		private Long siteId;

		public ShiroUser(String loginName, String realName, Set<Role> roles) {
			this.loginName = loginName;
			this.realName = realName;
			this.roles = roles;
		}

		public String getLoginName() {
			return loginName;
		}

		public String getRealName() {
			return realName;
		}
		
		public Set<Role> getRoles() {
			return roles;
		}

		public void setSite(Site site){
			this.site = site;
		}
		
		public Site getSite(){
			return site;
		}

		public Long getSiteId() {
			return siteId;
		}

		public void setSiteId(Long siteId) {
			this.siteId = siteId;
		}

		/**
		 * 本函数输出将作为默认的<shiro:principal/>输出.
		 */
		@Override
		public String toString() {
			return loginName;
		}

		/**
		 * 重载equals,只计算loginName;
		 */
		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this, "loginName");
		}

		/**
		 * 重载equals,只比较loginName
		 */
		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj, "loginName");
		}
	}
}
