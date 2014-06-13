package com.ewcms.visit.vo.totality;

import java.io.Serializable;

import com.ewcms.visit.util.NumberUtil;

/**
 * @author 吴智俊
 */
public class HostVo implements Serializable {

	private static final long serialVersionUID = -2235896411828907502L;

	private String host;
	private Long sumPv;
	private Long totalPv;

	public HostVo(String host, Long sumPv, Long totalPv) {
		super();
		this.host = host;
		this.sumPv = sumPv == null ? 0L : sumPv;
		this.totalPv = totalPv == null ? 0L : totalPv;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
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

	public String getRate(){
		return NumberUtil.percentage(sumPv, totalPv);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
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
		HostVo other = (HostVo) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		return true;
	}
}
