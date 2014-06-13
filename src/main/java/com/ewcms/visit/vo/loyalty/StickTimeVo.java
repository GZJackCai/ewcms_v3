package com.ewcms.visit.vo.loyalty;

import java.io.Serializable;
import java.util.Date;

/**
 * 停留时间
 * 
 * @author 吴智俊
 */
public class StickTimeVo implements Serializable {

	private static final long serialVersionUID = 7902115207730185276L;

	private Date visitDate;
	private Long sumSt;
	private Double avgSt;

	public StickTimeVo(Date visitDate, Long sumSt, Double avgSt) {
		super();
		this.visitDate = visitDate;
		this.sumSt = sumSt;
		this.avgSt = avgSt;
	}

	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Long getSumSt() {
		return sumSt;
	}

	public void setSumSt(Long sumSt) {
		this.sumSt = sumSt;
	}

	public Double getAvgSt() {
		return avgSt;
	}

	public void setAvgSt(Double avgSt) {
		this.avgSt = avgSt;
	}
}
