/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.online.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;
import com.ewcms.content.document.model.ArticleMain;
import com.ewcms.site.model.Organ;

/**
 * 网上办事主体
 * 
 * <ul>
 * <li>parent:所属父栏目</li>
 * <li>children:包含子栏目集</li>
 * <li>name: 栏目名称</li>
 * <li>sort:排序
 * <li>matters:事项对象集合</li>
 * <li>articleRmcs:文章主体对象集合</li>
 * <li>channelId:频道编号</li>
 * <li>organs:组织机构集合对象</li>
 * <li>isPublish:是否发布</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_workingbody")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_workingbody_id", allocationSize = 1)
public class WorkingBody extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -8453397499458969173L;

	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST }, targetEntity = WorkingBody.class)
	@JoinColumn(name = "parent_id")
	private WorkingBody parent;
	@OneToMany(cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, targetEntity = WorkingBody.class, mappedBy = "parent")
	@OrderBy("sort asc,id asc")
	private List<WorkingBody> children;
	@Column(name = "name", nullable = false)
	private String name;
	@Column(name = "sort")
	private Long sort;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Matter.class)
	@JoinTable(name = "plugin_workingbody_matter", joinColumns = @JoinColumn(name = "workingbody_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "matter_id", referencedColumnName = "id"))
	@OrderBy(value = "sort,id")
	private List<Matter> matters;
	@ManyToMany(cascade = {CascadeType.ALL}, targetEntity = ArticleMain.class, fetch = FetchType.LAZY)
	@JoinTable(name = "plugin_workingbody_articlemain", joinColumns = @JoinColumn(name = "workingbody_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "articleMain_id", referencedColumnName = "id"))
	@OrderBy("id")
	private List<ArticleMain> articleMains;
	@Column(name = "channel_id")
	private Integer channelId;
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Organ.class)
	@JoinTable(name = "plugin_workingbody_organ", joinColumns = @JoinColumn(name = "workingbody_id", referencedColumnName= "id"), inverseJoinColumns = @JoinColumn(name = "organ_id", referencedColumnName = "id"))
	@OrderBy(value = "id")
	private List<Organ> organs;
	@Column(name = "is_publish")
	private Boolean isPublish;

	public WorkingBody(){
		isPublish = false;
	}
	
	public WorkingBody getParent() {
		return parent;
	}

	public void setParent(WorkingBody parent) {
		this.parent = parent;
	}

	public List<WorkingBody> getChildren() {
		return children;
	}

	public void setChildren(List<WorkingBody> children) {
		this.children = children;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public List<Matter> getMatters() {
		return matters;
	}

	public void setMatters(List<Matter> matters) {
		this.matters = matters;
	}



	public List<ArticleMain> getArticleMains() {
		return articleMains;
	}

	public void setArticleMains(List<ArticleMain> articleMains) {
		this.articleMains = articleMains;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public List<Organ> getOrgans() {
		return organs;
	}

	public void setOrgans(List<Organ> organs) {
		this.organs = organs;
	}
	
	public Boolean getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Boolean isPublish) {
		this.isPublish = isPublish;
	}
}
