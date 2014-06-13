package com.ewcms.visit.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.visit.util.VisitTreeUtil;
import com.ewcms.web.vo.TreeNode;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/visit")
public class VisitTreeController {
	
	@RequestMapping(value = "/index")
	public String index(){
		return "visit/index";
	}
	
	@RequestMapping(value = "/tree")
	public @ResponseBody List<TreeNode> tree() {
		return VisitTreeUtil.showTree();
	}
}
