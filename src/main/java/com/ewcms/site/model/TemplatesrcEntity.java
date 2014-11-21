/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

/**
 * 
 */
package com.ewcms.site.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * @author 吴智俊
 */

@Entity
@Table(name = "site_templatesrcent")
@SequenceGenerator(name = "seq", sequenceName = "seq_site_templatesrcent_id", allocationSize = 1)
public class TemplatesrcEntity extends BaseSequenceEntity<Long> {
	
	private static final long serialVersionUID = -2125856364248355661L;
	
    @Column()
    private byte[] srcEntity;

	public byte[] getSrcEntity() {
		return srcEntity;
	}

	public void setSrcEntity(byte[] srcEntity) {
		this.srcEntity = srcEntity;
	}
}
