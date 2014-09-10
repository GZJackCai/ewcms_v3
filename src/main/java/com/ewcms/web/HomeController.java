package com.ewcms.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.content.message.push.PushApiService;
import com.ewcms.site.model.Site;
import com.ewcms.site.service.SiteService;
import com.ewcms.util.EwcmsContextUtil;

@Controller
public class HomeController {

	@Autowired
	private SiteService siteService;
	@Autowired
	private PushApiService pushApiService;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("yearMap", getYears());
	}
	
	@RequestMapping(value = "/home")
	public String home(@RequestParam(value = "siteId", required = false) Long siteId, @RequestParam(value = "hasSite", required = false) Boolean hasSite, Model model){
		Site site = getSite(siteId);
		hasSite = (site != null);
		model.addAttribute("hasSite", hasSite);
		
		if(site != null){
			model.addAttribute("siteId", site.getId());
			model.addAttribute("siteName", site.getSiteName());
		    initSiteInContext(site);
		}
		
		pushApiService.offline(EwcmsContextUtil.getLoginName());
		
		return "home";
	}
	
	@RequestMapping(value = "/siteSwitchIndex")
	public String siteSwitchIndex(Model model){
		Long siteId = EwcmsContextUtil.getCurrentSite().getId();
		model.addAttribute("siteId", siteId);
		return "/siteswitch";
	}
	
	@RequestMapping(value = "/siteSwitchQuery")
	@ResponseBody
	public Map<String, Object> siteSwitchQuery(@ModelAttribute QueryParameter params){
		params.setPage(1);
		params.setRows(5);
		return siteService.searchSiteSwitch(params);
	}
	
	/**
     * 得到操作站点
     * 
     * @param id 站点编号
     * @return 操作站点
     */
    private Site getSite(Long id) {
        Site site = null;
        if(id != null){
            site =  siteService.getSite(id);
        }else{
            //TODO 得到用户所属组织，通过组织得到站点。
            List<Site> list= siteService.getSiteListByOrgans(new Long[]{}, true);
            if(list != null && !list.isEmpty()){
                site = list.get(0);
            }
        }
        return site ;
    }
    
    /**
     * 初始站点到访问上下文中当，提供全局访问
     * 
     * @param site
     */
    private void initSiteInContext(Site site){
    	EwcmsContextUtil.setCurrentSite(site);
	}
    
    private List<Integer> getYears(){
    	Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
		List<Integer> years = new ArrayList<Integer>();
		for (int i = currentYear; i >= 2000; i--){
			years.add(i);
		}
		return years;
	}
}
