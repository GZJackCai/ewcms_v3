package com.ewcms.security.acl.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.security.acl.model.AclClass;

public interface AclClassDao extends PagingAndSortingRepository<AclClass, Long> {
	AclClass findByClassName(String className);
}
