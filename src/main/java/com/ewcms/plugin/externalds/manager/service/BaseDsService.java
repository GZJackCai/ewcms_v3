/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.externalds.manager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.plugin.externalds.manager.dao.BaseDsDao;
import com.ewcms.plugin.externalds.manager.dao.BeanDsDao;
import com.ewcms.plugin.externalds.manager.dao.CustomDsDao;
import com.ewcms.plugin.externalds.manager.dao.JdbcDsDao;
import com.ewcms.plugin.externalds.manager.dao.JndiDsDao;
import com.ewcms.plugin.externalds.model.BaseDs;
import com.ewcms.plugin.externalds.model.BeanDs;
import com.ewcms.plugin.externalds.model.CustomDs;
import com.ewcms.plugin.externalds.model.JdbcDs;
import com.ewcms.plugin.externalds.model.JndiDs;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

@Component
public class BaseDsService {
	
	@Autowired
	private BaseDsDao baseDsDao;
	@Autowired
	private BeanDsDao beanDsDao;
	@Autowired
	private CustomDsDao customDsDao;
	@Autowired
	private JdbcDsDao jdbcDsDao;
	@Autowired
	private JndiDsDao jndiDsDao;

	public void setBaseDsDao(BaseDsDao baseDsDao){
		this.baseDsDao = baseDsDao;
	}
	
	public Long saveOrUpdateBaseDs(BaseDs baseDs) {
		baseDsDao.save(baseDs);
		return baseDs.getId();
	}

	public BaseDs findByBaseDs(Long baseDsId) {
		return baseDsDao.findOne(baseDsId);
	}

	public Iterable<BaseDs> findAllBaseDs() {
		Iterable<BaseDs> baseDss = baseDsDao.findAll();
		List<BaseDs> newBaseDss = new ArrayList<BaseDs>();
		
		BaseDs newBaseDs = new BaseDs();
		newBaseDs.setId(0L);
		newBaseDs.setName("默认数据源");
		newBaseDss.add(newBaseDs);
		
		for (BaseDs baseDs : baseDss){
			newBaseDss.add(baseDs);
		}
		
		return newBaseDss;
	}
	
	public void deletedBaseDs(Long baseDsId) {
		baseDsDao.delete(baseDsId);
	}
	
	public Map<String, Object> searchBeanDs(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, beanDsDao, BeanDs.class);
	}

	public Map<String, Object> searchCustomDs(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, customDsDao, CustomDs.class);
	}
	
	public Map<String, Object> searchJdbcDs(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, jdbcDsDao, JdbcDs.class);
	}
	
	public Map<String, Object> searchJndiDs(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, jndiDsDao, JndiDs.class);
	}
}
