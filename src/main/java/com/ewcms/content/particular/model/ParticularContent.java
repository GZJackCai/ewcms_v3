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
 * 内容
 * 
 * <ul>
 * <li>detail:内容</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "particular_content")
@SequenceGenerator(name = "seq", sequenceName = "seq_particular_content_id", allocationSize = 1)
public class ParticularContent extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -4505457961298597538L;

	@Column(name = "detail")
	private byte[] detail;

	public String getDetail() {
		String detailString = "";
		try {
			detailString = new String(detail, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return detailString;
	}

	public void setDetail(String detail) {
		byte[] detailByte = null;
		try {
			detailByte = detail.getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.detail = detailByte;
	}
}
