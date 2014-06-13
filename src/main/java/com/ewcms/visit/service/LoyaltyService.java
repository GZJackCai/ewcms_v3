package com.ewcms.visit.service;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.visit.dao.LoyaltyDao;
import com.ewcms.visit.util.ChartVisitUtil;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.visit.util.NumberUtil;
import com.ewcms.visit.vo.loyalty.BackVo;
import com.ewcms.visit.vo.loyalty.DepthVo;
import com.ewcms.visit.vo.loyalty.FrequencyVo;
import com.ewcms.visit.vo.loyalty.StickTimeVo;

/**
 * 忠城度分析
 * 
 * @author 吴智俊
 */
@Component
public class LoyaltyService {

	@Autowired
	private LoyaltyDao loyaltyDao;
	
	public List<DepthVo> findDepthTable(Date startDate, Date endDate, Long siteId){
		return loyaltyDao.findDepthTable(startDate, endDate, siteId);
	}
	
	public String findDepthReport(Date startDate, Date endDate, Long siteId){
		List<DepthVo> depthVos = findDepthTable(startDate, endDate, siteId);
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		for (DepthVo vo : depthVos){
			if (vo.getDepth() < 30L){
				dataSet.put(vo.getDepth() + "页", vo.getCountDepth());
			}else{
				dataSet.put(">=30页", vo.getCountDepth());
			}
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	@SuppressWarnings("rawtypes")
	public String findDepthTrendReport(Date startDate, Date endDate, Long depth, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countDepthMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		List<Map> dateAreaCountDepthList = loyaltyDao.findDepthTrendReport(startDate, endDate, depth, siteId);
		Iterator countDepthIt = dateAreaCountDepthList.iterator();  
		while (countDepthIt.hasNext()){
			Map map = (Map)countDepthIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long countDepth = (Long) map.get("countDepth");
			countDepthMap.put(DateTimeUtil.getDateToString(visitDate), countDepth == null ? 0L : countDepth);
		}
		if (depth < 30L){
			dataSet.put(depth + "页", countDepthMap);
		}else{
			dataSet.put(">=30页", countDepthMap);
		}
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
	
	public List<FrequencyVo> findFrequencyTable(Date startDate, Date endDate, Long siteId){
		return loyaltyDao.findFrequencyTable(startDate, endDate, siteId);
	}
	
	public String findFrequencyReport(Date startDate, Date endDate, Long siteId){
		List<FrequencyVo> frequencyVos = findFrequencyTable(startDate, endDate, siteId);
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		for (FrequencyVo vo : frequencyVos){
			if (vo.getFrequency() < 30L){
				dataSet.put(vo.getFrequency() + "次", vo.getCountFrequency());
			}else{
				dataSet.put(">=30次", vo.getCountFrequency());
			}
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	@SuppressWarnings("rawtypes")
	public String findFrequencyTrendReport(Date startDate, Date endDate, Long frequency, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countFrequencyMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		List<Map> dateAreaCountFrequencyList = loyaltyDao.findFrequencyTrendReport(startDate, endDate, frequency, siteId);
		Iterator countFrequencyIt = dateAreaCountFrequencyList.iterator();  
		while (countFrequencyIt.hasNext()){
			Map map = (Map)countFrequencyIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long countFrequency = (Long) map.get("countFrequency");
			countFrequencyMap.put(DateTimeUtil.getDateToString(visitDate), countFrequency == null ? 0L : countFrequency);
		}
		if (frequency < 30L){
			dataSet.put(frequency + "次", countFrequencyMap);
		}else{
			dataSet.put(">=30次", countFrequencyMap);
		}
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
	
	public List<BackVo> findBackTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page){
		return loyaltyDao.findBackTable(startDate, endDate, siteId, rows, page);
	}
	
	@SuppressWarnings("rawtypes")
	public String findBackReport(Date startDate, Date endDate, Integer labelCount, Long siteId){
		Map<String, Map<String, String>> dataSet = new LinkedHashMap<String, Map<String, String>>();
		
		Map<String, String> nvMap = DateTimeUtil.getDateAreaStringMap(startDate, endDate);
		Map<String, String> rvMap = DateTimeUtil.getDateAreaStringMap(startDate, endDate);
		Map<String, String> rrMap = DateTimeUtil.getDateAreaStringMap(startDate, endDate);
		List<Map> dateAreaBackList = loyaltyDao.findBackReport(startDate, endDate, siteId);
		Iterator backIt = dateAreaBackList.iterator();  
		while (backIt.hasNext()){
			Map map = (Map)backIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long newVisitor = (Long) map.get("newVisitor");
			Long backVisitor = (Long) map.get("backVisitor");
			Double rate = 0D;
			if (newVisitor + backVisitor > 0) rate = new Double(NumberUtil.round(backVisitor * 100.0D / (newVisitor + backVisitor), 2));
			nvMap.put(DateTimeUtil.getDateToString(visitDate), String.valueOf((newVisitor == null ? 0L : newVisitor)));
			rvMap.put(DateTimeUtil.getDateToString(visitDate), String.valueOf((backVisitor == null ? 0L : backVisitor)));
			rrMap.put(DateTimeUtil.getDateToString(visitDate), String.valueOf(rate));
		}
		dataSet.put("新访客", nvMap);
		dataSet.put("回头客", rvMap);
		dataSet.put("回头率", rrMap);
		
		List<String> categories = DateTimeUtil.getDateAreaList(startDate, endDate);
		
		return ChartVisitUtil.getMixed2DChart(categories, dataSet, "回头率", labelCount);
	}
	
	public List<StickTimeVo> findStickTimeTable(Date startDate, Date endDate, Long siteId, Integer rows, Integer page){
		return loyaltyDao.findStickTimeTable(startDate, endDate, siteId, rows, page);
	}
	
	@SuppressWarnings("rawtypes")
	public String findStickTimeReport(Date startDate, Date endDate, Integer labelCount, Long siteId) {
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		Map<String, Long> avgPvMap = DateTimeUtil.getDateAreaLongMap(startDate, endDate);
		List<Map> dateAreaPvList = loyaltyDao.findStickTimeReport(startDate, endDate, siteId);
		Iterator sumPvIt = dateAreaPvList.iterator();  
		while (sumPvIt.hasNext()){
			Map map = (Map)sumPvIt.next();
			Date visitDate = (Date) map.get("visitDate");
			Long sumPv = (Long) map.get("sumSt");
			Double avgPv = (Double) map.get("avgSt");
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), sumPv == null ? 0L : sumPv);
			avgPvMap.put(DateTimeUtil.getDateToString(visitDate), avgPv == null ? 0L : avgPv.longValue());
		}
		dataSet.put("会话停留", sumPvMap);
		dataSet.put("页均停留", avgPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startDate, endDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
}
