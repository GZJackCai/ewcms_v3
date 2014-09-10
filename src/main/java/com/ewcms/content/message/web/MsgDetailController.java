package com.ewcms.content.message.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ewcms.content.message.model.MsgContent;
import com.ewcms.content.message.model.MsgReceive;
import com.ewcms.content.message.model.MsgSend;
import com.ewcms.content.message.service.MsgReceiveService;
import com.ewcms.content.message.service.MsgSendService;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/message/detail")
public class MsgDetailController {
	
	@Autowired
	private MsgReceiveService msgRecieveService;
	@Autowired
	private MsgSendService msgSendService;

	@RequestMapping(value = "/index/{username}_{id}_{type}")
	public String index(@PathVariable(value = "username")String username, @PathVariable(value = "id") Long id, @PathVariable(value = "type") String type, Model model){
		if (type.toLowerCase().equals("notice")){
			MsgSend msgSend = msgSendService.findMsgSend(id);
			model.addAttribute("title", msgSend.getTitle());
			model.addAttribute("detail", msgSend.getMsgContents().get(0).getDetail());
			model.addAttribute("results", null);
		}else if (type.toLowerCase().equals("subscription")){
			MsgSend msgSend = msgSendService.findMsgSend(id);
			model.addAttribute("title", msgSend.getTitle());
			List<MsgContent> msgContents = msgSend.getMsgContents();
			List<String> details = new ArrayList<String>();
			for (MsgContent msgContent : msgContents){
				details.add(msgContent.getDetail());
			}
			model.addAttribute("results", details);
		}else if (type.toLowerCase().equals("message")){
			MsgReceive msgReceive = msgRecieveService.findMsgReceive(id);
			model.addAttribute("title", msgReceive.getMsgContent().getTitle());
			model.addAttribute("detail", msgReceive.getMsgContent().getDetail());
			model.addAttribute("results", null);
			msgRecieveService.readMsgReceive(username, id);
		}
		return "content/message/detail/index";
	}
}
