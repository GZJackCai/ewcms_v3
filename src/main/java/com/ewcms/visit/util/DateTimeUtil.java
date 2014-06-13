/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.visit.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.visit.vo.totality.OnlineVo;
import com.ewcms.visit.vo.totality.SiteClickVo;
import com.ewcms.visit.vo.totality.TimeDistributedVo;

/**
 * 
 * @author wu_zhijun
 *
 */
public class DateTimeUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);
	
	private static final SimpleDateFormat DF = new SimpleDateFormat("yyyy-MM-dd");
	
	public static Calendar getCalendar(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	public static Date getThisWeekMonday() {
		Calendar calendar = getCalendar();
		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		calendar.add(Calendar.DATE, -day_of_week);
		return calendar.getTime();
	}
	
	public static Date getThisWeekSunday() {
		Calendar calendar = getCalendar();

		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		calendar.add(Calendar.DATE, -day_of_week);
		calendar.add(Calendar.DATE, 6);
		
		return calendar.getTime();
	}
	
	
	public static Date getThisMonthFirst(){
		Calendar calendar = getCalendar();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}
	
	public static Date getThisMonthLast(){
		Calendar calendar = getCalendar();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		return calendar.getTime();
	}
	
	public static String getDateToString(Date date){
		return DF.format(date);
	}
	
	/**
	 * 取日期区间集合
	 * 
	 * @return
	 */
	public static List<String> getDateAreaList(Date startDate, Date endDate) {
		List<String> list = new ArrayList<String>();
		try {
			Long mid = endDate.getTime() - startDate.getTime() + 1;
			int day = (int) (mid / (1000 * 60 * 60 * 24));

			list.add(getDateToString(startDate));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			for (int i = 0; i < day; i++) {
				calendar.add(Calendar.DATE, 1);
				list.add(getDateToString(calendar.getTime()));
			}
		} catch (Exception e) {
			logger.warn("日期转换错误");
		}
		return list;
	}

	/**
	 * 取日期区间集合
	 * 
	 * @return
	 */
	public static Map<String, Long> getDateAreaLongMap(Date startDate, Date endDate) {
		Map<String, Long> map = new TreeMap<String, Long>();
		try {
			Long mid = endDate.getTime() - startDate.getTime() + 1;
			int day = (int) (mid / (1000 * 60 * 60 * 24));

			map.put(DF.format(startDate), 0L);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			for (int i = 0; i < day; i++) {
				calendar.add(Calendar.DATE, 1);
				map.put(DF.format(calendar.getTime()), 0L);
			}
		} catch (Exception e) {
			logger.warn("日期转换错误");
		}
		return map;
	}
	
	public static Map<String, String> getDateAreaStringMap(Date startDate, Date endDate) {
		Map<String, String> map = new TreeMap<String, String>();
		try {
			Long mid = endDate.getTime() - startDate.getTime() + 1;
			int day = (int) (mid / (1000 * 60 * 60 * 24));

			map.put(DF.format(startDate), "0");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			for (int i = 0; i < day; i++) {
				calendar.add(Calendar.DATE, 1);
				map.put(DF.format(calendar.getTime()), "0");
			}
		} catch (Exception e) {
			logger.warn("日期转换错误");
		}
		return map;
	}
	
	public static List<SiteClickVo> getDateAreaSiteClickList(Date startDate, Date endDate){
		List<SiteClickVo> siteClickVos = new ArrayList<SiteClickVo>();
		try {
			Long mid = endDate.getTime() - startDate.getTime() + 1;
			int day = (int) (mid / (1000 * 60 * 60 * 24));

			siteClickVos.add(new SiteClickVo(endDate));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			for (int i = day; i > 0; i--) {
				calendar.add(Calendar.DATE, -1);
				siteClickVos.add(new SiteClickVo(calendar.getTime()));
			}
		} catch (Exception e) {
			logger.warn("日期转换错误");
		}
		return siteClickVos;
	}
	
	public static List<SiteClickVo> getDateAreaSiteClickList(Date startDate, Date endDate, Integer rows, Integer page){
		List<SiteClickVo> siteClickVos = getDateAreaSiteClickList(startDate, endDate);
		if (page <= 0) {
			page = 0;
		}else{
			page = page - 1;
		}
		Integer begin = 0;
		Integer end = 0;
		
		begin = page * rows;
		end = (page + 1) * rows;
		
		if (siteClickVos.size() < end){
			end = siteClickVos.size();
		}
		
		return siteClickVos.subList(begin, end);
	}
	
	public static Long days(Date startDate, Date endDate){
		Long mid = endDate.getTime() - startDate.getTime() + 1;
		return (Long) (mid / (1000 * 60 * 60 * 24));
	}
	
	public static List<TimeDistributedVo> getTimeAreaTimeDistributedList(){
		List<TimeDistributedVo> timeDistributedVos = new ArrayList<TimeDistributedVo>();
		for (int i = 0; i <= 23; i++){
			timeDistributedVos.add(new TimeDistributedVo(i));
		}
		return timeDistributedVos;
	}
	
	public static List<OnlineVo> getTimeAreaOnlineList(){
		List<OnlineVo> onlineVos = new ArrayList<OnlineVo>();
		for (int i = 0; i <= 23; i++){
			onlineVos.add(new OnlineVo(i));
		}
		return onlineVos;
	}
	
	public static Map<String, Long> getTimeAreaMap(){
		Map<String, Long> map = new TreeMap<String, Long>();
		for (int i = 0; i <= 23; i++){
			map.put(formatTimeArea(i), 0L);
		}
		return map;
	}
	
	public static List<String> getTimeAreaList(){
		List<String> list = new ArrayList<String>();
		for (int i = 0; i <= 23; i++){
			list.add(formatTimeArea(i));
		}
		return list;
	}

	public static String formatTimeArea(Integer hour){
		return String.format("%02d", hour);
	}
}
