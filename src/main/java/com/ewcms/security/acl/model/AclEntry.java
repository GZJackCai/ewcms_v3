package com.ewcms.security.acl.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * <ul>
 * <li>id:编号</li>
 * <li>aclIdEntity:AclIdEntity对象</li>
 * <li>order:排序</li>
 * <li>aclSid:AclSid对象</li>
 * <li>mask:操作权限掩码</li>
 * <li>granting:权限被授权或拒绝</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "acl_entry", uniqueConstraints = {@UniqueConstraint(name="uk_acl_identity_id_ace_order", columnNames = {"acl_identity_id", "ace_order"})})
@SequenceGenerator(name = "seq_acl_entry", sequenceName = "seq_acl_entry_id", allocationSize = 1)
public class AclEntry implements Serializable {

	private static final long serialVersionUID = 8175339408329912109L;

	@Id
	@GeneratedValue(generator = "seq_acl_entry", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@ManyToOne(cascade = CascadeType.REFRESH, targetEntity = AclIdEntity.class)
	@JoinColumn(name = "acl_identity_id", nullable = false)
	private AclIdEntity aclIdEntity;
	@Column(name = "ace_order", nullable = false)
	private Long aceOrder;
	@ManyToOne(cascade = CascadeType.REFRESH, targetEntity = AclSid.class)
	@JoinColumn(name = "acl_sid_id", nullable = false)
	private AclSid aclSid;
	@Column(name = "mask", nullable = false, columnDefinition = "text")
	private Integer mask;
	@Column(name = "granting", nullable = false)
	private Boolean granting = true;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AclIdEntity getAclIdEntity() {
		return aclIdEntity;
	}

	public void setAclIdEntity(AclIdEntity aclIdEntity) {
		this.aclIdEntity = aclIdEntity;
	}

	public Long getAceOrder() {
		return aceOrder;
	}

	public void setAceOrder(Long aceOrder) {
		this.aceOrder = aceOrder;
	}

	public AclSid getAclSid() {
		return aclSid;
	}

	public void setAclSid(AclSid aclSid) {
		this.aclSid = aclSid;
	}

	public Integer getMask() {
		return mask;
	}

	public void setMask(Integer mask) {
		this.mask = mask;
	}

	public Boolean getGranting() {
		return granting;
	}

	public void setGranting(Boolean granting) {
		this.granting = granting;
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
		AclEntry other = (AclEntry) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
