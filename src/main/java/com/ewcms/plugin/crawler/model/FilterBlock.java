/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.crawler.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 
 * 过滤块
 * 
 * <ul>
 * <li>regex:表达式</li>
 * <li>parent:父节点</li>
 * <li>sort:排序号</li>
 * </ul>
 * 
 * @author wuzhijun
 * 
 */
@Entity
@Table(name = "plugin_crawler_filterblock")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_crawler_filterblock_id", allocationSize = 1)
public class FilterBlock extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -3035720659241458188L;

	@Column(name = "regex")
	private String regex;
	@ManyToOne(cascade = { CascadeType.REFRESH }, targetEntity = FilterBlock.class)
	@JoinColumn(name = "parent_id", nullable = true)
	private FilterBlock parent;
	@Column(name = "sort")
	private Long sort;

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public FilterBlock getParent() {
		return parent;
	}

	public void setParent(FilterBlock parent) {
		this.parent = parent;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}
}
