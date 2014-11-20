package com.ewcms.visit.model.clientside;

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
@Table(name = "plugin_visit_clientside")
public class ClientSide implements Serializable{

	private static final long serialVersionUID = -5622798143666692139L;
	
	@EmbeddedId
	private ClientSidePk clientSidePk;
	@Column(name = "clientside_count")
	private Long clientSideCount = 0L;
	@Transient
	private Long clientSideSum = 0L; 
	
	public ClientSide(){
	}
	
	public ClientSide(ClientSidePk clientSidePk){
		super();
		this.clientSidePk = clientSidePk;
	}
	
	public ClientSide(String clientSideName, Long clientSideCount, Long clientSideSum){
		super();
		if (clientSidePk == null) clientSidePk = new ClientSidePk(clientSideName);
		this.clientSideCount = (clientSideCount == null) ? 0L : clientSideCount;
		this.clientSideSum = (clientSideSum == null) ? 0L : clientSideSum;
	}
	
	public ClientSide(Date visitDate, Long clientSideCount){
		super();
		if (clientSidePk == null) clientSidePk = new ClientSidePk(visitDate);
		this.clientSideCount = (clientSideCount == null) ? 0L : clientSideCount;
	}

	public ClientSidePk getClientSidePk() {
		return clientSidePk;
	}

	public void setClientSidePk(ClientSidePk clientSidePk) {
		this.clientSidePk = clientSidePk;
	}

	public Long getClientSideCount() {
		return clientSideCount;
	}

	public void setClientSideCount(Long clientSideCount) {
		this.clientSideCount = clientSideCount;
	}

	public Long getClientSideSum() {
		return clientSideSum;
	}

	public void setClientSideSum(Long clientSideSum) {
		this.clientSideSum = clientSideSum;
	}

	public String getRate(){
		return NumberUtil.percentage(clientSideCount, clientSideSum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((clientSidePk == null) ? 0 : clientSidePk.hashCode());
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
		ClientSide other = (ClientSide) obj;
		if (clientSidePk == null) {
			if (other.clientSidePk != null)
				return false;
		} else if (!clientSidePk.equals(other.clientSidePk))
			return false;
		return true;
	}
}
