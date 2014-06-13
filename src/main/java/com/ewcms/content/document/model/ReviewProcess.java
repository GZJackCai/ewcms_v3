/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Index;

import com.ewcms.util.Collections3;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 审核流程定义
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>channelId:频道编号</li>
 * <li>name:名称</li>
 * <li>userName:审核员</li>
 * <li>userRole:审核组</li>
 * <li>nextProcess:下个流程对象</li>
 * <li>prevProcess:上个流程对象</li>
 * </ul>
 * 
 * @author wu_zhijun
 *
 */
@Entity
@Table(name = "content_review_process")
@SequenceGenerator(name = "seq_content_review_process", sequenceName = "seq_content_review_process_id", allocationSize = 1)
public class ReviewProcess implements Serializable {

	private static final long serialVersionUID = 4540829300109250778L;

	@Id
	@GeneratedValue(generator = "seq_content_review_process", strategy = GenerationType.SEQUENCE)
	@Column(name = "id")
	private Long id;
	@Column(name = "channel_id", nullable = false)
	@Index(name = "idx_process_channel_id")
	private Long channelId;
	@Column(name = "name", nullable = false)
	private String name;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = ReviewUser.class)
	@JoinColumn(name = "process_id")
	@OrderBy(value = "id")
	@Index(name = "idx_user_process_id")
	private Set<ReviewUser> reviewUsers = new HashSet<ReviewUser>();
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = ReviewRole.class)
	@JoinColumn(name = "process_id")
	@OrderBy(value = "id")
	@Index(name = "idx_role_process_id")
	private Set<ReviewRole> reviewRoles = new HashSet<ReviewRole>();
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, targetEntity = ReviewProcess.class)
	@JoinColumn(name = "next_id")
	private ReviewProcess nextProcess;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER, targetEntity = ReviewProcess.class)
	@JoinColumn(name = "prev_id")
	private ReviewProcess prevProcess;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public Set<ReviewUser> getReviewUsers() {
		return reviewUsers;
	}

	public void setReviewUsers(Set<ReviewUser> reviewUsers) {
		this.reviewUsers = reviewUsers;
	}

	@JsonIgnore
	public Set<ReviewRole> getReviewRoles() {
		return reviewRoles;
	}

	public void setReviewRoles(Set<ReviewRole> reviewRoles) {
		this.reviewRoles = reviewRoles;
	}

	@JsonIgnore
	public ReviewProcess getNextProcess() {
		return nextProcess;
	}

	public void setNextProcess(ReviewProcess nextProcess) {
		this.nextProcess = nextProcess;
	}

	@JsonIgnore
	public ReviewProcess getPrevProcess() {
		return prevProcess;
	}

	public void setPrevProcess(ReviewProcess prevProcess) {
		this.prevProcess = prevProcess;
	}

	@Transient
	public String getRealNames() {
		return Collections3.extractToString(reviewUsers, "realName", ",");
	}
	
	@Transient
	public String getRoleNames(){
		return Collections3.extractToString(reviewRoles, "roleName", ",");
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		ReviewProcess other = (ReviewProcess) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
