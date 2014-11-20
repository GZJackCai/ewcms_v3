package com.ewcms.visit.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.visit.model.clickrate.SearchEngine;
import com.ewcms.visit.model.clickrate.SourceForm;
import com.ewcms.visit.model.clickrate.WebSite;
import com.ewcms.visit.service.ClickRateService;
import com.ewcms.visit.util.DateTimeUtil;

/**
 * 点击量来源
 *
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/clickrate")
public class ClickRateController {

	@Autowired
	private ClickRateService clickRateService;

	@RequestMapping(value = "/sourceform/index")
	public String sourceFormIndex() {
		return "visit/clickrate/sourceform";
	}

	@RequestMapping(value = "/sourceform/table")
	public @ResponseBody List<SourceForm> findSourceFormTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return clickRateService.findSourceFormTable(startDate, endDate,	EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/sourceform/report")
	public @ResponseBody String findSourceFormReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return clickRateService.findSourceFormReport(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/searchengine/index")
	public String searchEngineIndex() {
		return "visit/clickrate/searchengine";
	}

	@RequestMapping(value = "/searchengine/table")
	public @ResponseBody List<SearchEngine> findSearchEngineTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return clickRateService.findSearchEngineTable(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/searchengine/report")
	public @ResponseBody String findSearchEngineReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return clickRateService.findSearchEngineReport(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/searchengine/trend/index")
	public String searchEngineTrendIndex(
			@RequestParam(value = "engineName") String engineName,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("engineName", engineName);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/clickrate/searchenginetrend";
	}

	@RequestMapping(value = "/searchengine/trend/report")
	public @ResponseBody String findSearchEngineTrendReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "engineName") String engineName,
			@RequestParam(value = "labelCount") Integer labelCount) {
		try {
			return clickRateService.findSearchEngineTrendReport(startDate,
					endDate, engineName, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/website/index")
	public String webSiteIndex() {
		return "visit/clickrate/website";
	}

	@RequestMapping(value = "/website/table")
	public @ResponseBody List<WebSite> findWebSiteTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return clickRateService
					.findWebSiteTable(startDate, endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/website/report")
	public @ResponseBody String findWebSiteReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return clickRateService.findWebSiteReport(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/website/trend/index")
	public String webSiteTrendIndex(
			@RequestParam(value = "webSiteName") String webSiteName,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("webSiteName", webSiteName);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/website/websitetrend";
	}

	@RequestMapping(value = "/website/trend/report")
	public @ResponseBody String findWebSiteTrendReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "engineName") String engineName,
			@RequestParam(value = "labelCount") Integer labelCount) {
		try {
			return clickRateService.findWebSiteTrendReport(startDate, endDate,
					engineName, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}
}
