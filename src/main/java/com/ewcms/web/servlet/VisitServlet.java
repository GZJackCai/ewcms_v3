/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.web.servlet;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ewcms.util.EmptyUtil;
import com.ewcms.visit.model.Visit;
import com.ewcms.visit.model.VisitPk;
import com.ewcms.visit.service.VisitService;
import com.ewcms.visit.util.VisitUtil;

/**
 * @author wuzhijun
 */
public class VisitServlet extends HttpServlet {

	private static final long serialVersionUID = 8986742704742468271L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Visit v = new Visit();
		VisitPk pk = new VisitPk();
		
		pk.setVisitDate(new Date(Calendar.getInstance().getTime().getTime()));
		
		try {
			Long siteId = Long.parseLong(request.getParameter("siteId"));
			pk.setSiteId(siteId);

			Long channelId = Long.parseLong(request.getParameter("channelId"));
			v.setChannelId(channelId);
		} catch (Exception e) {
			return;
		}
		
        Long articleId =(EmptyUtil.isStringNotEmpty(request.getParameter("articleId")) ? Long.parseLong(request.getParameter("articleId")) : 0L);
		v.setArticleId(articleId);
		
		String uniqueID = VisitUtil.getCookieValue(request, "UniqueID");
		pk.setUniqueId(uniqueID);

		String IP = VisitUtil.getIP(request);
		pk.setIp(IP);
		if (EmptyUtil.isStringNotEmpty(IP) && IP.indexOf(",") > 0) {
			String arr[] = IP.split("\\,");
			for (int i = 0; i < arr.length; i++) {
				if (!EmptyUtil.isStringNotEmpty(arr[i])
						|| arr[i].trim().startsWith("10.")
						//|| arr[i].trim().startsWith("192.")
						)
					continue;
				pk.setIp(arr[i].trim());
				break;
			}
		}
		
		String url = request.getParameter("URL");
		if (EmptyUtil.isStringNotEmpty(url)) {
			url = url.replace('\'', '0').replace('\\', '0');
			String prefix = url.substring(0, 8);
			String tail = url.substring(8);
			tail = tail.replace("/+", "/");
			url = prefix + tail;
		}
		pk.setUrl(url);
		if (EmptyUtil.isStringEmpty(url)) return;
		
		Long stickTime = 10L;
		try {
			stickTime = new Double(Math.ceil(Double.parseDouble(request.getParameter("stickTime").trim()))).longValue();
		} catch (Exception e) {
		}
		v.setStickTime(Math.abs(stickTime));
		
		if ("KeepAlive".equalsIgnoreCase(request.getParameter("event"))) {
			v.setVisitPk(pk);
			findVisitService().addVisitByKeepAliveEvent(v);
			return;
		}
		
		String userAgent = request.getHeader("User-Agent");
		if (EmptyUtil.isStringEmpty(userAgent)) {
			userAgent = "Unknow";
		}
		v.setUserAgent(userAgent);

//		String referer = request.getParameter("Referer");
//		if (EmptyUtil.isStringNotEmpty(referer)) {
//			referer = referer.replace('\'', '0').replace('\\', '0');
//		}
//		v.setReferer(referer);
		
		Integer remotePort = request.getRemotePort();
		v.setRemotePort(remotePort);
		
		if (!"Unload".equalsIgnoreCase(request.getParameter("event"))) {
			try {
				String sites = VisitUtil.getCookieValue(request, "Sites");
				if (EmptyUtil.isStringEmpty(pk.getUniqueId())) {
					pk.setUniqueId(VisitUtil.getUniqueID());
					v.setRvFlag(false);
					VisitUtil.setCookieValue(request, response, "UniqueID", -1, pk.getUniqueId());
					VisitUtil.setCookieValue(request, response, "Sites", -1, "_" + pk.getSiteId());
				} else if (EmptyUtil.isStringNotEmpty(sites) && sites.indexOf("_" + pk.getSiteId()) >= 0) {
					v.setRvFlag(true);
				} else {
					v.setRvFlag(false);
					VisitUtil.setCookieValue(request, response, "Sites", -1, sites + "_" + pk.getSiteId());
				}
				v.setHost(request.getParameter("Host"));
				if (EmptyUtil.isStringNotEmpty(v.getHost())) {
					v.setHost(v.getHost().toLowerCase());
				} else {
					v.setHost("无");
				}
				String country = request.getParameter("country");
				String province = request.getParameter("province");
				String city = request.getParameter("city");
				
				v.setCountry(country);
				if (EmptyUtil.isStringEmpty(country)){
					v.setCountry("未知");
				}
				
				v.setProvince(province);
				if (EmptyUtil.isStringEmpty(province)){
					v.setProvince("未知");
				}
				
				v.setCity(city);
				if (EmptyUtil.isStringEmpty(city)){
					v.setCity("未知");
				}
				v.setCookieEnabled("1".equals(request.getParameter("ce")));
				v.setFlashVersion(request.getParameter("fv"));
				v.setFlashEnabled(EmptyUtil.isNotNull(v.getFlashVersion()));
				v.setJavaEnabled("1".equals(request.getParameter("je")));
				v.setLanguage(VisitUtil.getLanguage(request.getParameter("la")));
				if (v.getLanguage().equals("其他")) {
					v.setLanguage(VisitUtil.getLanguage("; "
							+ request.getHeader("accept-language") + ";"));
				}
				v.setOs(VisitUtil.getOS(v.getUserAgent()));
				v.setBrowser(VisitUtil.getBrowser(v.getUserAgent()));
				
				String referer = request.getParameter("Referer");
				if (EmptyUtil.isStringNotEmpty(referer)) {
					referer = referer.replace('\'', '0').replace('\\', '0');
				}
				v.setReferer(referer);

				v.setScreen(request.getParameter("sr"));
				v.setScreen(VisitUtil.getScreen(v.getScreen()));
				v.setColorDepth(request.getParameter("cd"));
				
				v.setFrequency(Long.parseLong(request.getParameter("vq")));
				
				v.setVisitPk(pk);
				
				findVisitService().addVisitByLoadEvent(v);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			v.setVisitPk(pk);
			findVisitService().addVisitByUnloadEvent(v);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private VisitService findVisitService(){
		ServletContext application = getServletContext();
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(application);
		VisitService visitService = (VisitService) wac.getBean("visitService");
		return visitService;
	}
}
