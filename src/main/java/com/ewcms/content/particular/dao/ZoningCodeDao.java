package com.ewcms.content.particular.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.particular.model.ZoningCode;

/**
 * @author 吴智俊
 */
public interface ZoningCodeDao extends PagingAndSortingRepository<ZoningCode, Long>, JpaSpecificationExecutor<ZoningCode> {
	ZoningCode findByCode(String code);
	
	@Query("select r from ProjectBasic p inner join p.zoningCode r where p.id=?1 And r.code=?2")
	List<ZoningCode> findSelected(Long projectBasicId, String code);
}
