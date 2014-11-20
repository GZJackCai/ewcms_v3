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
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_visit_summary")
public class Summary implements Serializable {

	private static final long serialVersionUID = -5258029666364694973L;

	@EmbeddedId
	private SummaryPk summaryPk;
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
	@Column(name = "stickTime_sum")
	private Long stickTimeSum = 0L;
	@Transient
	private String name;
	@Transient
	private String betide;
	@Transient
	private String ipBetide;
	@Transient
	private String uniqueIdBetide;
	@Transient
	private String pageViewBetide;
	@Transient
	private Long maxCount;

	public Summary(){
	}
	
	public Summary(Long ipCount, Long uniqueIdCount, Long pageViewSum, Long rvFlagCount, Long rvFlagSum, Double stickTimeAvg) {
		super();
		this.ipCount = (ipCount == null) ? 0L : ipCount;
		this.uniqueIdCount = (uniqueIdCount == null) ? 0L : uniqueIdCount;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.rvFlagCount = (rvFlagCount == null) ? 0L : rvFlagCount;
		this.rvFlagSum = (rvFlagSum == null) ? 0L : rvFlagSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}

	public Summary(Long ipCount, Long uniqueIdCount, Long pageViewSum, Long rvFlagCount, Long rvFlagSum, Double stickTimeAvg, Long stickTimeSum) {
		super();
		this.ipCount = (ipCount == null) ? 0L : ipCount;
		this.uniqueIdCount = (uniqueIdCount == null) ? 0L : uniqueIdCount;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.rvFlagCount = (rvFlagCount == null) ? 0L : rvFlagCount;
		this.rvFlagSum = (rvFlagSum == null) ? 0L : rvFlagSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
		this.stickTimeSum = (stickTimeSum == null) ? 0L : stickTimeSum;
	}
	
	public Summary(Date visitDate, Long ipCount, Long uniqueIdCount, Long pageViewSum, Long rvFlagCount, Long rvFlagSum, Double stickTimeAvg) {
		super();
		if (summaryPk == null) summaryPk = new SummaryPk(visitDate);
		this.ipCount = (ipCount == null) ? 0L : ipCount;
		this.uniqueIdCount = (uniqueIdCount == null) ? 0L : uniqueIdCount;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.rvFlagCount = (rvFlagCount == null) ? 0L : rvFlagCount;
		this.rvFlagSum = (rvFlagSum == null) ? 0L : rvFlagSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}
	
	public Summary(Integer hour, Long ipCount, Long uniqueIdCount, Long pageViewSum, Long rvFlagCount, Long rvFlagSum, Double stickTimeAvg) {
		super();
		if (summaryPk == null) summaryPk = new SummaryPk(hour);
		this.ipCount = (ipCount == null) ? 0L : ipCount;
		this.uniqueIdCount = (uniqueIdCount == null) ? 0L : uniqueIdCount;
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.rvFlagCount = (rvFlagCount == null) ? 0L : rvFlagCount;
		this.rvFlagSum = (rvFlagSum == null) ? 0L : rvFlagSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}
	
	public Summary(String betide, Long maxCount){
		super();
		this.betide = betide;
		this.maxCount = (maxCount == null) ? 0L : maxCount;
	}
	
	public Summary(Date visitDate){
		super();
		if (summaryPk == null) summaryPk = new SummaryPk(visitDate);
	}
	
	public Summary(Integer hour){
		super();
		if (summaryPk == null) summaryPk = new SummaryPk(hour);
	}
	
	public SummaryPk getSummaryPk() {
		return summaryPk;
	}

	public void setSummaryPk(SummaryPk summaryPk) {
		this.summaryPk = summaryPk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getStickTimeSum() {
		return stickTimeSum;
	}

	public void setStickTimeSum(Long stickTimeSum) {
		this.stickTimeSum = stickTimeSum;
	}

	public String getBetide() {
		return betide;
	}

	public void setBetide(String betide) {
		this.betide = betide;
	}

	public String getIpBetide() {
		return ipBetide;
	}

	public void setIpBetide(String ipBetide) {
		this.ipBetide = ipBetide;
	}

	public String getUniqueIdBetide() {
		return uniqueIdBetide;
	}

	public void setUniqueIdBetide(String uniqueIdBetide) {
		this.uniqueIdBetide = uniqueIdBetide;
	}

	public String getPageViewBetide() {
		return pageViewBetide;
	}

	public void setPageViewBetide(String pageViewBetide) {
		this.pageViewBetide = pageViewBetide;
	}

	public String getRate() {
		return NumberUtil.percentage(rvFlagCount, rvFlagSum);
	}

	public Long getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Long maxCount) {
		this.maxCount = maxCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((summaryPk == null) ? 0 : summaryPk.hashCode());
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
		Summary other = (Summary) obj;
		if (summaryPk == null) {
			if (other.summaryPk != null)
				return false;
		} else if (!summaryPk.equals(other.summaryPk))
			return false;
		return true;
	}

}
