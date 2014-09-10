package com.ewcms.web;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ewcms.content.document.service.ArticleMainService;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.util.XMLUtil;

/**
 * 
 * @author wuzhijun
 * 
 */
@Controller
@RequestMapping(value = "/fcf")
public class FcfController {

	@Autowired
	private ArticleMainService articleMainService;
	
	@RequestMapping(value = "/createArticle/{year}")
	public void createArticle(@PathVariable(value = "year")Integer year, HttpServletResponse response) throws Exception {
		if (EwcmsContextUtil.getCurrentSite() == null) return;
		Long siteId = EwcmsContextUtil.getCurrentSiteId();
		
		XMLUtil xml = new XMLUtil();
		Element graph = xml.addRoot("graph");
		// xml.addAttribute(graph, "caption", "文章编辑数");
		//xml.addAttribute(graph, "subCaption", getYear().toString());
		xml.addAttribute(graph, "basefontsize", "12");
		// xml.addAttribute(graph, "xAxisName", "月份");
		xml.addAttribute(graph, "showNames", "1");
		xml.addAttribute(graph, "decimalPrecision", "0");// 小数精确度，0为精确到个位
		// xml.addAttribute(graph, "showValues", "0");// 在报表上不显示数值
		xml.addAttribute(graph, "formatNumberScale", "0");
		Map<Integer, Long> map = articleMainService.findCreateArticleFcfChart(year, siteId);
		Iterator<Entry<Integer, Long>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Long> m = (Map.Entry<Integer, Long>)it.next();
			Integer key = m.getKey();
			Long total = (Long)map.get(key);
			Element set = xml.addNode(graph, "set");
			set.addAttribute("name", String.format("%02d",key) + "月");
			set.addAttribute("value", total.toString());
			//set.addAttribute("color", Integer.toHexString((int) (Math.random() * 255 * 255 * 255)).toUpperCase());
			set.addAttribute("color", "FF0000");
		}
		writer(response, xml.getXML());
	}
	
	@RequestMapping(value = "/publishArticle/{year}")
	public void publishArticle(@PathVariable(value = "year")Integer year, HttpServletResponse response) throws Exception{
		if (EwcmsContextUtil.getCurrentSite() == null) return;
		Long siteId = EwcmsContextUtil.getCurrentSiteId();
		
		XMLUtil xml = new XMLUtil();
		Element graph = xml.addRoot("graph");
		xml.addAttribute(graph, "basefontsize", "12");
		xml.addAttribute(graph, "showNames", "1");
		xml.addAttribute(graph, "decimalPrecision", "0");
		xml.addAttribute(graph, "formatNumberScale", "0");
		Map<Integer, Long> map = articleMainService.findReleaseArticleFcfChart(year, siteId);
		Iterator<Entry<Integer, Long>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Long> m = (Map.Entry<Integer, Long>)it.next();
			Integer key = m.getKey();
			Long total = (Long)map.get(key);
			Element set = xml.addNode(graph, "set");
			set.addAttribute("name", String.format("%02d",key) + "月");
			set.addAttribute("value", total.toString());
			//set.addAttribute("color", Integer.toHexString((int) (Math.random() * 255 * 255 * 255)).toUpperCase());
			set.addAttribute("color", "FF0000");
		}
		writer(response, xml.getXML());
	}
	
	@RequestMapping(value = "/publishPerson/{year}")
	public void publishPerson(@PathVariable(value = "year")Integer year, HttpServletResponse response) throws Exception{
		if (EwcmsContextUtil.getCurrentSite() == null) return;
		Long siteId = EwcmsContextUtil.getCurrentSiteId();

		XMLUtil xml = new XMLUtil();
		Element graph = xml.addRoot("graph");
		xml.addAttribute(graph, "basefontsize", "12");
		xml.addAttribute(graph, "showNames", "1");
		xml.addAttribute(graph, "decimalPrecision", "0");
		xml.addAttribute(graph, "formatNumberScale", "0");
		Map<String, Long> map = articleMainService.findReleaseArticlePersonFcfChart(year, siteId);
		Iterator<Entry<String, Long>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, Long> m = (Map.Entry<String, Long>)it.next();
			String key = m.getKey();
			Long total = (Long)map.get(key);
			Element set = xml.addNode(graph, "set");
			set.addAttribute("name", key);
			set.addAttribute("value", total.toString());
			set.addAttribute("color", Integer.toHexString((int) (Math.random() * 255 * 255 * 255)).toUpperCase());
		}
		
		writer(response, xml.getXML());
	}
	
	private void writer(HttpServletResponse response, String html){
		response.setHeader("Content-type", "text/html; charset=utf-8");
		//Http 1.0 header
		response.setDateHeader("Expires", 0);
		response.addHeader("Pragma", "no-cache");
		//Http 1.1 header
		response.setHeader("Cache-Control", "no-cache");
		try {
			response.getWriter().write(html);
			response.getWriter().flush();
		} catch (IOException e) {
		}
	}
}
