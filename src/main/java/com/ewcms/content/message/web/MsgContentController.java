package com.ewcms.content.message.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.content.message.model.MsgContent;
import com.ewcms.content.message.model.MsgSend;
import com.ewcms.content.message.service.MsgSendService;
import com.ewcms.security.service.ServiceException;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/message/content")
public class MsgContentController {
	
	@Autowired
	private MsgSendService msgSendService;
	
	@RequestMapping(value = "/delete")
	public @ResponseBody Boolean delete(@RequestParam(value = "selections") List<Long> selections){
		Boolean result = false;
		try{
			msgSendService.delSubscription(selections.get(0));
			result = true;
		}catch(Exception e){
		}
		return result;
	}
	
	@RequestMapping(value = "/edit/{msgSendId}", method = RequestMethod.GET)
	public String edit(@PathVariable(value="msgSendId")Long msgSendId, Model model) {
		MsgContent msgContent = new MsgContent();
		model.addAttribute("msgSendId", msgSendId);
		model.addAttribute("msgContent", msgContent);
		return "content/message/content/edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@RequestParam(required = false) List<Long> selections,@RequestParam(value = "msgSendId")Long msgSendId, @RequestParam(value = "title") String title, @RequestParam(value = "detail") String detail, Model model,RedirectAttributes redirectAttributes) {
		try {
			msgSendService.addSubscription(msgSendId, title, detail);
			selections = selections == null ? new ArrayList<Long>() : selections;
			selections.add(0, msgSendId);
			model.addAttribute("msgSend", new MsgSend());
			model.addAttribute("selections", selections);
			return "content/message/content/edit";
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
			return "redirect:/content/message/content/edit/" + msgSendId;
		}
	}

}
