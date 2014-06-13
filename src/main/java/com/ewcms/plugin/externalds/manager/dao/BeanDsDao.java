package com.ewcms.plugin.externalds.manager.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.plugin.externalds.model.BeanDs;

/**
 * @author 吴智俊
 */
public interface BeanDsDao extends PagingAndSortingRepository<BeanDs, Long>, JpaSpecificationExecutor<BeanDs> {

}
