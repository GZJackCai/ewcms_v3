package com.ewcms.visit.vo.totality;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 全站点击率
 * 
 * @author 吴智俊
 */
public class SiteClickVo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Date visitDate;
	private Long countIp = 0L;
	private Long countUv = 0L;
	private Long sumPv = 0L;
	private Long countRv = 0L;
	private Long sumRv = 0L;
	private Double avgTime = 0.0D;

	public SiteClickVo(Date visitDate) {
		super();
		this.visitDate = visitDate;
	}

	public SiteClickVo(Date visitDate, Long countIp, Long countUv, Long sumPv, Long countRv, Long sumRv, Double avgTime) {
		super();
		this.visitDate = visitDate;
		this.countIp = countIp;
		this.countUv = countUv;
		this.sumPv = sumPv;
		this.countRv = countRv;
		this.sumRv = sumRv;
		this.avgTime = avgTime;
	}
	
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+08:00")
	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Long getCountIp() {
		return countIp;
	}

	public void setCountIp(Long countIp) {
		this.countIp = countIp;
	}

	public Long getCountUv() {
		return countUv;
	}

	public void setCountUv(Long countUv) {
		this.countUv = countUv;
	}

	public Long getSumPv() {
		return sumPv;
	}

	public void setSumPv(Long sumPv) {
		this.sumPv = sumPv;
	}

	public Long getCountRv() {
		return countRv;
	}

	public void setCountRv(Long countRv) {
		this.countRv = countRv;
	}

	public Long getSumRv() {
		return sumRv;
	}

	public void setSumRv(Long sumRv) {
		this.sumRv = sumRv;
	}

	public Double getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(Double avgTime) {
		this.avgTime = avgTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((visitDate == null) ? 0 : visitDate.hashCode());
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
		SiteClickVo other = (SiteClickVo) obj;
		if (visitDate == null) {
			if (other.visitDate != null)
				return false;
		} else if (!visitDate.equals(other.visitDate))
			return false;
		return true;
	}
}
