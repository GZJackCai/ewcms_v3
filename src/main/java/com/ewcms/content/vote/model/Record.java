/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.vote.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 用户填写问卷调查结果 
 * 
 * <ul>
 * <li>subjectName:主题名
 * <li>subjectValue:主题值</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_vote_record")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_vote_record_id", allocationSize = 1)
public class Record extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 1681610497803424816L;

	@Column(name = "subject_name")
	private String subjectName;
	@Column(name = "subject_value", columnDefinition = "text")
	private String subjectValue;

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getSubjectValue() {
		return subjectValue;
	}

	public void setSubjectValue(String subjectValue) {
		this.subjectValue = subjectValue;
	}
}
