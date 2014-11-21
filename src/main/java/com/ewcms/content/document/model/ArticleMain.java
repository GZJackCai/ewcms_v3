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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 文章主体
 * 
 * <ul>
 * <li>channelId:频道编号</li>
 * <li>article:文章信息</li>
 * <li>isReference:是否引用</li>
 * <li>isShare:是否共享</li>
 * <li>sort:排序</li>
 * <li>top:新闻置顶</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "content_article_main",
       indexes = {@Index(name = "idx_articlemain_channel_id", columnList = "channel_id"),
		          @Index(name = "idx_articlemain_article_id", columnList = "article_id")
                 }
)
@SequenceGenerator(name="seq", sequenceName="seq_content_article_main_id", allocationSize = 1)
public class ArticleMain extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = 3770751230943609506L;
	
	@Column(name = "channel_id", nullable = false)
	private Long channelId;
	@ManyToOne(cascade = CascadeType.ALL, targetEntity = Article.class)
	@JoinColumn(name = "article_id")
	private Article article;
	@Column(name = "is_reference")
	private Boolean isReference;
	@Column(name = "sort")
	private Long sort;
	@Column(name = "top")
	private Boolean top;
	@Column(name = "is_share")
	private Boolean isShare;
	
	public ArticleMain(){
		isReference = false;
		sort = null;
		top = false;
		isShare = false;
	}
	
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	public Article getArticle() {
		return article;
	}

	public void setArticle(Article article) {
		this.article = article;
	}

	public Boolean getIsReference() {
		return isReference;
	}

	public void setIsReference(Boolean isReference) {
		this.isReference = isReference;
	}

	public Long getSort() {
		return sort;
	}

	public void setSort(Long sort) {
		this.sort = sort;
	}

	public Boolean getTop() {
		return top;
	}

	public void setTop(Boolean top) {
		this.top = top;
	}

	public Boolean getIsShare() {
		return isShare;
	}

	public void setIsShare(Boolean isShare) {
		this.isShare = isShare;
	}
}
