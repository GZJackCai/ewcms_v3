package com.ewcms.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.security.model.Permission;

public interface PermissionDao extends PagingAndSortingRepository<Permission, Long>, JpaSpecificationExecutor<Permission>, PermissionDaoCustom {
	Permission findByName(String name);
}
