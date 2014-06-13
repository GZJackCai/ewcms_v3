 package com.ewcms.security.acl.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.security.acl.dao.AclClassDao;
import com.ewcms.security.acl.dao.AclEntryDao;
import com.ewcms.security.acl.dao.AclIdEntityDao;
import com.ewcms.security.acl.dao.AclSidDao;
import com.ewcms.security.acl.model.AclClass;
import com.ewcms.security.acl.model.AclEntry;
import com.ewcms.security.acl.model.AclIdEntity;
import com.ewcms.security.acl.model.AclSid;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.util.Reflections;

@Component
@Transactional(readOnly = true)
public class AclService {
	
	private static Logger logger = LoggerFactory.getLogger(AclService.class);
	
	@Autowired
	private AclSidDao aclSidDao;
	@Autowired
	private AclEntryDao aclEntryDao;
	@Autowired
	private AclClassDao aclClassDao;
	@Autowired
	private AclIdEntityDao aclIdEntityDao;
	
	@Transactional(readOnly = false)
	public void saveAclEntry(Object object, String className, String sid, Boolean principal, Integer mask){
		try{
			String loginName = EwcmsContextUtil.getLoginName();
			AclSid ownerSid = aclSidDao.findByOwnerSid(loginName);
			if (ownerSid == null){
				ownerSid = new AclSid();
				ownerSid.setSid(loginName);
				ownerSid.setPrincipal(true);
				
				aclSidDao.save(ownerSid);
			}
			
			AclSid aclSid = aclSidDao.findBySidAndPrincipal(sid, principal);
			if (aclSid == null){
				aclSid = new AclSid();
				aclSid.setSid(sid);
				aclSid.setPrincipal(principal);
				
				aclSidDao.save(aclSid);
			}
			
			AclClass aclClass = aclClassDao.findByClassName(className);
			if (aclClass == null) {
				aclClass = new AclClass();
				aclClass.setClassName(className);
				
				aclClassDao.save(aclClass);
			}
			
			initAclIdEntity(object, aclClass, ownerSid);
			
			Long objectId = (Long) Reflections.invokeGetter(object, "id");
			if (objectId != null){
				
				AclEntry aclEntry = aclEntryDao.findByIdEntityAndSidAndClassName(objectId, aclSid.getId(), className);
				Long aceOrder = aclEntryDao.findByMaxOrder(objectId);
				if (aceOrder == null) aceOrder = 0L;
				else aceOrder++;
				if (aclEntry == null){
					aclEntry = new AclEntry();
					
					AclIdEntity aclIdEntity = aclIdEntityDao.findByClassIdAndObjectId(aclClass.getId(), objectId);
					aclEntry.setAclIdEntity(aclIdEntity);
					aclEntry.setAclSid(aclSid);
					aclEntry.setGranting(true);
					aclEntry.setAceOrder(aceOrder);
				}
				aclEntry.setMask(mask);
				aclEntryDao.save(aclEntry);
			}
		}catch(Exception e){
			logger.error(e.toString());
		}
	}
	
	private void initAclIdEntity(Object object, AclClass aclClass, AclSid ownerSid){
		if (object == null) return;
		
		Long objectId = (Long) Reflections.invokeGetter(object, "id");
		Long parentObjectId = null;
		Object parentObject = null;
		try{
			parentObject = Reflections.invokeGetter(object, "parent");
			parentObjectId = (Long) Reflections.invokeGetter(parentObject, "id");
		}catch(Exception e){
			logger.warn("Parent Object Is Null");
		}
		
		AclIdEntity aclIdEntity = aclIdEntityDao.findByClassIdAndObjectId(aclClass.getId(), objectId);
		if (aclIdEntity == null){
			aclIdEntity = new AclIdEntity();
			aclIdEntity.setAclClass(aclClass);
			aclIdEntity.setObjectId(objectId);
			aclIdEntity.setOwnerSid(ownerSid);
			aclIdEntity.setParent(null);
		}
		
		if (parentObject != null){
			AclIdEntity parentAclIdEntity = aclIdEntityDao.findByClassIdAndObjectId(aclClass.getId(), parentObjectId);
			aclIdEntity.setParent(parentAclIdEntity);
		}
		aclIdEntityDao.save(aclIdEntity);
		
		if (parentObject != null){
			initAclIdEntity(parentObject, aclClass, ownerSid);
		}
	}
	
	public List<AclEntry> findByObjectId(Long objectId){
		return aclEntryDao.findByObjectId(objectId);
	}
	
	public List<AclEntry> findByPermission(String loginName, List<String> roleNames){
		return aclEntryDao.findByPermission(loginName, roleNames);
	}
	
	public List<AclEntry> findByMask(Long idEntityId, String loginName, List<String> roleNames){
		return aclEntryDao.findByMask(idEntityId, loginName, roleNames);
	}
	
	public AclIdEntity findByClassNameAndObjectId(String className, Long objectId){
		return aclIdEntityDao.findByClassNameAndObjectId(className, objectId);
	}
	
	@Transactional(readOnly = false)
	public void inheriting(String className, Long objectId, Boolean inheriting){
		AclIdEntity aclIdEntity = aclIdEntityDao.findByClassNameAndObjectId(className, objectId);
		if (aclIdEntity != null && aclIdEntity.getInheriting() != inheriting){
			aclIdEntity.setInheriting(inheriting);
			aclIdEntityDao.save(aclIdEntity);
		}
	}
	
	@Transactional(readOnly = false)
	public void deleteAclEntry(String className, Long objectId, String sid, Boolean principal){
		AclEntry aclEntry = aclEntryDao.findByIdEntityAndSidNameAndClassName(objectId, sid, principal, className);
		if (aclEntry != null){
			aclEntry.setGranting(false);
			aclEntryDao.save(aclEntry);
		}
	}
	
	@Transactional(readOnly = false)
	public Integer findByMaxMask(Long idEntityId, String className, String loginName, List<String> roleNames){
		return aclEntryDao.findByMaxMask(idEntityId, className, loginName, roleNames);
	}
}
