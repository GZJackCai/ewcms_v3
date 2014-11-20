package com.ewcms.visit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.visit.dao.clickrate.SearchEngineDao;
import com.ewcms.visit.dao.clickrate.SourceFormDao;
import com.ewcms.visit.dao.clickrate.WebSiteDao;
import com.ewcms.visit.model.clickrate.SearchEngine;
import com.ewcms.visit.model.clickrate.SourceForm;
import com.ewcms.visit.model.clickrate.WebSite;
import com.ewcms.visit.util.ChartVisitUtil;
import com.ewcms.visit.util.DateTimeUtil;

/**
 *
 * @author 吴智俊
 */
@Component
@Transactional(readOnly = true)
public class ClickRateService {
	
	@Autowired
	private SourceFormDao sourceFormDao;
	@Autowired
	private SearchEngineDao searchEngineDao;
	@Autowired
	private WebSiteDao webSiteDao;
	
	public List<SourceForm> findSourceFormTable(Date startVisitDate, Date endVisitDate, Long siteId) {
		SourceForm sumSourceForm = sourceFormDao.sumSourceForm(startVisitDate, endVisitDate, siteId);
		List<SourceForm> sourceForms = sourceFormDao.findSourceForm(startVisitDate, endVisitDate, siteId);
		
		List<SourceForm> list = new ArrayList<SourceForm>();
		list.add(sumSourceForm);
		list.addAll(sourceForms);
		
		return list;
	}
	
	public String findSourceFormReport(Date startVisitDate, Date endVisitDate, Long siteId){
		SourceForm sumSourceForm = sourceFormDao.sumSourceForm(startVisitDate, endVisitDate, siteId);
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		dataSet.put("搜索引擎", sumSourceForm.getSearchCount());
		dataSet.put("直接输入", sumSourceForm.getDirectCount());
		dataSet.put("其他网站", sumSourceForm.getOtherCount());

		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public List<SearchEngine> findSearchEngineTable(Date startVisitDate, Date endVisitDate, Long siteId){
		return searchEngineDao.findSearchEngine(startVisitDate, endVisitDate, siteId);
	}
	
	public String findSearchEngineReport(Date startVisitDate, Date endVisitDate, Long siteId){
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		List<SearchEngine> searchEngines = searchEngineDao.findSearchEngine(startVisitDate, endVisitDate, siteId);
		for (SearchEngine searchEngine : searchEngines){
			dataSet.put(searchEngine.getSearchEnginePk().getEngineValue() + "(" + searchEngine.getSearchEnginePk().getEngineName() + ")", searchEngine.getEngineCount());
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findSearchEngineTrendReport(Date startVisitDate, Date endVisitDate, String engineName, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<SearchEngine> searchEngines = searchEngineDao.findSearchEngine(startVisitDate, endVisitDate, siteId, engineName);
		for (SearchEngine searchEngine : searchEngines){
			Date visitDate = searchEngine.getSearchEnginePk().getVisitDate();
			Long engineCount = searchEngine.getEngineCount();
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), engineCount == null ? 0L : engineCount);
		}
		dataSet.put("UV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
	
	public List<WebSite> findWebSiteTable(Date startVisitDate, Date endVisitDate, Long siteId){
		return webSiteDao.findWebSite(startVisitDate, endVisitDate, siteId);
	}
	
	public String findWebSiteReport(Date startVisitDate, Date endVisitDate, Long siteId){
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		List<WebSite> webSites = webSiteDao.findWebSite(startVisitDate, endVisitDate, siteId);
		for (WebSite webSite : webSites){
			dataSet.put(webSite.getWebSitePk().getWebSiteName(), webSite.getWebSiteCount());
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findWebSiteTrendReport(Date startVisitDate, Date endVisitDate, String WebSiteName, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<WebSite> webSites = webSiteDao.findWebSite(startVisitDate, endVisitDate, siteId, WebSiteName);
		for (WebSite webSite : webSites){
			Date visitDate = webSite.getWebSitePk().getVisitDate();
			Long engineCount = webSite.getWebSiteCount();
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), engineCount == null ? 0L : engineCount);
		}
		dataSet.put("UV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
}
