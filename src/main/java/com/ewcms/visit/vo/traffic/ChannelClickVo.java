package com.ewcms.visit.vo.traffic;

import java.io.Serializable;

/**
 * 栏目点击率
 * 
 * @author 吴智俊
 */
public class ChannelClickVo implements Serializable {

	private static final long serialVersionUID = -3113208605734885978L;

	private Long channelId;
	private String channelName;
	private Long levelPageView;
	private Double levelStickTime;
	private Long pageView;
	private Double stickTime;
	private Boolean isChildren;
	
	public ChannelClickVo(Long channelId, String channelName, Long levelPageView, Double levelStickTime, Long pageView, Double stickTime, Boolean isChildren){
		super();
		this.channelId = channelId;
		this.channelName = channelName;
		this.levelPageView = levelPageView == null ? 0L : levelPageView;
		this.levelStickTime = levelStickTime == null ? 0D : levelStickTime;
		this.pageView = pageView == null ? 0L : pageView;
		this.stickTime = stickTime == null ? 0D : stickTime;
		this.isChildren = isChildren;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public Long getPageView() {
		return pageView;
	}

	public void setPageView(Long pageView) {
		this.pageView = pageView;
	}

	public Double getStickTime() {
		return stickTime;
	}

	public void setStickTime(Double stickTime) {
		this.stickTime = stickTime;
	}

	public Long getLevelPageView() {
		return levelPageView;
	}

	public void setLevelPageView(Long levelPageView) {
		this.levelPageView = levelPageView;
	}

	public Double getLevelStickTime() {
		return levelStickTime;
	}

	public void setLevelStickTime(Double levelStickTime) {
		this.levelStickTime = levelStickTime;
	}

	public Boolean getIsChildren() {
		return isChildren;
	}

	public void setIsChildren(Boolean isChildren) {
		this.isChildren = isChildren;
	}

}
