package com.ewcms.security.acl.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.security.acl.model.AclIdEntity;

public interface AclIdEntityDao extends PagingAndSortingRepository<AclIdEntity, Long>, AclIdEntityDaoCustom{
}
