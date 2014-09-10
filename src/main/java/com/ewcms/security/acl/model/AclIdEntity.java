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
 * <li>aclClass:AclClass对象</li>
 * <li>objectId:AclClass对象主键值</li>
 * <li>parentId:父类对象主键值</li>
 * <li>ownerSid:AclSid对象</li>
 * <li>inheriting:是否继承父类</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "acl_identity", uniqueConstraints = {@UniqueConstraint(name="uk_acl_class_id_object_id", columnNames = {"acl_class_id", "object_id"})})
@SequenceGenerator(name = "seq_acl_identity", sequenceName = "seq_acl_identity_id", allocationSize = 1)
public class AclIdEntity implements Serializable {

	private static final long serialVersionUID = -5292583321222365262L;

	@Id
	@GeneratedValue(generator = "seq_acl_identity", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@ManyToOne(cascade = CascadeType.REFRESH, targetEntity = AclClass.class)
	@JoinColumn(name = "acl_class_id")
	private AclClass aclClass;
	@Column(name = "object_id", nullable = false)
	private Long objectId;
	@ManyToOne(cascade = CascadeType.REFRESH, targetEntity = AclIdEntity.class)
	@JoinColumn(name = "parent_id")
	private AclIdEntity parent;
	@ManyToOne(cascade = CascadeType.REFRESH, targetEntity = AclSid.class)
	@JoinColumn(name = "owner_id")
	private AclSid ownerSid;
	@Column(name = "inheriting", nullable = false)
	private Boolean inheriting = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AclClass getAclClass() {
		return aclClass;
	}

	public void setAclClass(AclClass aclClass) {
		this.aclClass = aclClass;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public AclIdEntity getParent() {
		return parent;
	}

	public void setParent(AclIdEntity parent) {
		this.parent = parent;
	}

	public AclSid getOwnerSid() {
		return ownerSid;
	}

	public void setOwnerSid(AclSid ownerSid) {
		this.ownerSid = ownerSid;
	}

	public Boolean getInheriting() {
		return inheriting;
	}

	public void setInheriting(Boolean inheriting) {
		this.inheriting = inheriting;
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
		AclIdEntity other = (AclIdEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
