package com.ewcms.content.history.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.content.history.model.HistoryModel;
import com.ewcms.content.history.service.HistoryModelService;
import com.ewcms.content.history.util.HistoryUtil;
import com.ewcms.site.model.Template;
import com.ewcms.web.QueryParameter;

@Controller
@RequestMapping(value = "/content/historymodel")
public class HistoryModelController {
	
	@Autowired
	private HistoryModelService historyModelService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "content/historymodel/index";
	}
	
	@RequestMapping(value = "/downloadTemplate")
	public void downloadTemplate(@RequestParam(value = "historyId") Long historyId,HttpServletResponse response){
		PrintWriter pw = null;
		InputStream in = null;
		try {
			if (historyId != null) {
				HistoryModel historyModel = historyModelService.findByHistoryModel(historyId);
				if (historyModel.getModelObject() != null && historyModel.getModelObject().length != 0) {
					String fileName = String.valueOf(historyModel.getIdValue() + "_" + historyId);
					fileName = URLEncoder.encode(fileName, "UTF-8");

					Object obj = HistoryUtil.conversion(historyModel.getModelObject());
					Template template = (Template) obj;
					
					String templateSource = new String(template.getTemplateEntity().getTplEntity(), "UTF-8");
					
					response.setCharacterEncoding("UTF-8");
					response.setHeader("Content-disposition", "attachment; filename=" + fileName + ".html");
					response.setContentType("application/x-download;charset=UTF-8");

					pw = response.getWriter();
					pw.write(templateSource);
					
					pw.flush();
				}
			}
		} catch (IOException e) {
		} finally {
			if (pw != null) {
				try {
					pw.close();
					pw = null;
				} catch (Exception e) {
				}
			}
			if (in != null) {
				try {
					in.close();
					in = null;
				} catch (Exception e) {
				}
			}
		}
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return historyModelService.search(params);
	}

	@RequestMapping(value = "/delete")
	public String delChannelTemplate(@RequestParam(value = "selections") List<Long> selections){
		for (Long id : selections){
			historyModelService.delHistoryModel(id);
		}
		return "redirect:/content/historymodel/index";
	}
}
