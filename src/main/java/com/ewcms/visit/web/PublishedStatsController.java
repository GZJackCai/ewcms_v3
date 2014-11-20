package com.ewcms.visit.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.visit.model.publishedstats.PublishedStats;
import com.ewcms.visit.service.PublishedStatsService;
import com.ewcms.web.vo.TreeNode;

/**
 *
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/publishedstats")
public class PublishedStatsController {

	@Autowired
	private PublishedStatsService publishStatsService;

	@RequestMapping(value = "/person/index")
	public String personIndex() {
		return "visit/publishedstats/person";
	}

	@RequestMapping(value = "/person/table")
	public @ResponseBody List<PublishedStats> findPersonPublishedStatsTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
			@RequestParam(value = "channelId", defaultValue = "-1") Long channelId) {
		try {
			return publishStatsService.findPersonPublishedStatsTable(startDate,
					endDate, EwcmsContextUtil.getCurrentSiteId(), channelId);
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/channel/index")
	public String channelIndex() {
		return "visit/publishedstats/channel";
	}

	@RequestMapping(value = "/channel/table")
	public @ResponseBody List<TreeNode> findChannelPublishedStatsTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return publishStatsService.findChannelPublishedStatsTable(
					startDate, endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

	@RequestMapping(value = "/organ/index")
	public String organIndex() {
		return "visit/publishedstats/organ";
	}

	@RequestMapping(value = "/organ/table")
	public @ResponseBody List<TreeNode> findOrganPublishedStatsTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		try {
			return publishStatsService.findOrganPublishedStatsTable(startDate,
					endDate, EwcmsContextUtil.getCurrentSiteId());
		} catch (Exception e) {
			return null;
		}
	}

}
