package com.ewcms.visit.model.totality;

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
 * @author wu_zhijun
 *
 */
@Entity
@Table(name = "plugin_visit_host")
public class Host implements Serializable {

	private static final long serialVersionUID = -6918809043318135376L;

	@EmbeddedId
	private HostPk hostPk;
	@Column(name = "host_count")
	private Long hostCount = 0L;
	@Transient
	private Long hostSum = 0L;

	public Host() {
	}
	
	public Host(Long hostCount){
		super();
		this.hostCount = (hostCount == null) ? 0L : hostCount;
	}
	
	public Host(String hostName, Long hostCount, Long hostSum){
		super();
		if (hostPk == null) hostPk = new HostPk(hostName);
		this.hostCount = (hostCount == null) ? 0L : hostCount;
		this.hostSum = (hostSum == null) ? 0L : hostSum;
	}
	
	public Host(Date visitDate, Long hostCount){
		super();
		if (hostPk == null) hostPk = new HostPk(visitDate);
		this.hostCount = (hostCount == null) ? 0L : hostCount;
	}
	
	public HostPk getHostPk() {
		return hostPk;
	}

	public void setHostPk(HostPk hostPk) {
		this.hostPk = hostPk;
	}

	public Long getHostCount() {
		return hostCount;
	}

	public void setHostCount(Long hostCount) {
		this.hostCount = hostCount;
	}

	public Long getHostSum() {
		return hostSum;
	}

	public void setHostSum(Long hostSum) {
		this.hostSum = hostSum;
	}

	public String getRate() {
		return NumberUtil.percentage(hostCount, hostSum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hostPk == null) ? 0 : hostPk.hashCode());
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
		Host other = (Host) obj;
		if (hostPk == null) {
			if (other.hostPk != null)
				return false;
		} else if (!hostPk.equals(other.hostPk))
			return false;
		return true;
	}

}
