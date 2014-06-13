package com.ewcms.site.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.site.model.Organ;

/**
 * 组织自定义DAO实现
 * 
 * @author wu_zhijun
 *
 */
@Component
public class OrganDaoImpl implements OrganDaoCustom {
	
	private static final String QUERY_ORGAN_BY_ROOT = "select o from Organ o where o.parent is null order by o.id";
	private static final String QUERY_ORGAN_BY_PARENTID = "select o from Organ o where o.parent.id=:parentId order by o.id";

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Organ> getOrganChildren(Long parentId) {
		if(parentId == null){
			return em.createQuery(QUERY_ORGAN_BY_ROOT, Organ.class).getResultList();
		}else{
			TypedQuery<Organ> query = em.createQuery(QUERY_ORGAN_BY_PARENTID, Organ.class);
			query.setParameter("parentId", parentId);
			return query.getResultList();
		}
	}
}
