/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.plugin.crawler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 
 * URL层级(域名)
 * 
 * <ul>
 * <li>url:URL地址</li>
 * <li>level:层级</li>
 * </ul>
 * 
 * @author wuzhijun
 * 
 */
@Entity
@Table(name = "plugin_crawler_domain")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_crawler_domain_id", allocationSize = 1)
public class Domain extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 464355707059434413L;

	@Column(name = "url")
	private String url;
	@Column(name = "level")
	private Long level;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getLevel() {
		return level;
	}

	public void setLevel(Long level) {
		this.level = level;
	}
}
