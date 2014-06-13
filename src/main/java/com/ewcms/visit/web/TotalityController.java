package com.ewcms.visit.web;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.visit.service.TotalityService;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.visit.vo.totality.HostVo;
import com.ewcms.visit.vo.totality.OnlineVo;
import com.ewcms.visit.vo.totality.RegionVo;
import com.ewcms.visit.vo.totality.SummaryVo;
import com.ewcms.visit.vo.totality.TimeDistributedVo;
import com.ewcms.web.QueryParameter;

/**
 * 总体情况
 * 
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/totality")
public class TotalityController {
	
	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
	
	@Autowired
	private TotalityService totalityService;
	
	@RequestMapping(value = "/summary/index")
	public String summaryIndex(Model model){
		model.addAttribute("firstAddDate", DateTimeUtil.getDateToString(totalityService.findFirstDate(EwcmsContextUtil.getCurrentSiteId())));
		model.addAttribute("visitDay", totalityService.findDays(EwcmsContextUtil.getCurrentSiteId()));
		return "visit/totality/summary";
	}
	
	@RequestMapping(value = "/summary/table")
	public @ResponseBody List<SummaryVo> summaryTable(){
		return totalityService.findSummaryTable(EwcmsContextUtil.getCurrentSiteId());
	}
	
	@RequestMapping(value = "/summary/report")
	public @ResponseBody String summaryReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try {
			return totalityService.findSummaryReport(DF.parse(startDate), DF.parse(endDate), labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return "";
		}
	}
	
	@RequestMapping(value = "/siteclick/index")
	public String siteClickIndex(){
		return "visit/totality/siteclick";
	}
	
	@RequestMapping(value = "/siteclick/table")
	public @ResponseBody Map<String, Object> siteClickTable(HttpServletRequest request, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try {
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			return totalityService.findSiteClickTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId(), rows, page);
		} catch (Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/siteclick/report")
	public @ResponseBody String siteClickReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return totalityService.findSiteClickReport(DF.parse(startDate), DF.parse(endDate), labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return "";
		}
	}

	@RequestMapping(value = "/visitrecord/index")
	public String visitRecordIndex(){
		return "visit/totality/visitrecord";
	}
	
	@RequestMapping(value = "/visitrecord/table")
	public @ResponseBody Map<String, Object> visitRecordTable(@ModelAttribute QueryParameter params){
		return totalityService.searchVisitRecord(params, EwcmsContextUtil.getCurrentSiteId());
	}
	
	@RequestMapping(value = "/timedistributed/index")
	public String timeDistributedIndex(){
		return "visit/totality/timedistributed";
	}
	
	@RequestMapping(value = "/timedistributed/table")
	public @ResponseBody List<TimeDistributedVo> findTimeDistributedTable(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try {
			return totalityService.findTimeDistributedTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/timedistributed/report")
	public @ResponseBody String findTimeDistributedReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return totalityService.findTimeDistributedReport(DF.parse(startDate), DF.parse(endDate), labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/entrance/index")
	public String entranceIndex(){
		return "visit/totality/entrance";
	}
	
	@RequestMapping(value = "/entrance/table")
	public @ResponseBody Map<String, Object> findEntranceTable(HttpServletRequest request, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try {
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			return totalityService.findEntranceTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId(), rows, page);
		} catch (Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/entrance/trend/index/{url}")
	public String entranceTrendIndex(@PathVariable(value = "url") String url, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		model.addAttribute("url", url);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/totality/entrancetrend";
	}
	
	@RequestMapping(value = "/entrance/trend/report")
	public @ResponseBody String findEntranceTrendReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "url") String url, @RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return totalityService.findEntranceTrendReport(DF.parse(startDate), DF.parse(endDate), url, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return "";
		}
	}

	@RequestMapping(value = "/exit/index")
	public String exitIndex(){
		return "visit/totality/exit";
	}
	
	@RequestMapping(value = "/exit/table")
	public @ResponseBody Map<String, Object> findExitTable(HttpServletRequest request, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try {
			Integer page = Integer.parseInt(request.getParameter("page"));
			Integer rows = Integer.parseInt(request.getParameter("rows"));
			return totalityService.findExitTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId(), rows, page);
		} catch (Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/exit/trend/index/{url}")
	public String exitTrendIndex(@PathVariable(value = "url") String url, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		model.addAttribute("url", url);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/totality/exittrend";
	}
	
	@RequestMapping(value = "/exit/trend/report")
	public @ResponseBody String findExitTrendReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "url") String url, @RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return totalityService.findExitTrendReport(DF.parse(startDate), DF.parse(endDate), url, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/host/index")
	public String hostIndex(){
		return "visit/totality/host";
	}
	
	@RequestMapping(value = "/host/table")
	public @ResponseBody List<HostVo> findHostTable(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try {
			return totalityService.findHostTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/host/report")
	public @ResponseBody String findHostReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			return totalityService.findHostReport(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/host/trend/index/{host}")
	public String hostTrendIndex(@PathVariable(value = "host") String host, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		model.addAttribute("host", host);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/totality/hosttrend";
	}
	
	@RequestMapping(value = "/host/trend/report")
	public @ResponseBody String findHostTrendReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, @RequestParam(value = "host") String host, @RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return totalityService.findHostTrendReport(DF.parse(startDate), DF.parse(endDate), host, labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return "";
		}
	}

	@RequestMapping(value = "/region/country/index")
	public String regionCountryIndex(){
		return "visit/totality/regioncountry";
	}
	
	@RequestMapping(value = "/region/province/index/{country}")
	public String regionProvinceIndex(@PathVariable(value = "country") String country, Model model){
		model.addAttribute("country", country);
		return "visit/totality/regionprovince";
	}
	
	@RequestMapping(value = "/region/city/index/{country}_{province}")
	public String regionCityIndex(@PathVariable(value = "country") String country, @PathVariable(value = "province") String province, Model model){
		model.addAttribute("country", country);
		model.addAttribute("province", province);
		return "visit/totality/regioncity";
	}
	
	@RequestMapping(value = "/region/country/table")
	public @ResponseBody List<RegionVo> findRegionCountryTable(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try {
			return totalityService.findRegionCountryTable(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/region/province/table/{country}")
	public @ResponseBody List<RegionVo> findRegionCountryTable(@PathVariable(value = "country") String country, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try {
			return totalityService.findRegionProvinceTable(country, DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/region/city/table/{country}_{province}")
	public @ResponseBody List<RegionVo> findRegionCityTable(@PathVariable(value = "country") String country, @PathVariable(value = "province") String province, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try {
			return totalityService.findRegionCityTable(country, province, DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/region/country/report")
	public @ResponseBody String findRegionCountryReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		try{
			return totalityService.findRegionCountryReport(DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/region/province/report/{country}")
	public @ResponseBody String findRegionCountryReport(@PathVariable(value = "country") String country, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		try{
			return totalityService.findRegionProvinceReport(country, DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch(Exception e){
			return "";
		}
	}

	@RequestMapping(value = "/region/city/report/{country}_{province}")
	public @ResponseBody String findRegionCityReport(@PathVariable(value = "country") String country, @PathVariable(value = "province") String province, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate, Model model){
		try{
			return totalityService.findRegionCityReport(country, province, DF.parse(startDate), DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch(Exception e){
			return "";
		}
	}

	@RequestMapping(value = "/region/country/trend/index/{country}")
	public String regionCountryTrendIndex(@PathVariable(value = "country") String country, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,Model model){
		model.addAttribute("country", country);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/totality/regioncountrytrend";
	}
	
	@RequestMapping(value = "/region/country/trend/report/{country}")
	public @ResponseBody String findRegionCountryTrendReport(@PathVariable(value = "country")String country, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return totalityService.findRegionCountryTrendReport(country, DF.parse(startDate), DF.parse(endDate), labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch(Exception e){
			return "";
		}
	}
	
	@RequestMapping(value = "/region/province/trend/index/{country}_{province}")
	public String regionProvinceTrendIndex(@PathVariable(value = "country") String country,  @PathVariable(value = "province") String province, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,Model model){
		model.addAttribute("country", country);
		model.addAttribute("province", province);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/totality/regionprovincetrend";
	}
	
	@RequestMapping(value = "/region/province/trend/report/{country}_{province}")
	public @ResponseBody String findRegionProvinceTrendReport(@PathVariable(value = "country") String country, @PathVariable(value = "province") String province, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return totalityService.findRegionProvinceTrendReport(country, province, DF.parse(startDate), DF.parse(endDate), labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch(Exception e){
			return "";
		}
	}

	@RequestMapping(value = "/region/city/trend/index/{country}_{province}_{city}")
	public String regionCityTrendIndex(@PathVariable(value = "country") String country,  @PathVariable(value = "province") String province, @PathVariable(value = "city") String city, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,Model model){
		model.addAttribute("country", country);
		model.addAttribute("province", province);
		model.addAttribute("city", city);
		model.addAttribute("startDate", startDate);
		model.addAttribute("endDate", endDate);
		return "visit/totality/regioncitytrend";
	}
	
	@RequestMapping(value = "/region/city/trend/report/{country}_{province}_{city}")
	public @ResponseBody String findRegionCityTrendReport(@PathVariable(value = "country") String country, @PathVariable(value = "province") String province, @PathVariable(value = "city") String city, @RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return totalityService.findRegionCityTrendReport(country, province, city, DF.parse(startDate), DF.parse(endDate), labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch(Exception e){
			return "";
		}
	}

	@RequestMapping(value = "/online/index")
	public String onlineIndex(){
		return "visit/totality/online";
	}
	
	@RequestMapping(value = "/online/table")
	public @ResponseBody List<OnlineVo> findOnlineTable(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate){
		try{
			return totalityService.findOnlineTable( DF.parse(startDate),  DF.parse(endDate), EwcmsContextUtil.getCurrentSiteId());
		} catch(Exception e){
			return null;
		}
	}
	
	@RequestMapping(value = "/online/report")
	public @ResponseBody String findOnlineReport(@RequestParam(value = "startDate") String startDate, @RequestParam(value = "endDate") String endDate,@RequestParam(value = "labelCount", defaultValue = "8") Integer labelCount){
		try{
			return totalityService.findOnlineReport(DF.parse(startDate), DF.parse(endDate), labelCount, EwcmsContextUtil.getCurrentSiteId());
		} catch(Exception e){
			return "";
		}
	}

}
