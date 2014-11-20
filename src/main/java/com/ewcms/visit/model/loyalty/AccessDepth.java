package com.ewcms.visit.model.loyalty;

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
@Table(name = "plugin_visit_accessdepth")
public class AccessDepth implements Serializable {

	private static final long serialVersionUID = 1094992559663789338L;

	@EmbeddedId
	private AccessDepthPk accessDepthPk;
	@Column(name = "depth_count")
	private Long depthCount = 0L;
	@Transient
	private Long depthSum = 0L;

	public AccessDepth() {
	}
	
	public AccessDepth(Long depthCount){
		super();
		this.depthCount = (depthCount == null) ? 0L : depthCount;
	}
	
	public AccessDepth(Long depth, Long depthCount, Long depthSum){
		super();
		if (accessDepthPk == null) accessDepthPk = new AccessDepthPk(depth);
		this.depthCount = (depthCount == null) ? 0L : depthCount;
		this.depthSum = (depthSum == null) ? 0L : depthSum;
	}
	
	public AccessDepth(Date visitDate, Long depthCount){
		super();
		if (accessDepthPk == null) accessDepthPk = new AccessDepthPk(visitDate);
		this.depthCount = (depthCount == null) ? 0L : depthCount;
	}
	
	public AccessDepthPk getAccessDepthPk() {
		return accessDepthPk;
	}

	public void setAccessDepthPk(AccessDepthPk accessDepthPk) {
		this.accessDepthPk = accessDepthPk;
	}

	public Long getDepthCount() {
		return depthCount;
	}

	public void setDepthCount(Long depthCount) {
		this.depthCount = depthCount;
	}

	public Long getDepthSum() {
		return depthSum;
	}

	public void setDepthSum(Long depthSum) {
		this.depthSum = depthSum;
	}
	
	public String getRate() {
		return NumberUtil.percentage(depthCount, depthSum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessDepthPk == null) ? 0 : accessDepthPk.hashCode());
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
		AccessDepth other = (AccessDepth) obj;
		if (accessDepthPk == null) {
			if (other.accessDepthPk != null)
				return false;
		} else if (!accessDepthPk.equals(other.accessDepthPk))
			return false;
		return true;
	}
}
