package com.ewcms.content.message.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.content.message.model.MsgReceive;
import com.ewcms.content.message.service.MsgReceiveService;
import com.ewcms.util.EmptyUtil;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/message/receive")
public class MsgReceiveController {
	
	@Autowired
	private MsgReceiveService msgReceiveService;
	
	@ModelAttribute
	public void init(Model model) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("true", "是");
		map.put("false", "否");
		model.addAttribute("bolMap", map);
	}
	
	@RequestMapping(value = "/index")
	public String index(){
		return "content/message/receive/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@ModelAttribute QueryParameter params){
		return msgReceiveService.search(params);
	}
	
	@RequestMapping(value = "/delete/{username}")
	public String delete(@PathVariable(value = "username")String username, @RequestParam(value = "selections") List<Long> selections){
		msgReceiveService.delMsgReceive(username, selections);
		return "redirect:/content/message/receive/index";
	}
	
	@RequestMapping(value = "/markread/{username}")
	public @ResponseBody Boolean markRead(@PathVariable(value = "username")String username, @RequestParam(value = "selections") List<Long> selections, @RequestParam(value = "read") Boolean read){
		Boolean result = false;
		try{
			msgReceiveService.markReadMsgReceive(username, selections.get(0), read);
			result = true;
		}catch(Exception e){
		}
		return result;
	}
	
	@RequestMapping(value = "/unread/{username}")
	public @ResponseBody List<MsgReceive> unRead(@PathVariable(value = "username")String username){
		List<MsgReceive> msgReceives = msgReceiveService.findMsgReceiveByUnRead(username);
		if (EmptyUtil.isCollectionNotEmpty(msgReceives)){
			return msgReceives;
		}else{
			return null;
		}
	}
}
