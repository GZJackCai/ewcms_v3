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
@Table(name = "plugin_visit_accessfrequency")
public class AccessFrequency implements Serializable {

	private static final long serialVersionUID = -1502088590474260205L;

	@EmbeddedId
	private AccessFrequencyPk accessFrequencyPk;
	@Column(name = "depth_count")
	private Long frequencyCount = 0L;
	@Transient
	private Long frequencySum = 0L;

	public AccessFrequency() {
	}
	
	public AccessFrequency(Long frequencyCount){
		super();
		this.frequencyCount = (frequencyCount == null) ? 0L : frequencyCount;
	}
	
	public AccessFrequency(Long frequency, Long frequencyCount, Long frequencySum){
		super();
		if (accessFrequencyPk == null) accessFrequencyPk = new AccessFrequencyPk(frequency);
		this.frequencyCount = (frequencyCount == null) ? 0L : frequencyCount;
		this.frequencySum = (frequencySum == null) ? 0L : frequencySum;
	}
	
	public AccessFrequency(Date visitDate, Long frequencyCount){
		super();
		if (accessFrequencyPk == null) accessFrequencyPk = new AccessFrequencyPk(visitDate);
		this.frequencyCount = (frequencyCount == null) ? 0L : frequencyCount;
	}

	public AccessFrequencyPk getAccessFrequencyPk() {
		return accessFrequencyPk;
	}

	public void setAccessFrequencyPk(AccessFrequencyPk accessFrequencyPk) {
		this.accessFrequencyPk = accessFrequencyPk;
	}

	public Long getFrequencyCount() {
		return frequencyCount;
	}

	public void setFrequencyCount(Long frequencyCount) {
		this.frequencyCount = frequencyCount;
	}

	public Long getFrequencySum() {
		return frequencySum;
	}

	public void setFrequencySum(Long frequencySum) {
		this.frequencySum = frequencySum;
	}

	public String getRate() {
		return NumberUtil.percentage(frequencyCount, frequencySum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((accessFrequencyPk == null) ? 0 : accessFrequencyPk
						.hashCode());
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
		AccessFrequency other = (AccessFrequency) obj;
		if (accessFrequencyPk == null) {
			if (other.accessFrequencyPk != null)
				return false;
		} else if (!accessFrequencyPk.equals(other.accessFrequencyPk))
			return false;
		return true;
	}
}
