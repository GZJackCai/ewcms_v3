package com.ewcms.site.dao;

import java.util.List;

import com.ewcms.site.model.Site;

/**
 * 站点自定义DAO接口
 * 
 * @author wu_zhijun
 *
 */
public interface SiteDaoCustom {
	
	/**
	 * 通过组织编号集合获取站点对象集合
	 * 
	 * @param organIds 组织编号集合
	 * @param publicenable 是否允许发布
	 * @return List 站点对象集合
	 */
	List<Site> getSiteListByOrgans(final Long[] organIds,final Boolean publicenable);
	
	/**
	 * 通过父站点编号和组织编号获取站点对象集合
	 * 
	 * @param parentSiteId 父站点编号
	 * @param organId 组织编号
	 * @return List 站点对象集合
	 */
	List<Site> getSiteChildren(final Long parentSiteId,final Long organId);
	
	/**
	 * 通过组织编号和父站点编号更新新的父站点编号
	 * 
	 * @param organId 组织编号
	 * @param parentSiteId 父站点编号
	 * @param newParentSiteId 新的父站点编号
	 */
	void updSiteParent(final Long organId,final Long parentSiteId,final Long newParentSiteId);
	
}
