package com.ewcms.visit.model.clickrate;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_visit_sourceform")
public class SourceForm implements Serializable {

	private static final long serialVersionUID = -6389249570243049479L;

	@EmbeddedId
	private SourceFormPk sourceFormPk;
	@Column(name = "direct_count")
	private Long directCount = 0L;
	@Column(name = "search_count")
	private Long searchCount = 0L;
	@Column(name = "other_count")
	private Long otherCount = 0L;
	
	public SourceForm(){
	}
	
	public SourceForm(SourceFormPk sourceFormPk){
		super();
		this.sourceFormPk = sourceFormPk;
	}
	
	public SourceForm(Date visitDate, Long directCount, Long searchCount, Long otherCount){
		super();
		if (sourceFormPk == null) sourceFormPk = new SourceFormPk(visitDate);
		this.directCount = (directCount == null) ? 0L : directCount;
		this.searchCount = (searchCount == null) ? 0L : searchCount;
		this.otherCount = (otherCount == null) ? 0L : otherCount;
	}
	
	public SourceForm(Long directCount, Long searchCount, Long otherCount){
		super();
		this.directCount = (directCount == null) ? 0L : directCount;
		this.searchCount = (searchCount == null) ? 0L : searchCount;
		this.otherCount = (otherCount == null) ? 0L : otherCount;
	}
	
	public SourceFormPk getSourceFormPk() {
		return sourceFormPk;
	}

	public void setSourceFormPk(SourceFormPk sourceFormPk) {
		this.sourceFormPk = sourceFormPk;
	}

	public Long getDirectCount() {
		return directCount;
	}

	public void setDirectCount(Long directCount) {
		this.directCount = directCount;
	}

	public Long getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Long searchCount) {
		this.searchCount = searchCount;
	}

	public Long getOtherCount() {
		return otherCount;
	}

	public void setOtherCount(Long otherCount) {
		this.otherCount = otherCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((sourceFormPk == null) ? 0 : sourceFormPk.hashCode());
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
		SourceForm other = (SourceForm) obj;
		if (sourceFormPk == null) {
			if (other.sourceFormPk != null)
				return false;
		} else if (!sourceFormPk.equals(other.sourceFormPk))
			return false;
		return true;
	}
}
