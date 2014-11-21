package com.ewcms.security.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 权限
 * 
 * <ul>
 * <li>name:名称</li>
 * <li>expression:表达式</li>
 * <li>caption:说明</li>
 * </ul>
 * 
 * @author wuzhijun
 */
@Entity
@Table(name = "acct_permission")
@SequenceGenerator(name = "seq", sequenceName = "seq_acct_permission_id", allocationSize = 1)
public class Permission extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 6992071754156726733L;

	@Column(name = "name", nullable = false, unique = true)
	private String name;
	@Column(name = "expression", nullable = false, unique = true)
	private String expression;
	@Column(name = "caption", nullable = false, unique = true)
	private String caption;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}
}
