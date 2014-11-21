/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.history.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 记录model对象操作
 * 
 * <ul>
 * <li>className:类名(包括路径和类名)</li>
 * <li>methodName:操作方法</li>
 * <li>createDate:创建时间</li>
 * <li>idName:关键字名</li>
 * <li>idValue:关键字值</li>
 * <li>idType:关键字类型</li>
 * <li>modelObject:model对象</li>
 * <li>userName:用户名</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "history_model")
@SequenceGenerator(name = "seq", sequenceName = "seq_history_model_id", allocationSize = 1)
public class HistoryModel extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 5018412502893297348L;
	
	@Column(name = "class_name", nullable = false)
	private String className;
	@Column(name = "method_name", nullable = false)
	private String methodName;
    @Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date", columnDefinition = "Timestamp default CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
	@Column(name = "id_name", nullable = false)
	private String idName;
	@Column(name = "id_value", nullable = false)
	private String idValue;
	@Column(name = "id_type", nullable = false)
	private String idType;
	@Column(name = "model_object")
	private byte[] modelObject;
	@Column(name = "user_name", nullable = false)
	private String userName;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}
	
	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getIdValue() {
		return idValue;
	}

	public void setIdValue(String idValue) {
		this.idValue = idValue;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	@JSONField(serialize = false)
	public byte[] getModelObject() {
		return modelObject;
	}

	public void setModelObject(byte[] modelObject) {
		this.modelObject = modelObject;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
