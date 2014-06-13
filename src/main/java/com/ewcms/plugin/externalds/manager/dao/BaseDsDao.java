/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.externalds.manager.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.plugin.externalds.model.BaseDs;

/**
 * 
 * @author wu_zhijun
 *
 */
public interface BaseDsDao extends PagingAndSortingRepository<BaseDs, Long> {

}
