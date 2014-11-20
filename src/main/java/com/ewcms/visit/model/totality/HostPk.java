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
public class HostPk implements Serializable{

	private static final long serialVersionUID = 916545748313221698L;

	@Temporal(TemporalType.DATE)
	@Column(name = "visit_date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date visitDate;
	@Column(name = "site_id", nullable = false)
	private Long siteId;
	@Column(name = "host")
	private String hostName;
	
	public HostPk() {
	}
	
	public HostPk(String hostName){
		super();
		this.hostName = hostName;
	}
	
	public HostPk(Date visitDate){
		super();
		this.visitDate = visitDate;
	}
	
	public HostPk(Date visitDate, Long siteId, String host){
		super();
		this.visitDate = visitDate;
		this.siteId = siteId;
		this.hostName = host;
	}
	
	@JSONField(format = "yyyy-MM-dd")
	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostName == null) ? 0 : hostName.hashCode());
		result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
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
		HostPk other = (HostPk) obj;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		if (siteId == null) {
			if (other.siteId != null)
				return false;
		} else if (!siteId.equals(other.siteId))
			return false;
		if (visitDate == null) {
			if (other.visitDate != null)
				return false;
		} else if (!visitDate.equals(other.visitDate))
			return false;
		return true;
	}
}
