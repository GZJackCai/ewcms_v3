/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.task.impl.event;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

import com.ewcms.publication.service.TemplateSourcePublishService;
import com.ewcms.site.model.TemplateSource;

/**
 * 发布模板资源事件
 * 
 * @author wangwei
 */
public class TemplateSourceEvent extends CompleteEvent {
    private final TemplateSource source;
    private final TemplateSourcePublishService templateSourcePublishService;
    
    public TemplateSourceEvent(AtomicInteger completeNumber,
            TemplateSource source,TemplateSourcePublishService templateSourcePublishService){
        super(completeNumber);
        Assert.notNull(source,"Template source is null");
        Assert.notNull(templateSourcePublishService,"Template source service is null");
        this.source = source;
        this.templateSourcePublishService = templateSourcePublishService;
    }
    
    @Override
    public void successAfter(String uri){
        templateSourcePublishService.publishTemplateSourceSuccess(source.getId());
    }
}
