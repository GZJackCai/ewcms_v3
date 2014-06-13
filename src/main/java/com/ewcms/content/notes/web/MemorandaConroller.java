package com.ewcms.content.notes.web;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.content.notes.model.Memoranda;
import com.ewcms.content.notes.model.Memoranda.BeforeStatus;
import com.ewcms.content.notes.model.Memoranda.FrequencyStatus;
import com.ewcms.content.notes.service.MemorandaService;
import com.ewcms.content.notes.util.Lunar;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/notes")
public class MemorandaConroller {
	
	@Autowired
	private MemorandaService memorandaService;
	
	@ModelAttribute
	public void init(Model model){
		model.addAttribute("beforeStatusMap", BeforeStatus.values());
		model.addAttribute("frequencyStatusMap", FrequencyStatus.values());
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("true", "是");
		map.put("false", "否");
		model.addAttribute("bolMap", map);
	}
	
	@RequestMapping(value = "/index")
	public String index(@RequestParam(value = "year", required = false)Integer year, @RequestParam(value = "month", required = false)Integer month, Model model, HttpServletRequest request){
		Calendar calendar = Calendar.getInstance();
		int currentDay = calendar.get(Calendar.DATE);
		
		Integer currentYear = Calendar.getInstance().get(Calendar.YEAR);
		Integer currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		model.addAttribute("currentYear", currentYear);
		model.addAttribute("currentMonth", currentMonth);
		
		model.addAttribute("toDayLunar",Lunar.getLunar("" +  currentYear, "" + currentMonth, "" + currentDay));
		
		if (year == null || month == null) {
			year = currentYear;
			month = currentMonth;
			model.addAttribute("year", year).addAttribute("month", month);
		}
		String contextPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		model.addAttribute("result", memorandaService.getInitCalendarToHtml(year, month, contextPath));
		
		return "content/notes/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return memorandaService.search(params);
	}
	
	@RequestMapping(value = "/changeDate/{year}_{month}")
	public @ResponseBody String changeDate(@PathVariable(value = "year") Integer year, @PathVariable(value = "month") Integer month, HttpServletRequest request){
		String contextPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		return memorandaService.getInitCalendarToHtml(year, month, contextPath).toString();
	}
	
	@RequestMapping(value = "/toDay/{currentYear}_{currentMonth}")
	public @ResponseBody String toDay(@PathVariable(value = "currentYear") Integer currentYear, @PathVariable(value = "currentMonth")Integer currentMonth, HttpServletRequest request){
		String contextPath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
		return memorandaService.getInitCalendarToHtml(currentYear, currentMonth, contextPath).toString();
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam(value = "selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long id : selections){
				memorandaService.delMemoranda(id);
	    	}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/notes/index";
	}
	
	@RequestMapping(value = "/drop/{memorandaId}_{year}_{month}_{dropDay}")
	public @ResponseBody Boolean drop(@PathVariable(value = "memorandaId")Long memorandaId, @PathVariable(value = "year") Integer year, @PathVariable(value = "month")Integer month, @PathVariable(value = "dropDay") Integer dropDay){
		Boolean result = false;
		try{
			memorandaService.updMemoranda(memorandaId, year, month, dropDay);
			result = true;
		}catch(Exception e){
		}
		return result;
	}
	
	@RequestMapping(value = "/add/{year}_{month}_{day}")
	public String add(@PathVariable(value = "year")Integer year, @PathVariable(value = "month")Integer month, @PathVariable(value = "day")Integer day, Model model){
		Memoranda memoranda = new Memoranda();
		model.addAttribute("selections", new ArrayList<Long>(0)).addAttribute("year", year).addAttribute("month", month).addAttribute("day", day).addAttribute("memoranda", memoranda);
		return "content/notes/edit";
	}
	
	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		Memoranda memoranda = new Memoranda();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			memoranda = memorandaService.findMemoranda(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("memoranda", memoranda);
		return "content/notes/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "memoranda") Memoranda memoranda, @RequestParam(value = "year")Integer year, @RequestParam(value = "month") Integer month, @RequestParam(value = "day") Integer day, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (memoranda.getId() != null && StringUtils.hasText(memoranda.getId().toString())){
				memorandaService.updMemoranda(memoranda);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					memoranda = memorandaService.findMemoranda(selections.get(0));
					model.addAttribute("memoranda", memoranda);
					model.addAttribute("selections", selections);
				}
			}else{
				memorandaService.addMemoranda(memoranda, year, month, day);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, memoranda.getId());
				model.addAttribute("memoranda", new Memoranda());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(Exception e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "content/notes/edit";	
	}
	
	@RequestMapping(value = "/list")
	public String list(){
		return "content/notes/list";
		
	}
}
