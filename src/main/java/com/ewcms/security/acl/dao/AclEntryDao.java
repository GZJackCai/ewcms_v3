package com.ewcms.security.acl.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.security.acl.model.AclEntry;

public interface AclEntryDao extends PagingAndSortingRepository<AclEntry, Long>, AclEntryDaoCustom{
}
