package com.ewcms.visit.web;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.visit.service.TrafficService;
import com.ewcms.visit.vo.traffic.ChannelClickVo;

/**
 * 访问量排行
 * 
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/traffic")
public class TrafficController {
	
	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private TrafficService trafficService;
	
	@RequestMapping(value = "/channel/index")
	public String channelIndex(){
		return "visit/traffic/channel";
	}
	
	@RequestMapping(value = "/channel/table")
	public @ResponseBody List<ChannelClickVo> findChannelClickTable(@RequestParam(value = "parentChannelId", defaultValue = "-1") Long parentChannelId, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			return trafficService.findChannelTable(DF.parse(startDate), DF.parse(endDate), parentChannelId, EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/channel/report")
	public @ResponseBody String findChannelClickReport(@RequestParam(value = "parentChannelId", defaultValue = "-1") Long parentChannelId, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			return trafficService.findChannelReport(DF.parse(startDate), DF.parse(endDate), parentChannelId, EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/channel/trend/index/{channelId}")
	public String channelTrendIndex(@PathVariable(value = "channelId") Long channelId, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		model.addAttribute("channelId", channelId);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/traffic/channeltrend";

	}
	
	@RequestMapping(value = "/channel/trend/report")
	public @ResponseBody String findChannelClickTrendReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "channelId") Long channelId, @RequestParam(value = "labelCount") Integer labelCount){
		try{
			return trafficService.findChannelTrendReport(DF.parse(startDate), DF.parse(endDate), channelId, labelCount, EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/article/index")
	public String articleIndex(){
		return "visit/traffic/article";
	}
	
	@RequestMapping(value = "/article/table")
	public @ResponseBody Map<String, Object> findArticleClickTable(HttpServletRequest request, @RequestParam(value = "parentChannelId", defaultValue = "-1") Long parentChannelId, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			return trafficService.findArticleClickTable(DF.parse(startDate), DF.parse(endDate), parentChannelId, EwcmsContextUtil.getCurrentSiteId(), rows, page);
		}catch(Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/url/index")
	public String urlIndex(){
		return "visit/traffic/url";
	}
	
	@RequestMapping(value = "/url/table")
	public @ResponseBody Map<String, Object> findUrlClickTable(HttpServletRequest request, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			return trafficService.findUrlClickTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId(), rows, page);
		}catch(Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/url/trend/index/{url}")
	public String urlTrendIndex(@PathVariable(value = "url") String url, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		model.addAttribute("url", url);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/traffic/urltrend";
	}
	
	@RequestMapping(value = "/url/trend/report")
	public @ResponseBody String findUrlTrendReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "url") String url, @RequestParam(value = "labelCount") Integer labelCount){
		try{
			return trafficService.findUrlClickTrendReport(DF.parse(startDate), DF.parse(endDate), url, labelCount, EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return "";
		}
	}
}
