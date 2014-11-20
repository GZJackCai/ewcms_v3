package com.ewcms.visit.model.loyalty;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * @author 吴智俊
 */
public class StickTime implements Serializable {

	private static final long serialVersionUID = 3406392487927869231L;

	private Date visitDate;
	private Long stickTimeSum;
	private Double stickTimeAvg;
	
	public StickTime(){
	}
	
	public StickTime(Date visitDate, Long stickTimeSum, Double stickTimeAvg){
		super();
		this.visitDate = visitDate;
		this.stickTimeSum = (stickTimeSum == null) ? 0L : stickTimeSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}

	@JSONField(format = "yyyy-MM-dd")
	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Long getStickTimeSum() {
		return stickTimeSum;
	}

	public void setStickTimeSum(Long stickTimeSum) {
		this.stickTimeSum = stickTimeSum;
	}

	public Double getStickTimeAvg() {
		return stickTimeAvg;
	}

	public void setStickTimeAvg(Double stickTimeAvg) {
		this.stickTimeAvg = stickTimeAvg;
	}

}
