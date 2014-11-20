package com.ewcms.visit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.site.service.ChannelService;
import com.ewcms.visit.dao.traffic.ArticleClickDao;
import com.ewcms.visit.dao.traffic.ChannelClickDao;
import com.ewcms.visit.dao.traffic.UrlClickDao;
import com.ewcms.visit.model.traffic.ArticleClick;
import com.ewcms.visit.model.traffic.ChannelClick;
import com.ewcms.visit.model.traffic.UrlClick;
import com.ewcms.visit.util.ChartVisitUtil;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.util.PaginationUtil;

@Component
@Transactional(readOnly = true)
public class TrafficService {
	
	@Autowired
	private ChannelClickDao channelClickDao;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ArticleClickDao articleClickDao;
	@Autowired
	private UrlClickDao urlClickDao;
	
	public List<ChannelClick> findChannelTable(Date startVisitDate, Date endVisitDate, Long channelId, Long siteId){
		channelId = channelService.findRootChannelId(channelId, siteId);
		List<ChannelClick> channelClicks = channelClickDao.findChannelClick(startVisitDate, endVisitDate, siteId, channelId);
		return channelClicks;
	}
	
	public String findChannelReport(Date startVisitDate, Date endVisitDate, Long channelId, Long siteId){
		channelId = channelService.findRootChannelId(channelId, siteId);
		
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		
		List<ChannelClick> channelClicks = channelClickDao.findChannelClick(startVisitDate, endVisitDate, siteId, channelId);
		for (ChannelClick channelClick : channelClicks){
			String channelName = channelClick.getChannelClickPk().getChannelName();
			Long pageViewSum = channelClick.getPageViewSum();
			Long parentPageViewSum = channelClick.getChildPageViewSum();
			Long sum = (pageViewSum == null ? 0 : pageViewSum) + (parentPageViewSum == null ? 0 : parentPageViewSum);
			dataSet.put(channelName, sum);
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findChannelTrendReport(Date startVisitDate, Date endVisitDate, Long channelId, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<ChannelClick> channelClicks = channelClickDao.findChannelClickTrend(startVisitDate, endVisitDate, siteId, channelId);
		for (ChannelClick channelClick : channelClicks){
			Date visitDate = channelClick.getChannelClickPk().getVisitDate();
			Long pageViewSum = channelClick.getPageViewSum();
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), pageViewSum == null ? 0L : pageViewSum);
		}
		dataSet.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
	
	public Map<String, Object> findArticleClickTable(Date startVisitDate, Date endVisitDate, Long parentChannelId, Long siteId, QueryParameter params){
		Pageable pageable = new PageRequest(params.getPage() - 1, params.getRows());

		List<Long> channelIds = new ArrayList<Long>();
		if (parentChannelId != null && parentChannelId > 1L){
			channelIds.add(parentChannelId);
			channelService.getChannelId(channelIds, parentChannelId);
		}
		
		Long total = 0L;
		Page<ArticleClick> articleClicks = null;
		if (channelIds.isEmpty()){
			total = articleClickDao.countArticleClick(startVisitDate, endVisitDate, siteId);
			articleClicks = articleClickDao.findArticleClick(startVisitDate, endVisitDate, siteId, pageable);
		}else{
			total = articleClickDao.countArticleClick(startVisitDate, endVisitDate, siteId, channelIds);
			articleClicks = articleClickDao.findArticleClick(startVisitDate, endVisitDate, siteId, channelIds, pageable);
		}
		return PaginationUtil.pagination(total, articleClicks.getContent());
	}
	
	public Map<String, Object> findUrlClickTable(Date startVisitDate, Date endVisitDate, Long siteId, QueryParameter params){
		Long total = urlClickDao.countUrlClick(startVisitDate, endVisitDate, siteId);
		Pageable pageable = new PageRequest(params.getPage() - 1, params.getRows());
		Page<UrlClick> urlClicks = urlClickDao.findUrlClick(startVisitDate, endVisitDate, siteId, pageable);
		return PaginationUtil.pagination(total, urlClicks.getContent());
	}
	
	public String findUrlClickTrendReport(Date startVisitDate, Date endVisitDate, String url, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<UrlClick> urlClicks = urlClickDao.findUrlClick(startVisitDate, endVisitDate, siteId, url);
		for (UrlClick urlClick : urlClicks){
			Date visitDate = urlClick.getUrlClickPk().getVisitDate();
			Long urlCount = urlClick.getUrlCount();
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), urlCount == null ? 0L : urlCount);
		}
		dataSet.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
}
