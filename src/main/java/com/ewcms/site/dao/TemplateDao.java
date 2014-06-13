package com.ewcms.site.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.site.model.Template;

/**
 * 模板DAO接口
 * 
 * @author wu_zhijun
 *
 */
public interface TemplateDao extends PagingAndSortingRepository<Template, Long>, JpaSpecificationExecutor<Template>, TemplateDaoCustom {
}
