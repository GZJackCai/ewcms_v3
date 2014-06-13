package com.ewcms.content.history.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;

@Component
public class HistoryModelDaoImpl implements HistoryModelDaoCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void delByBeforeDate(Date createDate) {
		String hql = "Delete HistoryModel h Where h.createDate<=?";
		em.createQuery(hql).setParameter(1, createDate).executeUpdate();
	}
}
