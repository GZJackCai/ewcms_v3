package com.ewcms.visit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.site.model.Channel;
import com.ewcms.site.service.ChannelService;
import com.ewcms.visit.dao.TrafficDao;
import com.ewcms.visit.util.ChartVisitUtil;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.visit.vo.traffic.ArticleClickVo;
import com.ewcms.visit.vo.traffic.ChannelClickVo;
import com.ewcms.visit.vo.traffic.UrlClickVo;
import com.ewcms.web.util.PaginationUtil;

/**
 * 访问量排行
 * 
 * @author 吴智俊
 */
@Component
@Transactional(readOnly = true)
public class TrafficService {
	
	@Autowired
	private TrafficDao trafficDao;
	@Autowired
	private ChannelService channelService;
	
	private Long findParentChannelId(Long parentChannelId, Long siteId){
		if (parentChannelId == null || parentChannelId == -1L){
			Channel rootChannel = channelService.getChannelRoot(siteId);
			if (rootChannel == null) return null;
			return rootChannel.getId();
		}
		return parentChannelId;
	}
	
	public List<ChannelClickVo> findChannelTable(Date startDate, Date endDate, Long parentChannelId, Long siteId){
		parentChannelId = findParentChannelId(parentChannelId, siteId);
		List<ChannelClickVo> channelClickVos = trafficDao.findChannelClickTable(startDate, endDate, parentChannelId, siteId);
		return channelClickVos;
	}
	
	@SuppressWarnings("rawtypes")
	public String findChannelReport(Date startDate, Date endDate, Long parentChannelId, Long siteId){
		parentChannelId = findParentChannelId(parentChannelId, siteId);
		
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		
		List<Map> channelClickList = trafficDao.findChannelClickReport(startDate, endDate, parentChannelId, siteId);
		Iterator channelClickIt = channelClickList.iterator();  
		while (channelClickIt.hasNext()){
			Map map = (Map)channelClickIt.next();
			String channelName = (String) map.get("channelName");
			Long levelPv = (Long) map.get("levelPv");
			Long pv = (Long) map.get("pv");
			Long sum = (levelPv == null ? 0 : levelPv) + (pv == null ? 0 : pv);
			dataSet.put(channelName, sum);
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	@SuppressWarnings("rawtypes")
	public String findChannelTrendReport(Date startDate, Date endDate, Long channelId, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		List<Map> dateAreaSumPvList = trafficDao.findChannelClickTrendReport(startDate, endDate, channelId, siteId);
		Iterator sumPvIt = dateAreaSumPvList.iterator();  
		while (sumPvIt.hasNext()){
			Map map = (Map)sumPvIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long sumPv = (Long) map.get("sumPv");
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), sumPv == null ? 0L : sumPv);
		}
		dataSet.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
	
	public Map<String, Object> findArticleClickTable(Date startDate, Date endDate, Long parentChannelId, Long siteId, Integer rows, Integer page){
		List<Long> channelIds = new ArrayList<Long>();
		if (parentChannelId != null && parentChannelId > 1L){
			channelIds.add(parentChannelId);
			getChannelId(channelIds, parentChannelId);
		}
		List<ArticleClickVo> articleClickVos = trafficDao.findArticleClickTable(startDate, endDate, channelIds, siteId, rows, page);
		Long total = trafficDao.findArticleClickCount(startDate, endDate, channelIds, siteId);
		return PaginationUtil.pagination(total, articleClickVos);
	}
	
	private void getChannelId(List<Long> channelIds, Long parentChannelId){
		List<Channel> channels = channelService.getChannelChildren(parentChannelId);
		if (!channels.isEmpty()){
			for (Channel channel : channels){
				channelIds.add(channel.getId());
				getChannelId(channelIds, channel.getId());
			}
		}
	}
	
	public Map<String, Object> findUrlClickTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page){
		Long total = trafficDao.findUrlClickCount(startDate, endDate, siteId);
		List<UrlClickVo> urlClickVos = trafficDao.findUrlClickTable(startDate, endDate, siteId, rows, page);
		return PaginationUtil.pagination(total, urlClickVos);
	}
	
	@SuppressWarnings("rawtypes")
	public String findUrlClickTrendReport(Date startDate, Date endDate, String url, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		List<Map> dateAreaSumPvList = trafficDao.findUrlClickTrendReport(startDate, endDate, url, siteId);
		Iterator sumPvIt = dateAreaSumPvList.iterator();  
		while (sumPvIt.hasNext()){
			Map map = (Map)sumPvIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long sumPv = (Long) map.get("sumPv");
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), sumPv == null ? 0L : sumPv);
		}
		dataSet.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
}
