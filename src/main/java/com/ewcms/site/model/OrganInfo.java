/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

/**
 * 
 */
package com.ewcms.site.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * <ul>
 * <li>address:机构地址</li>
 * <li>postCode:机构邮编</li>
 * <li>tranWay: 交通方式</li>
 * <li>tel:电话</li>
 * <li>serviceTime:上班服务时间</li>
 * <li>describe:机构介绍</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "site_organinfo")
@SequenceGenerator(name = "seq", sequenceName = "seq_site_organinfo_id", allocationSize = 1)
public class OrganInfo extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 7342446995346166030L;

	@Column(name = "address", length = 200)
	private String address;
	@Column(name = "postcode", length = 8)
	private String postCode;
	@Column(name = "tranway", length = 200)
	private String tranWay;
	@Column(name = "tel", length = 200)
	private String tel;
	@Column(name = "serviceTime", length = 200)
	private String serviceTime;
	@Column(columnDefinition = "text")
	private String describe;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getTranWay() {
		return tranWay;
	}

	public void setTranWay(String tranWay) {
		this.tranWay = tranWay;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}
}
