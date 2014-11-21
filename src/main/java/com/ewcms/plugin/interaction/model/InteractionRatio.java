/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ewcms.plugin.interaction.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 *
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_interaction_backratio")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_interaction_backratio_id", allocationSize = 1)
public class InteractionRatio extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -823177356332483391L;

    @Column(name = "ratio")
    private Integer ratio;
    @Column(name="no_ratio")
    private Integer noRatio;

    public Integer getNoRatio() {
        return noRatio;
    }

    public void setNoRatio(Integer noRatio) {
        this.noRatio = noRatio;
    }

    public Integer getRatio() {
        return ratio;
    }

    public void setRatio(Integer ratio) {
        this.ratio = ratio;
    }
}
