/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.content.particular.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;
import com.ewcms.site.model.Organ;

/**
 * 营业执照注册号为关联的其它信息
 * 
 * <ul>
 * <li>enterpriseBasic:营业执照注册号</li>
 * <li>organ:组织机构</li>
 * <li>content:内容</li>
 * <li>published:发布日期</li>
 * <li>dense:所属密级</li>
 * <li>channelId:专栏编号</li>
 * <li>release:发布</li>
 * </ul>
 * 
 * @author wuzhijun
 * 
 */
@Entity
@Table(name = "particular_enterprise_article")
@SequenceGenerator(name = "seq", sequenceName = "seq_particular_enterprise_article_id", allocationSize = 1)
public class EnterpriseArticle extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -6695808210400418495L;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH }, targetEntity = EnterpriseBasic.class)
	@JoinColumn(name = "enterprisebasic_yyzzzch", nullable = false)
	private EnterpriseBasic enterpriseBasic;
	@OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = Organ.class)
	@JoinColumn(name = "organ_id")
	private Organ organ;
	@OneToOne(cascade = { CascadeType.ALL }, targetEntity = ParticularContent.class)
	@JoinColumn(name = "content_id")
	private ParticularContent content;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "published")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date published;
	@Column(name = "dense")
	@Enumerated(EnumType.STRING)
	private Dense dense;
	@Column(name = "channel_id")
	private Integer channelId;
	@Column(name = "release")
	private Boolean release;

	public EnterpriseArticle(){
		release = false;
	}
	
	public EnterpriseBasic getEnterpriseBasic() {
		return enterpriseBasic;
	}

	public void setEnterpriseBasic(EnterpriseBasic enterpriseBasic) {
		this.enterpriseBasic = enterpriseBasic;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	public ParticularContent getContent() {
		return content;
	}

	public void setContent(ParticularContent content) {
		this.content = content;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public Dense getDense() {
		return dense;
	}

	public String getDenseDescription(){
		if (dense != null){
			return dense.getDescription();
		}else{
			return Dense.GENERAL.getDescription();
		}
	}
	
	public void setDense(Dense dense) {
		this.dense = dense;
	}

	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	public Boolean getRelease() {
		return release;
	}

	public void setRelease(Boolean release) {
		this.release = release;
	}
}
