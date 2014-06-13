/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.resource.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import com.ewcms.site.model.Site;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 资源信息
 *
 * id:编号
 * name:名称
 * size:大小
 * path:文件地址
 * imagePath:引导图地址
 * uri:访问地址
 * imageUri:引导图访问地址
 * type：资源类型
 * description：描述
 * site：所属站点
 * state：资源状态
 * createTime：创建实际
 * updateTime：修改时间
 * publishTime：发布时间
 * 
 * @author 吴智俊 王伟
 */
@Entity
@Table(name = "content_resource")
@SequenceGenerator(name = "seq_content_resource", sequenceName = "seq_content_resource_id", allocationSize = 1)
public class Resource implements Serializable {

	private static final long serialVersionUID = -6959680908438751060L;

	/**
     * 资源状态
     * @author wangwei
     */
    public enum Status {
        INIT,DELETE,NORMAL,RELEASED;
    }
    
    /**
     * 资源类型
     * 
     * @author wangwei
     */
    public enum Type {
        ANNEX("*","*.*", "附件"),
        IMAGE("jpg/gif/jpeg/png/bmp","*.jpg;*.gif;*.jpeg;*.png;*.bmp", "图片"), 
        FLASH("swf/flv","*.swf;*.flv", "FLASH"),
        VIDEO("mid/mp2/mp3/mp4/wav/avi/mov/mpeg/ram/m4v/rm/rmvb/smil/wmv/wma",
                "*.mid;*.mp2;*.mp3;*.mp4;*.wav;*.avi;*.mov;*.mpeg;*.ram;*.m4v;*.rm;*.rmvb;*.smil;*.wmv;*.wma", "视频和音频");

        private String fileDesc;
        private String fileExt;
        private String description;

        private Type(String fileDesc,String fileExt, String description) {
            this.fileDesc = fileDesc;
            this.fileExt = fileExt;
            this.description = description;
        }
        
        public String getFileDesc(){
            return fileDesc;
        }
        
        public String getFileExt(){
            return fileExt;
        }
        
        public String getDescription(){
        	return description;
        }
    }
    
    @Id
    @GeneratedValue(generator = "seq_content_resource", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(length = 100, nullable = false)
    private String name;
    @Column(nullable = false)
    private Long size;
    @Column(nullable = false,unique=true)
    private String path;
    @Column(name = "thumb_path",length = 300)
    private String thumbPath;
    @Column(name = "uri", nullable = false,length = 300)
    private String uri;
    @Column(name = "thumb_uri",length = 300)
    private String thumbUri;
    @Column(length = 20,nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(length = 200)
    private String description;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, targetEntity = Site.class)
    @JoinColumn(name = "site_id")
    private Site site;
    @Column(length=20,nullable=false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.INIT;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", columnDefinition = "Timestamp default CURRENT_TIMESTAMP", insertable = false, updatable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime ;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", insertable = false, updatable = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime = new Date();
    @Column(name = "publish_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getThumbUri() {
        return thumbUri;
    }

    public void setThumbUri(String thumbUri) {
        this.thumbUri = thumbUri;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

	@PrePersist
    public void prePersist(){
        createTime = new Date(System.currentTimeMillis());
        updateTime = new Date(System.currentTimeMillis());
        uri = uri.replace("\\", "/");
        path = resourcePath(site,uri).replace("\\", "/").replace("//", "/");
        if(StringUtils.isNotBlank(thumbUri)){
        	thumbUri = thumbUri.replace("\\", "/");
            thumbPath = resourcePath(site,thumbUri).replace("\\", "/").replace("//", "/");
        }
        
    }
    
    @PreUpdate
    public void preUpdate() {
        updateTime = new Date(System.currentTimeMillis());
        if(status != Status.RELEASED){
            publishTime = null;
        }
    }
    
    public static String resourcePath(Site site,String uri){
        String path =  site.getResourceDir() + "/" + uri;
        return "/" + StringUtils.join(StringUtils.split(path, "/"),"/");
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Resource other = (Resource) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
