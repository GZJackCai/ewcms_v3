package com.ewcms.visit.vo.totality;

import java.io.Serializable;

/**
 * 在线情况
 * 
 * @author 吴智俊
 */
public class OnlineVo implements Serializable {

	private static final long serialVersionUID = -8886047006292411466L;

	private Integer hour;
	private Long countFive=0L;;
	private Long countTen=0L;;
	private Long countFifteen=0L;;
	
	public OnlineVo(Integer hour){
		super();
		this.hour = hour;
	}
	
	public OnlineVo(Integer hour, Long countFive, Long countTen, Long countFifteen) {
		super();
		this.hour = hour;
		this.countFive = countFive;
		this.countTen = countTen;
		this.countFifteen = countFifteen;
	}

	public Integer getHour() {
		return hour;
	}

	public void setHour(Integer hour) {
		this.hour = hour;
	}

	public String getHourExpression(){
		return String.format("%1$02d:00 - %1$02d:59", hour);
	}
	
	public Long getCountFive() {
		return countFive;
	}

	public void setCountFive(Long countFive) {
		this.countFive = countFive;
	}

	public Long getCountTen() {
		return countTen;
	}

	public void setCountTen(Long countTen) {
		this.countTen = countTen;
	}

	public Long getCountFifteen() {
		return countFifteen;
	}

	public void setCountFifteen(Long countFifteen) {
		this.countFifteen = countFifteen;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hour == null) ? 0 : hour.hashCode());
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
		OnlineVo other = (OnlineVo) obj;
		if (hour == null) {
			if (other.hour != null)
				return false;
		} else if (!hour.equals(other.hour))
			return false;
		return true;
	}
}
