/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.content.particular.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 行业编码
 * 
 * <ul>
 * <li>code:行业编码</li>
 * <li>name:行业名称</li>
 * </ul>
 * 
 * @author wuzhijun
 */

@Entity
@Table(name = "particular_industry_code")
@SequenceGenerator(name = "seq", sequenceName = "seq_particular_industry_code_id", allocationSize = 1)
public class IndustryCode extends BaseSequenceEntity<Long> {
	
	
	private static final long serialVersionUID = 2121450391747251568L;
	
	@Column(name = "code", length = 4, nullable = false, unique = true)
	private String code;
	@Column(name = "name", length = 200, nullable = false)
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
