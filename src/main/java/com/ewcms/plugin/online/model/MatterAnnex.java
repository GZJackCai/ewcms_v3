/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.online.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 事项附件
 * 
 * <ul>
 * <li>url:地址</li>
 * <li>legend:说明</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_matter_annex")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_matter_annex_id", allocationSize = 1)
public class MatterAnnex extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 4831479226554897700L;

	@Column(name = "url", columnDefinition = "text")
	private String url;
	@Column(name = "legend", columnDefinition = "text")
	private String legend;
	@Column(name = "sort")
	private Integer sort;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLegend() {
		return legend;
	}

	public void setLegend(String legend) {
		this.legend = legend;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
