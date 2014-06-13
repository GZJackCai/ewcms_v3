/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.scheduling.generate.job.channel.dao;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.scheduling.generate.job.channel.model.EwcmsJobChannel;

/**
 * 频道定时任务DAO
 * 
 * @author 吴智俊
 */
@Component
public class EwcmsJobChannelDaoImpl implements EwcmsJobChannelDaoCustom {
	
	@PersistenceContext
	private EntityManager em;

	public EwcmsJobChannel findJobChannelByChannelId(final Long channelId) {
		String hql = "Select o From EwcmsJobChannel o Inner Join o.channel c Where c.id=:channelId";
		
		TypedQuery<EwcmsJobChannel> query = em.createQuery(hql, EwcmsJobChannel.class);
    	query.setParameter("channelId", channelId);

    	EwcmsJobChannel ewcmsJobChannel = null;
    	try{
    		ewcmsJobChannel = (EwcmsJobChannel) query.getSingleResult();
    	}catch(NoResultException e){
    	}
    	return ewcmsJobChannel;
	}
}
