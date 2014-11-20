package com.ewcms.visit.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.util.EmptyUtil;
import com.ewcms.visit.dao.VisitDao;
import com.ewcms.visit.dao.totality.EntryDao;
import com.ewcms.visit.dao.totality.HostDao;
import com.ewcms.visit.dao.totality.OnlineDao;
import com.ewcms.visit.dao.totality.RegionDao;
import com.ewcms.visit.dao.totality.SummaryDao;
import com.ewcms.visit.model.Visit;
import com.ewcms.visit.model.totality.Entry;
import com.ewcms.visit.model.totality.Host;
import com.ewcms.visit.model.totality.Online;
import com.ewcms.visit.model.totality.Region;
import com.ewcms.visit.model.totality.Summary;
import com.ewcms.visit.util.ChartVisitUtil;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.visit.util.VisitUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;
import com.ewcms.web.util.PaginationUtil;

/**
 * 总体情况
 * 
 * @author wu_zhijun
 *
 */
@Component
@Transactional(readOnly = true)
public class TotalityService {
	
	@Autowired
	private VisitDao visitDao;
	@Autowired
	private SummaryDao summaryDao;
	@Autowired
	private EntryDao entryDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private RegionDao regionDao;
	@Autowired
	private OnlineDao onlineDao;
	
	public Date findMinVisitDate(Long siteId){
		Date minVisitDate = summaryDao.findMinVisitDate(siteId);
		if (EmptyUtil.isNull(minVisitDate)) return DateTimeUtil.getCalendar().getTime();
		return minVisitDate;
	}
	
	public Date findMaxVisitDate(Long siteId){
		Date maxVisitDate = summaryDao.findMaxVisitDate(siteId);
		if (EmptyUtil.isNull(maxVisitDate)) return DateTimeUtil.getCalendar().getTime();
		return maxVisitDate;
	}
	
	public Integer findDays(Long siteId){
		Date minVisitDate = findMinVisitDate(siteId);
		if (EmptyUtil.isNull(minVisitDate)) return 1;
		Date current = DateTimeUtil.getCalendar().getTime();
		return (int) ((current.getTime() - minVisitDate.getTime())/(24*60*60*1000)) + 1;
	}
	
