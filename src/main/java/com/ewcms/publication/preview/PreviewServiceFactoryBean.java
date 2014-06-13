/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.preview;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.common.message.EwcmsMessageSource;
import com.ewcms.publication.freemarker.preview.PreviewService;
import com.ewcms.publication.service.ArticlePublishService;
import com.ewcms.publication.service.ChannelPublishService;
import com.ewcms.publication.service.TemplatePublishService;

import freemarker.template.Configuration;

/**
 * 预览服务Factory
 * 
 * @author wangwei
 */
@Component(value = "preview")
public class PreviewServiceFactoryBean implements FactoryBean<PreviewServiceable>,InitializingBean,MessageSourceAware{

    @Autowired
    private Configuration configuration;
    @Autowired
    private ArticlePublishService articlePublishService ;
    @Autowired
    private ChannelPublishService channelPublishService;
    @Autowired
    private TemplatePublishService templatePublishService;
    
    private MessageSource messageSource =new EwcmsMessageSource();
    
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(configuration,"template's configuration must setting");
        Assert.notNull(articlePublishService,"articleService must setting");
        Assert.notNull(channelPublishService,"channelService configuration must setting");
        Assert.notNull(templatePublishService,"templateService must setting");
    }

    @Override
    public PreviewServiceable getObject() throws Exception {
        PreviewService previewService = new PreviewService(configuration,articlePublishService,channelPublishService,templatePublishService);
        previewService.setMessageSource(messageSource);
        return previewService;
    }

    @Override
    public Class<?> getObjectType() {
        return PreviewServiceable.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
    
    public void setConfiguration(Configuration cfg){
        this.configuration = cfg;
    }

    public void setArticlePublishService(ArticlePublishService articlePublishService) {
        this.articlePublishService = articlePublishService;
    }

    public void setChannelPublishService(ChannelPublishService channelPublishService) {
        this.channelPublishService = channelPublishService;
    }

    public void setTemplatePublishService(TemplatePublishService templatePublishService) {
        this.templatePublishService = templatePublishService;
    }

    @Override
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
