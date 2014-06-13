/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.task.impl.event;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

import com.ewcms.content.resource.model.Resource;
import com.ewcms.publication.service.ResourcePublishService;

/**
 * 发布资源事件
 * 
 * @author wangwei
 */
public class ResourceEvent extends CompleteEvent {

    private final Resource resource;
    private final ResourcePublishService resourcePublishService;
    
    public ResourceEvent(AtomicInteger completeNumber,
            Resource resource,ResourcePublishService resourcePublishService){
        super(completeNumber);
        Assert.notNull(resource,"resource is null");
        Assert.notNull(resourcePublishService,"resource publish service is null");
        this.resource = resource;
        this.resourcePublishService = resourcePublishService;
    }
    
    @Override
    protected void successAfter(String uri){
        resourcePublishService.publishResourceSuccess(resource.getId());
    }
}
