package com.ewcms.content.message.web;

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

import com.ewcms.content.message.model.MsgSend;
import com.ewcms.content.message.service.MsgSendService;
import com.ewcms.security.model.User;
import com.ewcms.security.service.AccountService;
import com.ewcms.security.service.ServiceException;
import com.ewcms.util.Collections3;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.vo.ComboBoxString;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/message/send")
public class MsgSendController {

	@Autowired
	private MsgSendService msgSendService;
	@Autowired
	private AccountService accountService;
	
	@ModelAttribute
	public void init(Model model) {
		model.addAttribute("sendTypeMap", MsgSend.Type.values());
	}
	
	@RequestMapping(value = "/index")
	public String index(){
		return "content/message/send/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params){
		return msgSendService.search(params);
	}
	
	@RequestMapping(value = "/delete")
	public String delete(@RequestParam(value = "selections") List<Long> selections){
		for (Long id : selections){
			msgSendService.delMsgSend(id);
		}
		return "redirect:/content/message/send/index";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		MsgSend msgSend = new MsgSend();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			msgSend = msgSendService.findMsgSend(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("msgSend", msgSend);
		return "content/message/send/edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "msgSend") MsgSend msgSend, @RequestParam(required = false) List<Long> selections, @RequestParam(value="content", required = false) String content, @RequestParam(value="userNames", required = false) List<String> userNames, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Boolean close = Boolean.FALSE;
			if (msgSend.getId() != null && StringUtils.hasText(msgSend.getId().toString())) {
				msgSendService.updMsgSend(msgSend);
				selections.remove(0);
				if (selections == null || selections.isEmpty()) {
					close = Boolean.TRUE;
				} else {
					msgSend = msgSendService.findMsgSend(selections.get(0));
					model.addAttribute("msgSend", msgSend);
					model.addAttribute("selections", selections);
				}
			} else {
				msgSendService.addMsgSend(msgSend, content, userNames);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, msgSend.getId());
				model.addAttribute("msgSend", new MsgSend());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close", close);
			return "content/message/send/edit";
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/content/message/send/edit";
		}
	}

	
	@RequestMapping(value = "/user")
	public @ResponseBody List<ComboBoxString> user(@RequestParam(value = "msgSendId", defaultValue = "-1")Long msgSendId){
		List<String> userNames = msgSendService.findMsgReceiveUser(msgSendId);
		String userName = "";
		if (userNames != null && !userNames.isEmpty()){
			userName = Collections3.convertToString(userNames, ",");
		}
		
		List<ComboBoxString> comboBoxStrings = new ArrayList<ComboBoxString>();
		List<User> users = accountService.getAllUser();
		ComboBoxString comboBoxString = null;
		for (User user : users){
			comboBoxString = new ComboBoxString();
			comboBoxString.setId(user.getLoginName());
			comboBoxString.setText(user.getRealName());
			if (user.getLoginName().indexOf(userName) > 0){
				comboBoxString.setSelected(true);
			}
			
			comboBoxStrings.add(comboBoxString);
		}
		return comboBoxStrings;
	}
}
