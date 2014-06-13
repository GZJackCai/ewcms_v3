package com.ewcms.visit.vo;

import java.io.Serializable;

/**
 * 出口
 * 
 * @author 吴智俊
 */
public class ExportVo implements Serializable{
	
	private static final long serialVersionUID = -2767307005140813532L;
	
	private String eeUrl;
	private Long eeCount;
	private String eeRate;

	public ExportVo(String eeUrl, Long eeCount) {
		super();
		this.eeUrl = eeUrl;
		this.eeCount = eeCount;
		this.eeRate = "100%";
	}

	public String getEeUrl() {
		return eeUrl;
	}

	public void setEeUrl(String eeUrl) {
		this.eeUrl = eeUrl;
	}

	public Long getEeCount() {
		return eeCount;
	}

	public void setEeCount(Long eeCount) {
		this.eeCount = eeCount;
	}

	public String getEeRate() {
		return eeRate;
	}

	public void setEeRate(String eeRate) {
		this.eeRate = eeRate;
	}
}
