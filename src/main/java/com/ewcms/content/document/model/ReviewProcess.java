/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;
import com.ewcms.util.Collections3;

/**
 * 审核流程定义
 * 
 * <ul>
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
@SequenceGenerator(name = "seq", sequenceName = "seq_content_review_process_id", allocationSize = 1)
public class ReviewProcess extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 4540829300109250778L;

	@Column(name = "channel_id", nullable = false)
	private Long channelId;
	@Column(name = "name", nullable = false)
	private String name;
	@OneToMany(cascade = CascadeType.ALL, targetEntity = ReviewUser.class)
	@JoinColumn(name = "process_id")
	@OrderBy(value = "id")
	private Set<ReviewUser> reviewUsers = new HashSet<ReviewUser>();
	@OneToMany(cascade = CascadeType.ALL, targetEntity = ReviewRole.class)
	@JoinColumn(name = "process_id")
	@OrderBy(value = "id")
	private Set<ReviewRole> reviewRoles = new HashSet<ReviewRole>();
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = ReviewProcess.class)
	@JoinColumn(name = "next_id")
	private ReviewProcess nextProcess;
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = ReviewProcess.class)
	@JoinColumn(name = "prev_id")
	private ReviewProcess prevProcess;

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

	@JSONField(serialize = false)
	public Set<ReviewUser> getReviewUsers() {
		return reviewUsers;
	}

	public void setReviewUsers(Set<ReviewUser> reviewUsers) {
		this.reviewUsers = reviewUsers;
	}

	@JSONField(serialize = false)
	public Set<ReviewRole> getReviewRoles() {
		return reviewRoles;
	}

	public void setReviewRoles(Set<ReviewRole> reviewRoles) {
		this.reviewRoles = reviewRoles;
	}

	@JSONField(serialize = false)
	public ReviewProcess getNextProcess() {
		return nextProcess;
	}

	public void setNextProcess(ReviewProcess nextProcess) {
		this.nextProcess = nextProcess;
	}

	@JSONField(serialize = false)
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
}
