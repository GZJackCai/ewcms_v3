/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.scheduling.generate.job.channel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ewcms.plugin.BaseException;
import com.ewcms.scheduling.generate.job.channel.model.EwcmsJobChannel;
import com.ewcms.scheduling.generate.job.channel.service.EwcmsJobChannelService;
import com.ewcms.scheduling.manage.vo.PageDisplay;

/**
 * 频道定时任务Fac
 * 
 * @author 吴智俊
 */
@Service
public class EwcmsJobChannelFac implements EwcmsJobChannelFacable {
	@Autowired
	private EwcmsJobChannelService ewcmsJobChannelService;

	@Override
	public EwcmsJobChannel getScheduledJobChannel(Long jobId) {
		return ewcmsJobChannelService.getScheduledJobChannel(jobId);
	}

	@Override
	public EwcmsJobChannel findJobChannelByChannelId(Long channelId) {
		return ewcmsJobChannelService.findJobChannelByChannelId(channelId);
	}

	@Override
	public Long saveOrUpdateJobChannel(Long channelId, PageDisplay vo, Boolean isAppChildenChannel) throws BaseException {
		return ewcmsJobChannelService.saveOrUpdateJobChannel(channelId, vo, isAppChildenChannel);
	}
}
