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
 * 从业人员基本信息
 * 
 * <ul>
 * <li>name:人员姓名</li> 
 * <li>sex:性别</li> 
 * <li>publishingSector:发布部门</li>
 * <li>organ:组织机构</li>
 * <li>cardType:证件类型</li>
 * <li>cardCode:证件号码</li>
 * <li>dense:所属密级</li>
 * <li>channelId:专栏编号</li>
 * <li>release:发布</li>
 * </ul>
 * 
 * @author wuzhijun
 * 
 */
@Entity
@Table(name = "particular_employe_basic")
@SequenceGenerator(name = "seq", sequenceName = "seq_particular_employe_basic_id", allocationSize = 1)
public class EmployeBasic extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 2547138119716199680L;

	/**
	 * 性别
	 * 
	 * @author wuzhijun
	 */
	public enum Sex {
		MALE("男"), FEMALE("女");

		private String description;

		private Sex(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}

	public enum CardType{
		RESIDENT("居民身份证"), DRIVE("驾驶证"), JINGGUAN("警官证"), OFFICER("军官证"), OTHER("其他证件");
		
		private String description;

		private CardType(String description) {
			this.description = description;
		}

		public String getDescription() {
			return description;
		}
	}
	
	@Column(name = "name", nullable = false, length = 200)
	private String name;
	@Column(name = "sex", nullable = false)
	@Enumerated(EnumType.STRING)
	private Sex sex;
	@OneToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.EAGER, targetEntity = Organ.class)
	@JoinColumn(name = "organ_id")
	private Organ organ;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "published")
	private Date published;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "card_type")
	@Enumerated(EnumType.STRING)
	private CardType cardType;
	@Column(name = "card_code")
	private String cardCode;
	@Column(name = "dense", nullable = false)
	@Enumerated(EnumType.STRING)
	private Dense dense;
	@Column(name = "channel_id")
	private Integer channelId;
	@Column(name = "release")
	private Boolean release;
	
	public EmployeBasic(){
		release = false;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Sex getSex() {
		return sex;
	}

	public String getSexDescription(){
		if (sex != null){
			return sex.getDescription();
		}else{
			return Sex.MALE.getDescription();
		}
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public Organ getOrgan() {
		return organ;
	}

	public void setOrgan(Organ organ) {
		this.organ = organ;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public CardType getCardType() {
		return cardType;
	}

	public String getCardTypeDescription(){
		if (cardType != null){
			return cardType.getDescription();
		}else{
			return CardType.RESIDENT.getDescription();
		}
	}
	
	public void setCardType(CardType cardType) {
		this.cardType = cardType;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
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
