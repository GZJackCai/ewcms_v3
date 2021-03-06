/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.ewcms.common.model.BaseSequenceEntity;

/**
 * 文章信息
 * 
 * <ul>
 * <li>title:标题</li>
 * <li>titleStyle:标题样式</li>
 * <li>shortTitle:短标题</li>
 * <li>shortTitleStyle:短标题样式</li>
 * <li>subTitle:副标题</li>
 * <li>subTitlStyle:副标题样式</li>
 * <li>author:作者</li>
 * <li>origin:来源</li>
 * <li>keyword:关键字</li>
 * <li>tag:标签</li>
 * <li>summary:摘要</li>
 * <li>contents:内容集合对象</li>
 * <li>image:文章图片</li>
 * <li>isComment:允许评论</li>
 * <li>genre:文章类型</li>
 * <li>created:创建者</li>
 * <li>published:发布时间</li>
 * <li>modified:修改时间</li>
 * <li>status:状态</li>
 * <li>url:链接地址</li>
 * <li>isDelete:删除标志</li>
 * <li>relations:相关文章</li>
 * <li>createTime:创建时间</li>
 * <li>categories:文章分类属性集合</li>
 * <li>contentTotal:内容总页数<li>
 * <li>inside:使用内部标题</li>
 * <li>reviewProcess:审核流程对象</li>
 * </ul>
 * 
 * @author 吴智俊
 */
@Entity
@Table(name = "content_article", 
       indexes = {@Index(name = "idx_article_published", columnList = "published"),
		          @Index(name = "idx_article_modified", columnList = "modified"),
		          @Index(name = "idx_article_status", columnList = "status")
                 }
)
@SequenceGenerator(name = "seq", sequenceName = "seq_content_article_id", allocationSize = 1)
public class Article extends BaseSequenceEntity<Long> {

	private static final long serialVersionUID = -5809802652492615658L;

	/**
	 * 文章类型枚举
	 * @author wuzhijun
	 */
	public enum Genre {
		GENERAL("普通新闻"),TITLE("标题新闻");
		
		private String description;
		
		private Genre(String description){
			this.description = description;
		}
		
		public String getDescription(){
			return description;
		}

	}
	
	/**
	 * 文章状态枚举
	 * @author wuzhijun
	 */
	public enum Status {
		DRAFT("初稿"),REEDIT("重新编辑"),REVIEW("审核中"),REVIEWBREAK("审核中断"),PRERELEASE("发布版"),RELEASE("已发布");
		
		private String description;
		
		private Status(String description){
			this.description = description;
		}
		
		public String getDescription(){
			return description;
		}
	}
	
	@Column(name = "title", nullable = false)
	private String title;
	@Column(name = "title_style")
	private String titleStyle;
	@Column(name = "short_title")
	private String shortTitle;
	@Column(name = "short_title_style")
	private String shortTitleStyle;
	@Column(name = "sub_title")
	private String subTitle;
	@Column(name = "sub_title_style")
	private String subTitleStyle;
	@Column(name = "author")
	private String author;
	@Column(name = "origin")
	private String origin;
	@Column(name = "key_word", columnDefinition = "text")
	private String keyword;
	@Column(name = "tag")
	private String tag;
	@Column(name = "summary", columnDefinition = "text")
	private String summary;
	@OneToMany(cascade = CascadeType.ALL, targetEntity = Content.class, orphanRemoval = true)
	@JoinColumn(name = "article_id")
	@OrderBy(value = "page asc")
	private List<Content> contents = new ArrayList<Content>();
	@Column(name = "image")
	private String image;
	@Column(name = "is_comment")
	private Boolean isComment;
	@Column(name = "genre", nullable = false)
	@Enumerated(EnumType.STRING)
	private Genre genre;
	@Column(name = "created")
	private String created;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "published")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date published;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date modified;
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;
	@Column(name = "url", columnDefinition = "text")
	private String url;
	@Column(name = "is_delete")
	private Boolean isDelete;
	@OneToMany(cascade = CascadeType.ALL, targetEntity = Relation.class, mappedBy = "article")
	@OrderBy(value = "sort")
	private List<Relation> relations = new ArrayList<Relation>();
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createtime", nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REFRESH}, targetEntity = Category.class)
	@JoinTable(name = "content_article_category", joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"))
	@OrderBy(value = "id")
	//@Index(name = "idx_article_category_id")
	private List<Category> categories = new ArrayList<Category>();
	@Column(name = "total")
	private Integer contentTotal;
	@Column(name = "inside")
	private Boolean inside;
	@OneToOne(cascade={CascadeType.PERSIST, CascadeType.REFRESH}, targetEntity = ReviewProcess.class)
	@JoinColumn(name="reviewprocess_id")
	//@Index(name = "idx_article_reviewprocess_id")
	private ReviewProcess reviewProcess;
	
	public Article() {
		isComment = false;
		genre = Genre.GENERAL;
		status = Status.DRAFT;
		createTime = new Date(Calendar.getInstance().getTime().getTime());
		isDelete = false;
		inside = false;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@JSONField(serialize = false)
	public List<Content> getContents() {
		return contents;
	}

	public void setContents(List<Content> contents) {
		this.contents = contents;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getIsComment() {
		return isComment;
	}

	public void setIsComment(Boolean isComment) {
		this.isComment = isComment;
	}
	
	public Genre getGenre() {
		return genre;
	}
	
	public String getGenreDescription(){
		if (genre != null){
			return genre.getDescription();
		}else{
			return Genre.GENERAL.getDescription();
		}
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getStatusDescription(){
		return status.getDescription();
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	@JSONField(serialize = false)
	public List<Relation> getRelations() {
		return relations;
	}

	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public Integer getContentTotal() {
		return contentTotal;
	}

	public void setContentTotal(Integer contentTotal) {
		this.contentTotal = contentTotal;
	}

	public Boolean getInside() {
		return inside;
	}

	public void setInside(Boolean inside) {
		this.inside = inside;
	}

	public ReviewProcess getReviewProcess() {
		return reviewProcess;
	}

	public void setReviewProcess(ReviewProcess reviewProcess) {
		this.reviewProcess = reviewProcess;
	}

	public String getTitleStyle() {
		return titleStyle;
	}

	public void setTitleStyle(String titleStyle) {
		this.titleStyle = titleStyle;
	}

	public String getShortTitleStyle() {
		return shortTitleStyle;
	}

	public void setShortTitleStyle(String shortTitleStyle) {
		this.shortTitleStyle = shortTitleStyle;
	}

	public String getSubTitleStyle() {
		return subTitleStyle;
	}

	public void setSubTitleStyle(String subTitleStyle) {
		this.subTitleStyle = subTitleStyle;
	}
}
