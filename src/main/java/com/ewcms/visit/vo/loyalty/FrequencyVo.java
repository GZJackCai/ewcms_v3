package com.ewcms.visit.vo.loyalty;

import java.io.Serializable;

import com.ewcms.visit.util.NumberUtil;

/**
 * 访问频率
 * 
 * @author 吴智俊
 */
public class FrequencyVo implements Serializable {

	private static final long serialVersionUID = 994600680001332479L;

	private Long frequency;
	private Long countFrequency;
	private Long totalFrequency;

	public FrequencyVo(Long frequency, Long countFrequency, Long totalFrequency) {
		super();
		this.frequency = frequency;
		this.countFrequency = countFrequency;
		this.totalFrequency = totalFrequency;
	}

	public Long getFrequency() {
		return frequency;
	}

	public void setFrequency(Long frequency) {
		this.frequency = frequency;
	}

	public Long getCountFrequency() {
		return countFrequency;
	}

	public void setCountFrequency(Long countFrequency) {
		this.countFrequency = countFrequency;
	}

	public Long getTotalFrequency() {
		return totalFrequency;
	}

	public void setTotalFrequency(Long totalFrequency) {
		this.totalFrequency = totalFrequency;
	}

	public String getRate(){
		return NumberUtil.percentage(countFrequency, totalFrequency);
	}
}