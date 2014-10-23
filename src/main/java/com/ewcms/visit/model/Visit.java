/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.visit.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 访问统计
 * 
 * <ul>
 * <li>uniqueID:编号</li>
 * <li>screen:分辨率</li>
 * <li>colorDepth:色彩深度</li>
 * <li>language:语言</li>
 * <li>userAgent:用户代理</li>
 * <li>os:操作系统</li>
 * <li>javaEnabled:java是否启用</li>
 * <li>flashEnabled:flash是否启用</li>
 * <li>flashVersion:flash版本</li>
 * <li>cookieEnable:cookie是否启用</li>
 * <li>browser:浏览器</li>
 * <li>ip:ip</li>
 * <li>country:国家</li>
 * <li>province:省份</li>
 * <li>city:城市</li>
 * <li>siteId:站点编号</li>
 * <li>addDate:访问日期</li>
 * <li>rvFlag:回头标志</li>
 * </ul>
 * 
 * @author wuzhijun
 * 
 */
@Entity
@Table(name = "plugin_visit")
@SequenceGenerator(name = "seq_plugin_visit", sequenceName = "seq_plugin_visit_id", allocationSize = 1)
public class Visit implements Serializable {

	private static final long serialVersionUID = -4173049848036627669L;

	@Id
	@GeneratedValue(generator = "seq_plugin_visit", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@Column(name = "unique_id")
	private String uniqueId;
	@Column(name = "screen")
	private String screen;
	@Column(name = "color_depth")
	private String colorDepth;
	@Column(name = "language")
	private String language;
	@Column(name = "user_agent")
	private String userAgent;
	@Column(name = "os")
	private String os;
	@Column(name = "java_enabled")
	private Boolean javaEnabled;
	@Column(name = "flash_enabled")
	private Boolean flashEnabled;
	@Column(name = "falsh_version")
	private String flashVersion;
	@Column(name = "cookie_enabled")
	private Boolean cookieEnabled;
	@Column(name = "browser")
	private String browser;
	@Column(name = "ip")
	private String ip;
	@Column(name = "country")
	private String country;
	@Column(name = "province")
	private String province;
	@Column(name = "city")
	private String city;
	@Column(name = "rv_flag")
	private Boolean rvFlag;
	@Temporal(TemporalType.DATE)
	@Column(name = "visit_date", columnDefinition = "Date default CURRENT_DATE", insertable = false, updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date visitDate;
	@Temporal(TemporalType.TIME)
	@Column(name = "visit_time", columnDefinition = "TIME default CURRENT_TIME", insertable = false, updatable = false)
	@DateTimeFormat(pattern = "HH:mm:ss")
	private Date visitTime;
	@Column(name = "stick_time")
	private Long stickTime;
	@Column(name = "page_view")
	private Long pageView;
	@Column(name = "channel_id")
	private Long channelId;
	@Column(name = "article_id")
	private Long articleId;
	@Column(name = "event")
	private String event;
	@Column(name = "host")
	private String host;
	@Column(name = "frequency")
	private Long frequency;
	@Column(name = "site_id")
	private Long siteId;
	@Column(name = "url")
	private String url;
	@Column(name = "referer", columnDefinition = "text")
	private String referer;
	@Column(name = "depth")
	private Long depth;
	@Column(name = "remote_port")
	private Integer remotePort;

	public Visit(){
		host = "无";
		rvFlag = false;
		country = "未知";
		province = "未知";
		city = "未知";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getColorDepth() {
		return colorDepth;
	}

	public void setColorDepth(String colorDepth) {
		this.colorDepth = colorDepth;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public Boolean getJavaEnabled() {
		return javaEnabled;
	}

	public void setJavaEnabled(Boolean javaEnabled) {
		this.javaEnabled = javaEnabled;
	}

	public Boolean getFlashEnabled() {
		return flashEnabled;
	}

	public void setFlashEnabled(Boolean flashEnabled) {
		this.flashEnabled = flashEnabled;
	}

	public String getFlashVersion() {
		return flashVersion;
	}

	public void setFlashVersion(String flashVersion) {
		this.flashVersion = flashVersion;
	}

	public Boolean getCookieEnabled() {
		return cookieEnabled;
	}

	public void setCookieEnabled(Boolean cookieEnabled) {
		this.cookieEnabled = cookieEnabled;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Boolean getRvFlag() {
		return rvFlag;
	}

	public void setRvFlag(Boolean rvFlag) {
		this.rvFlag = rvFlag;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	@JSONField(format = "yyyy-MM-dd")
	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	@JSONField(format = "HH:mm:ss")
	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	public Long getStickTime() {
		return stickTime;
	}

	public void setStickTime(Long stickTime) {
		this.stickTime = stickTime;
	}

	public Long getPageView() {
		return pageView;
	}

	public void setPageView(Long pageView) {
		this.pageView = pageView;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Long getFrequency() {
		return frequency;
	}

	public void setFrequency(Long frequency) {
		this.frequency = frequency;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public Long getDepth() {
		return depth;
	}

	public void setDepth(Long depth) {
		this.depth = depth;
	}

	public Integer getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(Integer remotePort) {
		this.remotePort = remotePort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Visit other = (Visit) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
