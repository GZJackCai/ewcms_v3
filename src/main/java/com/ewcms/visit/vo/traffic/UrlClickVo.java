package com.ewcms.visit.vo.traffic;

import java.io.Serializable;

import com.ewcms.visit.util.NumberUtil;

/**
 * URL点击率
 * 
 * @author 吴智俊
 */
public class UrlClickVo implements Serializable {

	private static final long serialVersionUID = -7455704985994261985L;

	private String url;
	private Long sumPv;
	private Long totalPv;

	public UrlClickVo(String url, Long sumPv, Long totalPv) {
		super();
		this.url = url;
		this.sumPv = sumPv;
		this.totalPv = totalPv;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getSumPv() {
		return sumPv;
	}

	public void setSumPv(Long sumPv) {
		this.sumPv = sumPv;
	}

	public Long getTotalPv() {
		return totalPv;
	}

	public void setTotalPv(Long totalPv) {
		this.totalPv = totalPv;
	}

	public String getRate() {
		return NumberUtil.percentage(sumPv, totalPv);
	}
}
