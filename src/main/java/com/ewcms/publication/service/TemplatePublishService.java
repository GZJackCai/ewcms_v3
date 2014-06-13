/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.site.dao.TemplateDao;
import com.ewcms.site.model.Template;

/**
 * 模板加载服务
 * <br>
 * 提供生成页面所需要的模板。
 * 
 * @author wangwei
 */
@Component
public class TemplatePublishService {

	@Autowired
	private TemplateDao templateDao;
	
    /**
     * 得到模板对象
     * 
     * @param id b模板编号
     * @return 
     */
	public Template getTemplate(Long id){
    	return templateDao.findOne(id);
    }
    
    /**
     * 得到频道下所有模板
     * <br>
     * 得到已经发布模板，如有文章模板必需排在第一个
     * 
     * @param id 频道频道编号
     * @return 模板对象
     */
	public List<Template> getTemplatesInChannel(Long id){
    	return templateDao.getTemplatesInChannel(id);
    }
    
    /**
     * 通过UniquePath得到模板，模板不存在返回null值
     * 
     * @param path 模板唯一路径
     * @return
     */
	public Template getTemplateByUniquePath(String path){
    	return templateDao.getTemplateByPath(path);
    }
    
    /**
     * 得到频道模板唯一路径
     * 
     * @param siteId 站点编号
     * @param path 路径
     * @param name 模板名称
     * @return
     */
	public String getUniquePathOfChannelTemplate(Long siteId,Long channelId,String name){
    	return "";
    }
}
