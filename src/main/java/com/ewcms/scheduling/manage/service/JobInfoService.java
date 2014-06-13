/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.scheduling.manage.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.plugin.BaseException;
import com.ewcms.scheduling.generate.quartz.JobsQuartzSchedulerable;
import com.ewcms.scheduling.manage.dao.JobInfoDao;
import com.ewcms.scheduling.manage.dao.JobTriggerDao;
import com.ewcms.scheduling.model.JobInfo;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * @author 吴智俊
 */
@Component
public class JobInfoService {
	
	@Autowired
	private JobInfoDao jobInfoDao;
	@Autowired
	private JobTriggerDao jobTriggerDao;
	@Autowired
	private JobsQuartzSchedulerable jobsQuartzScheduler;
	
	public JobInfo saveJob(JobInfo jobInfo) {
		jobInfoDao.save(jobInfo);
		return jobInfo;
	}

	public JobInfo updateJob(JobInfo jobInfo) {
		if (jobInfo.getTrigger() != null){
			Long triggerId = jobInfo.getTrigger().getId();
			if (triggerId != null){
				jobTriggerDao.delete(triggerId);
				jobInfo.getTrigger().setId(null);
				jobInfo.getTrigger().setVersion(-1);
			}
		}
		jobInfoDao.save(jobInfo);
		return jobInfo;
	}

	public JobInfo findByJob(Long id)  {
		return jobInfoDao.findOne(id);
	}
	
	public void deletedJob(Long id)  {
		jobInfoDao.delete(id);
	}
	
	public List<JobInfo> findByAllJob() {
		return (List<JobInfo>)jobInfoDao.findAll();
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> search(QueryParameter params){
		Map<String, Object> searchMap = SearchMain.search(params, "IN_id", Long.class, jobInfoDao, JobInfo.class);
		List<JobInfo> jobInfos = (List<JobInfo>) searchMap.get("rows");
		try {
			jobInfos = jobsQuartzScheduler.getJobsRuntimeInformation(jobInfos);
		} catch (BaseException e) {
		}
		searchMap.put("rows", jobInfos);
		return searchMap;
	}

}
