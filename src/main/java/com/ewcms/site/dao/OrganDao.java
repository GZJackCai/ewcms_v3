package com.ewcms.site.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.site.model.Organ;

/**
 * 组织DAO接口
 * 
 * @author wu_zhijun
 *
 */
public interface OrganDao extends PagingAndSortingRepository<Organ, Long>, JpaSpecificationExecutor<Organ>, OrganDaoCustom {

}
