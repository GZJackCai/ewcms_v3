/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.resource.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.ewcms.content.resource.dao.ResourceDao;
import com.ewcms.content.resource.model.Resource;
import com.ewcms.content.resource.model.Resource.Status;
import com.ewcms.content.resource.model.Resource.Type;
import com.ewcms.content.resource.service.operator.FileOperator;
import com.ewcms.content.resource.service.operator.ResourceOperatorable;
import com.ewcms.publication.uri.UriRules;
import com.ewcms.site.model.Site;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.util.ImageUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 实现资源管理
 *
 * @author 吴智俊 王伟
 */
@Component
public class ResourceService {

    private static final Logger logger = LoggerFactory.getLogger(ResourceService.class);
    
    private static final String DEFAULT_RESOUCE_CONTEXT = "pub_res";
    private static final String DEFAULT_THUMB_SUFFIX="_thumb";
    
    @Autowired
    private ResourceDao resourceDao;
    
    private Site getCurrentSite() {
        Site site = EwcmsContextUtil.getCurrentSite();
        logger.debug("Current site is {}",site);
        return site;
    }
    
    private String getResourceContext(){
        //TODO Get resource context
        return DEFAULT_RESOUCE_CONTEXT;
    }
    
    private String getThumbSuffix(){
        return DEFAULT_THUMB_SUFFIX;
    }
    
    /**
     * 通过路径得到文件名
     * 
     * @param path 文件路径
     * @return
     */
    String getFilename(String path) {
        String[] names = path.split("/");
        return names[names.length - 1];
    }
 
    /**
     * 得到引导图Uri
     * 
     * @param uri 统一资源资源地址
     * @return
     */
    String getThumbUri(String uri){
        int index = StringUtils.lastIndexOf(uri, '.');
        if(index == -1){
            return uri+getThumbSuffix();
        }else{
            return StringUtils.overlay(uri, getThumbSuffix(), index,index);
        }
    }
    
    /**
     * 上传图片压缩
     * 
     * @param site 站点
     * @param uri  图片地址
     * @param path 图片地址
     * @return     压缩图片地址
     */
    private String imageCompression(Site site, String uri, MultipartFile in){
        String thumbUri = getThumbUri(uri);
        String imagePath = Resource.resourcePath(site, thumbUri);
        boolean success = ImageUtil.compression(in, imagePath, 128, 128);
        return success ? thumbUri : uri;
    }
    
    private String imageCompression(Site site,String uri,String path){
        String thumbUri = getThumbUri(uri);
        String imagePath = Resource.resourcePath(site, thumbUri);
        boolean success = ImageUtil.compression(path, imagePath, 128, 128);
        return success ? thumbUri : uri;
    }

    /**
     * 得到文件后缀名
     * 
     * @param name 文件名
     * @return
     */
    String getSuffix(String name) {
        if (StringUtils.contains(name, ".")) {
            return StringUtils.substringAfterLast(name, ".");
        }else{
            return "";
        }
    }
    
    public Resource upload(MultipartFile myUpload, Resource.Type type) throws IOException {
        Site site = getCurrentSite();
        ResourceOperatorable operator = new FileOperator(site.getResourceDir());
        String name = getFilename(myUpload.getOriginalFilename());
        String uri = operator.write(myUpload.getInputStream(), UriRules.newResource(getResourceContext()),getSuffix(name));
        Resource resource = new Resource();
        resource.setUri(uri);
        resource.setSize(myUpload.getSize());
        resource.setName(name);
        resource.setDescription(name);
        resource.setType(type);
        resource.setSite(site);
        if (type == Resource.Type.IMAGE) {
            resource.setThumbUri(imageCompression(site, uri, myUpload));
        }
        resourceDao.save(resource);

        return resource;
    }
    
    public Resource upload(Site site, MultipartFile myUpload, Type type) throws IOException{
        ResourceOperatorable operator = new FileOperator(site.getResourceDir());
        String name = getFilename(myUpload.getOriginalFilename());
        String uri = operator.write(myUpload.getInputStream(), UriRules.newResource(getResourceContext()),getSuffix(name));
        Resource resource = new Resource();
        resource.setUri(uri);
        resource.setSize(myUpload.getSize());
        resource.setName(name);
        resource.setDescription(name);
        resource.setType(type);
        resource.setStatus(Status.NORMAL);
        resource.setSite(site);
        if (type == Resource.Type.IMAGE) {
            resource.setThumbUri(imageCompression(site,uri,myUpload));
        }
        resourceDao.save(resource);

        return resource;
    }
    
    public Resource upload(Site site, File file, String path, Type type) throws IOException{
        ResourceOperatorable operator = new FileOperator(site.getResourceDir());
        String name = getFilename(path);
        String uri = operator.write(new FileInputStream(file), UriRules.newResource(getResourceContext()),getSuffix(name));
        Resource resource = new Resource();
        resource.setUri(uri);
        resource.setSize(file.length());
        resource.setName(name);
        resource.setDescription(name);
        resource.setType(type);
        resource.setStatus(Status.NORMAL);
        resource.setSite(site);
        if (type == Resource.Type.IMAGE) {
            resource.setThumbUri(imageCompression(site,uri,file.getPath()));
        }
        resourceDao.save(resource);

        return resource;
    }
    
