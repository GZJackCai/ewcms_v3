package com.ewcms.security.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.security.model.Role;

public interface RoleDao extends PagingAndSortingRepository<Role, Long>, JpaSpecificationExecutor<Role>, RoleDaoCustom {
	Role findByRoleName(String roleName);
	Role findByCaption(String caption);
}
