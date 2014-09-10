/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.publication.task.generator;

import java.util.List;

import com.ewcms.publication.cache.LRUCache;
import com.ewcms.publication.dao.ArticlePublishDaoable;
import com.ewcms.publication.dao.ChannelPublishDaoable;
import com.ewcms.publication.dao.ResourcePublishDaoable;
import com.ewcms.publication.dao.TemplatePublishDaoable;
import com.ewcms.publication.dao.TemplateSourcePublishDaoable;
import com.ewcms.publication.deploy.DepTask;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.publish.PublishRunnerable;
import com.ewcms.publication.task.Taskable;

import freemarker.template.Configuration;

/**
 * 发布整个站点任务
 * 
 * @author <a href="hhywangwei@gmail.com">王伟</a>
 */
public class SiteTask extends GenTaskBase {
	private static final int DEFAULT_CACHE_SIZE = 100;
	
	private final ChannelPublishDaoable channelPublishDao;
	private final TemplatePublishDaoable templatePublishDao;
	private final ArticlePublishDaoable articlePublishDao;
	
	public SiteTask(Site site, Configuration cfg, String tempRoot,
			boolean again, ResourcePublishDaoable resourcePublishDao, TemplateSourcePublishDaoable templateSourcePublishDao,
			ChannelPublishDaoable channelPublishDao, TemplatePublishDaoable templatePublishDao,	ArticlePublishDaoable articlePublishDao) {
		
		super(site, cfg, tempRoot, again, resourcePublishDao, templateSourcePublishDao);
		this.channelPublishDao = channelPublishDao;
		this.templatePublishDao = templatePublishDao;
		this.articlePublishDao = articlePublishDao;
	}
	
	@Override
	protected void regSubtask(PublishRunnerable pubRunner){
		Channel root = channelPublishDao.findRoot(site.getId());
		Taskable task = new ChannelTask(id,site, root, cfg, tempRoot, again, true, 
				resourcePublishDao, templateSourcePublishDao, channelPublishDao, templatePublishDao, articlePublishDao).
				setCache(new LRUCache<String,String>(DEFAULT_CACHE_SIZE));
		
		task.regTask(pubRunner);
	}
	
	@Override
	public TaskType getTaskType() {
		return TaskType.SITE;
	}

	@Override
	public String getRemark() {
		return String.format("(%d)%s--站点网页发布", site.getId(), site.getSiteName());
	}

	@Override
	protected String newKey() {
        StringBuilder builder = new StringBuilder();
		
		builder.append("ChannelTask[");
		builder.append(site.getId());
		builder.append("]");
		
		return builder.toString();
	}

	@Override
	protected void doHandler(List<DepTask> tasks, int count, int index, int batchSize) {
		//not task
	}

	@Override
	protected int totalCount() {
		return 0;
	}
}
