package com.ewcms.content.message.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/message")
public class MsgIndexController {

	@RequestMapping(value = "/index")
	public String index(){
		return "content/message/index";
	}
}
