/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 文章操作记录
 * 
 * <ul>
 * <li>articleMainId:文章主体编号</li>
 * <li>userName:操作员</li>
 * <li>statusDesc:状态描述</li>
 * <li>operateTime:操作时间</li>
 * <li>description:描述</li>
 * <li>reason:原因</li>
 * </ul>
 * 
 * @author wu_zhijun
 * 
 */
@Entity
@Table(name = "content_operate_track",
       indexes = {@Index(name = "idx_operatetrack_articlemain_id", columnList = "articlemain_id")
		         }
)
@SequenceGenerator(name = "seq", sequenceName = "seq_content_operate_track_id", allocationSize = 1)
public class OperateTrack extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -1094223279369849132L;

	@Column(name = "articlemain_id")
	private Long articleMainId;
	@Column(name = "username")
	private String userName;
	@Column(name = "userrealname")
	private String userRealName;
	@Column(name= "statusdesc")
	private String statusDesc;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "operatetime")
	private Date operateTime;
	@Column(name = "description", columnDefinition = "text")
	private String description;
	@Column(name = "reason", columnDefinition = "text")
	private String reason;
	
	public OperateTrack(){
		operateTime = new Date(Calendar.getInstance().getTime().getTime());
	}

	public Long getArticleMainId() {
		return articleMainId;
	}

	public void setArticleMainId(Long articleMainId) {
		this.articleMainId = articleMainId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserRealName() {
		return userRealName;
	}

	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
