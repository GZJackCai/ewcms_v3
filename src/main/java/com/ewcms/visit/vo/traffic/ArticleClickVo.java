package com.ewcms.visit.vo.traffic;

import java.io.Serializable;

/**
 * 文章点击率
 * 
 * @author 吴智俊
 */
public class ArticleClickVo implements Serializable {

	private static final long serialVersionUID = -8837148377562508918L;

	private String channelName;
	private String articleTitle;
	private String url;
	private String created;
	private Long sumPv;
	private Double avgSt;

	public ArticleClickVo(String channelName, String articleTitle, String url, String created, Long sumPv, Double avgSt) {
		super();
		this.channelName = channelName;
		this.articleTitle = articleTitle;
		this.url = url;
		this.created = created;
		this.sumPv = sumPv;
		this.avgSt = avgSt;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Long getSumPv() {
		return sumPv;
	}

	public void setSumPv(Long sumPv) {
		this.sumPv = sumPv;
	}

	public Double getAvgSt() {
		return avgSt;
	}

	public void setAvgSt(Double avgSt) {
		this.avgSt = avgSt;
	}

}
