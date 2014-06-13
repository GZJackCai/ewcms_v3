package com.ewcms.site.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.site.model.Channel;

/**
 * 栏目自定义DAO实现
 * 
 * @author wu_zhijun
 *
 */
@Component
public class ChannelDaoImpl implements ChannelDaoCustom {

	private static final String QUERY_CHANNEL_CHILDREN_BY_PARENTID = "select c from Channel c where c.parent.id=:parentId order by c.sort, c.id";
	private static final String QUERY_CHANNEL_ROOT_BY_SITEID = "select c from Channel c where c.parent is null and c.site.id=:siteId order by c.id";
	private static final String QUERY_CHANNEL_BY_PATH = "select c from Channel c Where c.site.id=:siteId and c.pubPath=:path order by c.id";
	private static final String QUERY_MAX_SIBLING_BY_PARENTID = "select max(c.sort) from Channel c where c.parent.id=:parentId";
	private static final String QUERY_CHANNEL_BY_PARENTID_AND_SORT = "select c from Channel c where c.parent.id=:parentId and c.sort=:sort";
	private static final String QUERY_CHANNEL_BY_PARENTID_AND_GTSORT = "select c from Channel c where c.id!=:channelId and c.parent.id=:parentId and c.sort>=:sort and c.sort<:oldSort order by c.sort,c.id";
	private static final String QUERY_CHANNEL_BY_PARENTID_AND_LTSORT = "select c from Channel c where c.id!=:channelId and c.parent.id=:parentId and c.sort<=:sort and c.sort>:oldSort order by o.sort Desc,c.id";
	private static final String QUERY_CHANNEL_BY_GTSORT = "select c from Channel c where c.parent.id=:parentId and c.sort>:sort order by c.sort Asc";
	private static final String QUERY_CHANNEL_BY_GROUPID = "select c.parent.id from Channel c group by c.parent.id order by c.parent.id";
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<Channel> getChannelChildren(Long parentId) {
		if (parentId == null){
			return new ArrayList<Channel>();
		}
		TypedQuery<Channel> query = em.createQuery(QUERY_CHANNEL_CHILDREN_BY_PARENTID, Channel.class);
		query.setParameter("parentId", parentId);
		
		return query.getResultList();
	}

	@Override
	public Channel getChannelRoot(Long siteId) {
		TypedQuery<Channel> query = em.createQuery(QUERY_CHANNEL_ROOT_BY_SITEID, Channel.class);
		query.setParameter("siteId", siteId);

		Channel channel = null;
		try{
			channel = (Channel)query.getSingleResult();
		}catch (NoResultException e){
		}
		
		return channel;
	}

	@Override
	public Channel getChannelByURL(Long siteId, String path) {
		TypedQuery<Channel> query = em.createQuery(QUERY_CHANNEL_BY_PATH, Channel.class);
		query.setParameter("siteId", siteId);
		query.setParameter("path", path);

		Channel channel = null;
		try{
			channel = (Channel) query.getSingleResult();
		}catch (NoResultException e){
			
		}
		return channel;
	}

	@Override
	public Long findMaxSiblingChannel(Long parentId) {
		TypedQuery<Long> query = em.createQuery(QUERY_MAX_SIBLING_BY_PARENTID, Long.class);
		query.setParameter("parentId", parentId);
		Long maxSort = 0L;
		try{
			maxSort = (Long) query.getSingleResult();
		}catch(NoResultException e){
		}
		if (maxSort == null){
			maxSort = 0L;
		}
		return maxSort;
	}

	@Override
	public Channel findChannelByParentIdAndSort(Long parentId, Long sort) {
		TypedQuery<Channel> query = em.createQuery(QUERY_CHANNEL_BY_PARENTID_AND_SORT, Channel.class);
        query.setParameter("parentId", parentId);
        query.setParameter("sort", sort);

        Channel channel = null;
        try{
        	channel = (Channel) query.getSingleResult();
        }catch(NoResultException e){
        }
        return channel;
	}

	@Override
	public List<Channel> findChannelByParentIdAndGtSort(Long channelId, Long parentId, Long sort, Long oldSort) {
		TypedQuery<Channel> query = em.createQuery(QUERY_CHANNEL_BY_PARENTID_AND_GTSORT, Channel.class);
        query.setParameter("channelId", channelId);
        query.setParameter("parentId", parentId);
        query.setParameter("sort", sort);
        query.setParameter("oldSort", oldSort);

        return query.getResultList();
	}

	@Override
	public List<Channel> findChannelByParentIdAndLtSort(Long channelId, Long parentId, Long sort, Long oldSort) {
		TypedQuery<Channel> query = em.createQuery(QUERY_CHANNEL_BY_PARENTID_AND_LTSORT, Channel.class);
        query.setParameter("channelId", channelId);
        query.setParameter("parentId", parentId);
        query.setParameter("sort", sort);
        query.setParameter("oldSort", oldSort);

        return query.getResultList();
	}

	@Override
	public List<Channel> findChannelByGreaterThanSort(Long parentId, Long sort) {
		TypedQuery<Channel> query = em.createQuery(QUERY_CHANNEL_BY_GTSORT, Channel.class);
		query.setParameter("parentId", parentId);
		query.setParameter("sort", sort);
		return query.getResultList();
	}

	@Override
	public List<Long> findChannelParent() {
		TypedQuery<Long> query = em.createQuery(QUERY_CHANNEL_BY_GROUPID, Long.class);
		return query.getResultList();
	}

}
