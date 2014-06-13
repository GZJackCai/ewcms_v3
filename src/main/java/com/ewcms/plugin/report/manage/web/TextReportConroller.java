package com.ewcms.plugin.report.manage.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.plugin.externalds.manager.service.BaseDsService;
import com.ewcms.plugin.report.manage.service.TextReportService;
import com.ewcms.plugin.report.model.TextReport;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/report/text")
public class TextReportConroller {
	@Autowired
	private TextReportService textReportService;
	@Autowired
	private BaseDsService baseDsService;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("baseDsList", baseDsService.findAllBaseDs());
	}

	@RequestMapping(value = "/index")
	public String index() {
		return "plugin/report/text/index";
	}

	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return textReportService.search(params);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		TextReport textReport = new TextReport();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			textReport = textReportService.findTextReportById(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("textReport", textReport);
		return "plugin/report/text/edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("textReport") TextReport textReport, @RequestParam(required = false) List<Long> selections, Model model,
			RedirectAttributes redirectAttributes, @RequestParam(value = "textReportFile", required = false) MultipartFile textReportFile,
			HttpServletRequest request) {
		InputStream in = null;
		try {
			Boolean annexFlag = Boolean.FALSE;
			request.setCharacterEncoding("UTF-8");
			if (textReportFile != null && textReportFile.getSize() > 0) {
				byte[] buffer = new byte[Integer.parseInt(String.valueOf(textReportFile.getSize()))];
				in = new BufferedInputStream(textReportFile.getInputStream(), Integer.parseInt(String.valueOf(textReportFile.getSize())));
				in.read(buffer);
				textReport.setTextEntity(buffer);
				in.close();
				annexFlag = Boolean.TRUE;
			}
			
			Boolean close = Boolean.FALSE;
			if (textReport.getId() != null && StringUtils.hasText(textReport.getId().toString())) {
				TextReport oldTextReport = textReportService.findTextReportById(textReport.getId());
				if (!annexFlag){
					textReport.setTextEntity(oldTextReport.getTextEntity());
				}
				textReportService.updTextReport(textReport);
				selections.remove(0);
				if (selections == null || selections.isEmpty()) {
					close = Boolean.TRUE;
				} else {
					textReport = textReportService.findTextReportById(selections.get(0));
					model.addAttribute("textReport", textReport);
					model.addAttribute("selections", selections);
				}
			} else {
				textReportService.addTextReport(textReport);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, textReport.getId());
				model.addAttribute("textReport", new TextReport());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close", close);
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		} finally{
			if (in != null){
				try {
					in.close();
				} catch (IOException e) {
				}
				in = null;
			}
		}
		return "plugin/report/text/edit";
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes) {
		try {
			for (Long id : selections) {
				textReportService.delTextReport(id);
			}
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/plugin/report/text/index";
	}
}
