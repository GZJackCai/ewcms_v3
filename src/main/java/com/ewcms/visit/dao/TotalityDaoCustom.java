package com.ewcms.visit.dao;

import java.util.Date;
import java.util.List;

import com.ewcms.visit.vo.totality.EntranceVo;
import com.ewcms.visit.vo.totality.SiteClickVo;

/**
 * @author 吴智俊
 */
public interface TotalityDaoCustom {
	
	public List<SiteClickVo> findSiteClickTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page);
	
	public List<EntranceVo> findEntrance(Date startDate, Date endDate, Long siteId, Integer rows, Integer page);
	
	public List<EntranceVo> findExit(Date startDate, Date endDate, Long siteId, Integer rows, Integer page);
}
