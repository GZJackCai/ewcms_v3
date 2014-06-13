package com.ewcms.visit.vo.totality;

import java.io.Serializable;

/**
 * 时间分布
 * 
 * @author 吴智俊
 */
public class TimeDistributedVo implements Serializable {

	private static final long serialVersionUID = -6028769456851157960L;

	private Integer hour;
	private Long countIp = 0L;
	private Long countUv = 0L;
	private Long sumPv = 0L;
	private Long countRv = 0L;
	private Long sumRv = 0L;
	private Double avgTime = 0.0D;

	public TimeDistributedVo(Integer hour) {
		super();
		this.hour = hour;
	}

	public TimeDistributedVo(Integer hour, Long countIp, Long countUv, Long sumPv, Long countRv, Long sumRv, Double avgTime) {
		super();
		this.hour = hour;
		this.countIp = countIp;
		this.countUv = countUv;
		this.sumPv = sumPv;
		this.countRv = countRv;
		this.sumRv = sumRv;
		this.avgTime = avgTime;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}
	
	public String getHourExpression(){
		return String.format("%1$02d:00 - %1$02d:59", hour);
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
		result = prime * result + ((hour == null) ? 0 : hour.hashCode());
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
		TimeDistributedVo other = (TimeDistributedVo) obj;
		if (hour == null) {
			if (other.hour != null)
				return false;
		} else if (!hour.equals(other.hour))
			return false;
		return true;
	}
}
