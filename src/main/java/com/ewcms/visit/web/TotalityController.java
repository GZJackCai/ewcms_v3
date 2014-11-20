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
import com.ewcms.visit.model.totality.Host;
import com.ewcms.visit.model.totality.Online;
import com.ewcms.visit.model.totality.Region;
import com.ewcms.visit.model.totality.Summary;
import com.ewcms.visit.service.TotalityService;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.web.QueryParameter;

/**
 * 总体情况
 * 
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/totality")
public class TotalityController {

	@Autowired
	private TotalityService totalityService;

	@RequestMapping(value = "/summary/index")
	public String summaryIndex(Model model) {
		model.addAttribute("firstAddDate", DateTimeUtil
				.getDateToString(totalityService
						.findMinVisitDate(EwcmsContextUtil.getCurrentSiteId())));
		model.addAttribute("visitDay",
				totalityService.findDays(EwcmsContextUtil.getCurrentSiteId()));
		return "visit/totality/summary";
	}

	@RequestMapping(value = "/summary/table")
	public @ResponseBody List<Summary> summaryTable() {
		return totalityService.findSummaryTable(EwcmsContextUtil
				.getCurrentSiteId());
	}

	@RequestMapping(value = "/summary/report")
	public @ResponseBody String summaryReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return totalityService.findSummaryReport(startDate, endDate,
					labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/siteclick/index")
	public String siteClickIndex() {
		return "visit/totality/siteclick";
	}

	@RequestMapping(value = "/siteclick/table")
	public @ResponseBody Map<String, Object> siteClickTable(
			@ModelAttribute QueryParameter params,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return totalityService.findSiteClickTable(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId(), params);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/siteclick/report")
	public @ResponseBody String siteClickReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return totalityService.findSiteClickReport(startDate, endDate,
					labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/visitrecord/index")
	public String visitRecordIndex() {
		return "visit/totality/visitrecord";
	}

	@RequestMapping(value = "/visitrecord/table")
	public @ResponseBody Map<String, Object> visitRecordTable(
			@ModelAttribute QueryParameter params) {
		return totalityService.searchVisitRecord(params,
				EwcmsContextUtil.getCurrentSiteId());
	}

	@RequestMapping(value = "/timedistributed/index")
	public String timeDistributedIndex() {
		return "visit/totality/timedistributed";
	}

	@RequestMapping(value = "/timedistributed/table")
	public @ResponseBody List<Summary> findTimeDistributedTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return totalityService.findTimeDistributedTable(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/timedistributed/report")
	public @ResponseBody String findTimeDistributedReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return totalityService.findTimeDistributedReport(startDate,
					endDate, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/{type}/index")
	public String entryIndex(@PathVariable(value = "type") String type,
			Model model) {
		model.addAttribute("type", type);
		return "visit/totality/entry";
	}

	@RequestMapping(value = "/{type}/table")
	public @ResponseBody Map<String, Object> findEntranceTable(
			@PathVariable(value = "type") String type,
			@ModelAttribute QueryParameter params,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return totalityService.findEntryTable(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId(), params, type);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/{type}/trend/index/")
	public String entranceTrendIndex(
			@PathVariable(value = "type") String type,
			@RequestParam(value = "url") String url,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("type", type);
		model.addAttribute("url", url);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/totality/entrytrend";
	}

	@RequestMapping(value = "/{type}/trend/report")
	public @ResponseBody String findEntryTrendReport(
			@PathVariable(value = "type") String type,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "url") String url,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return totalityService.findEntryTrendReport(startDate, endDate,
					url, labelCount, EwcmsContextUtil.getCurrentSiteId(), type);
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/host/index")
	public String hostIndex() {
		return "visit/totality/host";
	}

	@RequestMapping(value = "/host/table")
	public @ResponseBody List<Host> findHostTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return totalityService.findHostTable(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/host/report")
	public @ResponseBody String findHostReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return totalityService.findHostReport(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/host/trend/index/")
	public String hostTrendIndex(
			@RequestParam(value = "host") String host,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("host", host);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/totality/hosttrend";
	}

	@RequestMapping(value = "/host/trend/report")
	public @ResponseBody String findHostTrendReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "host") String host,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return totalityService.findHostTrendReport(startDate, endDate,
					host, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/region/country/index")
	public String regionCountryIndex() {
		return "visit/totality/regioncountry";
	}

	@RequestMapping(value = "/region/province/index/{country}")
	public String regionProvinceIndex(
			@PathVariable(value = "country") String country, Model model) {
		model.addAttribute("country", country);
		return "visit/totality/regionprovince";
	}

	@RequestMapping(value = "/region/city/index/{country}_{province}")
	public String regionCityIndex(
			@PathVariable(value = "country") String country,
			@PathVariable(value = "province") String province, Model model) {
		model.addAttribute("country", country);
		model.addAttribute("province", province);
		return "visit/totality/regioncity";
	}

	@RequestMapping(value = "/region/country/table")
	public @ResponseBody List<Region> findRegionCountryTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return totalityService.findRegionCountryTable(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/region/province/table/{country}")
	public @ResponseBody List<Region> findRegionCountryTable(
			@PathVariable(value = "country") String country,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return totalityService.findRegionProvinceTable(country, startDate,
					endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/region/city/table/{country}_{province}")
	public @ResponseBody List<Region> findRegionCityTable(
			@PathVariable(value = "country") String country,
			@PathVariable(value = "province") String province,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return totalityService.findRegionCityTable(country, province,
					startDate, endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/region/country/report")
	public @ResponseBody String findRegionCountryReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		try {
			return totalityService.findRegionCountryReport(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/region/province/report/{country}")
	public @ResponseBody String findRegionCountryReport(
			@PathVariable(value = "country") String country,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		try {
			return totalityService.findRegionProvinceReport(country, startDate,
					endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/region/city/report/{country}_{province}")
	public @ResponseBody String findRegionCityReport(
			@PathVariable(value = "country") String country,
			@PathVariable(value = "province") String province,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		try {
			return totalityService.findRegionCityReport(country, province,
					startDate, endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/region/country/trend/index/{country}")
	public String regionCountryTrendIndex(
			@PathVariable(value = "country") String country,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("country", country);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/totality/regioncountrytrend";
	}

	@RequestMapping(value = "/region/country/trend/report/{country}")
	public @ResponseBody String findRegionCountryTrendReport(
			@PathVariable(value = "country") String country,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return totalityService.findRegionCountryTrendReport(country,
					startDate, endDate, labelCount,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/region/province/trend/index/{country}_{province}")
	public String regionProvinceTrendIndex(
			@PathVariable(value = "country") String country,
			@PathVariable(value = "province") String province,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("country", country);
		model.addAttribute("province", province);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/totality/regionprovincetrend";
	}

	@RequestMapping(value = "/region/province/trend/report/{country}_{province}")
	public @ResponseBody String findRegionProvinceTrendReport(
			@PathVariable(value = "country") String country,
			@PathVariable(value = "province") String province,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return totalityService.findRegionProvinceTrendReport(country,
					province, startDate, endDate, labelCount,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/region/city/trend/index/{country}_{province}_{city}")
	public String regionCityTrendIndex(
			@PathVariable(value = "country") String country,
			@PathVariable(value = "province") String province,
			@PathVariable(value = "city") String city,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			Model model) {
		model.addAttribute("country", country);
		model.addAttribute("province", province);
		model.addAttribute("city", city);
		model.addAttribute("startDate", DateTimeUtil.getDateToString(startDate));
		model.addAttribute("endDate", DateTimeUtil.getDateToString(endDate));
		return "visit/totality/regioncitytrend";
	}

	@RequestMapping(value = "/region/city/trend/report/{country}_{province}_{city}")
	public @ResponseBody String findRegionCityTrendReport(
			@PathVariable(value = "country") String country,
			@PathVariable(value = "province") String province,
			@PathVariable(value = "city") String city,
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return totalityService.findRegionCityTrendReport(country, province,
					city, startDate, endDate, labelCount,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

	@RequestMapping(value = "/online/index")
	public String onlineIndex() {
		return "visit/totality/online";
	}

	@RequestMapping(value = "/online/table")
	public @ResponseBody List<Online> findOnlineTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return totalityService.findOnlineTable(startDate, endDate,
					EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/online/report")
	public @ResponseBody String findOnlineReport(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount) {
		try {
			return totalityService.findOnlineReport(startDate, endDate,
					labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}

}
