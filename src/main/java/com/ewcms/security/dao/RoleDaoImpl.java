package com.ewcms.security.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.security.model.Role;
import com.ewcms.security.model.User;

@Component
public class RoleDaoImpl implements RoleDaoCustom {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public void deleteWithReference(Long id) {
		//因為Role中沒有与User的关联，只能用笨办法，查询出拥有该权限组的用户, 并删除该用户的权限组.
		Role role = em.find(Role.class, id);
		String hql = "select u from User u left join u.roles g where g.id=:id";
		TypedQuery<User> query = em.createQuery(hql, User.class);
		query.setParameter("id", id);
		
		List<User> users = query.getResultList();
		
		for (User u : users) {
			u.getRoles().remove(role);
			em.merge(u);
		}
		em.remove(role);
	}

	@Override
	public Role findSelectedRole(Long userId, Long roleId) {
		String hql = "select g from User u left join u.roles g where u.id=:userId and g.id=:roleId";
		TypedQuery<Role> query = em.createQuery(hql, Role.class);
		query.setParameter("userId", userId);
		query.setParameter("roleId", roleId);
		Role role = null;
		try{
			role = query.getSingleResult();
		}catch(NoResultException e){
		}
		return role;
	}

	
}
