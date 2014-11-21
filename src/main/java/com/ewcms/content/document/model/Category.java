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
 * 文章分类属性
 * 
 * <ul>
 * <li>categoryName:分类名称</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "content_category")
@SequenceGenerator(name = "seq", sequenceName = "seq_content_category_id", allocationSize = 1)
public class Category extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -2075041245158111665L;

	@Column(name = "categroy_name", nullable = false)
	private String categoryName;

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
