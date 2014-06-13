/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.citizen.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.plugin.citizen.dao.CitizenDao;
import com.ewcms.plugin.citizen.model.Citizen;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 *
 * @author 吴智俊
 */
@Component
public class CitizenService {
	
	@Autowired
	private CitizenDao citizenDao;

	public Long addCitizen(Citizen citizen) {
		Citizen isCitizen = citizenDao.findByName(citizen.getName());
		if (isCitizen != null) return null;
		citizenDao.save(citizen);
		return citizen.getId();
	}

	public void delCitizen(Long citizenId) {
		citizenDao.delete(citizenId);
	}

	public List<Citizen> getAllCitizen() {
		return (List<Citizen>) citizenDao.findAll();
	}

	public Citizen getCitizen(Long citizenId) {
		return citizenDao.findOne(citizenId);
	}

	public Long updCitizen(Citizen citizen) {
		Citizen isCitizen = citizenDao.findByNotIdAndName(citizen.getId(), citizen.getName());
		if (isCitizen != null) return null;
		citizenDao.save(citizen);
		return citizen.getId();
	}
	
	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, citizenDao, Citizen.class);
	}
}
