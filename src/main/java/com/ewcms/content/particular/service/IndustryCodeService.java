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

import com.ewcms.content.particular.dao.IndustryCodeDao;
import com.ewcms.content.particular.model.IndustryCode;
import com.ewcms.plugin.BaseException;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

@Component
public class IndustryCodeService {

	@Autowired
	private IndustryCodeDao industryCodeDao;
	
	public Long addIndustryCode(IndustryCode industryCode) throws BaseException{
		String code = industryCode.getCode();
		IndustryCode entity = industryCodeDao.findByCode(code);
		if (entity != null) throw new BaseException("已存在相同的代码", "已存在相同的代码");
		industryCodeDao.save(industryCode);
		return industryCode.getId();
	}

	public Long updIndustryCode(IndustryCode industryCode) {
		industryCodeDao.save(industryCode);
		return industryCode.getId();
	}

	public void delIndustryCode(Long id) {
		industryCodeDao.delete(id);
	}

	public IndustryCode findIndustryCodeById(Long id) {
		return industryCodeDao.findOne(id);
	}

	public List<IndustryCode> findIndustryCodeAll() {
		return (List<IndustryCode>) industryCodeDao.findAll();
	}

	public Boolean findIndustryCodeSelected(Long projectBasicId, String industryCodeCode) {
		List<IndustryCode> industryCodes = industryCodeDao.findSelected(projectBasicId, industryCodeCode);
		if (industryCodes != null && !industryCodes.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	
	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, industryCodeDao, IndustryCode.class);
	}

}
