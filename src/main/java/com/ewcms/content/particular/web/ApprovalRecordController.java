package com.ewcms.content.particular.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.content.particular.model.ApprovalRecord;
import com.ewcms.content.particular.service.ApprovalRecordService;
import com.ewcms.plugin.BaseException;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/particular/approvalrecord")
public class ApprovalRecordController {

	@Autowired
	private ApprovalRecordService approvalRecordService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "content/particular/approvalrecord/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return approvalRecordService.search(params);
	}

	@RequestMapping(value = "/edit",method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		ApprovalRecord approvalRecord = new ApprovalRecord();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			approvalRecord = approvalRecordService.findApprovalRecordById(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("approvalRecord", approvalRecord);
		return "content/particular/approvalrecord/edit";
	}

	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "approvalRecord")ApprovalRecord approvalRecord, @RequestParam(required = false) List<Long> selections, Model model, RedirectAttributes redirectAttributes){
		try{
			Boolean close = Boolean.FALSE;
			if (approvalRecord.getId() != null && StringUtils.hasText(approvalRecord.getId().toString())){
				approvalRecordService.updApprovalRecord(approvalRecord);
				selections.remove(0);
				if(selections == null || selections.isEmpty()){
					close = Boolean.TRUE;
				}else{
					approvalRecord = approvalRecordService.findApprovalRecordById(selections.get(0));
					model.addAttribute("approvalRecord", approvalRecord);
					model.addAttribute("selections", selections);
				}
			}else{
				approvalRecordService.addApprovalRecord(approvalRecord);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, approvalRecord.getId());
				model.addAttribute("approvalRecord", new ApprovalRecord());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close",close);
		}catch(BaseException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "content/particular/approvalrecord/edit";	
	}
	
	@RequestMapping(value = "/delete", method = {RequestMethod.POST,RequestMethod.GET})
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes){
		try{
			for (Long id : selections){
				approvalRecordService.delApprovalRecord(id);
	    	}
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/particular/approvalrecord/index";
	}
}
