/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.site.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <ul>
 * <li>id:机构编号
 * <li>name:机构名称
 * <li>describe:机构说明
 * <li>icon: 机构引导图
 * <li>floatNo:层级
 * <li>orderNo:同级次序
 * <li>parent:父机构
 * <li>createTime:创建时间
 * <li>updateTime:修改时间
 * <li>homeSiteId:机构主站编号
 * </ul>
 * 
 * @author 周冬初
 * 
 */
@Entity
@Table(name = "site_organ")
@SequenceGenerator(name = "seq_site_organ", sequenceName = "seq_site_organ_id", allocationSize = 1)
public class Organ implements Serializable {

	private static final long serialVersionUID = 7433796728497541934L;

	@Id
	@GeneratedValue(generator = "seq_site_organ", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(length = 100)
	private String name;
	@Column()
	private String descripe;
	@Column()
	private byte[] icon;
	@Column()
	private String iconType;
	@Column(length = 100)
	private Integer floatNo;
	@Column(length = 100)
	private Integer orderNo;
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST }, targetEntity = Organ.class)
	@JoinColumn(name = "parent_id", nullable = true)
	private Organ parent;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", columnDefinition = "Timestamp default CURRENT_TIMESTAMP", insertable = false, updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updateTime", insertable = false, updatable = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime = new Date();
	@OneToOne(cascade = { CascadeType.ALL }, targetEntity = OrganInfo.class)
	@JoinColumn(name = "organId", nullable = true)
	private OrganInfo organInfo = new OrganInfo();
	@Formula(value = "(Select count(o.id) From site_organ o Where o.parent_id= id)")
	private int childrenCount = 0;
	@Column()
	private String url;
	@Column()
	private Long homeSiteId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescripe() {
		return descripe;
	}

	public void setDescripe(String descripe) {
		this.descripe = descripe;
	}

	public byte[] getIcon() {
		return icon;
	}

	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	public String getIconType() {
		return iconType;
	}

	public void setIconType(String iconType) {
		this.iconType = iconType;
	}

	public Integer getFloatNo() {
		return floatNo;
	}

	public void setFloatNo(Integer floatNo) {
		this.floatNo = floatNo;
	}

	public Integer getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Integer orderNo) {
		this.orderNo = orderNo;
	}

	public Organ getParent() {
		return parent;
	}

	public void setParent(Organ parent) {
		this.parent = parent;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getCreateTime() {
		return createTime;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpdateTime() {
		return updateTime;
	}

	public OrganInfo getOrganInfo() {
		return organInfo;
	}

	public void setOrganInfo(OrganInfo organInfo) {
		this.organInfo = organInfo;
	}

	public Long getHomeSiteId() {
		return homeSiteId;
	}

	public void setHomeSiteId(Long homeSiteId) {
		this.homeSiteId = homeSiteId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean hasChildren() {
		return this.childrenCount > 0;
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
		Organ other = (Organ) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
