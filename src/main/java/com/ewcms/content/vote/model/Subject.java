/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.vote.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 问卷调查主题
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>title:标题</li>
 * <li>status:选项状态</li>
 * <li>subjectItems:问卷调查明细列表对象集合</li>
 * <li>sort:排序</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_vote_subject")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_vote_subject_id", allocationSize = 1)
public class Subject extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 3724785214252997515L;

	/**
	 * 选项状态枚举
	 * @author wuzhijun
	 */
	public enum Status{
		RADIO("单选"),OPTION("多选"),INPUT("录入");
		
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
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = SubjectItem.class, orphanRemoval = true, mappedBy = "subject")
	@OrderBy(value = "sort")
	private List<SubjectItem> subjectItems = new ArrayList<SubjectItem>();
	@Column(name = "sort")
	private Long sort;
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
	@JoinColumn(name = "questionnaire_id")
	private Questionnaire questionnaire;
	
	public Subject(){
		this.status = Status.RADIO;
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
			return Status.RADIO.getDescription();
		}
	}
	
	@JSONField(serialize = false)
	public List<SubjectItem> getSubjectItems() {
		return subjectItems;
	}

	public void setSubjectItems(List<SubjectItem> subjectItems) {
		this.subjectItems = subjectItems;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	@JSONField(serialize = false)
	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}
}
