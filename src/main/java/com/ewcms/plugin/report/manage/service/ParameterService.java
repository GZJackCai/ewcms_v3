/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.report.manage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.plugin.report.manage.dao.ParameterDao;
import com.ewcms.plugin.report.model.Parameter;

/**
 * 
 * @author wu_zhijun
 *
 */
@Component
public class ParameterService {

	@Autowired
	private ParameterDao parameterDao;
	
	public Long updParameter(Parameter parameter){
		parameterDao.save(parameter);
		return parameter.getId();
	}
	
	public Parameter findParameterById(Long parameterId) {
		return parameterDao.findOne(parameterId);
	}

	public Boolean findSessionIsEntityByParameterIdAndUserName(Long parameterId, String userName) {
		return parameterDao.findSessionIsEntityByParameterIdAndUserName(parameterId, userName);
	}
}
