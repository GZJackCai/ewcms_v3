package com.ewcms.site.dao;

import java.util.List;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.ewcms.site.model.TemplateSource;

/**
 * 模板资源自定义DAO实现
 * 
 * @author wu_zhijun
 *
 */
public class TemplateSourceDaoImpl implements TemplateSourceDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<TemplateSource> getTemplateSourceChildren(final Long parentId, final Long siteId) {
		String hql;
		TypedQuery<TemplateSource> query;
		if (parentId == null) {
			hql = "From TemplateSource o Where o.parent is null and o.site.id=:siteId Order By o.id";

			query = em.createQuery(hql, TemplateSource.class);
			query.setParameter("siteId", siteId);
		} else {
			hql = "From TemplateSource o Where o.parent.id=:parentId and o.site.id=:siteId Order By o.id";

			query = em.createQuery(hql,TemplateSource.class);
			query.setParameter("parentId", parentId);
			query.setParameter("siteId", siteId);
		}
		return query.getResultList();
	}

	@Override
	public TemplateSource getChannelTemplateSource(final String siteSrcName, final Long siteId, final Long parentId) {
		String hql;
		TypedQuery<TemplateSource> query;
		if (parentId == null) {
			hql = "From TemplateSource o Where o.name=:siteSrcName and o.site.id=:siteId and o.parent is null";

			query = em.createQuery(hql, TemplateSource.class);
			query.setParameter("siteSrcName", siteSrcName);
			query.setParameter("siteId", siteId);
		} else {
			hql = "From TemplateSource o Where o.name=:siteSrcName and o.site.id=:siteId and o.parent.id=:parentId";

			query = em.createQuery(hql, TemplateSource.class);
			query.setParameter("siteSrcName", siteSrcName);
			query.setParameter("siteId", siteId);
			query.setParameter("parentId", parentId);
		}
		
		TemplateSource templateSource = null;
		try{
			templateSource = (TemplateSource) query.getSingleResult();
		}catch(NoResultException e){
		}
		return templateSource;
	}

	@Override
	public List<TemplateSource> getPublishTemplateSources(final Long siteId,final Boolean forceAgain) {
		String hql = "From TemplateSource o Where o.release=false And o.site.id=:siteId And o.sourceEntity Is Not null";
		if (forceAgain) {
			hql = "From TemplateSource o Where o.site.id=:siteId And o.sourceEntity Is Not null";
		}

		TypedQuery<TemplateSource> query = em.createQuery(hql, TemplateSource.class);
		query.setParameter("siteId", siteId);

		return query.getResultList();
	}
	
	@Override
	public TemplateSource getTemplateSourceByPath(final String path){
		String hql = "From TemplateSource o Where o.uniquePath=:path";

		TypedQuery<TemplateSource> query = em.createQuery(hql, TemplateSource.class);
		query.setParameter("path", path);

		TemplateSource templateSource = null;
		try{
			templateSource = (TemplateSource) query.getSingleResult();
		}catch(NoResultException e){
		}
		return templateSource;
	}
	
	@Override
	public List<TemplateSource> getTemplateSourceInChannel(final Long channelId){
		String hql = "From TemplateSource o Where o.channelId=:channelId";
		TypedQuery<TemplateSource> query = em.createQuery(hql, TemplateSource.class);
		query.setParameter("channelId", channelId);
		return query.getResultList();
	}

	@Override
	public TemplateSource findByIdAndChannelId(Long templateSourceId, Long channelId) {
		String hql = "from TemplateSource s where s.id=:templateSourceId And s.channelId=:channelId";
		
		TypedQuery<TemplateSource> query = em.createQuery(hql, TemplateSource.class);
		query.setParameter("templateSourceId", templateSourceId);
		query.setParameter("channelId", channelId);

		TemplateSource templateSource = null;
		try{
			templateSource = (TemplateSource) query.getSingleResult();
		}catch(NoResultException e){
		}
		return templateSource;
	}
}
