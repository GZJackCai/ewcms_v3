package com.ewcms.site.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.site.model.Site;

/**
 * 站点自定义DAO实现
 * 
 * @author wu_zhijun
 *
 */
@Component
public class SiteDaoImpl implements SiteDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Site> getSiteListByOrgans(Long[] organIds, Boolean publicenable) {
		String hql;
		if (organIds == null || organIds.length == 0) {
			hql = "select s from Site s where s.publicenable=:publicenable order by s.id";

			TypedQuery<Site> query = em.createQuery(hql, Site.class);
			query.setParameter("publicenable", publicenable);

			return query.getResultList();
		}

		//TODO HQL表达式改写
		String sqlStr = "?";
		for (int i = 1; i < organIds.length; i++) {
			sqlStr += ",?";
		}
		hql = "select s from Site s Where s.publicenable=:publicenable and s.organ.id in(" + sqlStr + ") order by s.id";

		TypedQuery<Site> query = em.createQuery(hql, Site.class);
		query.setParameter("publicenable", publicenable);

		for (int i = 0; i < organIds.length; i++) {
			query.setParameter(2 + i, organIds[i]);
		}

		return query.getResultList();
	}

	@Override
	public List<Site> getSiteChildren(Long parentSiteId, Long organId) {
		String hql;
		if (parentSiteId == null) {
			if (organId == null) {
				hql = "select s from Site s where s.parent.id is null order by s.id";

				TypedQuery<Site> query = em.createQuery(hql, Site.class);

				return query.getResultList();
			} else {
				hql = "select s from Site s where s.parent.id is null and s.organ.id=:organId order by s.id";

				TypedQuery<Site> query = em.createQuery(hql, Site.class);
				query.setParameter("organId", organId);

				return query.getResultList();
			}
		} else {
			if (organId == null) {
				hql = "select s from Site s where s.parent.id=:parentSiteId order by s.id";

				TypedQuery<Site> query = em.createQuery(hql, Site.class);
				query.setParameter("parentSiteId", parentSiteId);

				return query.getResultList();
			} else {
				hql = "select s from Site s where s.parent.id=:parentSiteId and s.organ.id=:organId order by s.id";

				TypedQuery<Site> query = em.createQuery(hql, Site.class);
				query.setParameter("parentSiteId", parentSiteId);
				query.setParameter("organId", organId);

				return query.getResultList();
			}
		}
	}

	@Override
	public void updSiteParent(Long organId, Long parentSiteId, Long newParentSiteId) {
		String hql = "update Site s set s.parent.id=:newParentSiteId Where s.parent.id=:parentSiteId and s.organ.id !=:organId";

		Query query = em.createQuery(hql);

		query.setParameter("newParentSiteId", newParentSiteId);
		query.setParameter("parentSiteId", parentSiteId);
		query.setParameter("organId", organId);

		query.executeUpdate();
	}
}
