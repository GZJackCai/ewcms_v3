package com.ewcms.visit.vo;

import java.io.Serializable;

/**
 * 来源
 * 
 * @author 吴智俊
 */
public class RefererVo implements Serializable {

	private static final long serialVersionUID = 6949544213195648435L;

	private String referer;
	private Long refererCount;
	
	public RefererVo(String referer, Long refererCount){
		super();
		this.referer = referer;
		this.refererCount = refererCount;
	}

	public String getReferer() {
		return referer;
	}

	public void setReferer(String referer) {
		this.referer = referer;
	}

	public Long getRefererCount() {
		return refererCount;
	}

	public void setRefererCount(Long refererCount) {
		this.refererCount = refererCount;
	}
	
}
