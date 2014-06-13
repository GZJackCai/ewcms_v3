package com.ewcms.content.resource.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.content.resource.model.Resource;

/**
 * @author 吴智俊
 */
@Component
public class ResourceDaoImpl implements ResourceDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Resource> findSoftDeleteResources(final Long siteId) {
		String hql = "From Resource o Where o.site.id= ?1 And o.status = ?2";
		TypedQuery<Resource> query = em.createQuery(hql, Resource.class);
		query.setParameter(1, siteId);
		query.setParameter(2, Resource.Status.DELETE);
		return query.getResultList();
	}

	@Override
	public List<Resource> findPublishResources(final Long siteId, final Boolean forceAgain) {
		String hql = "From Resource o Where o.site.id= ?1 And o.status In (?2)";
		TypedQuery<Resource> query = em.createQuery(hql, Resource.class);
		query.setParameter(1, siteId);
		List<Resource.Status> status = new ArrayList<Resource.Status>();
		if (forceAgain) {
			status.add(Resource.Status.RELEASED);
		}
		status.add(Resource.Status.NORMAL);
		query.setParameter(2, status);
		return query.getResultList();
	}

	@Override
	public Resource getResourceByUri(final Long siteId, final String uri) {
		String hql = "From Resource o Where o.site.id= ?1 And (o.uri = ?2 or o.thumbUri = ?3)";
		TypedQuery<Resource> query = em.createQuery(hql, Resource.class);

		query.setParameter(1, siteId);
		query.setParameter(2, uri);
		query.setParameter(3, uri);

		Resource resource = null;
		try {
			resource = (Resource) query.getSingleResult();
		} catch (Exception e) {
		}
		return resource;
	}
}
