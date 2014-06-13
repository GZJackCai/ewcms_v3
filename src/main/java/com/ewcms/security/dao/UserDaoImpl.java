package com.ewcms.security.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDaoCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void updatePassword(String username, String password) {
		String hql = "Update User u Set u.password=:password Where u.username=:username";

    	Query query = em.createQuery(hql);
    	query.setParameter("username", username);
    	query.setParameter("password", password);

    	query.executeUpdate();
	}
}
