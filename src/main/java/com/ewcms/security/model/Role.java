package com.ewcms.security.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.ewcms.common.model.BaseSequenceEntity;
import com.ewcms.util.Collections3;

/**
 * 角色
 * 
 * <ul>
 * <li>roleName:角色名称</li>
 * <li>caption:说明</li>
 * <li>permissins:权限集合对象</li>
 * </ul>
 * 
 * @author wuzhijun
 *
 */
@Entity
@Table(name = "acct_role")
@SequenceGenerator(name = "seq", sequenceName = "seq_acct_role_id", allocationSize = 1)
public class Role extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -6368774703931624240L;
		
	@Column(name = "role_name", nullable = false, unique = true)
	private String roleName;
	@Column(name = "caption", nullable = false, unique = true)
	private String caption;
    @ManyToMany(cascade = {CascadeType.REFRESH}, targetEntity = Permission.class, fetch = FetchType.LAZY)
    @JoinTable(name = "acct_role_permission", joinColumns =@JoinColumn(name = "role_id"),inverseJoinColumns =@JoinColumn(name = "permission_id"))
    @OrderBy("id")
	private Set<Permission> permissions = new HashSet<Permission>();
    
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String RoleName) {
		this.roleName = RoleName;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	
	@SuppressWarnings("unchecked")
	@Transient
	public List<String> getPermissionNames() {
		return Collections3.extractToList(permissions, "name");
	}
	
	@SuppressWarnings("unchecked")
	@Transient
	public List<String> getPermissionExpressions(){
		return Collections3.extractToList(permissions, "expression");
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
