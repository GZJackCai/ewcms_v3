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
import com.ewcms.visit.model.loyalty.AccessDepth;
import com.ewcms.visit.model.loyalty.AccessFrequency;
import com.ewcms.visit.service.LoyaltyService;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.web.QueryParameter;

/**
 * 忠诚度分析
 * 
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/loyalty")
public class LoyaltyController {

	@Autowired
	private LoyaltyService loyaltyService;

	@RequestMapping(value = "/depth/index")
	public String depthIndex() {
		return "visit/loyalty/depth";
	}

	@RequestMapping(value = "/depth/table")
	public @ResponseBody List<AccessDepth> findDepthTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return loyaltyService.findDepthTable(startDate, endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/depth/report")
	public @ResponseBody String findDepthReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return loyaltyService.findDepthReport(startDate, endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/depth/trend/index/{depth}")
	public String depthTrendIndex(
			@PathVariable(value = "depth") Long depth,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("depth", depth);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/loyalty/depthtrend";
	}

	@RequestMapping(value = "/depth/trend/report")
	public @ResponseBody String findDepthTrendReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "depth") Long depth,
			@RequestParam(value = "labelCount") Integer labelCount) {
		try {
			return loyaltyService.findDepthTrendReport(startDate, endDate,
					depth, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/frequency/index")
	public String frequencyIndex() {
		return "visit/loyalty/frequency";
	}

	@RequestMapping(value = "/frequency/table")
	public @ResponseBody List<AccessFrequency> findFrequencyTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return loyaltyService
					.findFrequencyTable(startDate, endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/frequency/report")
	public @ResponseBody String findFrequencyReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return loyaltyService.findFrequencyReport(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/frequency/trend/index/{frequency}")
	public String frequencyTrendIndex(
			@PathVariable(value = "frequency") Long frequency,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("frequency", frequency);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/loyalty/frequencytrend";
	}

	@RequestMapping(value = "/frequency/trend/report")
	public @ResponseBody String findFrequencyTrendReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "frequency") Long frequency,
			@RequestParam(value = "labelCount") Integer labelCount) {
		try {
			return loyaltyService.findFrequencyTrendReport(startDate, endDate,
					frequency, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/back/index")
	public String backIndex() {
		return "visit/loyalty/back";
	}

	@RequestMapping(value = "/back/table")
	public @ResponseBody Map<String, Object> findBackTable(
			@ModelAttribute QueryParameter params,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return loyaltyService.findBackTable(startDate, endDate, EwcmsContextUtil.getCurrentSiteId(),
					params);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/back/report")
	public @ResponseBody String findBackReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return loyaltyService.findBackReport(startDate, endDate,
					labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/sticktime/index")
	public String stickTimeIndex() {
		return "visit/loyalty/sticktime";
	}

	@RequestMapping(value = "/sticktime/table")
	public @ResponseBody Map<String, Object> findStickTimeTable(
			@ModelAttribute QueryParameter params,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return loyaltyService.findStickTimeTable(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId(), params);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/sticktime/report")
	public @ResponseBody String findStickTimeReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return loyaltyService.findStickTimeReport(startDate, endDate,
					labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

}
