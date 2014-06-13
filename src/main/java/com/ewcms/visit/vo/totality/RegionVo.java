package com.ewcms.visit.vo.totality;

import java.io.Serializable;
import java.util.Date;

import com.ewcms.visit.util.NumberUtil;

/**
 * 区域分布
 * 
 * @author 吴智俊
 */
public class RegionVo implements Serializable {

	private static final long serialVersionUID = 8698528761589248644L;

	private Date visitDate;
	private String name;
	private Long countIp = 0L;
	private Long countUv = 0L;
	private Long sumPv = 0L;
	private Double avgTime = 0.0D;
	private Long totalPv = 0L;

	public RegionVo(String name, Long countIp, Long countUv, Long sumPv, Double avgTime,Long totalPv) {
		super();
		this.name = name;
		this.countIp = countIp;
		this.countUv = countUv;
		this.sumPv = sumPv;
		this.avgTime = avgTime;
		this.totalPv = totalPv;
	}
	
	public RegionVo(Date visitDate, Long countIp, Long countUv, Long sumPv) {
		super();
		this.visitDate = visitDate;
		this.countIp = countIp;
		this.countUv = countUv;
		this.sumPv = sumPv;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Long getTotalPv() {
		return totalPv;
	}

	public void setTotalPv(Long totalPv) {
		this.totalPv = totalPv;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Double getAvgTime() {
		return avgTime;
	}

	public void setAvgTime(Double avgTime) {
		this.avgTime = avgTime;
	}
	
	public String getRate() {
		return NumberUtil.percentage(sumPv, totalPv);
	}

}
