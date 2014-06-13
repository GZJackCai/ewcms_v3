/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.content.particular.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.content.particular.dao.ApprovalRecordDao;
import com.ewcms.content.particular.model.ApprovalRecord;
import com.ewcms.plugin.BaseException;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author wu_zhijun
 *
 */
@Component
public class ApprovalRecordService  {

	@Autowired
	private ApprovalRecordDao approvalRecordDao;
	
	public Long addApprovalRecord(ApprovalRecord approvalRecord) throws BaseException {
		String code = approvalRecord.getCode();
		ApprovalRecord entity = approvalRecordDao.findByCode(code);
		if (entity != null) throw new BaseException("已存在相同的代码", "已存在相同的代码");
		approvalRecordDao.save(approvalRecord);
		return approvalRecord.getId();
	}

	public Long updApprovalRecord(ApprovalRecord approvalRecord){
		approvalRecordDao.save(approvalRecord);
		return approvalRecord.getId();
	}

	public void delApprovalRecord(Long id) {
		approvalRecordDao.delete(id);
	}

	public ApprovalRecord findApprovalRecordById(Long id){
		return approvalRecordDao.findOne(id);
	}

	public List<ApprovalRecord> findApprovalRecordAll() {
		return (List<ApprovalRecord>) approvalRecordDao.findAll();
	}

	public Boolean findApprovalRecordSelected(Long projectBasicId, String approvalRecordCode) {
		List<ApprovalRecord> approvalRecords = approvalRecordDao.findBySelected(projectBasicId, approvalRecordCode);
		if (approvalRecords != null && !approvalRecords.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, approvalRecordDao, ApprovalRecord.class);
	}

}
