package com.ewcms.site.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.site.model.Site;

/**
 * 站点DAO接口
 * 
 * @author wu_zhijun
 *
 */
public interface SiteDao extends PagingAndSortingRepository<Site, Long>, JpaSpecificationExecutor<Site>, SiteDaoCustom {

}
