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
@Table(name = "plugin_visit_entry")
public class Entry implements Serializable {

	private static final long serialVersionUID = -1941536337013387260L;

	@EmbeddedId
	private EntryPk entryPk;
	@Column(name = "entry_count")
	private Long entryCount = 0L;
	@Transient
	private Long entrySum = 0L;

	public Entry() {
	}
	
	public Entry(Long entryCount){
		super();
		this.entryCount = (entryCount == null) ? 0L : entryCount;
	}
	
	public Entry(String url, Long entryCount, Long entrySum){
		super();
		if (entryPk == null) entryPk = new EntryPk(url);
		this.entryCount = (entryCount == null) ? 0L : entryCount;
		this.entrySum = (entrySum == null) ? 0L : entrySum;
	}
	
	public Entry(Date visitDate, Long entryCount){
		super();
		if (entryPk == null) entryPk = new EntryPk(visitDate);
		this.entryCount = (entryCount == null) ? 0L : entryCount;
	}
	
	public EntryPk getEntryPk() {
		return entryPk;
	}

	public void setEntryPk(EntryPk entryPk) {
		this.entryPk = entryPk;
	}

	public Long getEntryCount() {
		return entryCount;
	}

	public void setEntryCount(Long entryCount) {
		this.entryCount = entryCount;
	}

	public Long getEntrySum() {
		return entrySum;
	}

	public void setEntrySum(Long entrySum) {
		this.entrySum = entrySum;
	}

	public String getRate() {
		return NumberUtil.percentage(entryCount, entrySum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entryPk == null) ? 0 : entryPk.hashCode());
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
		Entry other = (Entry) obj;
		if (entryPk == null) {
			if (other.entryPk != null)
				return false;
		} else if (!entryPk.equals(other.entryPk))
			return false;
		return true;
	}

}
