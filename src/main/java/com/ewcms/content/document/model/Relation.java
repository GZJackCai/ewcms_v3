/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 相关文章
 * 
 * <ul>
 * <li>sort:排序</li>
 * <li>article:文章信息</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "content_relation",
       indexes = {@Index(name = "idx_relation_aticle_id", columnList = "relation_article_id")
                 }
)
@SequenceGenerator(name = "seq", sequenceName = "seq_content_relation_id", allocationSize = 1)
public class Relation extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -4281309365714981737L;

	@Column(name = "sort")
	private Integer sort;
	@OneToOne(cascade={CascadeType.PERSIST,CascadeType.MERGE}, targetEntity = Article.class)
	@JoinColumn(name="relation_article_id")
	private Article relationArticle;
	@ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH}, optional = false)
	@JoinColumn(name = "article_id")
	private Article article;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	public Article getRelationArticle() {
		return relationArticle;
	}

	public void setRelationArticle(Article relationArticle) {
		this.relationArticle = relationArticle;
	}

	@JSONField(serialize = false)
	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}
}
