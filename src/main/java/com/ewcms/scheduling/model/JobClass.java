/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.scheduling.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * @author 吴智俊
 */
@Entity
@Table(name = "job_class")
@SequenceGenerator(name = "seq", sequenceName = "seq_job_class_id", allocationSize = 1)
public class JobClass extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -4428638031352721701L;

	@Column(name = "classname", nullable = false)
	private String className;
	@Column(name = "classentity", nullable = false, unique = true)
	private String classEntity;
	@Column(name = "description", columnDefinition = "text")
	private String description;

	public String getClassEntity() {
		return classEntity;
	}

	public void setClassEntity(String classEntity) {
		this.classEntity = classEntity;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
