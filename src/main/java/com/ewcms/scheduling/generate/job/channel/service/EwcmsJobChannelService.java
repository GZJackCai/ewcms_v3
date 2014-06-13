/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.scheduling.generate.job.channel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.plugin.BaseException;
import com.ewcms.scheduling.generate.job.JobClassEntity;
import com.ewcms.scheduling.generate.job.channel.dao.EwcmsJobChannelDao;
import com.ewcms.scheduling.generate.job.channel.model.EwcmsJobChannel;
import com.ewcms.scheduling.manage.SchedulingFac;
import com.ewcms.scheduling.manage.dao.JobClassDao;
import com.ewcms.scheduling.manage.dao.JobInfoDao;
import com.ewcms.scheduling.manage.util.ConversionUtil;
import com.ewcms.scheduling.manage.vo.PageDisplay;
import com.ewcms.scheduling.model.JobClass;
import com.ewcms.scheduling.model.JobInfo;
import com.ewcms.site.dao.ChannelDao;
import com.ewcms.site.model.Channel;

/**
 * 频道定时任务Service
 * 
 * @author 吴智俊
 */
@Component
public class EwcmsJobChannelService {
	@Autowired
	private EwcmsJobChannelDao ewcmsJobChannelDao;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private JobInfoDao jobInfoDao;
	@Autowired
	private JobClassDao jobClassDao;
	@Autowired
	private SchedulingFac schedulingFac;
	
	public Long saveOrUpdateJobChannel(Long channelId, PageDisplay vo, Boolean isAppChildenChannel) throws BaseException{
		Channel channel = channelDao.findOne(channelId);
		if (channel != null) {
			JobInfo jobInfo = new JobInfo();
			if (vo.getJobId() != null && vo.getJobId().intValue() > 0){
				jobInfo = jobInfoDao.findOne(vo.getJobId());
			}
			
			if (jobInfo == null) {
				throw new BaseException("定时任务已经被删除,请重新操作!","定时任务已经被删除,请重新操作!");
			}
			
			jobInfo = ConversionUtil.constructJobInfo(jobInfo,vo);

			EwcmsJobChannel jobChannel = new EwcmsJobChannel();
			jobChannel.setSubChannel(isAppChildenChannel);
			if (vo.getJobId() != null && vo.getJobId().intValue() > 0) {
				jobChannel.setId(vo.getJobId());
				jobChannel.setJobClass(jobInfo.getJobClass());
			}else{
				JobClass jobClass = jobClassDao.findByClassEntity(JobClassEntity.JOB_CHANNEL);
				if (jobClass == null) {
					jobClass = new JobClass();
					jobClass.setClassEntity(JobClassEntity.JOB_CHANNEL);
					jobClass.setClassName("频道定时器类");
					jobClass.setDescription("频道定时器类");
					jobClassDao.save(jobClass);
				}
				jobChannel.setJobClass(jobClass);
			}

			jobChannel.setDescription(jobInfo.getDescription());
			jobChannel.setLabel(jobInfo.getLabel());
			jobChannel.setNextFireTime(jobInfo.getNextFireTime());
			jobChannel.setOutputLocale(jobInfo.getOutputLocale());
			jobChannel.setPreviousFireTime(jobInfo.getPreviousFireTime());
			jobChannel.setState(jobInfo.getState());
			jobChannel.setTrigger(jobInfo.getTrigger());
			jobChannel.setUserName(jobInfo.getUserName());
			jobChannel.setVersion(jobInfo.getVersion());
			jobChannel.setChannel(channel);
			if (jobChannel.getId() == null) {
				return schedulingFac.saveScheduleJob(jobChannel);
			} else {
				return schedulingFac.updateScheduledJob(jobChannel);
			}
		}
		return null;
	}

	public EwcmsJobChannel getScheduledJobChannel(Long jobId) {
		return (EwcmsJobChannel)ewcmsJobChannelDao.findOne(jobId);
	}

	public EwcmsJobChannel findJobChannelByChannelId(Long channelId) {
		return ewcmsJobChannelDao.findJobChannelByChannelId(channelId);
	}
	
	public Channel findChannelByChannelId(Long channelId) {
		return channelDao.findOne(channelId);
	}
}
