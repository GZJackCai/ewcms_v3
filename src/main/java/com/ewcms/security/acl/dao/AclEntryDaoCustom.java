package com.ewcms.security.acl.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.ewcms.security.acl.model.AclEntry;

/**
 * @author 吴智俊
 */
public interface AclEntryDaoCustom {
	@Query("select e from AclEntry e left join e.aclIdEntity i left join e.aclSid s left join i.aclClass c where i.objectId=?1 and s.id=?2 and c.className=?3 and e.granting=true")
	AclEntry findByIdEntityAndSidAndClassName(Long idEntityId, Long sidId, String className);
	
	@Query("select e from AclEntry e left join e.aclIdEntity i left join e.aclSid s left join i.aclClass c where i.objectId=?1 and s.sid=?2 and s.principal=?3 and c.className=?4 and e.granting=true")
	AclEntry findByIdEntityAndSidNameAndClassName(Long idEntityId, String sid, Boolean principal, String className);
	
	@Query("select max(e.aceOrder) from AclEntry e left join e.aclIdEntity i where i.objectId=?1 and e.granting=true")
	Long findByMaxOrder(Long idEntityId);
	
	@Query("select e from AclEntry e left join e.aclIdEntity i where i.objectId=?1 and e.granting=true")
	List<AclEntry> findByObjectId(Long objectId);
	
	@Query("select e from AclEntry e left join e.aclSid s where (s.sid=?1 or s.sid in(?2)) and e.granting=true ")
	List<AclEntry> findByPermission(String loginName, List<String> roleNames);
	
	@Query("select e from AclEntry e left join e.aclIdEntity i left join e.aclSid s where i.objectId=?1 and (s.sid=?2 or s.sid in (?3)) and e.granting=true")
	List<AclEntry> findByMask(Long idEntityId, String loginName, List<String> roleNames);
	
	@Query("select max(e.mask) from AclEntry e left join e.aclIdEntity i left join e.aclSid s left join i.aclClass c where i.objectId=?1 and c.className=?2 and (s.sid=?3 or s.sid in (?4)) and e.granting=true")
	Integer findByMaxMask(Long idEntityId, String className, String loginName, List<String> roleNames);
}