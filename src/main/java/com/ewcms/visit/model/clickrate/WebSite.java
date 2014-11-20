package com.ewcms.visit.model.clickrate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.ewcms.visit.util.NumberUtil;

/**
 *
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_visit_website")
public class WebSite implements Serializable {

	private static final long serialVersionUID = 159143108307621948L;

	@EmbeddedId
	private WebSitePk webSitePk;
	@Column(name = "website_count")
	private Long webSiteCount = 0L;
	@Transient
	private Long webSiteSum = 0L;
	
	public WebSite(){
	}
	
	public WebSite(WebSitePk webSitePk){
		super();
		this.webSitePk = webSitePk;
	}
	
	public WebSite(String webSiteName, Long webSiteCount, Long webSiteSum){
		super();
		if (webSitePk == null) webSitePk = new WebSitePk(webSiteName);
		this.webSiteCount = (webSiteCount == null) ? 0L : webSiteCount;
		this.webSiteSum = (webSiteSum == null) ? 0L : webSiteSum;
	}
	
	public WebSite(Date visitDate, Long webSiteCount){
		super();
		if (webSitePk == null) webSitePk = new WebSitePk(visitDate);
		this.webSiteCount = (webSiteCount == null) ? 0L : webSiteCount;
	}

	public WebSitePk getWebSitePk() {
		return webSitePk;
	}

	public void setWebSitePk(WebSitePk webSitePk) {
		this.webSitePk = webSitePk;
	}

	public Long getWebSiteCount() {
		return webSiteCount;
	}

	public void setWebSiteCount(Long webSiteCount) {
		this.webSiteCount = webSiteCount;
	}

	public Long getWebSiteSum() {
		return webSiteSum;
	}

	public void setWebSiteSum(Long webSiteSum) {
		this.webSiteSum = webSiteSum;
	}

	public String getRate(){
		return NumberUtil.percentage(webSiteCount, webSiteSum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((webSitePk == null) ? 0 : webSitePk.hashCode());
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
		WebSite other = (WebSite) obj;
		if (webSitePk == null) {
			if (other.webSitePk != null)
				return false;
		} else if (!webSitePk.equals(other.webSitePk))
			return false;
		return true;
	}
}
