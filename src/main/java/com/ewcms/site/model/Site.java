/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.site.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Formula;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;

/**
 * <ul>
 * <li>siteName:站点名称</li>
 * <li>siteRoot:站点目录</li>
 * <li>describe:站点描述</li>
 * <li>siteURL: 站点访问URL地址</li>
 * <li>metaKey:meta搜索关键字</li>
 * <li>metaDescripe:meta关键字说明</li>
 * <li>siteConfig:站点配置</li>
 * <li>siteChannel:站点栏目</li>
 * <li>siteTemplateList:站点模板集</li>
 * <li>createTime:站点创建时间</li>
 * <li>updateTime:最后修改时间</li>
 * <li>extraFile:生成扩展文件名称</li>
 * <li>parent:父站点</li>
 * <li>publicenable:是否允许发布</li>
 * <li>siteServer:站点发布服务器信息对象</li>
 * <li>resourceDir:站点资源发布 绝对目录</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "site_site")
@SequenceGenerator(name = "seq", sequenceName = "seq_site_id", allocationSize = 1)
public class Site extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -53151561901976248L;

	@Column(length = 100)
	private String siteName;
	@Column(length = 20)
	private String siteRoot;
	@Column(length = 100)
	private String describe;
	@Column(length = 150)
	private String siteURL;
	@Column()
	private String metaKey;
	@Column()
	private String metaDescripe;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createtime", columnDefinition = "Timestamp default CURRENT_TIMESTAMP", insertable = false, updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updatetime", insertable = false, updatable = true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime = new Date();
	@Column(length = 15)
	private String extraFile;
	@ManyToOne(cascade = { CascadeType.REFRESH, CascadeType.MERGE,
			CascadeType.PERSIST }, targetEntity = Site.class)
	@JoinColumn(name = "parent_id", nullable = true)
	private Site parent;
	@Column()
	private Boolean publicenable = false;
	@Column()
	private String resourceDir;
	@ManyToOne(cascade = { CascadeType.REFRESH }, targetEntity = Organ.class)
	@JoinColumn(name = "organ_id", nullable = true)
	private Organ organ;
	@Formula(value = "(Select count(o.id) From site_site o Where o.parent_id= id)")
	private int childrenCount = 0;
	@OneToOne(cascade = { CascadeType.ALL }, targetEntity = SiteServer.class)
	@JoinColumn(name = "serverId", nullable = true)
	private SiteServer siteServer;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteRoot() {
		return siteRoot;
	}

	public void setSiteRoot(String siteRoot) {
		this.siteRoot = siteRoot;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getSiteURL() {
		return siteURL;
	}

	public void setSiteURL(String siteURL) {
		this.siteURL = siteURL;
	}

	public String getMetaKey() {
		return metaKey;
	}

	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}

	public String getMetaDescripe() {
		return metaDescripe;
	}

	public void setMetaDescripe(String metaDescripe) {
		this.metaDescripe = metaDescripe;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public String getExtraFile() {
		return extraFile;
	}

	public void setExtraFile(String extraFile) {
		this.extraFile = extraFile;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getUpdateTime() {
		return updateTime;
	}

	public Site getParent() {
		return parent;
	}

	@JSONField(serialize = false)
	public void setParent(Site parent) {
		this.parent = parent;
	}

	public Boolean getPublicenable() {
		return publicenable;
	}

	public void setPublicenable(Boolean publicenable) {
		this.publicenable = publicenable;
	}

	public String getResourceDir() {
		return resourceDir;
	}

	public void setResourceDir(String resourceDir) {
		this.resourceDir = resourceDir;
	}

	public SiteServer getSiteServer() {
		return siteServer;
	}

	public void setSiteServer(SiteServer siteServer) {
		this.siteServer = siteServer;
	}

	@JSONField(serialize = false)
	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public boolean hasChildren() {
		return this.childrenCount > 0;
	}
}
