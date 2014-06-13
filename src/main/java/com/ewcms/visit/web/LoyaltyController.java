package com.ewcms.visit.web;

import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.visit.service.LoyaltyService;
import com.ewcms.visit.vo.loyalty.BackVo;
import com.ewcms.visit.vo.loyalty.DepthVo;
import com.ewcms.visit.vo.loyalty.FrequencyVo;
import com.ewcms.visit.vo.loyalty.StickTimeVo;

/**
 * 忠诚度分析
 * 
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/loyalty")
public class LoyaltyController {

	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private LoyaltyService loyaltyService;
	
	@RequestMapping(value = "/depth/index")
	public String depthIndex(){
		return "visit/loyalty/depth";
	}
	
	@RequestMapping(value = "/depth/table")
	public @ResponseBody List<DepthVo> findDepthTable(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			return loyaltyService.findDepthTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/depth/report")
	public @ResponseBody String findDepthReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			return loyaltyService.findDepthReport(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/depth/trend/index/{depth}")
	public String depthTrendIndex(@PathVariable(value = "depth")Long depth, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		model.addAttribute("depth", depth);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/loyalty/depthtrend";
	}
	
	@RequestMapping(value = "/depth/trend/report")
	public @ResponseBody String findDepthTrendReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "depth") Long depth, @RequestParam(value = "labelCount") Integer labelCount){
		try{
			return loyaltyService.findDepthTrendReport(DF.parse(startDate), DF.parse(endDate), depth, labelCount, EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/frequency/index")
	public String frequencyIndex(){
		return "visit/loyalty/frequency";
	}
	
	@RequestMapping(value = "/frequency/table")
	public @ResponseBody List<FrequencyVo> findFrequencyTable(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			return loyaltyService.findFrequencyTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/frequency/report")
	public @ResponseBody String findFrequencyReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			return loyaltyService.findFrequencyReport(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/frequency/trend/index/{frequency}")
	public String frequencyTrendIndex(@PathVariable(value = "frequency") Long frequency, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		model.addAttribute("frequency", frequency);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/loyalty/frequencytrend";
	}
	
	@RequestMapping(value = "/frequency/trend/report")
	public @ResponseBody String findFrequencyTrendReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "frequency") Long frequency, @RequestParam(value = "labelCount") Integer labelCount){
		try{
			return loyaltyService.findFrequencyTrendReport(DF.parse(startDate), DF.parse(endDate), frequency, labelCount, EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/back/index")
	public String backIndex(){
		return "visit/loyalty/back";
	}
	
	@RequestMapping(value = "/back/table")
	public @ResponseBody List<BackVo> findBackTable(HttpServletRequest request, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			return loyaltyService.findBackTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId(), rows, page);
		}catch(Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/back/report")
	public @ResponseBody String findBackReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return loyaltyService.findBackReport(DF.parse(startDate), DF.parse(endDate), labelCount, EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/sticktime/index")
	public String stickTimeIndex(){
		return "visit/loyalty/sticktime";
	}
	
	@RequestMapping(value = "/sticktime/table")
	public @ResponseBody List<StickTimeVo> findStickTimeTable(HttpServletRequest request, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			return loyaltyService.findStickTimeTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId(), rows, page);
		}catch(Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/sticktime/report")
	public @ResponseBody String findStickTimeReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return loyaltyService.findStickTimeReport(DF.parse(startDate), DF.parse(endDate), labelCount, EwcmsContextUtil.getCurrentSiteId());
		}catch(Exception e){
			return "";
		}
	}

}
