/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.site.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

@Entity
@Table(name = "site_templateentity")
@SequenceGenerator(name = "seq", sequenceName = "seq_site_templateentity_id", allocationSize = 1)
public class TemplateEntity extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -5672007956313222294L;

    @Column()
    private byte[] tplEntity;

	public byte[] getTplEntity() {
		return tplEntity;
	}

	public void setTplEntity(byte[] tplEntity) {
		this.tplEntity = tplEntity;
	}
}
