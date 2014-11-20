package com.ewcms.visit.model.publishedstats;

import java.io.Serializable;

/**
 *
 * @author 吴智俊
 */
public class PublishedStats implements Serializable{

	private static final long serialVersionUID = 3045685086271107178L;

	private String organName;
	private String realName;
	private String loginName;
	private Long draftCount = 0L;
	private Long reeditCount = 0L;
	private Long reviewCount = 0L;
	private Long releaseCount = 0L;
	
	/**
	 * 人员发布统计构造函数
	 * 
	 * @param organName
	 * @param realName
	 * @param loginName
	 * @param draftCount
	 * @param reeditCount
	 * @param reviewCount
	 * @param releaseCount
	 */
	public PublishedStats(String organName, String realName, String loginName, Long draftCount, Long reeditCount, Long reviewCount, Long releaseCount) {
		super();
		this.organName = organName;
		this.realName = realName;
		this.loginName = loginName;
		this.draftCount = (draftCount == null) ? 0L : draftCount;
		this.reeditCount = (reeditCount == null) ? 0L : reeditCount;
		this.reviewCount = (reviewCount == null) ? 0L : reviewCount;
		this.releaseCount = (releaseCount == null) ? 0L : releaseCount;
	}
	
	/**
	 * 栏目发布统计/组织机构发布统计构造函数
	 * 
	 * @param loginName
	 * @param draftCount
	 * @param reeditCount
	 * @param reviewCount
	 * @param releaseCount
	 */
	public PublishedStats(String realName, Long draftCount, Long reeditCount, Long reviewCount, Long releaseCount) {
		super();
		this.realName = realName;
		this.draftCount = (draftCount == null) ? 0L : draftCount;
		this.reeditCount = (reeditCount == null) ? 0L : reeditCount;
		this.reviewCount = (reviewCount == null) ? 0L : reviewCount;
		this.releaseCount = (releaseCount == null) ? 0L : releaseCount;
	}
	
	public String getOrganName() {
		return organName;
	}

	public void setOrganName(String organName) {
		this.organName = organName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoninName(String loginName) {
		this.loginName = loginName;
	}

	public Long getDraftCount() {
		return draftCount;
	}

	public void setDraftCount(Long draftCount) {
		this.draftCount = draftCount;
	}

	public Long getReeditCount() {
		return reeditCount;
	}

	public void setReeditCount(Long reeditCount) {
		this.reeditCount = reeditCount;
	}

	public Long getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Long reviewCount) {
		this.reviewCount = reviewCount;
	}

	public Long getReleaseCount() {
		return releaseCount;
	}

	public void setReleaseCount(Long releaseCount) {
		this.releaseCount = releaseCount;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((loginName == null) ? 0 : loginName.hashCode());
		result = prime * result
				+ ((organName == null) ? 0 : organName.hashCode());
		result = prime * result
				+ ((realName == null) ? 0 : realName.hashCode());
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
		PublishedStats other = (PublishedStats) obj;
		if (loginName == null) {
			if (other.loginName != null)
				return false;
		} else if (!loginName.equals(other.loginName))
			return false;
		if (organName == null) {
			if (other.organName != null)
				return false;
		} else if (!organName.equals(other.organName))
			return false;
		if (realName == null) {
			if (other.realName != null)
				return false;
		} else if (!realName.equals(other.realName))
			return false;
		return true;
	}
}
