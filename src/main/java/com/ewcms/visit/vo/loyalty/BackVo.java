package com.ewcms.visit.vo.loyalty;

import java.io.Serializable;
import java.util.Date;

import com.ewcms.visit.util.NumberUtil;

/**
 * 回头率
 * 
 * @author 吴智俊
 */
public class BackVo implements Serializable {

	private static final long serialVersionUID = -3232254951186524626L;

	private Date visitDate;
	private Long newVisitor;
	private Long backVisitor;
	
	public BackVo(Date visitDate, Long newVisitor, Long backVisitor) {
		super();
		this.visitDate = visitDate;
		this.newVisitor = newVisitor;
		this.backVisitor = backVisitor;
	}

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

	public String getRate(){
		return NumberUtil.percentage(backVisitor, newVisitor + backVisitor);
	}
}
