/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ewcms.scheduling.manage.service;

import java.lang.Class;
import java.util.List;
import java.util.Map;

import org.quartz.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.plugin.BaseException;
import com.ewcms.scheduling.manage.dao.JobClassDao;
import com.ewcms.scheduling.model.JobClass;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author 吴智俊
 */
@Component
public class JobClassService {

	@Autowired
	private JobClassDao jobClassDao;

	public Long saveJobClass(JobClass jobClass) throws BaseException {
		if (validator(jobClass)) {
			jobClassDao.save(jobClass);
			return jobClass.getId();
		}
		return null;
	}

	public Long updateJobClass(JobClass alqcJobClass) {
		jobClassDao.save(alqcJobClass);
		return alqcJobClass.getId();
	}

	public JobClass findByJobClass(Long id) {
		return jobClassDao.findOne(id);
	}

	public List<JobClass> findByAllJobClass() {
		return (List<JobClass>) jobClassDao.findAll();
	}

	public void deletedJobClass(Long id) {
		jobClassDao.delete(id);
	}

	protected Boolean validator(JobClass jobClass) throws BaseException {
		String jobClassEntity = jobClass.getClassEntity().trim();
		if (jobClassEntity != null && jobClassEntity.length() > 0) {
			try {
				Class<?> classEntity = Class.forName(jobClassEntity);
				if (Job.class.isAssignableFrom(classEntity)) {
					return true;
				} else {
					throw new BaseException("不是一个有效的作业", "不是一个有效的作业");
				}
			} catch (ClassNotFoundException ex) {
				throw new BaseException("未找到执行的作业", "未找到执行的作业");
			}
		} else {
			throw new BaseException("未设置作业", "未设置作业");
		}

	}

	public JobClass findByClassEntity(String classEntity) {
		return jobClassDao.findByClassEntity(classEntity);
	}
	
	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, jobClassDao, JobClass.class);
	}
}
