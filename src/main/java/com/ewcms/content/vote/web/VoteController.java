package com.ewcms.content.vote.web;

import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/vote")
public class VoteController {
	
	@RequestMapping(value = "/index")
	public String index(){
		return "content/vote/index";
	}
}
