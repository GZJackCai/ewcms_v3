package com.ewcms.visit.model.totality;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author wu_zhijun
 *
 */
@Entity
@Table(name = "plugin_visit_online")
public class Online implements Serializable {

	private static final long serialVersionUID = -418099724947879349L;

	@EmbeddedId
	private OnlinePk onlinePk;
	@Column(name = "five_count")
	private Long fiveCount = 0L;
	@Column(name = "ten_count")
	private Long tenCount = 0L;
	@Column(name = "fifteen_count")
	private Long fifteenCount = 0L;
	
	public Online() {
	}
	
	public Online(Integer hour){
		super();
		if (onlinePk == null) onlinePk = new OnlinePk(hour);
	}
	
	public Online(Long fiveCount, Long tenCount, Long fifteenCount) {
		super();
		this.fiveCount = (fiveCount == null) ? 0L : fiveCount;
		this.tenCount = (tenCount == null) ? 0L : tenCount;
		this.fifteenCount = (fifteenCount == null) ? 0L : fifteenCount;
	}

	public Online(Integer hour, Long fiveCount, Long tenCount, Long fifteenCount) {
		super();
		if (onlinePk == null) onlinePk = new OnlinePk(hour);
		this.fiveCount = (fiveCount == null) ? 0L : fiveCount;
		this.tenCount = (tenCount == null) ? 0L : tenCount;
		this.fifteenCount = (fifteenCount == null) ? 0L : fifteenCount;
	}

	public OnlinePk getOnlinePk() {
		return onlinePk;
	}

	public void setOnlinePk(OnlinePk onlinePk) {
		this.onlinePk = onlinePk;
	}

	public Long getFiveCount() {
		return fiveCount;
	}

	public void setFiveCount(Long fiveCount) {
		this.fiveCount = fiveCount;
	}

	public Long getTenCount() {
		return tenCount;
	}

	public void setTenCount(Long tenCount) {
		this.tenCount = tenCount;
	}

	public Long getFifteenCount() {
		return fifteenCount;
	}

	public void setFifteenCount(Long fifteenCount) {
		this.fifteenCount = fifteenCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((onlinePk == null) ? 0 : onlinePk.hashCode());
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
		Online other = (Online) obj;
		if (onlinePk == null) {
			if (other.onlinePk != null)
				return false;
		} else if (!onlinePk.equals(other.onlinePk))
			return false;
		return true;
	}

}
