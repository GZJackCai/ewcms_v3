/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.report.model.data;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 数据父类
 * 
 * <ul>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_report_data")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_report_data_id", allocationSize = 1)
@Inheritance(strategy = InheritanceType.JOINED)
public class Data extends BaseSequenceEntity<Long> {

    private static final long serialVersionUID = -4261767148281294408L;
}
