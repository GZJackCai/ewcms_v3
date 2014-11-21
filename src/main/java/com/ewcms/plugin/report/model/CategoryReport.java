/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.report.model;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 报表分类
 * 
 * <ul>
 * <li>name:名称</li>
 * <li>remarks:备注</li>
 * <li>texts:报表记录集</li>
 * <li>charts:图表记录集</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "plugin_report_category")
@SequenceGenerator(name = "seq", sequenceName = "seq_plugin_report_category_id", allocationSize = 1)
public class CategoryReport extends BaseSequenceEntity<Long> {

    private static final long serialVersionUID = 6590119941274234278L;
    
    @Column(name = "name", nullable = false, length = 50, unique = true)
    private String name;
    @Column(name = "remarks",columnDefinition = "text")
    private String remarks;
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, targetEntity = TextReport.class)
    @JoinTable(name = "plugin_report_category_text", joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "text_id", referencedColumnName = "id"))
    @OrderBy("id")
    private Set<TextReport> texts = new LinkedHashSet<TextReport>();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY, targetEntity = ChartReport.class)
    @JoinTable(name = "plugin_report_category_chart", joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "chart_id", referencedColumnName = "id"))
    @OrderBy("id")
    private Set<ChartReport> charts = new LinkedHashSet<ChartReport>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @JSONField(serialize = false)
    public Set<TextReport> getTexts() {
        return texts;
    }

    public void setTexts(Set<TextReport> texts) {
        this.texts = texts;
    }

    @JSONField(serialize = false)
    public Set<ChartReport> getCharts() {
        return charts;
    }

    public void setCharts(Set<ChartReport> charts) {
        this.charts = charts;
    }
}
