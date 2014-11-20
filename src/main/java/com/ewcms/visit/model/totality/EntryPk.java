package com.ewcms.visit.model.totality;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 
 * @author wu_zhijun
 *
 */
@Embeddable
public class EntryPk implements Serializable {

	private static final long serialVersionUID = 4041123551492587325L;

	@Column(name = "site_id", nullable = false)
	private Long siteId;
	@Temporal(TemporalType.DATE)
	@Column(name = "visit_date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date visitDate;
	@Column(name = "url", columnDefinition = "text")
	private String url;
	@Column(name = "event")
	private String event;

	public EntryPk() {
	}
	
	public EntryPk(String url){
		super();
		this.url = url;
	}
	
	public EntryPk(Date visitDate){
		super();
		this.visitDate = visitDate;
	}
	
	public EntryPk(Date visitDate, Long siteId, String url, String event){
		super();
		this.visitDate = visitDate;
		this.siteId = siteId;
		this.url = url;
		this.event = event;
	}
	
	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	@JSONField(format = "yyyy-MM-dd")
	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((event == null) ? 0 : event.hashCode());
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result
				+ ((visitDate == null) ? 0 : visitDate.hashCode());
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
		EntryPk other = (EntryPk) obj;
		if (event == null) {
			if (other.event != null)
				return false;
		} else if (!event.equals(other.event))
			return false;
		if (siteId == null) {
			if (other.siteId != null)
				return false;
		} else if (!siteId.equals(other.siteId))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (visitDate == null) {
			if (other.visitDate != null)
				return false;
		} else if (!visitDate.equals(other.visitDate))
			return false;
		return true;
	}
}
