package com.ewcms.content.particular.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.particular.model.IndustryCode;

/**
 * @author 吴智俊
 */
public interface IndustryCodeDao extends PagingAndSortingRepository<IndustryCode, Long>, JpaSpecificationExecutor<IndustryCode> {
	IndustryCode findByCode(String code);
	
	@Query("select r from ProjectBasic p inner join p.industryCode r where p.id=?1 And r.code=?2")
	List<IndustryCode> findSelected(Long projectBasicId, String code);

}
