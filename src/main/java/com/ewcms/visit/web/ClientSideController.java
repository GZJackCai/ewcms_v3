package com.ewcms.visit.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.visit.model.clientside.ClientSide;
import com.ewcms.visit.model.clientside.ClientSidePk.ClientSideType;
import com.ewcms.visit.service.ClientSideService;
import com.ewcms.visit.util.DateTimeUtil;

/**
 * 点击量来源
 *
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/clientside")
public class ClientSideController {

	@Autowired
	private ClientSideService clientSideService;

	@RequestMapping(value = "/{typeValue}/index")
	public String clientSideIndex(@PathVariable(value = "typeValue") String typeValue, Model model) {
		model.addAttribute("typeValue", typeValue);
		return "visit/clientside/index";
	}

	@RequestMapping(value = "/{typeValue}/table")
	public @ResponseBody List<ClientSide> findClientSideTable(
			@PathVariable(value = "typeValue") String typeValue,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return clientSideService.findClientSideTable(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId(), ClientSideType.valueOf(typeValue.toUpperCase()));
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/{typeValue}/report")
	public @ResponseBody String findClientSideReport(
			@PathVariable(value = "typeValue") String typeValue,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return clientSideService.findClientSideReport(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId(), ClientSideType.valueOf(typeValue.toUpperCase()));
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/{typeValue}/trend/index")
	public String clientSideTrendIndex(
			@PathVariable(value = "typeValue") String typeValue,
			@RequestParam(value = "clientSideName") String clientSideName,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("clientSideName", clientSideName);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		model.addAttribute("typeValue", typeValue);
		return "visit/clientside/trend";
	}

	@RequestMapping(value = "/{typeValue}/trend/report")
	public @ResponseBody String findClientSideTrendReport(
			@PathVariable(value = "typeValue") String typeValue,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "clientSideName") String clientSideName,
			@RequestParam(value = "labelCount") Integer labelCount) {
		try {
			return clientSideService.findClientSideTrendReport(startDate,
					endDate, ClientSideType.valueOf(typeValue.toUpperCase()),
					clientSideName, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}
}
