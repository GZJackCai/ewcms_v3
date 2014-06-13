/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication;

import com.ewcms.publication.task.TaskRegistryable;

/**
 * 实现定时发布服务
 * 
 * @author wangwei
 */
public class SchedulePublishFac {

    private final String username = TaskRegistryable.MANAGER_USERNAME;
    private PublishService publishService;
    
    public void publishSite(Long siteId) throws PublishException {
        publishService.publishSite(siteId, false, username);
    }

    public void publishChannel(Long channelId, boolean children)throws PublishException {
        publishService.publishChannel(channelId, children, false, username);
    }

    public void setPublishService(PublishService publishService){
        this.publishService = publishService;
    }
}
