package com.ewcms.visit.dao;

import java.util.Date;
import java.util.List;

import com.ewcms.visit.vo.loyalty.BackVo;
import com.ewcms.visit.vo.loyalty.StickTimeVo;

/**
 * @author 吴智俊
 */
public interface LoyaltyDaoCustom {
	
	public List<BackVo> findBackTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page);
	
	public List<StickTimeVo> findStickTimeTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page);
}
