package com.ewcms.scheduling.generate.job.channel.dao;

import com.ewcms.scheduling.generate.job.channel.model.EwcmsJobChannel;

/**
 * @author 吴智俊
 */
public interface EwcmsJobChannelDaoCustom {
	EwcmsJobChannel findJobChannelByChannelId(final Long channelId);
}
