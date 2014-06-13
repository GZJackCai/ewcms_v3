package com.ewcms.site.dao;

import java.util.List;

import com.ewcms.site.model.Organ;

/**
 * 组织自定义DAO接口
 * 
 * @author wu_zhijun
 *
 */
public interface OrganDaoCustom {

	/**
	 * 通过父组织编号获取子组织对象集合
	 * 
	 * @param parentId 父组织编号
	 * @return List 组织对象集合
	 */
	List<Organ> getOrganChildren(final Long parentId);
	
}
