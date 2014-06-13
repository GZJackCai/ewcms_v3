package com.ewcms.security.acl.dao;

import org.springframework.data.jpa.repository.Query;

import com.ewcms.security.acl.model.AclIdEntity;

/**
 * @author 吴智俊
 */
public interface AclIdEntityDaoCustom {
	
	@Query("select e from AclIdEntity e left join e.aclClass c where c.id=?1 and e.objectId=?2")
	AclIdEntity findByClassIdAndObjectId(Long aclClassId, Long objectId);
	
	@Query("select e from AclIdEntity e left join e.aclClass c where c.className=?1 and e.objectId=?2")
	AclIdEntity findByClassNameAndObjectId(String className, Long objectId);
}
