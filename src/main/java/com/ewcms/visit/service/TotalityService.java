package com.ewcms.visit.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.util.EmptyUtil;
import com.ewcms.visit.dao.TotalityDao;
import com.ewcms.visit.model.Visit;
import com.ewcms.visit.util.ChartVisitUtil;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.visit.vo.totality.EntranceVo;
import com.ewcms.visit.vo.totality.HostVo;
import com.ewcms.visit.vo.totality.OnlineVo;
import com.ewcms.visit.vo.totality.RegionVo;
import com.ewcms.visit.vo.totality.SiteClickVo;
import com.ewcms.visit.vo.totality.SummaryVo;
import com.ewcms.visit.vo.totality.TimeDistributedVo;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;
import com.ewcms.web.util.PaginationUtil;

/**
 * 总体情况
 * 
 * @author 吴智俊
 */
@Component
@Transactional(readOnly = true)
public class TotalityService {
	
	@Autowired
	private TotalityDao totalityDao;
	
	public Date findFirstDate(Long siteId) {
		Date firstDate = totalityDao.findFirstDate(siteId);
		if (EmptyUtil.isNull(firstDate)) return DateTimeUtil.getCalendar().getTime();
		return firstDate;
	}
	
	public Integer findDays(Long siteId) {
		Date firstAddDate = totalityDao.findFirstDate(siteId);
		if (EmptyUtil.isNull(firstAddDate)) return 1;
		Date current = DateTimeUtil.getCalendar().getTime();
		return (int) ((current.getTime() - firstAddDate.getTime())/(24*60*60*1000)) + 1;
	}
	
	public List<SummaryVo> findSummaryTable(Long siteId) {
		List<SummaryVo> summaryVos = new ArrayList<SummaryVo>();
		
		SummaryVo currentVo = totalityDao.findToDaySummary(siteId);
		summaryVos.add(currentVo);
		
		SummaryVo yesterdayNowVo = totalityDao.findYesterdayNowSummary(siteId);
		summaryVos.add(yesterdayNowVo);
		
		SummaryVo toDayForecastVo = new SummaryVo();
		List<SummaryVo> toDayForecastVos = totalityDao.findToDayForecastSummary(siteId);
		for (SummaryVo summaryVo : toDayForecastVos){
			toDayForecastVo.setName(summaryVo.getName());
			toDayForecastVo.setCountIp(toDayForecastVo.getCountIp() + summaryVo.getCountIp());
			toDayForecastVo.setCountRv(toDayForecastVo.getCountRv() + summaryVo.getCountRv());
			toDayForecastVo.setCountUv(toDayForecastVo.getCountUv() + summaryVo.getCountUv());
			toDayForecastVo.setSumPv(toDayForecastVo.getSumPv() + summaryVo.getSumPv());
			toDayForecastVo.setSumRv(toDayForecastVo.getSumRv() + summaryVo.getSumRv());
			toDayForecastVo.setAvgTime(toDayForecastVo.getAvgTime() + summaryVo.getAvgTime());
		}
		summaryVos.add(toDayForecastVo);
		
		SummaryVo yesterdayVo = totalityDao.findYesterdaySummary(siteId);
		summaryVos.add(yesterdayVo);
		
		SummaryVo thisWeekVo = totalityDao.findThisWeekSummary(DateTimeUtil.getThisWeekMonday(), DateTimeUtil.getThisWeekSunday(), siteId);
		summaryVos.add(thisWeekVo);
		
		SummaryVo thisMonthVo = totalityDao.findThisMonthSummary(DateTimeUtil.getThisMonthFirst(), DateTimeUtil.getThisMonthLast(), siteId);
		summaryVos.add(thisMonthVo);
		
		Date startDate = totalityDao.findFirstDate(siteId);
		Date endDate = totalityDao.findLastDate(siteId);
		
		Integer day = 0;
		SummaryVo avgVo = null;
		try{
			day = endDate.compareTo(startDate) + 1;
			avgVo = totalityDao.findAvgSummary(startDate, endDate, siteId);
			avgVo.setCountIp(avgVo.getCountIp()/day);
			avgVo.setCountRv(avgVo.getCountRv()/day);
			avgVo.setCountUv(avgVo.getCountUv()/day);
			avgVo.setAvgTime(avgVo.getAvgTime()/day);
			avgVo.setSumPv(avgVo.getSumPv()/day);
			avgVo.setSumRv(avgVo.getSumRv()/day);
		}catch(Exception e){
			avgVo = new SummaryVo();
			avgVo.setName("平均");
		}
		summaryVos.add(avgVo);
		
		
		SummaryVo maxVo = new SummaryVo();
		SummaryVo betideVo = new SummaryVo();
		
		maxVo.setName("最高");
		betideVo.setName("");
		
		List<SummaryVo> maxIps = totalityDao.findMaxIpSummary(siteId);
		if (maxIps != null && !maxIps.isEmpty() && maxIps.size() >= 1){
			SummaryVo maxIp = maxIps.get(0);
			betideVo.setBetideIp(maxIp.getBetide());
			maxVo.setCountIp(maxIp.getMaxCount());
		}
		
		List<SummaryVo> maxUvs = totalityDao.findMaxUvSummary(siteId);
		if (maxUvs != null && !maxUvs.isEmpty() && maxUvs.size() >=1){
			SummaryVo maxUv = maxUvs.get(0);
			betideVo.setBetideUv(maxUv.getBetide());
			maxVo.setCountUv(maxUv.getMaxCount());
		}
		
		List<SummaryVo> maxPvs = totalityDao.findMaxPvSummary(siteId);
		if (maxPvs != null && !maxPvs.isEmpty() && maxPvs.size() >= 1){
			SummaryVo maxPv = maxPvs.get(0);
			betideVo.setBetidePv(maxPv.getBetide());
			maxVo.setSumPv(maxPv.getMaxCount());
		}
		summaryVos.add(maxVo);
		summaryVos.add(betideVo);
		
		return summaryVos;
	}

