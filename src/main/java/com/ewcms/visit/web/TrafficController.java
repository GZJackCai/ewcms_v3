package com.ewcms.visit.web;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.visit.model.traffic.ChannelClick;
import com.ewcms.visit.service.TrafficService;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.web.QueryParameter;

/**
 * 访问量排行
 * 
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/traffic")
public class TrafficController {

	@Autowired
	private TrafficService trafficService;

	@RequestMapping(value = "/channel/index")
	public String channelIndex() {
		return "visit/traffic/channel";
	}

	@RequestMapping(value = "/channel/table")
	public @ResponseBody List<ChannelClick> findChannelClickTable(
			@RequestParam(value = "parentChannelId", defaultValue = "-1") Long channelId,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return trafficService.findChannelTable(startDate, endDate, channelId, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/channel/report")
	public @ResponseBody String findChannelClickReport(
			@RequestParam(value = "parentChannelId", defaultValue = "-1") Long channelId,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return trafficService.findChannelReport(startDate, endDate,	channelId, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/channel/trend/index/{channelId}")
	public String channelTrendIndex(
			@PathVariable(value = "channelId") Long channelId,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("channelId", channelId);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/traffic/channeltrend";

	}

	@RequestMapping(value = "/channel/trend/report")
	public @ResponseBody String findChannelClickTrendReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "channelId") Long channelId,
			@RequestParam(value = "labelCount") Integer labelCount) {
		try {
			return trafficService.findChannelTrendReport(startDate, endDate, channelId, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/article/index")
	public String articleIndex() {
		return "visit/traffic/article";
	}

	@RequestMapping(value = "/article/table")
	public @ResponseBody Map<String, Object> findArticleClickTable(
			@ModelAttribute QueryParameter params,
			@RequestParam(value = "parentChannelId", defaultValue = "-1") Long parentChannelId,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return trafficService.findArticleClickTable(startDate, endDate,	parentChannelId, EwcmsContextUtil.getCurrentSiteId(), params);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/url/index")
	public String urlIndex() {
		return "visit/traffic/url";
	}

	@RequestMapping(value = "/url/table")
	public @ResponseBody Map<String, Object> findUrlClickTable(
			@ModelAttribute QueryParameter params,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return trafficService.findUrlClickTable(startDate, endDate, EwcmsContextUtil.getCurrentSiteId(),
					params);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/url/trend/index")
	public String urlTrendIndex(
			@RequestParam(value = "url") String url,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("url", url);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/traffic/urltrend";
	}

	@RequestMapping(value = "/url/trend/report")
	public @ResponseBody String findUrlTrendReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "url") String url,
			@RequestParam(value = "labelCount") Integer labelCount) {
		try {
			return trafficService.findUrlClickTrendReport(startDate, endDate, url, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}
}
