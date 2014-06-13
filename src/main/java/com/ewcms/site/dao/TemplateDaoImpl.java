package com.ewcms.site.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.site.model.Template;
import com.ewcms.site.model.Template.TemplateType;

/**
 * 模板自定义DAO实现
 * 
 * @author wu_zhijun
 *
 */
@Component
public class TemplateDaoImpl implements TemplateDaoCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Template> getTemplateList(final Long siteId){
		String hql = "From Template o Where o.site.id=:siteId and o.templateEntity is not null and o.channelId is not null Order By o.id";

		TypedQuery<Template> query = em.createQuery(hql,Template.class);
		query.setParameter("siteId", siteId);

		return query.getResultList();
	}
	
	@Override
	public List<Template> getTemplateChildren(final Long parentTemplateId,final Long siteId,final Long channelId){	
		String hql;
		TypedQuery<Template> query;
		if (parentTemplateId == null) {
			hql = "From Template o Where o.parent is null and o.site.id=:siteId Order By o.id";
			query = em.createQuery(hql, Template.class);
			query.setParameter("siteId", siteId);
		} else {
			if (channelId == null) {
				hql = "From Template o Where o.parent.id=:parentTemplateId and o.site.id=:siteId Order By o.id";

				query = em.createQuery(hql, Template.class);
				query.setParameter("parentTemplateId", parentTemplateId);
				query.setParameter("siteId", siteId);
			} else {
				hql = "From Template o Where o.parent.id=:parentTemplateId and o.site.id=:siteId and o.channelId=:channelId Order By o.id";

				query = em.createQuery(hql, Template.class);
				query.setParameter("parentTemplateId", parentTemplateId);
				query.setParameter("siteId", siteId);
				query.setParameter("channelId", channelId);
			}
		}
		return query.getResultList();		
	}

	@Override
	public Template getChannelTemplate(final String templateName,final Long siteId,final Long parentTemplateId){
		String hql;
		TypedQuery<Template> query;
		if (parentTemplateId == null) {
			hql = "From Template o Where o.name=:templateName and o.site.id=:siteId and o.parent is null";

			query = em.createQuery(hql, Template.class);
			query.setParameter("templateName", templateName);
			query.setParameter("siteId", siteId);
		} else {
			hql = "From Template o Where o.name=:templateName and o.site.id=:siteId and o.parent.id=:parentTemplateId";

			query = em.createQuery(hql, Template.class);
			query.setParameter("templateName", templateName);
			query.setParameter("siteId", siteId);
			query.setParameter("parentTemplateId", parentTemplateId);
		}
		
		Template template = null;
		try{
			template = (Template) query.getSingleResult();
		}catch(NoResultException e){
		}
		return template;
	}
	
	@Override
	public Template getTemplateByPath(final String path){
		String hql = "From Template o Where o.uniquePath=:path";

		TypedQuery<Template> query = em.createQuery(hql, Template.class);
		query.setParameter("path", path);

		Template template = null;
		try{
			template = (Template) query.getSingleResult();
		}catch(NoResultException e){
		}
		return template;
	}
	
	@Override
	public List<Template> getTemplatesInChannel(final Long channelId){
		String hql = "From Template o Where o.channelId=:channelId order by o.type";
		TypedQuery<Template> query = em.createQuery(hql, Template.class);
		query.setParameter("channelId", channelId);
		return query.getResultList();
	}
	
	@Override
	public Template findTemplateByChannelIdAndTemplateType(final Long channelId, final TemplateType templateType){
		String hql = "From Template t Where t.channelId=:channelId And t.type=:templateType";
		TypedQuery<Template> query = em.createQuery(hql, Template.class);
		query.setParameter("channelId", channelId);
		query.setParameter("templateType", templateType);
		
		Template template = null;
		try{
			template = (Template) query.getSingleResult();
		}catch(NoResultException e){
		}
		return template;
	}

	@Override
	public Template findByIdAndChannelId(Long templateId, Long channelId) {
		String hql = "from Template t where t.id=:templateId and t.channelId=:channelId order by t.id";
		TypedQuery<Template> query = em.createQuery(hql, Template.class);
		query.setParameter("templateId", templateId);
		query.setParameter("channelId", channelId);
		
		Template template = null;
		try{
			template = (Template) query.getSingleResult();
		}catch(NoResultException e){
		}
		return template;
	}
}
