package com.ewcms.visit.vo.totality;

import java.io.Serializable;
import java.util.Date;

/**
 * 访问记录
 * 
 * @author 吴智俊
 */
public class LastVisitVo implements Serializable {

	private static final long serialVersionUID = -5203537499008312131L;

	private String ipValue;
	private String host;
	private String country;
	private String province;
	private String city;
	private String url;
	private Date visitDate;
	private Date visitTime;
	private String referer;
	private String browser;
	private String os;
	private String screen;
	private String language;
	private String flashVersion;

	public LastVisitVo(String ipValue, String host, String country, String province, String city, String url, Date visitDate,
			Date visitTime, String referer, String browser, String os,
			String screen, String language, String flashVersion) {
		super();
		this.ipValue = ipValue;
		this.host = host;
		this.country = country;
		this.province = province;
		this.city = city;
		this.url = url;
		this.visitDate = visitDate;
		this.visitTime = visitTime;
		this.referer = referer;
		this.browser = browser;
		this.os = os;
		this.screen = screen;
		this.language = language;
		this.flashVersion = flashVersion;
	}
	
	public String getIpValue() {
		return ipValue;
	}

	public void setIpValue(String ipValue) {
		this.ipValue = ipValue;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Date getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(Date visitTime) {
		this.visitTime = visitTime;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public String getBrowser() {
		return browser;
	}

	public void setBrowser(String browser) {
		this.browser = browser;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getFlashVersion() {
		return flashVersion;
	}

	public void setFlashVersion(String flashVersion) {
		this.flashVersion = flashVersion;
	}
}
