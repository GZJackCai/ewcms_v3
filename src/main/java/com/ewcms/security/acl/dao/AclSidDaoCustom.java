package com.ewcms.security.acl.dao;

import org.springframework.data.jpa.repository.Query;

import com.ewcms.security.acl.model.AclSid;

/**
 * @author 吴智俊
 */
public interface AclSidDaoCustom {
	@Query("from AclSid s where s.sid=?1 and s.principal=?2")
	AclSid findBySidAndPrincipal(String sid, Boolean principal);
	
	@Query("from AclSid s where s.sid=?1 and s.principal=true")
	AclSid findByOwnerSid(String sid);
}
