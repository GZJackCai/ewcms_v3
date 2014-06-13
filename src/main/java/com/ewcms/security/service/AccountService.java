package com.ewcms.security.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
//import org.javasimon.aop.Monitored;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.security.dao.RoleDao;
import com.ewcms.security.dao.PermissionDao;
import com.ewcms.security.dao.UserDao;
import com.ewcms.security.model.Role;
import com.ewcms.security.model.Permission;
import com.ewcms.security.model.User;
import com.ewcms.security.service.ShiroDbRealm.HashPassword;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

@Component
@Transactional(readOnly = true)
//@Monitored
public class AccountService {
	
	private static Logger logger = LoggerFactory.getLogger(AccountService.class);

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private PermissionDao permissionDao;
	@Autowired(required = false)
	private ShiroDbRealm shiroRealm;

	// -- User Manager --//
	public User getUser(Long id) {
		return userDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public void saveUser(User user, Boolean isBasic) {
		if (!isBasic && user.getId() != null && isSupervisor(user.getId())) {
			logger.warn("操作员{}尝试修改超级管理员用户", SecurityUtils.getSubject().getPrincipal());
			throw new ServiceException("不能修改超级管理员用户");
		}
		//设定安全的密码，使用passwordService提供的salt并经过1024次 sha-1 hash
		if (StringUtils.isNotBlank(user.getPlainPassword()) && shiroRealm != null) {
			HashPassword hashPassword = shiroRealm.encrypt(user.getPlainPassword());
			user.setSalt(hashPassword.salt);
			user.setPassword(hashPassword.password);
		}
		userDao.save(user);
		if (shiroRealm != null) {
			shiroRealm.clearCachedAuthorizationInfo(user.getLoginName());
		}
	}
	
	@Transactional(readOnly = false)
	public void updatePassword(String loginName, String newPassword) throws ServiceException{
		User user = userDao.findByLoginName(loginName);
		
		if (StringUtils.isBlank(newPassword))
			throw new ServiceException("新密码不能为空");
		
		if (shiroRealm == null)
			throw new ServiceException("用户未登录或过期，请重新登录");
		
		HashPassword newHashPassword = shiroRealm.encrypt(newPassword);
		user.setSalt(newHashPassword.salt);
		user.setPassword(newHashPassword.password);

		userDao.save(user);
		if (shiroRealm != null){
			shiroRealm.clearCachedAuthorizationInfo(user.getLoginName());
		}
	}
	
	public void updUserInfo(User user){
		User oldUser = userDao.findByLoginName(user.getLoginName());
		
		oldUser.setRealName(user.getRealName());
		oldUser.setIdentification(user.getIdentification());
		oldUser.setBirthday(user.getBirthday());
		oldUser.setEmail(user.getEmail());
		oldUser.setPhone(user.getPhone());
		oldUser.setMphone(user.getMphone());
		
		userDao.save(oldUser);
		if (shiroRealm != null){
			shiroRealm.clearCachedAuthorizationInfo(user.getLoginName());
		}
	}

	/**
	 * 删除用户,如果尝试删除超级管理员将抛出异常.
	 */
	@Transactional(readOnly = false)
	public void deleteUser(Long id) {
		if (isSupervisor(id)) {
			logger.warn("操作员{}尝试删除超级管理员用户", SecurityUtils.getSubject().getPrincipal());
			throw new ServiceException("不能删除超级管理员用户");
		}
		userDao.delete(id);
	}

	/**
	 * 判断是否超级管理员.
	 */
	private boolean isSupervisor(Long id) {
		return id == 1;
	}

	public List<User> getAllUser() {
		return (List<User>) userDao.findAll(new Sort(Direction.ASC, "id"));
	}

	public User findUserByLoginName(String loginName) {
		return userDao.findByLoginName(loginName);
	}
	
	public Map<String, Object> searchUser(QueryParameter params) {
		return SearchMain.search(params, "IN_id", Long.class, userDao, User.class);
	}

	// -- Role Manager --//
	public Role getRole(Long id) {
		return roleDao.findOne(id);
	}

	public List<Role> getAllRole() {
		return (List<Role>) roleDao.findAll((new Sort(Direction.ASC, "id")));
	}

	@Transactional(readOnly = false)
	public void saveRole(Role entity) {
		roleDao.save(entity);
		shiroRealm.clearAllCachedAuthorizationInfo();
	}

	@Transactional(readOnly = false)
	public void deleteRole(Long id) {
		roleDao.deleteWithReference(id);
		shiroRealm.clearAllCachedAuthorizationInfo();
	}
	
	public Role findSelectedRole(Long userId, Long roleId){
		return roleDao.findSelectedRole(userId, roleId);
	}
	
	public Role findRoleByRoleName(String roleName){
		return roleDao.findByRoleName(roleName);
	}
	
	public Role findRoleByCaption(String caption){
		return roleDao.findByCaption(caption);
	}
	
	public Map<String, Object> searchRole(QueryParameter params) {
		return SearchMain.search(params, "IN_id", Long.class, roleDao, Role.class);
	}
	
	//-- Permission Manager --//
	public Permission getPermission(Long id){
		return permissionDao.findOne(id);
	}
	
	public List<Permission> getAllPermission(){
		return (List<Permission>) permissionDao.findAll(new Sort(Direction.ASC, "id"));
	}
	
	public Permission findSelectedPermission(Long roleId, Long permissionId){
		return permissionDao.findSelectedPermission(roleId, permissionId);
	}
	
	public Permission findPermissionByName(String name){
		return permissionDao.findByName(name);
	}
	
	public Map<String, Object> searchPermission(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, permissionDao, Permission.class);
	}
}