    public Resource update(Long id, MultipartFile myUpload, Resource.Type type)throws IOException {
        Resource resource = resourceDao.findOne(id);
        Assert.notNull(resource,"Resourc is not exist,id = " + id);
        
        FileUtils.copyInputStreamToFile(myUpload.getInputStream(), new File(resource.getPath()));
        
        if (type == Resource.Type.IMAGE) {
            if(resource.getPath().equals(resource.getThumbPath())){
                String thumbUri = getThumbUri(resource.getUri());
                String thumbPath = Resource.resourcePath(resource.getSite(), thumbUri);
                if(ImageUtil.compression(myUpload, thumbPath, 128, 128)){
                    resource.setThumbUri(thumbUri);
                }
            }else{
                if(!ImageUtil.compression(myUpload, resource.getThumbPath(), 128, 128)){
                    FileUtils.forceDeleteOnExit(new File(resource.getThumbPath()));
                    resource.setThumbUri(resource.getUri());
                }
            }
        }
        resource.setStatus(Status.NORMAL);
        resource.setUpdateTime(new Date(System.currentTimeMillis()));
        resource.setName(getFilename(myUpload.getOriginalFilename()));
        resourceDao.save(resource);
        return resource;
    }
    
    public Resource updateThumb(Long id,MultipartFile myUpload) throws IOException {
        
        Resource resource = resourceDao.findOne(id);
        Assert.notNull(resource,"Resourc is not exist,id = " + id);

        String thumbPath = resource.getThumbPath();
        Site site = getCurrentSite();
        ResourceOperatorable operator = new FileOperator(site.getResourceDir());
        if(StringUtils.isBlank(thumbPath)){
            String name = getFilename(myUpload.getOriginalFilename());
            String uri = operator.write(myUpload.getInputStream(), UriRules.newResourceThumb(getResourceContext()),getSuffix(name));
            resource.setThumbUri(uri);
            resource.setThumbPath(Resource.resourcePath(site, uri));
        }else{
            FileUtils.copyInputStreamToFile(myUpload.getInputStream(), new File(thumbPath));
        }
        resource.setStatus(Status.NORMAL);
        resourceDao.save(resource);    
        
//        Hibernates.initLazyProperty(resource.getSite());
        
        return resource;
    }

    public List<Resource> save(Map<Long, String> descriptions) {
        List<Resource> resources = new ArrayList<Resource>();
        for (Long id : descriptions.keySet()) {
            Resource resource = resourceDao.findOne(id);
            if(resource == null || resource.getStatus() == Status.DELETE){
                continue;
            }
            String desc = descriptions.get(id);
            resource.setStatus(Status.NORMAL);
            resource.setDescription(desc);
            resourceDao.save(resource);
            resources.add(resource);
        }
        return resources;
    }
    
    /**
     * 删除属与资源的文件
     * 
     * @param resource
     */
    private void deleteResourceFile(Resource resource){
        File f = new File(resource.getPath());
        f.deleteOnExit();
        if(StringUtils.isNotBlank(resource.getThumbPath())){
            f = new File(resource.getThumbPath());
            f.deleteOnExit();
        }
    }
    
    public void delete(List<Long> resourceIds) {
        for(Long resourceId : resourceIds){
            Resource resource = resourceDao.findOne(resourceId);
            if (resource.getStatus() == Resource.Status.DELETE){
	            deleteResourceFile(resource);
	            resourceDao.delete(resource);
	            
	            if(resource.getStatus() == Resource.Status.RELEASED){
	              //TODO 写入删除日志，下架该资源
	            }    
            }
        }
    }

    public Resource getResource(Long id) {
        return resourceDao.findOne(id);
    }

    public Resource updateDescription(Long id, String description) {
        Resource resource = getResource(id);
        resource.setDescription(description);
        
        resourceDao.save(resource);
        return resource;
    }
    
    public void softDelete(List<Long> resourceIds) {
        for(Long resourceId : resourceIds){
            Resource resource = getResource(resourceId);
            resource.setStatus(Resource.Status.DELETE);
            resourceDao.save(resource);
        }
    }

    public void clearSoftDelete() {
    	Long siteId = getCurrentSite().getId();
        List<Resource> resources = resourceDao.findSoftDeleteResources(siteId);
        for(Resource resource : resources){
            deleteResourceFile(resource);
            if (resource.getStatus() == Resource.Status.DELETE){
            	resourceDao.delete(resource);
            }
        }
    }
    
    public void revert(Long[] ids) {
        for(Long id : ids){
            Resource resource = getResource(id);
            if(resource.getStatus() == Resource.Status.DELETE){
                resource.setStatus(Resource.Status.NORMAL);
                resourceDao.save(resource);
            }
        }
    }
    
    public List<Resource> findPublishResources(Long siteId, Boolean forceAgain) {
        return resourceDao.findPublishResources(siteId,forceAgain);
    }

    public void publishResourceSuccess(Long id) {
        Resource resource = resourceDao.findOne(id);
        resource.setStatus(Resource.Status.RELEASED);
        resource.setPublishTime(new Date(System.currentTimeMillis()));
        resourceDao.save(resource);
    }
    
    public Resource getResourceByUri(String uri) {
        Site site = getCurrentSite();
        Long siteId = site.getId();
        return resourceDao.getResourceByUri(siteId, uri);
    }
    
	public Map<String, Object> search(QueryParameter params, String type){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_type", Type.valueOf(StringUtils.upperCase(type)));
		parameters.put("NOTEQ_status", Status.DELETE);
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("publishTime", Direction.DESC);
		sorts.put("updateTime",  Direction.DESC);
		sorts.put("createTime",  Direction.DESC);
		sorts.put("id",  Direction.DESC);
		params.setSorts(sorts);
		
		return SearchMain.search(params, "IN_id", Long.class, resourceDao, Resource.class);
	}

	public Map<String, Object> searchRecycle(QueryParameter params){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_status", Status.DELETE);
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("updateTime",  Direction.DESC);
		sorts.put("createTime",  Direction.DESC);
		sorts.put("id",  Direction.DESC);

		return SearchMain.search(params, "IN_id", Long.class, resourceDao, Resource.class);
	}
}
