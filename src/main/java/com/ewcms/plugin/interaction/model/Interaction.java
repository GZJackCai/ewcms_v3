/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ewcms.plugin.interaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 *
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_interaction")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_interaction_id", allocationSize = 1)
public class Interaction extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 3018757843242796157L;
	
    @Column(name="username")
    private String username;
    @Column(name="name")
    private String name;
    @Column(name="title")
    private String title;
    @Column(name="type")
    private Integer type = 1;
    @Column(name="content", columnDefinition="text")
    private String content;
    @Column(name="replay", columnDefinition="text")
    private String replay;
    @Column(name="state")
    private int state = 0;
    @Column(name="checked")
    private boolean checked = false;
    @Column(name="ip")
    private String ip;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="date")
    private Date date;
    @Column(name="counter")
    private int counter;
    @Column(name="organ_id")
    private int organId;
    @Column(name="organ_name")
    private String organName;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="replay_date")
    private Date replayDate;
    @Column(name="tel")
    private String tel;
    
    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReplay() {
        return replay;
    }

    public void setReplay(String replay) {
        this.replay = replay;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getOrganId() {
        return organId;
    }

    public void setOrganId(int organId) {
        this.organId = organId;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public Date getReplayDate() {
        return replayDate;
    }

    public void setReplayDate(Date replayDate) {
        this.replayDate = replayDate;
    }

    public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
}
