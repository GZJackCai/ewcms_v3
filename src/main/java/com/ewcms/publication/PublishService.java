/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ewcms.publication.service.ArticlePublishService;
import com.ewcms.publication.service.ChannelPublishService;
import com.ewcms.publication.service.ResourcePublishService;
import com.ewcms.publication.service.SitePublishService;
import com.ewcms.publication.service.TemplatePublishService;
import com.ewcms.publication.service.TemplateSourcePublishService;
import com.ewcms.publication.task.TaskException;
import com.ewcms.publication.task.TaskRegistryable;
import com.ewcms.publication.task.Taskable;
import com.ewcms.publication.task.impl.ChannelTask;
import com.ewcms.publication.task.impl.DetailTask;
import com.ewcms.publication.task.impl.ResourceTask;
import com.ewcms.publication.task.impl.SiteTask;
import com.ewcms.publication.task.impl.TemplateSourceTask;
import com.ewcms.publication.task.impl.TemplateTask;
import com.ewcms.site.model.Channel;
import com.ewcms.site.model.Site;
import com.ewcms.site.model.Template;
import com.ewcms.site.model.Template.TemplateType;

import freemarker.template.Configuration;

/**
 * 实现发布服务
 * 
 * @author wangwei
 */
@Component
public class PublishService {

	private static final Logger logger = LoggerFactory.getLogger(PublishService.class);

	private ChannelPublishService channelPublishService;
	private ArticlePublishService articlePublishService;
	private TemplatePublishService templatePublishService;
	private SitePublishService sitePublishService;
	private ResourcePublishService resourcePublishService;
	private TemplateSourcePublishService templatePublishSourceService;
	private Configuration cfg;
	private TaskRegistryable taskRegistry;

	/**
	 * 得到站点对象 </br> 站点不存在抛出异常
	 * 
	 * @param siteId
	 *            站点编号
	 * @return
	 * @throws PublishException
	 */
	private Site getSite(Long siteId) throws PublishException {
		Site site = sitePublishService.getSite(siteId);
		if (site == null) {
			logger.error("Site id is {},but site is null", siteId);
			throw new PublishException("Sit is null");
		}
		return site;
	}

	public void publishTemplateSource(Long siteId, Long[] publishIds, String username) throws PublishException {
		Site site = getSite(siteId);
		Taskable task = new TemplateSourceTask.Builder(templatePublishSourceService, site).setPublishIds(publishIds).setUsername(username).setAgain(true).build();
		taskRegistry.registerNewTask(site, task);
	}

	public void publishTemplateSourceBySite(Long siteId, boolean again, String username) throws PublishException {
		Site site = getSite(siteId);
		Taskable task = new TemplateSourceTask.Builder(templatePublishSourceService, site).setAgain(again).setUsername(username).build();
		taskRegistry.registerNewTask(site, task);
	}

	public void publishResource(Long siteId, Long[] publishIds, String username) throws PublishException {
		Site site = getSite(siteId);
		Taskable task = new ResourceTask.Builder(resourcePublishService, site).setAgain(true).setPublishIds(publishIds).setUsername(username).build();
		taskRegistry.registerNewTask(site, task);
	}

	public void publishResourceBySite(Long siteId, boolean again, String username) throws PublishException {
		Site site = getSite(siteId);
		Taskable task = new ResourceTask.Builder(resourcePublishService, site).setAgain(again).setUsername(username).build();
		taskRegistry.registerNewTask(site, task);
	}

	public void publishTemplate(Long templateId, boolean again, String username) throws PublishException {
		Template template = templatePublishService.getTemplate(templateId);
		if (template == null) {
			logger.error("Template id is {},but templet is null", templateId);
			throw new PublishException("Template is null");
		}
		Site site = template.getSite();
		Channel channel = channelPublishService.getChannel(template.getChannelId());
		Taskable task = new TemplateTask.Builder(cfg, templatePublishSourceService, resourcePublishService, articlePublishService, templatePublishService, site, channel, template)
				.setAgain(again).setUsername(username).build();
		taskRegistry.registerNewTask(site, task);
	}

	private Channel getChannel(Long channelId) throws PublishException {
		Channel channel = channelPublishService.getChannel(channelId);
		if (channel == null) {
			logger.error("Channel id is {},Channel is null", channelId);
			throw new PublishException("Channel is null");
		}
		return channel;
	}

	public void publishChannel(Long channelId, boolean chidren, boolean again, String username) throws PublishException {
		Channel channel = getChannel(channelId);
		Site site = channel.getSite();
		Taskable task = new ChannelTask.Builder(cfg, templatePublishService, templatePublishSourceService, resourcePublishService, articlePublishService, channelPublishService, site, channel)
				.setPublishChildren(chidren).setAgain(again).setUsername(username).build();
		taskRegistry.registerNewTask(site, task);
	}

	public void publishSite(Long siteId, boolean again, String username) throws PublishException {
		Site site = getSite(siteId);
		Taskable task = new SiteTask.Builder(cfg, templatePublishService, templatePublishSourceService, resourcePublishService, articlePublishService, channelPublishService, site)
				.setUsername(username).setAgain(again).build();
		taskRegistry.registerNewTask(site, task);
	}

	public void publishArticle(Long channelId, Long[] publishIds, String username) throws PublishException {
		Channel channel = getChannel(channelId);
		Site site = channel.getSite();
		List<Template> templates = templatePublishService.getTemplatesInChannel(channelId);
		for (Template template : templates) {
			if (template.getType() != TemplateType.DETAIL) {
				continue;
			}
			Taskable task = new DetailTask.Builder(cfg, templatePublishSourceService, resourcePublishService, articlePublishService, site, channel, template).setUsername(username)
					.setAgain(true).setPublishIds(publishIds).build();
			taskRegistry.registerNewTask(site, task);
		}
	}

	public void removePublish(Long siteId, String id, String username) throws PublishException {
		try {
			taskRegistry.removeTask(siteId, id, username);
		} catch (TaskException e) {
			throw new PublishException(e);
		}
	}

	public void closeSitePublish(Long siteId) {
		taskRegistry.closeSite(siteId);
	}

	public List<Taskable> getSitePublishTasks(Long siteId) {
		return taskRegistry.getSiteTasks(siteId);
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

	public void setSitePublishService(SitePublishService sitePublishService) {
		this.sitePublishService = sitePublishService;
	}

	public void setResourcePublishService(ResourcePublishService resourcePublishService) {
		this.resourcePublishService = resourcePublishService;
	}

	public void setTemplateSourcePublishService(TemplateSourcePublishService templateSourcePublishService) {
		this.templatePublishSourceService = templateSourcePublishService;
	}

	public void setTaskRegistry(TaskRegistryable taskRegistry) {
		this.taskRegistry = taskRegistry;
	}

	public void setConfiguration(Configuration cfg) {
		this.cfg = cfg;
	}
}
