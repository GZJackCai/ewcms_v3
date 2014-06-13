package com.ewcms.security.acl.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * <ul>
 * <li>id:编号</li>
 * <li>principal:用户或角色(true:用户、false:角色)</li>
 * <li>sid:认证对象(一个用户名或角色名)</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "acl_sid", uniqueConstraints = {@UniqueConstraint(columnNames = {"sid", "principal"})})
@SequenceGenerator(name = "seq_acl_sid", sequenceName = "seq_acl_sid_id", allocationSize = 1)
public class AclSid implements Serializable {

	private static final long serialVersionUID = -6054119834180726791L;

	@Id
	@GeneratedValue(generator = "seq_acl_sid", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@Column(name = "principal", nullable = false)
	private Boolean principal = false;
	@Column(name = "sid", nullable = false)
	private String sid;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		AclSid other = (AclSid) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
