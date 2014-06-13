package com.ewcms.security.acl.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.security.acl.model.AclSid;

/**
 * @author 吴智俊
 */
public interface AclSidDao extends PagingAndSortingRepository<AclSid, Long>, AclSidDaoCustom {

}
