package com.ewcms.visit.service;

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

import com.ewcms.visit.dao.loyalty.AccessDepthDao;
import com.ewcms.visit.dao.loyalty.AccessFrequencyDao;
import com.ewcms.visit.dao.loyalty.BackRateDao;
import com.ewcms.visit.dao.loyalty.StickTimeDao;
import com.ewcms.visit.model.loyalty.AccessDepth;
import com.ewcms.visit.model.loyalty.AccessFrequency;
import com.ewcms.visit.model.loyalty.BackRate;
import com.ewcms.visit.model.loyalty.StickTime;
import com.ewcms.visit.util.ChartVisitUtil;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.util.PaginationUtil;

/**
 *
 * @author 吴智俊
 */
@Component
@Transactional(readOnly = true)
public class LoyaltyService {

	@Autowired
	private AccessDepthDao accessDepthDao;
	@Autowired
	private AccessFrequencyDao accessFrequencyDao;
	@Autowired
	private BackRateDao backRateDao;
	@Autowired
	private StickTimeDao stickTimeDao;
	
	public List<AccessDepth> findDepthTable(Date startVisitDate, Date endVisitDate, Long siteId){
		return accessDepthDao.findDepth(startVisitDate, endVisitDate, siteId);
	}
	
	public String findDepthReport(Date startVisitDate, Date endVisitDate, Long siteId){
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		
		List<AccessDepth> accessDepths = accessDepthDao.findDepth(startVisitDate, endVisitDate, siteId);
		for (AccessDepth accessDepth : accessDepths){
			if (accessDepth.getAccessDepthPk().getDepth() < 30L){
				dataSet.put(accessDepth.getAccessDepthPk().getDepth() + "页", accessDepth.getDepthCount());
			}else{
				dataSet.put(">=30页", accessDepth.getDepthCount());
			}
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findDepthTrendReport(Date startVisitDate, Date endVisitDate, Long depth, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countDepthMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<AccessDepth> accessDepths = accessDepthDao.findDepth(startVisitDate, endVisitDate, siteId, depth);
		for (AccessDepth accessDepth : accessDepths){
			Date visitDate = accessDepth.getAccessDepthPk().getVisitDate();
			Long depthCount = accessDepth.getDepthCount();
			countDepthMap.put(DateTimeUtil.getDateToString(visitDate), depthCount == null ? 0L : depthCount);
		}
		if (depth < 30L){
			dataSet.put(depth + "页", countDepthMap);
		}else{
			dataSet.put(">=30页", countDepthMap);
		}
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
	
	public List<AccessFrequency> findFrequencyTable(Date startVisitDate, Date endVisitDate, Long siteId){
		return accessFrequencyDao.findFrequency(startVisitDate, endVisitDate, siteId);
	}
	
	public String findFrequencyReport(Date startVisitDate, Date endVisitDate, Long siteId){
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		
		List<AccessFrequency> accessFrequencys = accessFrequencyDao.findFrequency(startVisitDate, endVisitDate, siteId);
		for (AccessFrequency accessFrequency : accessFrequencys){
			if (accessFrequency.getAccessFrequencyPk().getFrequency() < 30L){
				dataSet.put(accessFrequency.getAccessFrequencyPk().getFrequency() + "页", accessFrequency.getFrequencyCount());
			}else{
				dataSet.put(">=30页", accessFrequency.getFrequencyCount());
			}
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findFrequencyTrendReport(Date startVisitDate, Date endVisitDate, Long frequency, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> countDepthMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<AccessFrequency> accessFrequencys = accessFrequencyDao.findFrequency(startVisitDate, endVisitDate, siteId, frequency);
		for (AccessFrequency accessFrequency : accessFrequencys){
			Date visitDate = accessFrequency.getAccessFrequencyPk().getVisitDate();
			Long frequencyCount = accessFrequency.getFrequencyCount();
			countDepthMap.put(DateTimeUtil.getDateToString(visitDate), frequencyCount == null ? 0L : frequencyCount);
		}
		if (frequency < 30L){
			dataSet.put(frequency + "页", countDepthMap);
		}else{
			dataSet.put(">=30页", countDepthMap);
		}
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
	
	public Map<String, Object> findBackTable(Date startVisitDate, Date endVisitDate, Long siteId, QueryParameter params){
		Long total = backRateDao.countBackRate(startVisitDate, endVisitDate, siteId);
		Pageable pageable = new PageRequest(params.getPage() - 1, params.getRows());
		Page<BackRate> backRates = backRateDao.findBackRate(startVisitDate, endVisitDate, siteId, pageable);
		return PaginationUtil.pagination(total, backRates.getContent());
	}
	
	public String findBackReport(Date startVisitDate, Date endVisitDate, Integer labelCount, Long siteId){
		Map<String, Map<String, String>> dataSet = new LinkedHashMap<String, Map<String, String>>();
		
		Map<String, String> nvMap = DateTimeUtil.getDateAreaStringMap(startVisitDate, endVisitDate);
		Map<String, String> rvMap = DateTimeUtil.getDateAreaStringMap(startVisitDate, endVisitDate);
		Map<String, String> rrMap = DateTimeUtil.getDateAreaStringMap(startVisitDate, endVisitDate);
		List<BackRate> backRates = backRateDao.findBackRate(startVisitDate, endVisitDate, siteId);
		for (BackRate backRate : backRates){
			Date visitDate = backRate.getVisitDate();
			
			nvMap.put(DateTimeUtil.getDateToString(visitDate), String.valueOf(backRate.getNewVisitor()));
			rvMap.put(DateTimeUtil.getDateToString(visitDate), String.valueOf(backRate.getBackVisitor()));
			rrMap.put(DateTimeUtil.getDateToString(visitDate), String.valueOf(backRate.getRate()));
		}
		dataSet.put("新访客", nvMap);
		dataSet.put("回头客", rvMap);
		dataSet.put("回头率", rrMap);
		
		List<String> categories = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		
		return ChartVisitUtil.getMixed2DChart(categories, dataSet, "回头率", labelCount);
	}
	
	public Map<String, Object> findStickTimeTable(Date startVisitDate, Date endVisitDate, Long siteId, QueryParameter params){
		Long total = stickTimeDao.countStickTime(startVisitDate, endVisitDate, siteId);
		Pageable pageable = new PageRequest(params.getPage() - 1, params.getRows());
		Page<StickTime> stickTimes = stickTimeDao.findStickTime(startVisitDate, endVisitDate, siteId, pageable);
		return PaginationUtil.pagination(total, stickTimes.getContent());
	}
	
	public String findStickTimeReport(Date startVisitDate, Date endVisitDate, Integer labelCount, Long siteId) {
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		Map<String, Long> avgPvMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<StickTime> stickTimes = stickTimeDao.findStickTime(startVisitDate, endVisitDate, siteId);
		for (StickTime stickTime : stickTimes){
			Date visitDate = stickTime.getVisitDate();
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), stickTime.getStickTimeSum());
			avgPvMap.put(DateTimeUtil.getDateToString(visitDate), stickTime.getStickTimeAvg().longValue());
		}
		dataSet.put("会话停留", sumPvMap);
		dataSet.put("页均停留", avgPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
}
