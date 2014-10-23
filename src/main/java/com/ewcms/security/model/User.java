package com.ewcms.security.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.site.model.Organ;
import com.ewcms.util.Collections3;

/**
 * 用户信息
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>loginName:登录名</li>
 * <li>password:密码</li>
 * <li>realName:用户名</li>
 * <li>email:邮箱</li>
 * <li>registerDate:注册日期</li>
 * <li>status:使用状态</li>
 * <li>roles:角色对象集合</li>
 * <li>permissions:权限对象集合</li>
 * </ul>
 * 
 * @author wuzhijun
 *
 */
@Entity
@Table(name = "acct_user")
@SequenceGenerator(name = "seq_acct_user", sequenceName = "seq_acct_user_id", allocationSize = 1)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User implements Serializable {

	private static final long serialVersionUID = 4456583773180927749L;

	@Id
    @GeneratedValue(generator = "seq_acct_user",strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@Column(name = "login_name", nullable = false, unique = true, length = 20)
	private String loginName;
	@Transient
	private String plainPassword;
	@Column(name = "pass_word")
	private String password;
	@Column(name = "salt")
    private String salt;
	@Column(name = "real_name", nullable = false)
	private String realName;
	@Column(name = "e_mail")
	private String email;
	@Temporal(TemporalType.DATE)
	@Column(name = "register_date", columnDefinition = "Date default CURRENT_DATE", insertable = false, updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date registerDate;
	@Column(name = "status", columnDefinition = "Boolean default false")
	private Boolean status;
    @ManyToMany(cascade = {CascadeType.REFRESH}, targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "acct_user_role", joinColumns =@JoinColumn(name = "user_id"),inverseJoinColumns =@JoinColumn(name = "role_id"))
    @OrderBy("id ASC")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	private Set<Role> roles = new HashSet<Role>();
    @ManyToMany(cascade = {CascadeType.REFRESH}, targetEntity = Permission.class, fetch = FetchType.EAGER)
    @JoinTable(name = "acct_user_permission", joinColumns =@JoinColumn(name = "user_id"),inverseJoinColumns =@JoinColumn(name = "permission_id"))
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("id ASC")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Permission> permissions = new HashSet<Permission>();
    @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = Organ.class)
	@JoinColumn(name = "organ_id")
    private Organ organ;    
    @Temporal(TemporalType.DATE)
	@Column(name = "birthday")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	@Column(name = "credential_id", length = 50)
	private String identification;
	@Column(length = 20)
	private String phone;
	@Column(length = 20)
	private String mphone;
   
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPlainPassword() {
		return plainPassword;
	}

	public void setPlainPassword(String plainPassword) {
		this.plainPassword = plainPassword;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getRegisterDate() {
		return registerDate;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMphone() {
		return mphone;
	}

	public void setMphone(String mphone) {
		this.mphone = mphone;
	}

	@SuppressWarnings("unchecked")
	@Transient
	public List<String> getRoleNames() {
		return Collections3.extractToList(roles, "roleName");
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((loginName == null) ? 0 : loginName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (loginName == null) {
			if (other.loginName != null)
				return false;
		} else if (!loginName.equals(other.loginName))
			return false;
		return true;
	}
}
