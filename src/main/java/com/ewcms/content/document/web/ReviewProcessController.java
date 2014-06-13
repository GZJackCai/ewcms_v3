package com.ewcms.content.document.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.ewcms.content.document.model.ReviewProcess;
import com.ewcms.content.document.service.ReviewProcessService;
import com.ewcms.plugin.BaseException;
import com.ewcms.security.model.Role;
import com.ewcms.security.model.User;
import com.ewcms.security.service.AccountService;
import com.ewcms.web.vo.ComboBoxString;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/document/process")
public class ReviewProcessController {

	@Autowired
	private ReviewProcessService reviewProcessService;
	@Autowired
	private AccountService accountService;

	@RequestMapping(value = "/index/{channelId}")
	public String index(@PathVariable(value = "channelId") Long channelId, Model model) {
		model.addAttribute("channelId", channelId);
		return "content/document/process/index";
	}

	@RequestMapping(value = "/query/{channelId}")
	public @ResponseBody
	Map<String, Object> query(@PathVariable(value = "channelId") Long channelId) {
		return reviewProcessService.search(channelId);
	}

	@RequestMapping(value = "/edit/{channelId}", method = RequestMethod.GET)
	public String edit(@PathVariable(value = "channelId") Long channelId, @RequestParam(required = false) List<Long> selections, Model model) {
		ReviewProcess reviewProcess = new ReviewProcess();
		if (selections == null || selections.isEmpty()) {
			reviewProcess.setChannelId(channelId);
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			reviewProcess = reviewProcessService.findReviewProcess(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("reviewProcess", reviewProcess);
		return "content/document/process/edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "reviewProcess") ReviewProcess reviewProcess, @RequestParam(required = false) List<Long> selections,
			@RequestParam(value = "userNames", required = false) List<String> userNames,
			@RequestParam(value = "roleNames", required = false) List<String> roleNames, Model model, RedirectAttributes redirectAttributes) {
		try {
			Boolean close = Boolean.FALSE;
			if (reviewProcess.getId() != null && StringUtils.hasText(reviewProcess.getId().toString())) {
				reviewProcessService.updReviewProcess(reviewProcess, userNames, roleNames);
				selections.remove(0);
				if (selections == null || selections.isEmpty()) {
					close = Boolean.TRUE;
				} else {
					reviewProcess = reviewProcessService.findReviewProcess(selections.get(0));
					model.addAttribute("reviewProcess", reviewProcess);
					model.addAttribute("selections", selections);
				}
			} else {
				reviewProcessService.addReviewProcess(reviewProcess.getChannelId(), reviewProcess, userNames, roleNames);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, reviewProcess.getId());
				model.addAttribute("reviewProcess", new ReviewProcess());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close", close);
			return "content/document/process/edit";
		} catch (BaseException e) {
			String result = "";
			if (selections != null && !selections.isEmpty()){
				for (Long selection : selections){
					result = "selections=" + selection + "&";
				}
			}
			redirectAttributes.addFlashAttribute("message", e.getPageMessage());
			return "redirect:/content/document/process/edit/" + reviewProcess.getChannelId() + "?" + result;
		}

	}

	@RequestMapping(value = "/delete/{channelId}", method = { RequestMethod.POST, RequestMethod.GET })
	public String remove(@PathVariable(value = "channelId") Long channelId, @RequestParam("selections") List<Long> selections,
			RedirectAttributes redirectAttributes) {
		try {
			for (Long id : selections) {
				reviewProcessService.delReviewProcess(id);
			}
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/document/process/index/" + channelId;
	}

	@RequestMapping(value = "/up/{channelId}")
	public @ResponseBody
	Boolean up(@PathVariable(value = "channelId") Long channelId, @RequestParam(value = "selections") List<Long> selections) {
		Boolean result = false;
		try {
			reviewProcessService.upReivewProcess(channelId, selections.get(0));
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	@RequestMapping(value = "/down/{channelId}")
	public @ResponseBody
	Boolean down(@PathVariable(value = "channelId") Long channelId, @RequestParam(value = "selections") List<Long> selections) {
		Boolean result = false;
		try {
			reviewProcessService.downReviewProcess(channelId, selections.get(0));
			result = true;
		} catch (Exception e) {
		}
		return result;
	}
	
	@RequestMapping(value = "/user")
	public @ResponseBody List<ComboBoxString> user(@RequestParam(value = "processId", required = false)Long processId){
		List<ComboBoxString> comboBoxStrings = new ArrayList<ComboBoxString>();
		List<User> users = accountService.getAllUser();
		ComboBoxString comboBoxString = null;
		for (User user : users){
			comboBoxString = new ComboBoxString();
			comboBoxString.setId(user.getLoginName());
			comboBoxString.setText(user.getRealName());
			if (processId != null){
				Boolean isEntity = reviewProcessService.findReviewUserIsEntityByProcessIdAndUserName(processId, user.getLoginName());
				if (isEntity) comboBoxString.setSelected(true);
			}
			
			comboBoxStrings.add(comboBoxString);
		}
		return comboBoxStrings;
	}
	
	@RequestMapping(value = "/role")
	public @ResponseBody List<ComboBoxString> role(@RequestParam(value = "processId", required = false)Long processId){
		List<ComboBoxString> comboBoxStrings = new ArrayList<ComboBoxString>();
		List<Role> roles = accountService.getAllRole();
		ComboBoxString comboBoxString = null;
		for (Role role : roles){
			comboBoxString = new ComboBoxString();
			comboBoxString.setId(role.getRoleName());
			comboBoxString.setText(role.getRoleName());
			if (processId != null){
				Boolean isEntity = reviewProcessService.findReviewRoleIsEntityByProcessIdAndRoleName(processId, role.getRoleName());
				if (isEntity) comboBoxString.setSelected(true);
			}
			comboBoxStrings.add(comboBoxString);
		}
		return comboBoxStrings;
	}
}
