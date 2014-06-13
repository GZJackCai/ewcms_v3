/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

/**
 * 
 */
package com.ewcms.site.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 模板资源
 * 
 * <ul>
 * <li>id:编号</li>
 * <li>name:模板资源名称</li>
 * <li>describe:资源说明</li>
 * <li>updTime:资源 最后修改时间</li>
 * <li>size:资源大小</li>
 * <li>parentId:资源父目录</li>
 * <li>sourceEntity: 资源实体</li>
 * <li>channelId:资源所属专栏</li>
 * <li>site:资源所属站点</li>
 * <li>channelId:资源路径</li>
 * <li>release:资源是否已发布</li>
 * </ul>
 * 
 * @author 周冬初
 * @author wuzhijun
 * 
 */
@Entity
@Table(name = "site_templatesource")
@SequenceGenerator(name = "seq_site_templatesource", sequenceName = "seq_site_templatesource_id", allocationSize = 1)
public class TemplateSource implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String PATH_SEPARATOR = "/";
	@Id
	@GeneratedValue(generator = "seq_site_templatesource", strategy = GenerationType.SEQUENCE)
	private Long id;
	@Column(length = 50)
	private String name;
	@Column(columnDefinition = "text")
	private String describe;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updtime")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updTime = new Date();
	@Column(length = 20)
	private String size;
	@ManyToOne(cascade = { CascadeType.REFRESH }, targetEntity = Site.class)
	@JoinColumn(name = "site_id", nullable = false)
	private Site site;
	@ManyToOne(cascade = { CascadeType.REFRESH }, targetEntity = TemplateSource.class)
	@JoinColumn(name = "parent_id", nullable = true)
	private TemplateSource parent;
	@OneToOne(cascade = { CascadeType.ALL }, targetEntity = TemplatesrcEntity.class, fetch = FetchType.EAGER, orphanRemoval = true)
	@JoinColumn(name = "tplEntityId", nullable = true)
	private TemplatesrcEntity sourceEntity;
	@Formula(value = "(Select count(o.id) From site_templatesource o Where o.parent_id= id)")
	private int childrenCount = 0;
	@Column(name = "channel_Id")
	private Long channelId;
	@Column(length = 150)
	private String path;
	@Column(length = 150, unique = true)
	private String uniquePath;
	@Column()
	private Boolean release = false;
	@Column()
	@Enumerated(EnumType.STRING)
	private FileType fileType;

	public String getUniquePath() {
		return uniquePath;
	}

	public void setUniquePath(String uniquePath) {
		this.uniquePath = uniquePath;
	}

	public Boolean getRelease() {
		return release;
	}

	public void setRelease(Boolean release) {
		this.release = release;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
	public Date getUpdTime() {
		return updTime;
	}

	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Site getSite() {
		return site;
	}

	public void setSite(Site site) {
		this.site = site;
	}

	public TemplateSource getParent() {
		return parent;
	}

	public void setParent(TemplateSource parent) {
		this.parent = parent;
	}

	@JsonIgnore
	public TemplatesrcEntity getSourceEntity() {
		return sourceEntity;
	}

	public void setSourceEntity(TemplatesrcEntity sourceEntity) {
		this.sourceEntity = sourceEntity;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public boolean hasChildren() {
		return this.childrenCount > 0;
	}

	public FileType getFileType() {
		return fileType;
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	@PreUpdate
	@PrePersist
	public void afterPersist() {
		constructPath();
	}

	private void constructPath() {
		StringBuilder builder = new StringBuilder();
		for (TemplateSource templatesrc = this; templatesrc != null; templatesrc = templatesrc.parent) {
			if (StringUtils.isBlank(templatesrc.getName())) {
				break;
			}
			String dir = removeStartAndEndPathSeparator(templatesrc.getName());
			builder.insert(0, dir);
			builder.insert(0, PATH_SEPARATOR);
		}
		path = removeStartAndEndPathSeparator(builder.toString());
		uniquePath = getSite().getId().toString() + PATH_SEPARATOR + path;
	}

	private String removeStartAndEndPathSeparator(final String dir) {
		String path = dir;
		path = StringUtils.removeStart(path, PATH_SEPARATOR);
		path = StringUtils.removeEnd(path, PATH_SEPARATOR);

		return path;
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
		TemplateSource other = (TemplateSource) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
