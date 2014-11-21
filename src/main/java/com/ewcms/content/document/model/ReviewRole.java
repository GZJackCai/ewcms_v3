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
 * 审核用户组
 * 
 * @author wu_zhijun
 * 
 */
@Entity
@Table(name = "content_review_role")
@SequenceGenerator(name = "seq", sequenceName = "seq_content_review_role_id", allocationSize = 1)
public class ReviewRole extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 5703561985089566858L;

	@Column(name = "roleName")
	private String roleName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
