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

import com.ewcms.visit.model.totality.Online;
import com.ewcms.visit.model.totality.Summary;

//import com.ewcms.visit.vo.totality.OnlineVo;
//import com.ewcms.visit.vo.totality.SiteClickVo;
//import com.ewcms.visit.vo.totality.TimeDistributedVo;

/**
 * 
 * @author wu_zhijun
 *
 */
public class DateTimeUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DateTimeUtil.class);
	
	private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	public static Calendar getCalendar(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	/**
	 * 获取当前日期(使用默认格式)
	 * 
	 * @return
	 */
	public static Date getCurrent(){
		return getCurrent(DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 获取当前日期(使用自定义格式)
	 * 
	 * @param format
	 * @return
	 */
	public static Date getCurrent(String format) {
		SimpleDateFormat simple = new SimpleDateFormat(format);
		Date current = new Date(Calendar.getInstance().getTime().getTime());
		try {
			current = simple.parse(simple.format(current));
		} catch (Exception e) {
		}
		return current;
	}
	
	/**
	 * 获取昨天日期(使用默认值)
	 * 
	 * @return
	 */
	public static Date getYesterday(){
		return getYesterday(DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 获取昨天日期(使用自定义值)
	 * 
	 * @param format
	 * @return
	 */
	public static Date getYesterday(String format){
		SimpleDateFormat simple = new SimpleDateFormat(format);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, - 1);
		Date date = calendar.getTime();
		try {
			date = simple.parse(simple.format(date));
		} catch (Exception e) {
		}
		return date;
	}
	
	/**
	 * 获取小时(默认当前时间)
	 * 
	 * @return
	 */
	public static Integer getHour(){
		return getHour(null);
	}
	
	/**
	 * 获取小时(传入时间)
	 * 
	 * @param time 时间
	 * @return
	 */
	public static Integer getHour(Date time){
		Calendar calendar = Calendar.getInstance();
		if (time != null){
			calendar.setTime(time);
		}
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public static Date getThisWeekMonday() {
		Calendar calendar = getCalendar();
		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		calendar.add(Calendar.DATE, - day_of_week);
		return calendar.getTime();
	}
	
	public static Date getThisWeekSunday() {
		Calendar calendar = getCalendar();

		int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 2;
		calendar.add(Calendar.DATE, - day_of_week);
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
	
	/**
	 * 日期型转换成字符串型(使用默认格式)
	 * 
	 * @param date 日期
	 * @return
	 */
	public static String getDateToString(Date date){
		return getDateToString(date, DEFAULT_DATE_FORMAT);
	}
	
	/**
	 * 日期型转换成字符串型(使用自定义格式)
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String getDateToString(Date date, String format){
		SimpleDateFormat simple = new SimpleDateFormat(format);
		return simple.format(date);
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
		SimpleDateFormat simple = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		
		Map<String, Long> map = new TreeMap<String, Long>();
		try {
			Long mid = endDate.getTime() - startDate.getTime() + 1;
			int day = (int) (mid / (1000 * 60 * 60 * 24));

			map.put(simple.format(startDate), 0L);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			for (int i = 0; i < day; i++) {
				calendar.add(Calendar.DATE, 1);
				map.put(simple.format(calendar.getTime()), 0L);
			}
		} catch (Exception e) {
			logger.warn("日期转换错误");
		}
		return map;
	}
	
	public static Map<String, String> getDateAreaStringMap(Date startDate, Date endDate) {
		SimpleDateFormat simple = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		Map<String, String> map = new TreeMap<String, String>();
		try {
			Long mid = endDate.getTime() - startDate.getTime() + 1;
			int day = (int) (mid / (1000 * 60 * 60 * 24));

			map.put(simple.format(startDate), "0");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(startDate);
			for (int i = 0; i < day; i++) {
				calendar.add(Calendar.DATE, 1);
				map.put(simple.format(calendar.getTime()), "0");
			}
		} catch (Exception e) {
			logger.warn("日期转换错误");
		}
		return map;
	}
	
	public static List<Summary> getDateAreaSiteClickList(Date startDate, Date endDate){
		List<Summary> summarys = new ArrayList<Summary>();
		try {
			Long mid = endDate.getTime() - startDate.getTime() + 1;
			int day = (int) (mid / (1000 * 60 * 60 * 24));

			summarys.add(new Summary(endDate));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(endDate);
			for (int i = day; i > 0; i--) {
				calendar.add(Calendar.DATE, -1);
				summarys.add(new Summary(calendar.getTime()));
			}
		} catch (Exception e) {
			logger.warn("日期转换错误");
		}
		return summarys;
	}
	
	public static List<Summary> getDateAreaSiteClickList(Date startDate, Date endDate, Integer rows, Integer page){
		List<Summary> siteClickVos = getDateAreaSiteClickList(startDate, endDate);
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
	
	public static List<Summary> getTimeAreaTimeDistributedList(){
		List<Summary> timeDistributedVos = new ArrayList<Summary>();
		for (int i = 0; i <= 23; i++){
			timeDistributedVos.add(new Summary(i));
		}
		return timeDistributedVos;
	}
	
	public static List<Online> getTimeAreaOnlineList(){
		List<Online> onlineVos = new ArrayList<Online>();
		for (int i = 0; i <= 23; i++){
			onlineVos.add(new Online(i));
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
