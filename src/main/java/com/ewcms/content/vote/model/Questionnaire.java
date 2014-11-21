/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.vote.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 问卷调查主体
 * 
 * <ul>
 * <li>title:调查主题</li>
 * <li>numberSum:投票人数</li>
 * <li>startTime:开始时间</li>
 * <li>endTime:结束时间</li>
 * <li>channelId:频道编号</li>
 * <li>questionnaireStatus:查看状态</li>
 * <li>verifiCode:验证码</li>
 * <li>subjects:调查与投票明细对象集合</li>
 * <li>sort:排序</li>
 * <li>voteEnd:结束标志</li>
 * </ul>
 * 
 * @author wu_zhijun
 */
@Entity
@Table(name = "plugin_vote_questionnaire")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_vote_questionnaire_id", allocationSize = 1)
public class Questionnaire extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -6666565689620227616L;

	/**
	 * 查看状态枚举
	 * @author wuzhijun
	 */
	public enum Status{
		VIEW("直接查看"),VOTEVIEW("投票后查看"),NOVIEW("不允许查看");
		
		private String description;
		
		private Status(String description){
			this.description = description;
		}
		
		public String getDescription(){
			return description;
		}
	}
	
	@Column(name = "title", nullable = false, length = 100)
	private String title;
	@Column(name = "number_sum", nullable = false)
	private Long numberSum;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "starttime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "endtime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date endTime;
	@Column(name = "channel_id", nullable = false)
	private Long channelId;
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status;
	@Column(name = "verificode")
	private Boolean verifiCode;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Subject.class, orphanRemoval = true, mappedBy = "questionnaire")
	@OrderBy(value = "sort")
	private List<Subject> subjects = new ArrayList<Subject>();
	@Column(name = "sort")
	private Long sort;
	@Column(name = "voteEnd")
	private Boolean voteEnd;
	
	public Questionnaire(){
		numberSum = 0L;
		verifiCode = false;
		status = Status.VIEW;
		voteEnd = false;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getNumberSum() {
		return numberSum;
	}

	public void setNumberSum(Long numberSum) {
		this.numberSum = numberSum;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getStatusDescription(){
		if (status != null){
			return status.getDescription();
		}else{
			return Status.VIEW.getDescription();
		}
	}
	
	public Boolean getVerifiCode() {
		return verifiCode;
	}

	public void setVerifiCode(Boolean verifiCode) {
		this.verifiCode = verifiCode;
	}

	@JSONField(serialize = false)
	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public Boolean getVoteEnd() {
		return voteEnd;
	}

	public void setVoteEnd(Boolean voteEnd) {
		this.voteEnd = voteEnd;
	}
}
