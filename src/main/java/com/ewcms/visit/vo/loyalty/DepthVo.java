package com.ewcms.visit.vo.loyalty;

import java.io.Serializable;

import com.ewcms.visit.util.NumberUtil;

/**
 * 访问深度
 * 
 * @author 吴智俊
 */
public class DepthVo implements Serializable {

	private static final long serialVersionUID = 1298547382885706507L;

	private Long depth;
	private Long countDepth;
	private Long totalDepth;

	public DepthVo(Long depth, Long countDepth, Long totalDepth) {
		super();
		this.depth = depth;
		this.countDepth = countDepth;
		this.totalDepth = totalDepth;
	}

	public Long getDepth() {
		return depth;
	}

	public void setDepth(Long depth) {
		this.depth = depth;
	}

	public Long getCountDepth() {
		return countDepth;
	}

	public void setCountDepth(Long countDepth) {
		this.countDepth = countDepth;
	}

	public Long getTotalDepth() {
		return totalDepth;
	}

	public void setTotalDepth(Long totalDepth) {
		this.totalDepth = totalDepth;
	}
	
	public String getRate(){
		return NumberUtil.percentage(countDepth, totalDepth);
	}

}