package com.ewcms.content.message.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
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
	
	@RequestMapping(value = "/delete")
	public String delete(@RequestParam(value = "selections") List<Long> selections){
		for (Long id : selections){
			msgReceiveService.delMsgReceive(id);
		}
		return "redirect:/content/message/receive/index";
	}
	
	@RequestMapping(value = "/markread")
	public @ResponseBody Boolean markRead(@RequestParam(value = "selections") List<Long> selections, @RequestParam(value = "read") Boolean read){
		Boolean result = false;
		try{
			msgReceiveService.markReadMsgReceive(selections.get(0), read);
			result = true;
		}catch(Exception e){
		}
		return result;
	}
	
	@RequestMapping(value = "/unread")
	public @ResponseBody List<MsgReceive> unRead(){
		List<MsgReceive> msgReceives = msgReceiveService.findMsgReceiveByUnRead();
		if (EmptyUtil.isCollectionNotEmpty(msgReceives)){
			return msgReceives;
		}else{
			return null;
		}
	}
}
