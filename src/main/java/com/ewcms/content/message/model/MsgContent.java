/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.message.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 信息内容
 * 
 * <ul>
 * <li>title:标题</li>
 * <li>detail:内容</li>
 * </ul>
 * 
 * @author wu_zhijun
 * 
 */
@Entity
@Table(name = "plugin_message_content")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_message_content_id", allocationSize = 1)
public class MsgContent extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 8153404292985294025L;

	@Column(name = "title", nullable = false, length = 200)
	private String title;
	@Column(name = "detail", columnDefinition = "text")
	private String detail;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}
}
