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

@Entity
@Table(name = "plugin_crawler_storage")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_crawler_storage_id", allocationSize = 1)
public class Storage extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -6416296085234192162L;

	@Column(name = "gather_id")
	private Long gatherId;
	@Column(name = "gather_name")
	private String gatherName;
	@Column(name = "title")
	private String title;
	@Column(name = "url", unique = true)
	private String url;

	public Long getGatherId() {
		return gatherId;
	}

	public void setGatherId(Long gatherId) {
		this.gatherId = gatherId;
	}

	public String getGatherName() {
		return gatherName;
	}

	public void setGatherName(String gatherName) {
		this.gatherName = gatherName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
