/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task.impl;

import java.util.ArrayList;
import java.util.List;

import com.ewcms.publication.service.ArticlePublishService;
import com.ewcms.publication.service.ChannelPublishService;
import com.ewcms.publication.service.ResourcePublishService;
import com.ewcms.publication.service.TemplatePublishService;
import com.ewcms.publication.service.TemplateSourcePublishService;
import com.ewcms.publication.task.Taskable;
import com.ewcms.publication.task.impl.process.TaskProcessable;
import com.ewcms.site.model.Channel;
import com.ewcms.site.model.Site;

import freemarker.template.Configuration;

/**
 * 发布整个站点任务
 * 
 * @author wangwei
 */
public class SiteTask extends TaskBase {

    public static class Builder extends BaseBuilder<Builder> {
        
        private final Configuration cfg;
        private final ChannelPublishService channelPublishService;
        private final TemplatePublishService templatePublishService;
        private final TemplateSourcePublishService templateSourcePublishService;
        private final ResourcePublishService resourcePublishService;
        private final ArticlePublishService articlePublishService;
        
        public Builder(Configuration cfg,
                TemplatePublishService templatePublishService,
                TemplateSourcePublishService templateSourcePublishService,
                ResourcePublishService resourcePublishService,
                ArticlePublishService articlePublishService,
                ChannelPublishService channelPublishService,
                Site site){
            
            super(site);
            
            this.cfg = cfg;
            this.templatePublishService = templatePublishService;
            this.templateSourcePublishService = templateSourcePublishService;
            this.resourcePublishService = resourcePublishService;
            this.articlePublishService = articlePublishService;
            this.channelPublishService = channelPublishService;
        }
        
        @Override
        protected String getDescription() {
            return String.format("%s站点发布", site.getSiteName()) ;
        }
        
        private void dependenceResourceAndTemplateSource(List<Taskable> dependences){
            dependences.add(
                    new  TemplateSourceTask
                    .Builder(templateSourcePublishService,site)
                    .setUsername(username)
                    .build());
            dependences.add(
                    new ResourceTask
                    .Builder(resourcePublishService,site)
                    .setUsername(username)
                    .build());
        }

        private void dependenceRootChannel(List<Taskable> dependences){
            Channel root = channelPublishService.getChannelRoot(site.getId());
            dependences.add(new ChannelTask.Builder(cfg, templatePublishService, 
                    templateSourcePublishService, resourcePublishService,
                    articlePublishService, channelPublishService,site,root)
                    .setAgain(again)
                    .setUsername(username)
                    .setDependence(true)
                    .setPublishChildren(true)
                    .build());
        }
        
        @Override
        protected List<Taskable> getDependenceTasks() {
            List<Taskable> dependences = new ArrayList<Taskable>();
            dependenceResourceAndTemplateSource(dependences);
            dependenceRootChannel(dependences);
            return dependences;
        }

        @Override
        protected List<TaskProcessable> getTaskProcesses() {
            return new ArrayList<TaskProcessable>(0);
        }
    }
    
    public SiteTask(String id,Builder builder){
        super(id,builder);
    }
}
