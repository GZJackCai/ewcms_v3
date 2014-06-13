package com.ewcms.visit.vo.totality;

import java.io.Serializable;

import com.ewcms.visit.util.NumberUtil;

/**
 * 综合报告
 * 
 * @author 吴智俊
 */
public class SummaryVo implements Serializable {

	private static final long serialVersionUID = 6161830025242798489L;

	private String name;
	private Long countIp = 0L;
	private Long countUv = 0L;
	private Long sumPv = 0L; 
	private Long countRv = 0L;
	private Long sumRv = 0L;
	private Double avgTime = 0.0D; 
	
	private String betide;
	private Long maxCount = 0L;
	private String betideIp;
	private String betideUv;
	private String betidePv;
	
	public SummaryVo(){
	}
	
	public SummaryVo(String name, Long countIp, Long countUv, Long sumPv, Long countRv, Long sumRv, Double avgTime) {
		super();
		this.name = name;
		this.countIp = (countIp == null) ? 0 : countIp;
		this.countUv = (countUv == null) ? 0 : countUv;
		this.sumPv = (sumPv == null) ? 0 : sumPv;
		this.countRv = (countRv == null) ? 0 : countRv;
		this.sumRv = (sumRv == null) ? 0 : sumRv;
		this.avgTime = (avgTime == null) ? 0.0 : avgTime;
	}
	
	public SummaryVo(String betide, Long maxCount){
		super();
		this.betide = betide;
		this.maxCount = maxCount;
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

	public String getRate() {
		return NumberUtil.percentage(countRv, sumRv);
	}

	public void setRate(String rate) {
	}

	public String getBetideIp() {
		return betideIp;
	}

	public void setBetideIp(String betideIp) {
		this.betideIp = betideIp;
	}

	public String getBetideUv() {
		return betideUv;
	}

	public void setBetideUv(String betideUv) {
		this.betideUv = betideUv;
	}

	public String getBetidePv() {
		return betidePv;
	}

	public void setBetidePv(String betidePv) {
		this.betidePv = betidePv;
	}

	public String getBetide() {
		return betide;
	}

	public void setBetide(String betide) {
		this.betide = betide;
	}

	public Long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
	}
}
