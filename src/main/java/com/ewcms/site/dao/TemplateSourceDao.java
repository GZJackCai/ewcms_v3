package com.ewcms.site.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.site.model.TemplateSource;

/**
 * 模板资源DAO接口
 * 
 * @author wu_zhijun
 *
 */
public interface TemplateSourceDao extends PagingAndSortingRepository<TemplateSource, Long>, JpaSpecificationExecutor<TemplateSource>, TemplateSourceDaoCustom {

}
