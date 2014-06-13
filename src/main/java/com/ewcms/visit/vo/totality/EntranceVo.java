package com.ewcms.visit.vo.totality;

import java.io.Serializable;

import com.ewcms.visit.util.NumberUtil;

/**
 * 入口/出口
 * 
 * @author 吴智俊
 */
public class EntranceVo implements Serializable {

	private static final long serialVersionUID = 3578509063780229217L;

	private String eeUrl;
	private Long eeCount;
	private Long eeSum;

	public EntranceVo(String eeUrl, Long eeCount, Long eeSum) {
		super();
		this.eeUrl = eeUrl;
		this.eeCount = eeCount;
		this.eeSum = eeSum;
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

	public Long getEeSum() {
		return eeSum;
	}

	public void setEeSum(Long eeSum) {
		this.eeSum = eeSum;
	}

	public String getEeRate(){
		return NumberUtil.percentage(eeCount, eeSum);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eeUrl == null) ? 0 : eeUrl.hashCode());
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
		EntranceVo other = (EntranceVo) obj;
		if (eeUrl == null) {
			if (other.eeUrl != null)
				return false;
		} else if (!eeUrl.equals(other.eeUrl))
			return false;
		return true;
	}
}
