package com.ewcms.visit.dao;

import java.util.Date;
import java.util.List;

import com.ewcms.visit.vo.traffic.ArticleClickVo;
import com.ewcms.visit.vo.traffic.UrlClickVo;

/**
 * @author 吴智俊
 */
public interface TrafficDaoCustom {
	
	public List<ArticleClickVo> findArticleClickTable(Date startDate, Date endDate, List<Long> channelIds, Long siteId, Integer rows, Integer page);
	
	public Long findArticleClickCount(Date startDate, Date endDate, List<Long> channelIds, Long siteId);
	
	public List<UrlClickVo> findUrlClickTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page);
	
	public Long findUrlClickCount(Date startDate, Date endDate, Long siteId);
}
