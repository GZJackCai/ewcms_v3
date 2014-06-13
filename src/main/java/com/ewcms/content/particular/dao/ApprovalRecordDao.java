package com.ewcms.content.particular.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.particular.model.ApprovalRecord;

/**
 * @author 吴智俊
 */
public interface ApprovalRecordDao extends PagingAndSortingRepository<ApprovalRecord, Long>, JpaSpecificationExecutor<ApprovalRecord>{
	ApprovalRecord findByCode(String code);
	
	@Query("select r from ProjectBasic p inner join p.approvalRecord r where p.id=?1 And r.code=?2")
	List<ApprovalRecord> findBySelected(final Long projectBasicId, final String code);
}
