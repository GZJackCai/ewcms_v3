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
@Table(name = "plugin_visit_region")
public class Region implements Serializable {

	private static final long serialVersionUID = -4851053329012454249L;

	@EmbeddedId
	private RegionPk regionPk;
	@Column(name = "pageView_sum")
	private Long pageViewSum = 0L;
	@Column(name = "uniqueId_count")
	private Long uniqueIdCount = 0L;
	@Column(name = "ip_count")
	private Long ipCount = 0L;
	@Column(name = "rvFlag_count")
	private Long rvFlagCount = 0L;
	@Column(name = "rvFlag_sum")
	private Long rvFlagSum = 0L;
	@Column(name = "stickTime_avg")
	private Double stickTimeAvg = 0D;
	@Transient
	private String rate;
	
	public Region() {
	}

	public Region(String country, Long pageViewSum){
		super();
		if (regionPk == null) regionPk = new RegionPk(country);
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
	}
	
	public Region(Long ipCount, Long uniqueIdCount, Long pageViewSum, Long rvFlagCount, Long rvFlagSum, Double stickTimeAvg){
		super();
		this.ipCount = (ipCount == null) ? 0L : ipCount;
		this.uniqueIdCount = (uniqueIdCount == null) ? 0L : uniqueIdCount;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.rvFlagCount = (rvFlagCount == null) ? 0L : rvFlagCount;
		this.rvFlagSum = (rvFlagSum == null) ? 0L : rvFlagSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}
	
	public Region(String country, Long ipCount, Long uniqueIdCount, Long pageViewSum, Long rvFlagCount, Long rvFlagSum, Double stickTimeAvg){
		super();
		if (regionPk == null) regionPk = new RegionPk(country);
		this.ipCount = (ipCount == null) ? 0L : ipCount;
		this.uniqueIdCount = (uniqueIdCount == null) ? 0L : uniqueIdCount;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.rvFlagCount = (rvFlagCount == null) ? 0L : rvFlagCount;
		this.rvFlagSum = (rvFlagSum == null) ? 0L : rvFlagSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}
	
	public Region(String country, String province, Long ipCount, Long uniqueIdCount, Long pageViewSum, Long rvFlagCount, Long rvFlagSum, Double stickTimeAvg){
		super();
		if (regionPk == null) regionPk = new RegionPk(country, province);
		this.ipCount = (ipCount == null) ? 0L : ipCount;
		this.uniqueIdCount = (uniqueIdCount == null) ? 0L : uniqueIdCount;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.rvFlagCount = (rvFlagCount == null) ? 0L : rvFlagCount;
		this.rvFlagSum = (rvFlagSum == null) ? 0L : rvFlagSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}
	
	public Region(String country, String province, String city, Long ipCount, Long uniqueIdCount, Long pageViewSum, Long rvFlagCount, Long rvFlagSum, Double stickTimeAvg){
		super();
		if (regionPk == null) regionPk = new RegionPk(country, province, city);
		this.ipCount = (ipCount == null) ? 0L : ipCount;
		this.uniqueIdCount = (uniqueIdCount == null) ? 0L : uniqueIdCount;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.rvFlagCount = (rvFlagCount == null) ? 0L : rvFlagCount;
		this.rvFlagSum = (rvFlagSum == null) ? 0L : rvFlagSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}
	
	public Region(Date visitDate, Long ipCount, Long uniqueIdCount, Long pageViewSum, Long rvFlagCount, Long rvFlagSum, Double stickTimeAvg){
		super();
		if (regionPk == null) regionPk = new RegionPk(visitDate);
		this.ipCount = (ipCount == null) ? 0L : ipCount;
		this.uniqueIdCount = (uniqueIdCount == null) ? 0L : uniqueIdCount;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.rvFlagCount = (rvFlagCount == null) ? 0L : rvFlagCount;
		this.rvFlagSum = (rvFlagSum == null) ? 0L : rvFlagSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}
	
	public RegionPk getRegionPk() {
		return regionPk;
	}

	public void setRegionPk(RegionPk regionPk) {
		this.regionPk = regionPk;
	}

	public Long getPageViewSum() {
		return pageViewSum;
	}

	public void setPageViewSum(Long pageViewSum) {
		this.pageViewSum = pageViewSum;
	}

	public Long getUniqueIdCount() {
		return uniqueIdCount;
	}

	public void setUniqueIdCount(Long uniqueIdCount) {
		this.uniqueIdCount = uniqueIdCount;
	}

	public Long getIpCount() {
		return ipCount;
	}

	public void setIpCount(Long ipCount) {
		this.ipCount = ipCount;
	}

	public Long getRvFlagCount() {
		return rvFlagCount;
	}

	public void setRvFlagCount(Long rvFlagCount) {
		this.rvFlagCount = rvFlagCount;
	}

	public Long getRvFlagSum() {
		return rvFlagSum;
	}

	public void setRvFlagSum(Long rvFlagSum) {
		this.rvFlagSum = rvFlagSum;
	}

	public Double getStickTimeAvg() {
		return stickTimeAvg;
	}

	public void setStickTimeAvg(Double stickTimeAvg) {
		this.stickTimeAvg = stickTimeAvg;
	}

	public String getRate() {
		return NumberUtil.percentage(rvFlagCount, rvFlagSum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((regionPk == null) ? 0 : regionPk.hashCode());
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
		Region other = (Region) obj;
		if (regionPk == null) {
			if (other.regionPk != null)
				return false;
		} else if (!regionPk.equals(other.regionPk))
			return false;
		return true;
	}

}
