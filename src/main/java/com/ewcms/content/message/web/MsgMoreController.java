package com.ewcms.content.message.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.content.message.model.MsgSend;
import com.ewcms.content.message.service.MsgSendService;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/message/more")
public class MsgMoreController {
	@Autowired
	private MsgSendService msgSendService;
	
	@RequestMapping(value = "/index")
	public String index(){
		return "content/message/more/index";
	}
	
	@RequestMapping(value = "/query")
	public @ResponseBody Map<String, Object> query(@RequestParam(value = "type") MsgSend.Type type, @ModelAttribute QueryParameter params){
		return msgSendService.searchMore(params, type);
	}
}
