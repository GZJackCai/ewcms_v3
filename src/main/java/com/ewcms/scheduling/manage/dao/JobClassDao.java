package com.ewcms.scheduling.manage.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.scheduling.model.JobClass;

/**
 * 
 * @author wu_zhijun
 *
 */
public interface JobClassDao extends PagingAndSortingRepository<JobClass, Long>, JpaSpecificationExecutor<JobClass>{
	JobClass findByClassEntity(String classEntity);
}
