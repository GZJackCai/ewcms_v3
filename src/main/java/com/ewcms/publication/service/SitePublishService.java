/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.site.dao.SiteDao;
import com.ewcms.site.model.Site;

/**
 * 站点接口
 * <br>
 * 发布资源时需要依赖站点信息。
 * 
 * @author wangwei
 */
@Component
public class SitePublishService {

	@Autowired
	private SiteDao siteDao;
    /**
     * 通过站点编号得到站点
     * 
     * @param id 站点编号
     * @return
     */
	public Site getSite(Long id){
    	return siteDao.findOne(id);
    }
}
