package com.ewcms.scheduling.manage.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.scheduling.model.JobInfo;

/**
 * 
 * @author wu_zhijun
 *
 */
public interface JobInfoDao extends PagingAndSortingRepository<JobInfo, Long>, JpaSpecificationExecutor<JobInfo>{

}