	@SuppressWarnings("rawtypes")
	public String findSummaryReport(Date startDate, Date endDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countIpMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		List<Map> dateAreaCountIpList = totalityDao.findCountIpByVisitDate(startDate, endDate, siteId);
		Iterator countIpIt = dateAreaCountIpList.iterator();  
		while (countIpIt.hasNext()){
			Map map = (Map)countIpIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long countIp = (Long) map.get("countIp");
			countIpMap.put(DateTimeUtil.getDateToString(visitDate), countIp == null ? 0L : countIp);
		}
		dataSetMap.put("IP", countIpMap);
		
		Map<String, Long> countUvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		List<Map> dateAreaCountUvList = totalityDao.findCountUvByVisitDate(startDate, endDate, siteId);
		Iterator countUvIt = dateAreaCountUvList.iterator();  
		while (countUvIt.hasNext()){
			Map map = (Map)countUvIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long countUv = (Long) map.get("countUv");
			countUvMap.put(DateTimeUtil.getDateToString(visitDate), countUv == null ? 0L : countUv);
		}
		dataSetMap.put("UV", countUvMap);
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		List<Map> dateAreaSumPvList = totalityDao.findSumPvByVisitDate(startDate, endDate, siteId);
		Iterator sumPvIt = dateAreaSumPvList.iterator();  
		while (sumPvIt.hasNext()){
			Map map = (Map)sumPvIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long sumPv = (Long) map.get("sumPv");
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), sumPv == null ? 0L : sumPv);
		}
		dataSetMap.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public Map<String, Object> findSiteClickTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page){
		List<SiteClickVo> dateAreaSiteClicks = DateTimeUtil.getDateAreaSiteClickList(startDate, endDate, rows, page);
		List<SiteClickVo> siteClickVos = totalityDao.findSiteClickTable(startDate, endDate, siteId, rows, page);
		if (siteClickVos != null && !siteClickVos.isEmpty()){
			for (SiteClickVo vo : siteClickVos){
				Collections.replaceAll(dateAreaSiteClicks, new SiteClickVo(vo.getVisitDate()), vo);
			}
		}
		Long total = DateTimeUtil.days(startDate, endDate) + 1;
		return PaginationUtil.pagination(total, dateAreaSiteClicks);
	}
	
	public String findSiteClickReport(Date startDate, Date endDate, Integer labelCount, Long siteId){
		return findSummaryReport(startDate, endDate, labelCount, siteId);
	}
	
	public List<TimeDistributedVo> findTimeDistributedTable(Date startDate, Date endDate, Long siteId){
		List<TimeDistributedVo> timeAreaTimeDistributeds = DateTimeUtil.getTimeAreaTimeDistributedList();
		List<TimeDistributedVo> timeDistributedVos = totalityDao.findTimeDistributed(startDate, endDate, siteId);
		if (timeDistributedVos != null && !timeDistributedVos.isEmpty()){
			for (TimeDistributedVo vo : timeDistributedVos){
				Collections.replaceAll(timeAreaTimeDistributeds, new TimeDistributedVo(vo.getHour()), vo);
			}
		}
		return timeAreaTimeDistributeds;
	}
	
	@SuppressWarnings("rawtypes")
	public String findTimeDistributedReport(Date startDate, Date endDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countIpMap = DateTimeUtil.getTimeAreaMap();
		List<Map> dateAreaCountIpList = totalityDao.findCountIpByVisitTime(startDate, endDate, siteId);
		Iterator countIpIt = dateAreaCountIpList.iterator();  
		while (countIpIt.hasNext()){
			Map map = (Map)countIpIt.next();
			Integer hour = (Integer) map.get("timeDate");
			Long countIp = (Long) map.get("countIp");
			countIpMap.put(DateTimeUtil.formatTimeArea(hour), countIp == null ? 0L : countIp);
		}
		dataSetMap.put("IP", countIpMap);
		
		Map<String, Long> countUvMap = DateTimeUtil.getTimeAreaMap();
		List<Map> dateAreaCountUvList = totalityDao.findCountIpByVisitTime(startDate, endDate, siteId);
		Iterator countUvIt = dateAreaCountUvList.iterator();
		while (countUvIt.hasNext()){
			Map map = (Map) countUvIt.next();
			Integer hour = (Integer) map.get("timeDate");
			Long countUv = (Long) map.get("countUv");
			countUvMap.put(DateTimeUtil.formatTimeArea(hour), countUv == null ? 0L : countUv);
		}
		dataSetMap.put("UV", countUvMap);
		
		Map<String, Long> sumPvMap = DateTimeUtil.getTimeAreaMap();
		List<Map> dateAreaSumPvList = totalityDao.findSumPvByVisitTime(startDate, endDate, siteId);
		Iterator sumPvIt = dateAreaSumPvList.iterator();
		while (sumPvIt.hasNext()){
			Map map = (Map) sumPvIt.next();
			Integer hour = (Integer) map.get("timeDate");
			Long sumPv = (Long) map.get("sumPv");
			sumPvMap.put(DateTimeUtil.formatTimeArea(hour), sumPv == null ? 0L : sumPv);
		}
		dataSetMap.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getTimeAreaList();
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public Map<String, Object> searchVisitRecord(QueryParameter params, Long siteId){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_siteId", siteId);
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("visitDate",  Direction.DESC);
		sorts.put("visitTime", Direction.DESC);
		params.setSorts(sorts);

		return SearchMain.search(params, "IN_id", Long.class, totalityDao, Visit.class);
	}
	
	public Map<String, Object> findEntranceTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page){
		Long total = totalityDao.findEntranceCount(startDate, endDate, siteId);
		List<EntranceVo> entranceVos = totalityDao.findEntrance(startDate, endDate, siteId, rows, page);
		return PaginationUtil.pagination(total, entranceVos);
	}
	
	@SuppressWarnings("rawtypes")
	public String findEntranceTrendReport(Date startDate, Date endDate, String url, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countUrlMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		
		List<Map> dateAreaCountUrlList = totalityDao.findEntranceTrend(startDate, endDate, url, siteId);
		Iterator countUrlIt = dateAreaCountUrlList.iterator();  
		while (countUrlIt.hasNext()){
			Map map = (Map)countUrlIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long countUrl = (Long) map.get("countUrl");
			countUrlMap.put(DateTimeUtil.getDateToString(visitDate), countUrl == null ? 0L : countUrl);
		}
		dataSetMap.put("URL", countUrlMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public Map<String, Object> findExitTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page){
		Long total = totalityDao.findExitCount(startDate, endDate, siteId);
		List<EntranceVo> exitVos = totalityDao.findExit(startDate, endDate, siteId, rows, page);
		return PaginationUtil.pagination(total, exitVos);
	}
	
	@SuppressWarnings("rawtypes")
	public String findExitTrendReport(Date startDate, Date endDate, String url, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countUrlMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		
		List<Map> dateAreaCountUrlList = totalityDao.findExitTrend(startDate, endDate, url, siteId);
		Iterator countUrlIt = dateAreaCountUrlList.iterator();  
		while (countUrlIt.hasNext()){
			Map map = (Map)countUrlIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long countUrl = (Long) map.get("countUrl");
			countUrlMap.put(DateTimeUtil.getDateToString(visitDate), countUrl == null ? 0L : countUrl);
		}
		dataSetMap.put("URL", countUrlMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public List<HostVo> findHostTable(Date startDate, Date endDate, Long siteId){
		return totalityDao.findHost(startDate, endDate, siteId);
	}
	
	public String findHostReport(Date startDate, Date endDate, Long siteId){
		Map<String, Long> dataSet = new TreeMap<String, Long>();
		List<HostVo> hostVos = totalityDao.findHost(startDate, endDate, siteId);
		for (HostVo vo : hostVos){
			dataSet.put(vo.getHost(), vo.getSumPv());
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	@SuppressWarnings("rawtypes")
	public String findHostTrendReport(Date startDate, Date endDate, String host, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countUrlMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		
		List<Map> dateAreaCountUrlList = totalityDao.findHostTrend(startDate, endDate, host, siteId);
		Iterator countUrlIt = dateAreaCountUrlList.iterator();  
		while (countUrlIt.hasNext()){
			Map map = (Map)countUrlIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long sumPv = (Long) map.get("sumPv");
			countUrlMap.put(DateTimeUtil.getDateToString(visitDate), sumPv == null ? 0L : sumPv);
		}
		dataSetMap.put("Host", countUrlMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}

	public List<RegionVo> findRegionCountryTable(Date startDate, Date endDate, Long siteId){
		return totalityDao.findRegionCountryTable(startDate, endDate, siteId);
	}
	
	@SuppressWarnings("rawtypes")
	public String findRegionCountryReport(Date startDate, Date endDate, Long siteId){
		Map<String, Long> dataSet = new TreeMap<String, Long>();
		List<Map> dateAreaCountUrlList = totalityDao.findRegionCountryReport(startDate, endDate, siteId);
		Iterator countUrlIt = dateAreaCountUrlList.iterator();  
		while (countUrlIt.hasNext()){
			Map map = (Map)countUrlIt.next();
			String country = (String) map.get("country");
			Long sumPv = (Long) map.get("sumPv");
			dataSet.put(country, sumPv == null ? 0L : sumPv);
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public List<RegionVo> findRegionProvinceTable(String country, Date startDate, Date endDate, Long siteId){
		return totalityDao.findRegionProvinceTable(country, startDate, endDate, siteId);
	}
	
	@SuppressWarnings("rawtypes")
	public String findRegionProvinceReport(String country, Date startDate, Date endDate, Long siteId){
		Map<String, Long> dataSet = new TreeMap<String, Long>();
		List<Map> dateAreaCountUrlList = totalityDao.findRegionProvinceReport(country, startDate, endDate, siteId);
		Iterator countUrlIt = dateAreaCountUrlList.iterator();  
		while (countUrlIt.hasNext()){
			Map map = (Map)countUrlIt.next();
			String province = (String) map.get("province");
			Long sumPv = (Long) map.get("sumPv");
			dataSet.put(province, sumPv == null ? 0L : sumPv);
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}

	public List<RegionVo> findRegionCityTable(String country, String province, Date startDate, Date endDate, Long siteId){
		return totalityDao.findRegionCityTable(country, province, startDate, endDate, siteId);
	}
	
	@SuppressWarnings("rawtypes")
	public String findRegionCityReport(String country, String province, Date startDate, Date endDate, Long siteId){
		Map<String, Long> dataSet = new TreeMap<String, Long>();
		List<Map> dateAreaCountUrlList = totalityDao.findRegionCityReport(country, province, startDate, endDate, siteId);
		Iterator countUrlIt = dateAreaCountUrlList.iterator();  
		while (countUrlIt.hasNext()){
			Map map = (Map)countUrlIt.next();
			String city = (String) map.get("city");
			Long sumPv = (Long) map.get("sumPv");
			dataSet.put(city, sumPv == null ? 0L : sumPv);
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findRegionCountryTrendReport(String country, Date startDate, Date endDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countIpMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		Map<String, Long> countUvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		
		List<RegionVo> dateAreaCountryList = totalityDao.findRegionCountryTrendReport(country, startDate, endDate, siteId);
		if (dateAreaCountryList != null && !dateAreaCountryList.isEmpty()){
			for (RegionVo vo : dateAreaCountryList){
				String visitDate = DateTimeUtil.getDateToString(vo.getVisitDate());
				
				Long countIp = vo.getCountIp();
				countIpMap.put(visitDate, countIp);
				
				Long countUv = vo.getCountUv();
				countUvMap.put(visitDate, countUv);
				
				Long sumPv = vo.getSumPv();
				sumPvMap.put(visitDate, sumPv);
			}
		}
		dataSetMap.put("IP", countIpMap);
		dataSetMap.put("UV", countUvMap);
		dataSetMap.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public String findRegionProvinceTrendReport(String country, String province, Date startDate, Date endDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countIpMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		Map<String, Long> countUvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		
		List<RegionVo> dateAreaProvinceList = totalityDao.findRegionProvinceTrendReport(country, province, startDate, endDate, siteId);
		if (dateAreaProvinceList != null && !dateAreaProvinceList.isEmpty()){
			for (RegionVo vo : dateAreaProvinceList){
				String visitDate = DateTimeUtil.getDateToString(vo.getVisitDate());
				
				Long countIp = vo.getCountIp();
				countIpMap.put(visitDate, countIp);
				
				Long countUv = vo.getCountUv();
				countUvMap.put(visitDate, countUv);
				
				Long sumPv = vo.getSumPv();
				sumPvMap.put(visitDate, sumPv);
			}
		}
		dataSetMap.put("IP", countIpMap);
		dataSetMap.put("UV", countUvMap);
		dataSetMap.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public String findRegionCityTrendReport(String country, String province, String city, Date startDate, Date endDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countIpMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		Map<String, Long> countUvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		
		List<RegionVo> dateAreaCityList = totalityDao.findRegionCityTrendReport(country, province, city, startDate, endDate, siteId);
		if (dateAreaCityList != null && !dateAreaCityList.isEmpty()){
			for (RegionVo vo : dateAreaCityList){
				String visitDate = DateTimeUtil.getDateToString(vo.getVisitDate());
				
				Long countIp = vo.getCountIp();
				countIpMap.put(visitDate, countIp);
				
				Long countUv = vo.getCountUv();
				countUvMap.put(visitDate, countUv);
				
				Long sumPv = vo.getSumPv();
				sumPvMap.put(visitDate, sumPv);
			}
		}
		dataSetMap.put("IP", countIpMap);
		dataSetMap.put("UV", countUvMap);
		dataSetMap.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}

	public List<OnlineVo> findOnlineTable(Date startDate, Date endDate, Long siteId){
		List<OnlineVo> timeAreaOnlines = DateTimeUtil.getTimeAreaOnlineList();
		List<OnlineVo> onlineVos = totalityDao.findOnline(startDate, endDate, siteId);
		if (onlineVos != null && !onlineVos.isEmpty()){
			for (OnlineVo vo : onlineVos){
				Collections.replaceAll(timeAreaOnlines, new OnlineVo(vo.getHour()), vo);
			}
		}
		return timeAreaOnlines;
	}
	
	public String findOnlineReport(Date startDate, Date endDate, Integer labelCount, Long siteId){
		List<OnlineVo> onlineVos = totalityDao.findOnline(startDate, endDate, siteId);
		
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> mapFive = DateTimeUtil.getTimeAreaMap();
		Map<String, Long> mapTen = DateTimeUtil.getTimeAreaMap();
		Map<String, Long> mapFifteen = DateTimeUtil.getTimeAreaMap();
		
		for (OnlineVo vo : onlineVos){
			String hour = DateTimeUtil.formatTimeArea(vo.getHour());
			Long countFive = vo.getCountFive();
			Long countTen = vo.getCountTen();
			Long countFifteen = vo.getCountFifteen();
			mapFive.put(hour, countFive);
			mapTen.put(hour, countTen);
			mapFifteen.put(hour, countFifteen);
		}
		dataSet.put("停留0 - 5分钟", mapFive);
		dataSet.put("停留6 - 10分钟", mapTen);
		dataSet.put("停留>10分钟", mapFifteen);
		
		List<String> dateAreaList = DateTimeUtil.getTimeAreaList();
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
}
