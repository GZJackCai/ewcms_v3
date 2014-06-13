/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.service.ChannelPublishService;
import com.ewcms.publication.service.TemplatePublishService;
import com.ewcms.publication.task.Taskable;
import com.ewcms.site.model.Channel;
import com.ewcms.site.model.Site;
import com.ewcms.site.model.Template;
import com.ewcms.util.EwcmsContextUtil;

/**
 * 实现管理平台发布服务
 * 
 * @author wangwei
 */
public class WebPublishFac {

    private static final Logger logger = LoggerFactory.getLogger(WebPublishFac.class);
    
    private PublishService publishService;
    private ChannelPublishService channelPublishService;
    private TemplatePublishService templatePublishService;
    
    /**
     * 得到操作站点编号
     * 
     * @return 站点编号
     * @throws PublishException
     */
    private Long getCurrentSiteId()throws PublishException{
        Site site = EwcmsContextUtil.getCurrentSite();
        if(site == null || site.getId() == null){
            logger.debug("Current Site is not exist");
            throw new PublishException("Current Site is not exist");
        }
        return site.getId();
    }
    
    /**
     * 得到操作用户名
     * 
     * @return 用户名
     * @throws PublishException
     */
    private String getCurrentUsername(){
        return EwcmsContextUtil.getShiroUser().getLoginName();
    }
    
  

    public void publishSite(boolean again) throws PublishException {
        publishService.publishSite(getCurrentSiteId(), again, getCurrentUsername());
    }

    /**
     * 验证频道发布是否有效
     * <br>
     * 防止恶意发布
     * 
     * @param id  频道编号
     * @throws PublishException
     */
    private Channel publishChannelEnable(Long id)throws PublishException{
        Channel channel = channelPublishService.getChannel(id);
        if(channel == null){
            logger.error("channel is null");
            throw new PublishException("Channel is not exits");
        }
        Long siteId = channel.getSite().getId();
        if(siteId != getCurrentSiteId().intValue()){
            logger.error("Channel is not current site's it");
            throw new PublishException("Channel is not current site's it");
        }
        if(!channel.getPublicenable()){
            logger.error("Channel was not publish");
            throw new PublishException("Channel was not publish");
        }
        return channel;
    }
    
    public void publishChannel(Long id, boolean again, boolean children)throws PublishException {
        publishChannelEnable(id);
        publishService.publishChannel(id, children, again, getCurrentUsername());
        
    }

    public void publishSiteResource(boolean again) throws PublishException {
        publishService.publishResourceBySite(getCurrentSiteId(), again, getCurrentUsername());
    }

    public void publishResources(Long[] ids) throws PublishException {
        publishService.publishResource(getCurrentSiteId(), ids, getCurrentUsername());
    }

    public void publishSiteTemplateSource(boolean again)throws PublishException {
        publishService.publishTemplateSourceBySite(getCurrentSiteId(), again, getCurrentUsername());
    }

    public void publishTemplateSources(Long[] ids) throws PublishException {
        publishService.publishTemplateSource(getCurrentSiteId(), ids, getCurrentUsername());
    }

    public void publishTemplateContent(Long id, boolean again)throws PublishException {
        Template template = templatePublishService.getTemplate(id);
        publishChannelEnable(template.getChannelId());
        publishService.publishTemplate(id, again, getCurrentUsername());
    }

    public void publishArticles(Long channelId,Long[] ids) throws PublishException {
        publishChannelEnable(channelId);
        publishService.publishArticle(channelId, ids, getCurrentUsername());
    }
    
    public void closePublish()throws PublishException {
    	Long siteId = getCurrentSiteId();
        publishService.closeSitePublish(siteId);
    }
    
    public List<Taskable> getSitePublishTasks(Long siteId) {
        List<Taskable> tasks = publishService.getSitePublishTasks(siteId);
        //TODO 控制用户任务显示，超级权限用户显示所有发布任务，其他用户只显示自己任务
        return tasks;
    }
    
	public void removePublish(Long siteId, String id, String username) throws PublishException {
		publishService.removePublish(siteId, id, username);
	}

    public void setPublishService(PublishService publishService){
        this.publishService = publishService;
    }
    
    public void setChannelPublishService(ChannelPublishService channelPublishService){
        this.channelPublishService = channelPublishService;
    }
    
    public void setTemplatePublishService(TemplatePublishService templatePublishService){
        this.templatePublishService = templatePublishService;
    }
}
