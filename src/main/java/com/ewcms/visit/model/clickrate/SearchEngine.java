package com.ewcms.visit.model.clickrate;

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
@Table(name = "plugin_visit_searchengine")
public class SearchEngine implements Serializable {

	private static final long serialVersionUID = 159143108307621948L;

	@EmbeddedId
	private SearchEnginePk searchEnginePk;
	@Column(name = "engine_count")
	private Long engineCount = 0L;
	@Transient
	private Long engineSum = 0L;
	
	public SearchEngine(){
	}
	
	public SearchEngine(SearchEnginePk searchEnginePk){
		super();
		this.searchEnginePk = searchEnginePk;
	}
	
	public SearchEngine(String engineName, Long engineCount, Long engineSum){
		super();
		if (searchEnginePk == null) searchEnginePk = new SearchEnginePk(engineName);
		this.engineCount = (engineCount == null) ? 0L : engineCount;
		this.engineSum = (engineSum == null) ? 0L : engineSum;
	}
	
	public SearchEngine(Date visitDate, Long engineCount){
		super();
		if (searchEnginePk == null) searchEnginePk = new SearchEnginePk(visitDate);
		this.engineCount = (engineCount == null) ? 0L : engineCount;
	}

	public SearchEnginePk getSearchEnginePk() {
		return searchEnginePk;
	}

	public void setSearchEnginePk(SearchEnginePk searchEnginePk) {
		this.searchEnginePk = searchEnginePk;
	}

	public Long getEngineCount() {
		return engineCount;
	}

	public void setEngineCount(Long engineCount) {
		this.engineCount = engineCount;
	}
	
	public Long getEngineSum() {
		return engineSum;
	}

	public void setEngineSum(Long engineSum) {
		this.engineSum = engineSum;
	}

	public String getRate(){
		return NumberUtil.percentage(engineCount, engineSum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((searchEnginePk == null) ? 0 : searchEnginePk.hashCode());
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
		SearchEngine other = (SearchEngine) obj;
		if (searchEnginePk == null) {
			if (other.searchEnginePk != null)
				return false;
		} else if (!searchEnginePk.equals(other.searchEnginePk))
			return false;
		return true;
	}
}
