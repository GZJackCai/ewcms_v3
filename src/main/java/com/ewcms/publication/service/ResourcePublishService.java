/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.content.resource.model.Resource;
import com.ewcms.content.resource.service.ResourceService;

/**
 * 资源加载和操作接口
 * <br>
 * 提供发布资源所需要的数据，并更改以发布发状态。
 * 
 * @author wangwei
 */
@Component
public class ResourcePublishService {

	@Autowired
	private ResourceService resourceService;
	
    /**
     * 得到资源
     * 
     * @param id  资源编号
     * @return
     */
	public Resource getResource(Long id){
    	return resourceService.getResource(id);
    }
    
    /**
     * 查询需要发布的资源
     * <br>
     * 再发布时会得到所有要发布的资源（包括：normal和released）。
     * 
     * @param siteId 站点编号
     * @param forceAgain 再发布 
     * @return 需要发布的资源
     */
	public List<Resource> findPublishResources(Long siteId, Boolean forceAgain){
    	return resourceService.findPublishResources(siteId, forceAgain);
    }

    /**
     * 发布资源成功
     * <br>
     * 标示资源为发布状态。
     * 
     * @param id 资源编号
     */
	public void publishResourceSuccess(Long id){
		resourceService.publishResourceSuccess(id);
    }
}
