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
 * 匹配块
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
@Table(name = "plugin_crawler_matchblock")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_crawler_matchblock_id", allocationSize = 1)
public class MatchBlock extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 27792816081447537L;

	@Column(name = "regex")
	private String regex;
	@ManyToOne(cascade = { CascadeType.REFRESH }, targetEntity = MatchBlock.class)
	@JoinColumn(name = "parent_id", nullable = true)
	private MatchBlock parent;
	@Column(name = "sort")
	private Long sort;

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public MatchBlock getParent() {
		return parent;
	}

	public void setParent(MatchBlock parent) {
		this.parent = parent;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}
}
