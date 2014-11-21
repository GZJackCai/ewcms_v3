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
@Table(name = "plugin_interaction_speak")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_interaction_speak_id", allocationSize = 1)
public class Speak extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -3735127388543391139L;

    @Column(name = "username")
    private String username;
    @Column(name = "name")
    private String name;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Column(name = "ip")
    private String ip;
    @Column(name = "checked")
    private boolean checked = false;
    @Column(name="date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Column(name="interaction_id")
    private Integer interactionId;

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getInteractionId() {
        return interactionId;
    }

    public void setInteractionId(Integer interactionId) {
        this.interactionId = interactionId;
    }
}
