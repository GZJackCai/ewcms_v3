package com.ewcms.scheduling.manage.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.scheduling.model.JobTrigger;

/**
 * @author 吴智俊
 */
public interface JobTriggerDao extends PagingAndSortingRepository<JobTrigger, Long> {

}
