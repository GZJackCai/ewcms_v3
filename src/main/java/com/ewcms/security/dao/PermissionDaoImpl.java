package com.ewcms.security.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.security.model.Permission;

@Component
public class PermissionDaoImpl implements PermissionDaoCustom {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Permission findSelectedPermission(Long roleId, Long permissionId) {
		String hql = "select p from Role g left join g.permissions p where g.id=:roleId and p.id=:permissionId";
		TypedQuery<Permission> query = em.createQuery(hql, Permission.class);
		
		query.setParameter("roleId", roleId);
		query.setParameter("permissionId", permissionId);
		Permission permission = null;
		try{
			permission = query.getSingleResult();
		}catch(NoResultException e){
		}
		return permission;
	}

}
