/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

/**
 * 
 */
package com.ewcms.site.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.site.dao.SiteDao;
import com.ewcms.site.model.Organ;
import com.ewcms.site.model.Site;
import com.ewcms.site.model.SiteServer;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;
import com.ewcms.web.vo.TreeNode;

/**
 * @author wu_zhijun
 * 
 */
@Component
@Transactional(readOnly = true)
public class SiteService {

	@Autowired
	private SiteDao siteDao;

	@Transactional(readOnly = false)
	public Long addSite(Long parentId, String siteName, Long organId) {
		Site site = new Site();
		site.setSiteName(siteName);
		if(parentId!=null) site.setParent(siteDao.findOne(parentId));
		Organ organVo = new Organ();
		organVo.setId(organId);
		site.setOrgan(organVo);
		siteDao.save(site);
		return site.getId();
	}
	
	@Transactional(readOnly = false)
	public Long saveSiteServer(Site vo) {
		Site oldvo = getSite(vo.getId());
		SiteServer siteServer = vo.getSiteServer();
		if(siteServer.getPassword()==null||siteServer.getPassword().length()==0){
			if(oldvo.getSiteServer()!=null){
				siteServer.setPassword(oldvo.getSiteServer().getPassword());
			}
		}
		oldvo.setSiteServer(vo.getSiteServer());
		updSite(oldvo);
		return oldvo.getSiteServer().getId();
	}
	
	@Transactional(readOnly = false)
	public void delSiteBatch(List<Long> idList) {
		for (Long id : idList) {
			delSite(id);
		}
	}

	@Transactional(readOnly = false)
	public Long updSite(Site vo) {
		siteDao.save(vo);
		Site currSite = getCurSite();
		if(currSite != null){
			if(currSite.getId() == vo.getId()){
				//initSiteInContext(vo);
			}
		}
		return vo.getId();
	}

	@Transactional(readOnly = false)
	public void delSite(Long id) {
		siteDao.delete(id);
	}

	public Site getSite(Long id) {
		return siteDao.findOne(id);
	}

	public List<Site> getSiteListByOrgans(Long[] organs, Boolean publicenable) {
		return siteDao.getSiteListByOrgans(organs, publicenable);
	}

	public void updSiteParent(Long organId, Long parentId,Long newParentId) {
		siteDao.updSiteParent(organId, parentId, newParentId);
	}
	
	/**
	 * 获取机构跟站点集
	 * 
	 */ 	
	public List<TreeNode> getOrganSiteTreeList(Long organId){
		return getSiteChildren(null,organId);
	}
	
	/**
	 * 获取机构站点子站点集
	 * 
	 */ 	
	public List<TreeNode> getOrganSiteTreeList(Long parentId,Long organId){
		return getSiteChildren(parentId,organId);
	}
	
	/**
	 * 获取客户跟站点集
	 * 
	 */ 	
	public List<TreeNode> getCustomerSiteTreeList(){
		return getSiteChildren(null,null);
	}
	
	/**
	 * 获取客户站点子站点集
	 * 
	 */ 	
	public List<TreeNode> getCustomerSiteTreeList(Long parentId){
		return getSiteChildren(parentId,null);
	}	
	
	/**
	 * 获取子站点.
	 */
	public List<TreeNode> getSiteChildren(Long parentId, Long organId) {
		List<TreeNode> tnList = new ArrayList<TreeNode>();
		List<Site> siteList = siteDao.getSiteChildren(parentId, organId);
		for (Site vo : siteList) {
			TreeNode tnVo = new TreeNode();
			tnVo.setId(vo.getId().toString());
			tnVo.setText(vo.getSiteName());

			if (vo.hasChildren()) {
				tnVo.setState("closed");
			} else {
				tnVo.setState("open");
			}
			tnList.add(tnVo);
		}
		return tnList;
	}
	
	public Map<String, Object> searchSiteSwitch(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, siteDao, Site.class);
	}
	
    private Site getCurSite() {
        return EwcmsContextUtil.getCurrentSite();
    }
    
}
