package com.ewcms.visit.model.clientside;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * @author 吴智俊
 */
@Embeddable
public class ClientSidePk implements Serializable {

	private static final long serialVersionUID = 7442522082565204242L;
	
	public enum ClientSideType{
		OS("os"), 
		BROWSER("browser"),
		LANGUAGE("language"), 
		SCREEN("screen"), 
		COLORDEPTH("colordepth"), 
		JAVAENABLED("javaenabled"),
		FLASHVERSION("flashversion"),
		COOKIEENABLED("cookieenabled");
		
		private String description;
		
		private ClientSideType(String description){
			this.description = description;
		}
		
		public String getDescription(){
			return description;
		}
	}
	
	@Column(name = "site_id", nullable = false)
	private Long siteId;
	@Temporal(TemporalType.DATE)
	@Column(name = "visit_date", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date visitDate;
	@Enumerated(EnumType.STRING)
	@Column(name = "clientside_type")
	private ClientSideType clientSideType;
	@Column(name = "clientside_name")
	private String clientSideName;

	public ClientSidePk() {
	}
	
	public ClientSidePk(String clientSideName){
		super();
		this.clientSideName = clientSideName;
	}
	
	public ClientSidePk(Date visitDate){
		super();
		this.visitDate = visitDate;
	}
	
	public ClientSidePk(Date visitDate, Long siteId, ClientSideType clientSideType, String clientSideName){
		super();
		this.visitDate = visitDate;
		this.siteId = siteId;
		this.clientSideType = clientSideType;
		this.clientSideName = clientSideName;
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

	public String getClientSideName() {
		return clientSideName;
	}

	public void setClientSideName(String clientSideName) {
		this.clientSideName = clientSideName;
	}

	public ClientSideType getClientSideType() {
		return clientSideType;
	}

	public void setClientSideType(ClientSideType clientSideType) {
		this.clientSideType = clientSideType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientSideName == null) ? 0 : clientSideName.hashCode());
		result = prime * result
				+ ((clientSideType == null) ? 0 : clientSideType.hashCode());
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
		ClientSidePk other = (ClientSidePk) obj;
		if (clientSideName == null) {
			if (other.clientSideName != null)
				return false;
		} else if (!clientSideName.equals(other.clientSideName))
			return false;
		if (clientSideType != other.clientSideType)
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