	public List<Summary> findSummaryTable(Long siteId){
		List<Summary> summarys = new ArrayList<Summary>();
		
		Summary todaySummary = summaryDao.findSumSummary(DateTimeUtil.getCurrent(),DateTimeUtil.getCurrent(), siteId);
		if (todaySummary == null) todaySummary = new Summary();
		todaySummary.setName("今天");
		summarys.add(todaySummary);
		
		Summary yesterdayNowSummary = summaryDao.findYesterdayNowSummary(DateTimeUtil.getYesterday(), DateTimeUtil.getHour(), siteId);
		if (yesterdayNowSummary == null) yesterdayNowSummary = new Summary();
		yesterdayNowSummary.setName("昨天此时");
		summarys.add(yesterdayNowSummary);
		
		Summary todayForecastSummary = summaryDao.findTodayForecastSummary(DateTimeUtil.getYesterday(), DateTimeUtil.getHour(), siteId);
		if (todayForecastSummary == null) todayForecastSummary = new Summary();
		todayForecastSummary.setName("今日预计");
		todayForecastSummary.setIpCount(todayForecastSummary.getIpCount() + todaySummary.getIpCount());
		todayForecastSummary.setUniqueIdCount(todayForecastSummary.getUniqueIdCount() + todaySummary.getUniqueIdCount());
		todayForecastSummary.setPageViewSum(todayForecastSummary.getPageViewSum() + todaySummary.getPageViewSum());
		todayForecastSummary.setRvFlagCount(todayForecastSummary.getRvFlagCount() + todaySummary.getRvFlagCount());
		todayForecastSummary.setRvFlagSum(todayForecastSummary.getRvFlagSum() + todaySummary.getRvFlagSum());
		todayForecastSummary.setStickTimeAvg(todayForecastSummary.getStickTimeAvg() + todaySummary.getStickTimeAvg());
		summarys.add(todayForecastSummary);
		
		Summary yesterdaySummary = summaryDao.findSumSummary(DateTimeUtil.getYesterday(), DateTimeUtil.getYesterday(), siteId);
		if (yesterdaySummary == null) yesterdaySummary = new Summary();
		yesterdaySummary.setName("昨日");
		summarys.add(yesterdaySummary);
		
		Summary thisWeekSummary = summaryDao.findSumSummary(DateTimeUtil.getThisWeekMonday(), DateTimeUtil.getThisWeekSunday(), siteId);
		if (thisWeekSummary == null) thisWeekSummary = new Summary();
		thisWeekSummary.setName("本周");
		summarys.add(thisWeekSummary);
		
		Summary thisMonthSummary = summaryDao.findSumSummary(DateTimeUtil.getThisMonthFirst(), DateTimeUtil.getThisMonthLast(), siteId);
		if (thisMonthSummary == null) thisMonthSummary = new Summary();
		thisMonthSummary.setName("本月");
		summarys.add(thisMonthSummary);
		
		Date startVisitDate = findMinVisitDate(siteId);
		Date endVisitDate = DateTimeUtil.getCalendar().getTime();
		Integer days = findDays(siteId);
		Summary avgSummary = summaryDao.findSumSummary(startVisitDate, endVisitDate, siteId);
		if (avgSummary == null) avgSummary = new Summary();
		avgSummary.setName("平均");
		avgSummary.setIpCount(avgSummary.getIpCount()/days);
		avgSummary.setUniqueIdCount(avgSummary.getUniqueIdCount()/days);
		avgSummary.setPageViewSum(avgSummary.getPageViewSum()/days);
		avgSummary.setRvFlagCount(avgSummary.getRvFlagCount()/days);
		avgSummary.setRvFlagSum(avgSummary.getRvFlagSum()/days);
		avgSummary.setStickTimeAvg(avgSummary.getStickTimeAvg()/days);
		summarys.add(avgSummary);
		
		Summary maxIpCount = summaryDao.findMaxIpCount(siteId);
		Summary maxUniqueIdCount = summaryDao.findMaxUniqueIdCount(siteId);
		Summary maxPageViewSum = summaryDao.findMaxPageViewSum(siteId);
		
		Summary maxSummary = new Summary();
		maxSummary.setName("最高");
		maxSummary.setIpCount(maxIpCount.getMaxCount());
		maxSummary.setUniqueIdCount(maxUniqueIdCount.getMaxCount());
		maxSummary.setPageViewSum(maxPageViewSum.getMaxCount());
		summarys.add(maxSummary);
		
		Summary betideSummary = new Summary();
		betideSummary.setName("");
		betideSummary.setIpBetide(maxIpCount.getBetide());
		betideSummary.setUniqueIdBetide(maxUniqueIdCount.getBetide());
		betideSummary.setPageViewBetide(maxPageViewSum.getBetide());
		summarys.add(betideSummary);
		
		return summarys;
	}
	
