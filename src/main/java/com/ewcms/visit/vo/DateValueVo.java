package com.ewcms.visit.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 吴智俊
 */
public class DateValueVo implements Serializable {

	private static final long serialVersionUID = -7804271606441105526L;

	private Date date;
	private Long value;
	
	public DateValueVo(Date date, Long value){
		super();
		this.date = date;
		this.value = value;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}
}
