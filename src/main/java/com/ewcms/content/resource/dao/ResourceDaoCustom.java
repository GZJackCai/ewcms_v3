/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.resource.dao;

import java.util.List;

import com.ewcms.content.resource.model.Resource;

/**
 * 资源数据操作接口
 * 
 * @author 吴智俊
 */
public interface ResourceDaoCustom {

    /**
     * 查询状态为DELETE的资源
     * 
     * @param siteId 站点编号
     * @return
     */
    List<Resource> findSoftDeleteResources(Long siteId);
    
    /**
     * 查询所属站点未发布资源
     * 
     * @param siteId 站点编号
     * @param forceAgain 再发布
     * @return
     */
    List<Resource> findPublishResources(Long siteId, Boolean forceAgain);
    
    /**
     * 通过通过站点编号和uri得到资源
     * 
     * @param siteId 站点编号
     * @param uri 资源地址
     * @return
     */
    Resource getResourceByUri(Long siteId,String uri);
}