	public String findSummaryReport(Date startVisitDate, Date endVisitDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> ipDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		Map<String, Long> uniqueIdDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		Map<String, Long> pageViewDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<Summary> summarys = summaryDao.findVisitDateSummary(startVisitDate, endVisitDate, siteId);
		for (Summary summary : summarys){
			Date visitDate = summary.getSummaryPk().getVisitDate();
			String visitDateStr = DateTimeUtil.getDateToString(visitDate);
			
			Long ipCount = summary.getIpCount();
			ipDateAreaMap.put(visitDateStr, ipCount == null ? 0L : ipCount);
			
			Long uniqueIdCount = summary.getUniqueIdCount();
			uniqueIdDateAreaMap.put(visitDateStr, uniqueIdCount == null ? 0L : uniqueIdCount);
			
			Long pageViewSum = summary.getPageViewSum();
			pageViewDateAreaMap.put(visitDateStr, pageViewSum == null ? 0L : pageViewSum);
		}
		
		dataSetMap.put("IP", ipDateAreaMap);
		dataSetMap.put("UV", uniqueIdDateAreaMap);
		dataSetMap.put("PV", pageViewDateAreaMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public Map<String, Object> findSiteClickTable(Date startVisitDate, Date endVisitDate, Long siteId, QueryParameter params){
		List<Summary> dateAreaSiteClicks = DateTimeUtil.getDateAreaSiteClickList(startVisitDate, endVisitDate, params.getRows(), params.getPage());
		
		Pageable pageable = new PageRequest(params.getPage() - 1, params.getRows());
		Page<Summary> summarys = summaryDao.findVisitDateSummary(startVisitDate, endVisitDate, siteId, pageable);
		if (summarys != null && !summarys.getContent().isEmpty()){
			List<Summary> list = summarys.getContent();
			for (Summary summary : list){
				Collections.replaceAll(dateAreaSiteClicks, new Summary(summary.getSummaryPk().getVisitDate()), summary);
			}
		}
		Long total = DateTimeUtil.days(startVisitDate, endVisitDate) + 1;
		return PaginationUtil.pagination(total, dateAreaSiteClicks);
	}
	
	public String findSiteClickReport(Date startDate, Date endDate, Integer labelCount, Long siteId){
		return findSummaryReport(startDate, endDate, labelCount, siteId);
	}
	
	public List<Summary> findTimeDistributedTable(Date startDate, Date endDate, Long siteId){
		List<Summary> timeAreaTimeDistributeds = DateTimeUtil.getTimeAreaTimeDistributedList();
		List<Summary> summarys = summaryDao.findHourSummary(startDate, endDate, siteId);
		if (summarys != null && !summarys.isEmpty()){
			for (Summary summary : summarys){
				Collections.replaceAll(timeAreaTimeDistributeds, new Summary(summary.getSummaryPk().getHour()), summary);
			}
		}
		return timeAreaTimeDistributeds;
	}
	
	public String findTimeDistributedReport(Date startVisitDate, Date endVisitDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> ipDateAreaMap = DateTimeUtil.getTimeAreaMap();
		Map<String, Long> uniqueIdDateAreaMap = DateTimeUtil.getTimeAreaMap();
		Map<String, Long> pageViewDateAreaMap = DateTimeUtil.getTimeAreaMap();
		List<Summary> summarys = summaryDao.findHourSummary(startVisitDate, endVisitDate, siteId);
		for (Summary summary : summarys){
			Integer hour = summary.getSummaryPk().getHour();
			String hourStr = DateTimeUtil.formatTimeArea(hour);
			
			Long ipCount = summary.getIpCount();
			ipDateAreaMap.put(hourStr, ipCount == null ? 0L : ipCount);
			
			Long uniqueIdCount = summary.getUniqueIdCount();
			uniqueIdDateAreaMap.put(hourStr, uniqueIdCount == null ? 0L : uniqueIdCount);
			
			Long pageViewSum = summary.getPageViewSum();
			pageViewDateAreaMap.put(hourStr, pageViewSum == null ? 0L : pageViewSum);
		}
		
		dataSetMap.put("IP", ipDateAreaMap);
		dataSetMap.put("UV", uniqueIdDateAreaMap);
		dataSetMap.put("PV", pageViewDateAreaMap);
		
		List<String> dateAreaList = DateTimeUtil.getTimeAreaList();
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public Map<String, Object> searchVisitRecord(QueryParameter params, Long siteId){
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("visitPk.visitDate",  Direction.DESC);
		sorts.put("visitTime", Direction.DESC);
		params.setSorts(sorts);

		return SearchMain.search(params, visitDao, Visit.class);
	}
	
	public Map<String, Object> findEntryTable(Date startVisitDate, Date endVisitDate, Long siteId, QueryParameter params, String type){
		String event = "";
		if (type.equals("exit")){
			event = VisitUtil.UNLOAD_EVENT;
		}
		Long total = entryDao.countEntry(startVisitDate, endVisitDate, siteId, event);
		Pageable pageable = new PageRequest(params.getPage() - 1, params.getRows());
		Page<Entry> entries = entryDao.findEntry(startVisitDate, endVisitDate, siteId, event, pageable);
		return PaginationUtil.pagination(total, entries.getContent());
	}
	
	public String findEntryTrendReport(Date startVisitDate, Date endVisitDate, String url, Integer labelCount, Long siteId, String type){
		String event = "";
		if (type.equals("exit")){
			event = VisitUtil.UNLOAD_EVENT;
		}

		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countUrlMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<Entry> entries = entryDao.findEntry(startVisitDate, endVisitDate, siteId, event, url);
		for (Entry entry : entries){
			Date visitDate = entry.getEntryPk().getVisitDate();
			Long entryCount = entry.getEntryCount();
			countUrlMap.put(DateTimeUtil.getDateToString(visitDate), entryCount == null ? 0L : entryCount);
		}
		dataSetMap.put("URL", countUrlMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);	
	}
	
	public List<Host> findHostTable(Date startVisitDate, Date endVisitDate, Long siteId){
		return hostDao.findHostName(startVisitDate, endVisitDate, siteId);
	}
	
	public String findHostReport(Date startVisitDate, Date endVisitDate, Long siteId){
		Map<String, Long> dataSet = new TreeMap<String, Long>();
		List<Host> hosts = hostDao.findHostName(startVisitDate, endVisitDate, siteId);
		for (Host host : hosts){
			dataSet.put(host.getHostPk().getHostName(), host.getHostCount());
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findHostTrendReport(Date startVisitDate, Date endVisitDate, String host, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countHostMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		
		List<Host> hosts = hostDao.findHostVisitDate(startVisitDate, endVisitDate, siteId, host);
		for (Host entity : hosts){
			Date visitDate = entity.getHostPk().getVisitDate();
			Long hostCount = entity.getHostCount();
			countHostMap.put(DateTimeUtil.getDateToString(visitDate), hostCount == null ? 0L : hostCount);
		}
		dataSetMap.put("Host", countHostMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public List<Region> findRegionCountryTable(Date startDate, Date endDate, Long siteId){
		return regionDao.findRegionCountry(startDate, endDate, siteId);
	}
	
	public String findRegionCountryReport(Date startDate, Date endDate, Long siteId){
		Map<String, Long> dataSet = new TreeMap<String, Long>();
		List<Region> regions = regionDao.findRegionCountry(startDate, endDate, siteId);
		for (Region region : regions){
			String country = region.getRegionPk().getCountry();
			Long pageViewSum = region.getPageViewSum();
			dataSet.put(country, pageViewSum == null ? 0L : pageViewSum);
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findRegionCountryTrendReport(String country, Date startVisitDate, Date endVisitDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> ipDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		Map<String, Long> uniqueIdDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		Map<String, Long> pageViewDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		
		List<Region> regions = regionDao.findVisitDateRegionCountry(startVisitDate, endVisitDate, siteId, country);
		for (Region region : regions){
			String visitDate = DateTimeUtil.getDateToString(region.getRegionPk().getVisitDate());
			
			Long ipCount = region.getIpCount();
			ipDateAreaMap.put(visitDate, ipCount == null ? 0L : ipCount);
			
			Long uniqueIdCount = region.getUniqueIdCount();
			uniqueIdDateAreaMap.put(visitDate, uniqueIdCount == null ? 0L : uniqueIdCount);
			
			Long pageViewSum = region.getPageViewSum();
			pageViewDateAreaMap.put(visitDate, pageViewSum == null ? 0L : pageViewSum);
		}
		
		dataSetMap.put("IP", ipDateAreaMap);
		dataSetMap.put("UV", uniqueIdDateAreaMap);
		dataSetMap.put("PV", pageViewDateAreaMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public List<Region> findRegionProvinceTable(String country, Date startVisitDate, Date endVisitDate, Long siteId){
		return regionDao.findRegionProvince(startVisitDate, endVisitDate, siteId, country);
	}
	
	public String findRegionProvinceReport(String country, Date startVisitDate, Date endVisitDate, Long siteId){
		Map<String, Long> dataSet = new TreeMap<String, Long>();
		List<Region> regions = regionDao.findRegionProvince(startVisitDate, endVisitDate, siteId, country);
		for (Region region : regions){
			String province = region.getRegionPk().getProvince();
			Long pageViewSum = region.getPageViewSum();
			dataSet.put(province, pageViewSum == null ? 0L : pageViewSum);
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findRegionProvinceTrendReport(String country, String province, Date startVisitDate, Date endVisitDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> ipDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		Map<String, Long> uniqueIdDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		Map<String, Long> pageViewDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		
		List<Region> regions = regionDao.findVisitDateRegionProvince(startVisitDate, endVisitDate, siteId, country, province);
		for (Region region : regions){
			String visitDate = DateTimeUtil.getDateToString(region.getRegionPk().getVisitDate());
			
			Long ipCount = region.getIpCount();
			ipDateAreaMap.put(visitDate, ipCount == null ? 0L : ipCount);
			
			Long uniqueIdCount = region.getUniqueIdCount();
			uniqueIdDateAreaMap.put(visitDate, uniqueIdCount == null ? 0L : uniqueIdCount);
			
			Long pageViewSum = region.getPageViewSum();
			pageViewDateAreaMap.put(visitDate, pageViewSum == null ? 0L : pageViewSum);
		}
		
		dataSetMap.put("IP", ipDateAreaMap);
		dataSetMap.put("UV", uniqueIdDateAreaMap);
		dataSetMap.put("PV", pageViewDateAreaMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public List<Region> findRegionCityTable(String country, String province, Date startVisitDate, Date endVisitDate, Long siteId){
		return regionDao.findRegionCity(startVisitDate, endVisitDate, siteId, country, province);
	}
	
	public String findRegionCityReport(String country, String province, Date startVisitDate, Date endVisitDate, Long siteId){
		Map<String, Long> dataSet = new TreeMap<String, Long>();
		List<Region> regions = regionDao.findRegionCity(startVisitDate, endVisitDate, siteId, country, province);
		for (Region region : regions){
			String city = region.getRegionPk().getCity();
			Long pageViewSum = region.getPageViewSum();
			dataSet.put(city, pageViewSum == null ? 0L : pageViewSum);
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findRegionCityTrendReport(String country, String province, String city, Date startVisitDate, Date endVisitDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSetMap = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> ipDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		Map<String, Long> uniqueIdDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		Map<String, Long> pageViewDateAreaMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		
		List<Region> regions = regionDao.findVisitDateRegionCity(startVisitDate, endVisitDate, siteId, country, province, city);
		for (Region region : regions){
			String visitDate = DateTimeUtil.getDateToString(region.getRegionPk().getVisitDate());
			
			Long ipCount = region.getIpCount();
			ipDateAreaMap.put(visitDate, ipCount == null ? 0L : ipCount);
			
			Long uniqueIdCount = region.getUniqueIdCount();
			uniqueIdDateAreaMap.put(visitDate, uniqueIdCount == null ? 0L : uniqueIdCount);
			
			Long pageViewSum = region.getPageViewSum();
			pageViewDateAreaMap.put(visitDate, pageViewSum == null ? 0L : pageViewSum);
		}
		
		dataSetMap.put("IP", ipDateAreaMap);
		dataSetMap.put("UV", uniqueIdDateAreaMap);
		dataSetMap.put("PV", pageViewDateAreaMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSetMap, labelCount);
	}
	
	public List<Online> findOnlineTable(Date startDate, Date endDate, Long siteId){
		List<Online> timeAreaOnlines = DateTimeUtil.getTimeAreaOnlineList();
		List<Online> onlines = onlineDao.findOnline(startDate, endDate, siteId);
		if (onlines != null && !onlines.isEmpty()){
			for (Online online : onlines){
				Collections.replaceAll(timeAreaOnlines, new Online(online.getOnlinePk().getHour()), online);
			}
		}
		return timeAreaOnlines;
	}
	
	public String findOnlineReport(Date startDate, Date endDate, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> mapFive = DateTimeUtil.getTimeAreaMap();
		Map<String, Long> mapTen = DateTimeUtil.getTimeAreaMap();
		Map<String, Long> mapFifteen = DateTimeUtil.getTimeAreaMap();
		
		List<Online> onlines = onlineDao.findOnline(startDate, endDate, siteId);
		for (Online online : onlines){
			String hour = DateTimeUtil.formatTimeArea(online.getOnlinePk().getHour());
			Long fiveCount = online.getFiveCount();
			Long tenCount = online.getTenCount();
			Long fifteenCount = online.getFifteenCount();
			mapFive.put(hour, fiveCount);
			mapTen.put(hour, tenCount);
			mapFifteen.put(hour, fifteenCount);
		}
		dataSet.put("停留0 - 5分钟", mapFive);
		dataSet.put("停留6 - 10分钟", mapTen);
		dataSet.put("停留>10分钟", mapFifteen);
		
		List<String> dateAreaList = DateTimeUtil.getTimeAreaList();
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
}
