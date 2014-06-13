package com.ewcms.plugin.report.manage.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.plugin.report.model.Parameter;

/**
 * @author 吴智俊
 */
@Component
public class ParameterDaoImpl implements ParameterDaoCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public Boolean findSessionIsEntityByParameterIdAndUserName(final Long parameterId, final String userName){
    	String hql = "Select p From Parameter As p Where p.id=:parameterId And p.defaultValue=:userName";
    	
    	TypedQuery<Parameter> query = em.createQuery(hql, Parameter.class);
    	query.setParameter("parameterId", parameterId);
    	query.setParameter("userName", userName);
    	
    	List<Parameter> list = query.getResultList();
    	return list.isEmpty() ? false : true;
    }

}
