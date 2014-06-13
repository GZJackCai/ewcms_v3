package com.ewcms.plugin.report.manage.web;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.plugin.report.manage.service.RepositoryService;
import com.ewcms.plugin.report.model.Repository;
import com.ewcms.security.service.ServiceException;
import com.ewcms.site.model.Site;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/plugin/report/repository")
public class RepositoryController {

	@Autowired
	private RepositoryService repositoryService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "plugin/report/repository/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params){
		return repositoryService.search(params);
	}
	
	@RequestMapping(value = "/download/{repositoryId}")
	public void download(@PathVariable(value = "repositoryId")Long repositoryId ,HttpServletResponse response) {
		PrintWriter pw = null;
		InputStream in = null;
		try {
			if (repositoryId != null) {
				Repository repository = repositoryService.findRepositoryById(repositoryId);
				String type = repository.getType();
				byte[] bytes = repository.getEntity();

				response.setCharacterEncoding("UTF-8");
				
				if (type.toLowerCase().equals("pdf")) {
					response.setContentType("application/pdf");
				} else if (type.toLowerCase().equals("png")) {
					response.setContentType("image/png");
				}

				pw = response.getWriter();
				in = new ByteArrayInputStream(bytes);
				int len = 0;
				while ((len = in.read()) > -1) {
					pw.write(len);
				}
				pw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
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

	@RequestMapping(value = "/publish")
	public @ResponseBody Boolean publish(@RequestParam(value = "selections") List<Long> selections) {
		Boolean result = false;
		if (selections != null && !selections.isEmpty()) {
			Site site = EwcmsContextUtil.getCurrentSite();
			if (site != null && site.getId() != null){
				repositoryService.publishRepository(selections, site);
				result = true;
			}
		}
		return result;
	}
	
	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes) {
		try {
			for (Long id : selections) {
					repositoryService.delRepository(id);
			}
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/plugin/report/repository/index";
	}

}
