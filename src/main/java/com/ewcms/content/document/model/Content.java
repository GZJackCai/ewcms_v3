/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.content.document.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 文章内容
 * 
 * <ul>
 * <li>detail:内容</li>
 * <li>page:页数</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "content_content")
@SequenceGenerator(name = "seq", sequenceName = "seq_content_content_id", allocationSize = 1)
public class Content extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -4505457961298597538L;

	@Column(name = "detail")
	private byte[] detail;
	@Column(name = "page", nullable = false)
	private Integer page;

	public String getDetail() {
		String detailString = "";
		try {
			detailString = new String(detail, "UTF-8");
		} catch (Exception e) {
		}
		return detailString;
	}

	public void setDetail(String detail) {
		byte[] detailByte = null;
		try {
			detailByte = detail.getBytes("UTF-8");
		} catch (Exception e) {
		}
		this.detail = detailByte;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}
}
