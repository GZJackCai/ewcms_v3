package com.ewcms.visit.web;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.visit.service.InteractiveService;
import com.ewcms.web.vo.TreeNode;

/**
 *
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit/interactive")
public class InteractiveController {

	@Autowired
	private InteractiveService interactiveService;

	@RequestMapping(value = "/zhengmin/index")
	public String zhengMinIndex() {
		return "visit/interactive/zhengmin";
	}

	@RequestMapping(value = "/zhengmin/table")
	public @ResponseBody List<TreeNode> zhengMinTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return interactiveService.findZhengMin(startDate, endDate);
	}

	@RequestMapping(value = "/advisory/index")
	public String advisoryIndex() {
		return "visit/interactive/advisory";
	}

	@RequestMapping(value = "/advisory/table")
	public @ResponseBody List<TreeNode> advisoryTable(
			@RequestParam(value = "startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
			@RequestParam(value = "endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
		return interactiveService.findAdvisory(startDate, endDate);
	}
}
