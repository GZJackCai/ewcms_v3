package com.ewcms.visit.model.traffic;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 
 * @author wu_zhijun
 *
 */
@Entity
@Table(name = "plugin_visit_channelclick")
public class ChannelClick implements Serializable {

	private static final long serialVersionUID = 906993409704978626L;

	@EmbeddedId
	private ChannelClickPk channelClickPk;
	@Column(name = "pageView_sum")
	private Long pageViewSum = 0L;
	@Column(name = "stickTime_avg")
	private Double stickTimeAvg = 0D;
	@Column(name = "child_pageView_sum")
	private Long childPageViewSum = 0L;
	@Column(name = "child_stickTime_avg")
	private Double childStickTimeAvg = 0D;

	public ChannelClick() {
	}
	
	public ChannelClick(Long pageViewSum, Double stickTimeAvg, Long childPageViewSum, Double childStickTimeAvg){
		super();
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
		this.childPageViewSum = (childPageViewSum == null) ? 0L : childPageViewSum;
		this.childStickTimeAvg = (childStickTimeAvg == null) ? 0D : childStickTimeAvg;
	}
	
	public ChannelClick(Date visitDate, Long pageViewSum){
		super();
		if (channelClickPk == null) channelClickPk = new ChannelClickPk(visitDate);
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
	}
	
	public ChannelClick(Long channelId, String channelName, Long pageViewSum, Double stickTimeAvg, Long childPageViewSum, Double childStickTimeAvg){
		super();
		if (channelClickPk == null) channelClickPk = new ChannelClickPk(channelId, channelName);
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
		this.childPageViewSum = (childPageViewSum == null) ? 0L : childPageViewSum;
		this.childStickTimeAvg = (childStickTimeAvg == null) ? 0D : childStickTimeAvg;
	}
	
	public ChannelClick(Long pageViewSum, Double stickTimeAvg){
		super();
		this.pageViewSum = (pageViewSum == null) ? 0L : pageViewSum;
		this.stickTimeAvg = (stickTimeAvg == null) ? 0D : stickTimeAvg;
	}
	
	public ChannelClickPk getChannelClickPk() {
		return channelClickPk;
	}

	public void setChannelClickPk(ChannelClickPk channelClickPk) {
		this.channelClickPk = channelClickPk;
	}

	public Long getPageViewSum() {
		return pageViewSum;
	}

	public void setPageViewSum(Long pageViewSum) {
		this.pageViewSum = pageViewSum;
	}

	public Double getStickTimeAvg() {
		return stickTimeAvg;
	}

	public void setStickTimeAvg(Double stickTimeAvg) {
		this.stickTimeAvg = stickTimeAvg;
	}

	public Long getChildPageViewSum() {
		return childPageViewSum;
	}

	public void setChildPageViewSum(Long childPageViewSum) {
		this.childPageViewSum = childPageViewSum;
	}

	public Double getChildStickTimeAvg() {
		return childStickTimeAvg;
	}

	public void setChildStickTimeAvg(Double childStickTimeAvg) {
		this.childStickTimeAvg = childStickTimeAvg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((channelClickPk == null) ? 0 : channelClickPk.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChannelClick other = (ChannelClick) obj;
		if (channelClickPk == null) {
			if (other.channelClickPk != null)
				return false;
		} else if (!channelClickPk.equals(other.channelClickPk))
			return false;
		return true;
	}
}
