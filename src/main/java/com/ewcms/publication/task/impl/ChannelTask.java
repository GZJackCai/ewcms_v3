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
import com.ewcms.site.model.Template;

import freemarker.template.Configuration;

/**
 * 发布频道任务
 * <br>
 * 发布频道下所有需要发布的资源和任务（如：首页，文章等）
 * 
 * @author wangwei
 */
public class ChannelTask extends TaskBase{
    
    public static class Builder extends BaseBuilder<Builder>{
        private final Configuration cfg;
        private final ChannelPublishService channelPublishService;
        private final TemplatePublishService templatePublishService;
        private final TemplateSourcePublishService templateSourcePublishService;
        private final ResourcePublishService resourcePublishService;
        private final ArticlePublishService articlePublishService;
        private final Channel channel;
        private boolean publishChildren = false;
        
        public Builder(Configuration cfg,
                TemplatePublishService templatePublishService,
                TemplateSourcePublishService templateSourcePublishService,
                ResourcePublishService resourcePublishService,
                ArticlePublishService articlePublishService,
                ChannelPublishService channelPublishService,
                Site site,Channel channel){
            
            super(site);
            
            this.cfg = cfg;
            this.templatePublishService = templatePublishService;
            this.templateSourcePublishService = templateSourcePublishService;
            this.resourcePublishService = resourcePublishService;
            this.articlePublishService = articlePublishService;
            this.channelPublishService = channelPublishService;
            this.channel = channel;
        }
        
        public Builder setPublishChildren(boolean publishChildren){
            this.publishChildren = publishChildren;
            return this;
        }
        
        @Override
        public String getDescription() {
            return String.format("%s频道发布",channel.getName());
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
        
        private void dependenceTemplate(List<Taskable> dependences){
            List<Template> templates = templatePublishService.getTemplatesInChannel(channel.getId());
            for(Template template : templates){
                Taskable task = new TemplateTask.Builder(
                        cfg, templateSourcePublishService, resourcePublishService,
                        articlePublishService, templatePublishService, site, channel, template)
                        .setDependence(true)
                        .setUsername(username)
                        .setAgain(again)
                        .build();
                 dependences.add(task);
            }
        }
        
        private void dependenceChildren(List<Taskable> dependences){
            List<Channel> children = channelPublishService.getChannelChildren(channel.getId());
            if(children == null || children.isEmpty()){
                return ;
            }
            for(Channel child : children){
                dependences.add(
                        new ChannelTask.Builder(
                        cfg, templatePublishService, templateSourcePublishService, resourcePublishService,
                        articlePublishService, channelPublishService,site,child)
                        .setAgain(again)
                        .setUsername(username)
                        .setDependence(true)
                        .setPublishChildren(true)
                        .build());
            }
        }
        
        public List<Taskable> getDependenceTasks(){
            List<Taskable> dependences = new ArrayList<Taskable>();
            if(!dependence){
                dependenceResourceAndTemplateSource(dependences);
            }
            if(channel.getPublicenable()){
                dependenceTemplate(dependences);    
            }
            if(publishChildren){
                dependenceChildren(dependences);
            }
            return dependences;
        }

        @Override
        public List<TaskProcessable> getTaskProcesses() {
            return new ArrayList<TaskProcessable>(0);
        }
    }
    
    public ChannelTask(String id,Builder builder){
        super(id,builder);
    }
}
