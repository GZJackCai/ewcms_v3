/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.citizen.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;
import com.ewcms.plugin.online.model.Matter;

/**
 * 公民
 * 
 * <ul>
 * <li>name:名称</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "content_citizen")
@SequenceGenerator(name = "seq", sequenceName = "seq_content_citizen_id", allocationSize = 1)
public class Citizen extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -908885747631291293L;

	@Column(name = "citizen_name", nullable = false)
	private String name;
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, mappedBy="citizens")
	@OrderBy(value = "id")
	private List<Matter> matters;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JSONField(serialize = false)
	public List<Matter> getMatters() {
		return matters;
	}

	public void setMatters(List<Matter> matters) {
		this.matters = matters;
	}
}
