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

import com.ewcms.content.particular.dao.ZoningCodeDao;
import com.ewcms.content.particular.model.ZoningCode;
import com.ewcms.plugin.BaseException;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

@Component
public class ZoningCodeService {

	@Autowired
	private ZoningCodeDao zoningCodeDao;
	
	public Long addZoningCode(ZoningCode zoningCode) throws BaseException{
		String code = zoningCode.getCode();
		ZoningCode entity = zoningCodeDao.findByCode(code);
		if (entity != null) throw new BaseException("已存在相同的代码", "已存在相同的代码");
		zoningCodeDao.save(zoningCode);
		return zoningCode.getId();
	}

	public Long updZoningCode(ZoningCode zoningCode) {
		zoningCodeDao.save(zoningCode);
		return zoningCode.getId();
	}

	public void delZoningCode(Long id) {
		zoningCodeDao.delete(id);
	}

	public ZoningCode findZoningCodeById(Long id) {
		return zoningCodeDao.findOne(id);
	}

	public List<ZoningCode> findZoningCodeAll() {
		return (List<ZoningCode>) zoningCodeDao.findAll();
	}

	public Boolean findZoningCodeSelected(Long projectBasicId, String zoningCodeCode) {
		List<ZoningCode> zoningCodes = zoningCodeDao.findSelected(projectBasicId, zoningCodeCode);
		if (zoningCodes != null && !zoningCodes.isEmpty()){
			return true;
		}else{
			return false;
		}
	}

	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, zoningCodeDao, ZoningCode.class);
	}
}
