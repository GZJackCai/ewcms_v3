/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.vote.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 问卷调查主题明细
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>title:选项标题</li>
 * <li>status:问卷调查主题明细状态</li>
 * <li>sort:排序</li>
 * <li>content:内容</li>
 * <li>voteNumber:票数</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_vote_subject_item")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_vote_subject_item_id", allocationSize = 1)
public class SubjectItem extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 8605463440202838775L;

	/**
	 * 问卷调查主题明细枚举
	 * @author wuzhijun
	 */
	public enum Status{
		CHOOSE("选项"),SINGLETEXT("单行文本"),MULTITEXT("多行文本");
		
		private String description;
		
		private Status(String description){
			this.description = description;
		}
		
		public String getDescription(){
			return description;
		}
	}
	
	@Column(name = "title", length = 100)
	private String title;
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;
	@Column(name = "sort")
	private Long sort;
	@Column(name = "content", columnDefinition = "text")
	private String content;
	@Column(name = "votenumber")
	private Long voteNumber;
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
	@JoinColumn(name = "subject_id")
	private Subject subject;
	
	public SubjectItem(){
		this.status = Status.CHOOSE;
		voteNumber = 0L;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
			return Status.CHOOSE.getDescription();
		}
	}
	
	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getVoteNumber() {
		return voteNumber;
	}

	public void setVoteNumber(Long voteNumber) {
		this.voteNumber = voteNumber;
	}

	@JSONField(serialize = false)
	public Subject getSubject() {
		return subject;
	}

	public void setSubject(Subject subject) {
		this.subject = subject;
	}
}
