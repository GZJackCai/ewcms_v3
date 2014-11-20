package com.ewcms.visit.model.traffic;

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
@Table(name = "plugin_visit_urlclick")
public class UrlClick implements Serializable {

	private static final long serialVersionUID = 5431930420807803112L;

	@EmbeddedId
	private UrlClickPk urlClickPk;
	@Column(name = "url_count")
	private Long urlCount = 0L;
	@Transient
	private Long urlSum = 0L;
	
	public UrlClick(){
	}
	
	public UrlClick(Long urlCount){
		super();
		this.urlCount = (urlCount == null) ? 0L : urlCount;
	}
	
	public UrlClick(String url, Long urlCount, Long urlSum){
		super();
		if (urlClickPk == null) urlClickPk = new UrlClickPk(url);
		this.urlCount = (urlCount == null) ? 0L : urlCount;
		this.urlSum = (urlSum == null) ? 0L : urlSum;
	}
	
	public UrlClick(Date visitDate, Long urlCount){
		super();
		if (urlClickPk == null) urlClickPk = new UrlClickPk(visitDate);
		this.urlCount = (urlCount == null) ? 0L : urlCount;
	}

	public UrlClickPk getUrlClickPk() {
		return urlClickPk;
	}

	public void setUrlClickPk(UrlClickPk urlClickPk) {
		this.urlClickPk = urlClickPk;
	}

	public Long getUrlCount() {
		return urlCount;
	}

	public void setUrlCount(Long urlCount) {
		this.urlCount = urlCount;
	}

	public Long getUrlSum() {
		return urlSum;
	}

	public void setUrlSum(Long urlSum) {
		this.urlSum = urlSum;
	}

	public String getRate() {
		return NumberUtil.percentage(urlCount, urlSum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((urlClickPk == null) ? 0 : urlClickPk.hashCode());
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
		UrlClick other = (UrlClick) obj;
		if (urlClickPk == null) {
			if (other.urlClickPk != null)
				return false;
		} else if (!urlClickPk.equals(other.urlClickPk))
			return false;
		return true;
	}
}
