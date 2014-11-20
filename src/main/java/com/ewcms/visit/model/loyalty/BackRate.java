package com.ewcms.visit.model.loyalty;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.visit.util.NumberUtil;

/**
 *
 * @author 吴智俊
 */
public class BackRate implements Serializable {

	private static final long serialVersionUID = -4045195077031612430L;

	private Date visitDate;
	private Long newVisitor = 0L;
	private Long backVisitor = 0L;
	private Long sumVisitor = 0L;
	private Double rate = 0D;
	
	public BackRate(){
	}
	
	public BackRate(Date visitDate, Long newVisitor, Long sumVisitor){
		super();
		this.visitDate = visitDate;
		this.newVisitor = (newVisitor == null) ? 0L : newVisitor;
		this.sumVisitor = (sumVisitor == null) ? 0L : sumVisitor;
		this.backVisitor = this.sumVisitor - this.newVisitor;
		this.rate = (this.sumVisitor > 0) ? new Double(NumberUtil.round(this.backVisitor * 100.0D / (this.sumVisitor), 2)) : 0D;
	}
	
	@JSONField(format = "yyyy-MM-dd")
	public Date getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(Date visitDate) {
		this.visitDate = visitDate;
	}

	public Long getNewVisitor() {
		return newVisitor;
	}

	public void setNewVisitor(Long newVisitor) {
		this.newVisitor = newVisitor;
	}

	public Long getBackVisitor() {
		return backVisitor;
	}

	public void setBackVisitor(Long backVisitor) {
		this.backVisitor = backVisitor;
	}

	public Long getSumVisitor() {
		return sumVisitor;
	}

	public void setSumVisitor(Long sumVisitor) {
	}

	public Double getRate() {
		return rate;
	}

	public void setkRate(Double rate) {
	}
}
