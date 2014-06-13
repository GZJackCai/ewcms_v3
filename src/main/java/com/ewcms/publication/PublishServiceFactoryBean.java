/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.ewcms.publication.service.ArticlePublishService;
import com.ewcms.publication.service.ChannelPublishService;
import com.ewcms.publication.service.ResourcePublishService;
import com.ewcms.publication.service.SitePublishService;
import com.ewcms.publication.service.TemplatePublishService;
import com.ewcms.publication.service.TemplateSourcePublishService;
import com.ewcms.publication.task.MemoryTaskRegistry;
import com.ewcms.publication.task.TaskRegistryable;

import freemarker.template.Configuration;

/**
 * 发布服务FactoryBean
 * <br/>
 * 通过spring创建PublishService服务，确保创建服务为单例。 
 * 
 * @author wangwei
 */
public class PublishServiceFactoryBean implements InitializingBean,FactoryBean<PublishService>{
    
    private ChannelPublishService channelPublishService;
    private ArticlePublishService articlePublishService;
    private TemplatePublishService templatePublishService;
    private SitePublishService sitePublishService;
    private ResourcePublishService resourcePublishService;
    private TemplateSourcePublishService templateSourcePublishService;
    private Configuration cfg;
    private TaskRegistryable taskRegistry;
    private  PublishService publishService ;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(channelPublishService,"channelService must setting");
        Assert.notNull(articlePublishService,"articleService must setting");
        Assert.notNull(templatePublishService,"templateService must setting");
        Assert.notNull(sitePublishService,"siteService must setting");
        Assert.notNull(resourcePublishService,"resourceService must setting");
        Assert.notNull(templateSourcePublishService,"templateSourceService must setting");
        Assert.notNull(cfg,"Templcat configuration must setting");
        
        publishService = new PublishService();
        publishService.setChannelPublishService(channelPublishService);
        publishService.setArticlePublishService(articlePublishService);
        publishService.setTemplatePublishService(templatePublishService);
        publishService.setSitePublishService(sitePublishService);
        publishService.setResourcePublishService(resourcePublishService);
        publishService.setTemplateSourcePublishService(templateSourcePublishService);
        publishService.setConfiguration(cfg);
        taskRegistry = (taskRegistry == null ? new MemoryTaskRegistry() : taskRegistry);
        publishService.setTaskRegistry(taskRegistry);
    }
    
    public PublishService getObject() throws Exception {
        return publishService;
    }

    public Class<?> getObjectType() {
        return PublishService.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void setArticleService(ArticlePublishService articleService) {
        this.articlePublishService = articleService;
    }

    public void setChannelService(ChannelPublishService channelService) {
        this.channelPublishService = channelService;
    }

    public void setTemplateService(TemplatePublishService templateService) {
        this.templatePublishService = templateService;
    }
    
    public void setSiteService(SitePublishService siteService) {
        this.sitePublishService = siteService;
    }

    public void setResourceService(ResourcePublishService resourceService) {
        this.resourcePublishService = resourceService;
    }

    public void setTemplateSourceService(
            TemplateSourcePublishService templateSourceService) {
        this.templateSourcePublishService = templateSourceService;
    }
    
    public void setTaskRegistry(TaskRegistryable taskRegistry){
        this.taskRegistry = taskRegistry;
    }
    
    public void setConfiguration(Configuration cfg){
        this.cfg = cfg;
    }
}
