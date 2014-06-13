package com.ewcms.visit.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_visit_summary")
public class Summary implements Serializable {

	private static final long serialVersionUID = -5258029666364694973L;

	@EmbeddedId
	private SiteDatePk siteDatePk;
	@Column(name = "pv_count")
	private Long pvCount;
	@Column(name = "uv_count")
	private Long uvCount;
	@Column(name = "ip_count")
	private Long ipCount;
	@Column(name = "rv_rate")
	private Double rvRate;
	@Column(name = "avg_stick_time")
	private Double avgStickTime;

	public SiteDatePk getSiteDatePk() {
		return siteDatePk;
	}

	public void setSiteDatePk(SiteDatePk siteDatePk) {
		this.siteDatePk = siteDatePk;
	}

	public Long getPvCount() {
		return pvCount;
	}

	public void setPvCount(Long pvCount) {
		this.pvCount = pvCount;
	}

	public Long getUvCount() {
		return uvCount;
	}

	public void setUvCount(Long uvCount) {
		this.uvCount = uvCount;
	}

	public Long getIpCount() {
		return ipCount;
	}

	public void setIpCount(Long ipCount) {
		this.ipCount = ipCount;
	}

	public Double getRvRate() {
		return rvRate;
	}

	public void setRvRate(Double rvRate) {
		this.rvRate = rvRate;
	}

	public Double getAvgStickTime() {
		return avgStickTime;
	}

	public void setAvgStickTime(Double avgStickTime) {
		this.avgStickTime = avgStickTime;
	}
}
