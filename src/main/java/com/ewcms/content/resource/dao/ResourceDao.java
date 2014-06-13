/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.resource.dao;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.resource.model.Resource;

/**
 * 资源操作数据接口
 * 
 * @author wuzhijun
 */
public interface ResourceDao extends PagingAndSortingRepository<Resource, Long>, JpaSpecificationExecutor<Resource>, ResourceDaoCustom {
}
