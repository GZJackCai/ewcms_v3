package com.ewcms.content.document.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.model.ArticleMain;
import com.ewcms.content.document.model.Article.Status;

/**
 * @author 吴智俊
 */
@Component
public class ArticleMainDaoImpl implements ArticleMainDaoCustom {
	
	@PersistenceContext
	private EntityManager em;

	@Override
	public ArticleMain findArticleMainByArticleMainAndChannel(final Long articleMainId, final Long channelId){
		String hql = "FROM ArticleMain AS r WHERE r.id=:articleMainId And r.channelId=:channelId";

		TypedQuery<ArticleMain> query = em.createQuery(hql, ArticleMain.class);
		query.setParameter("articleMainId", articleMainId);
		query.setParameter("channelId", channelId);

		ArticleMain articleMain = null;
		try{
			articleMain = (ArticleMain)query.getSingleResult();
		}catch(NoResultException e){
		}
		return articleMain;
	}
	
	@Override
	public List<ArticleMain> findArticleMainByChannel(final Long channelId){
		String hql = "Select r FROM ArticleMain AS r WHERE r.channelId=:channelId Order By r.sort";

		TypedQuery<ArticleMain> query = em.createQuery(hql, ArticleMain.class);
		query.setParameter("channelId", channelId);

		return query.getResultList();
	}
	
	@Override
	public ArticleMain findArticleMainByChannelAndEqualSort(final Long channelId, final Long sort, final Boolean top){
		String hql = "Select c From ArticleMain c Left Join c.article o Where c.channelId=:channelId And c.sort=:sort And c.top=:top Order By c.sort";

		TypedQuery<ArticleMain> query = em.createQuery(hql, ArticleMain.class);
		query.setParameter("channelId", channelId);
		query.setParameter("sort", sort);
		query.setParameter("top", top);

		ArticleMain articleMain = null;
		try{
			articleMain = (ArticleMain)query.getSingleResult();
		}catch(NoResultException e){
		}
		
		return articleMain;
	}
	
	@Override
	public List<ArticleMain> findArticleMainByChannelAndThanSort(final Long channelId, final Long sort, final Boolean top){
		String hql = "Select c From ArticleMain c Left Join c.article o Where c.channelId=:channelId And c.sort>=:sort And c.top=:top Order By c.sort";

		TypedQuery<ArticleMain> query = em.createQuery(hql, ArticleMain.class);
		query.setParameter("channelId", channelId);
		query.setParameter("sort", sort);
		query.setParameter("top", top);

		return query.getResultList();
	}
	
	@Override
	public List<ArticleMain> findArticleMainByChannelIdAndUserName(final Long channelId, final String userName){
		String hql = "Select m From ArticleMain As m Left Join m.article As a Where m.channelId=:channelId And a.created=:created";

		TypedQuery<ArticleMain> query = em.createQuery(hql, ArticleMain.class);
		query.setParameter("channelId", channelId);
		query.setParameter("created", userName);

		return query.getResultList();
	}
	
	@Override
	public Boolean findArticleIsEntityByArticleAndCategory(final Long articleId, final Long categoryId){
    	String hql = "Select a From Article As a Left Join a.categories As c Where a.id=:articleId And c.id=:categoryId";

    	TypedQuery<Article> query = em.createQuery(hql, Article.class);
    	query.setParameter("articleId", articleId);
    	query.setParameter("categoryId", categoryId);

    	List<Article> list = query.getResultList();
    	return list.isEmpty()? false : true;
    }
    
	@Override
	public Map<Long, Long> findBeApprovalArticleMain(final String userName, final List<String> groupNames){
    	Map<Long, Long> map = new HashMap<Long, Long>();
        String hql = "Select o.channelId, Count(o) From ArticleMain As o Left Join o.article AS r Left Join r.reviewProcess As p Left Join p.reviewUsers As u Left Join p.reviewGroups As g Where r.delete=false And r.status=:status And (u.userName=:userName ";
        if (groupNames != null && !groupNames.isEmpty()){
        	for (String groupName : groupNames)
        		hql += " Or g.groupName='" + groupName + "' ";
        }
        hql += ") Group By o.channelId "; 
        
        TypedQuery<Object[]> query = em.createQuery(hql, Object[].class);
        query.setParameter("status", Status.REVIEW);
        query.setParameter("userName", userName);
        
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
        	Long channelId = (Long)result[0];
            Long count = (Long)result[1];
            if (count > 0){
            	map.put(channelId, count);
            }
        }
        return map;
    }
    
	@Override
	public Boolean findArticleTitleIsEntityByCrawler(final String title, final Long channelId, final String userName){
    	String hql = "Select o From ArticleMain c Left Join c.article o Where o.title=:title And c.channelId=:channelId And o.created=:created And c.reference=false";

    	TypedQuery<Article> query = em.createQuery(hql, Article.class);
    	query.setParameter("title", title);
    	query.setParameter("channelId", channelId);
    	query.setParameter("created", userName);

    	List<Article> list = query.getResultList();
    	return list.isEmpty() ? false : true;
    }
    
	@Override
	public Long findArticleMainCountByCrawler(final Long channelId, final String userName){
    	String hql = "Select Count(c.id) From ArticleMain As c Left Join c.article As o Where c.channelId=:channelId And o.created=:created And c.reference=false";

    	TypedQuery<Long> query = em.createQuery(hql, Long.class);
    	query.setParameter("channelId", channelId);
    	query.setParameter("created", userName);

    	return query.getSingleResult();
    }
	
	@Override
    public Map<Integer, Long> findCreateArticleFcfChart(final Integer year, final Long siteId){
    	Map<Integer, Long> map = new HashMap<Integer, Long>();
    	
    	for (int i = 1; i <= 12; i++){
    		map.put(i, 0L);
    	}
    	
        String hql = "Select MONTH(a.createTime) As month , Count(a.id) As total " +
        		     "From ArticleMain As m Left Join m.article As a " +
        		     ", Channel As c Right Join c.site As s " +
        		     " Where c.id=m.channelId And s.id=:siteId " +
        		     " And YEAR(a.createTime)=:year And a.delete=false " +
        		     " Group By MONTH(a.createTime)";
        
        TypedQuery<Object[]> query = em.createQuery(hql, Object[].class);
        query.setParameter("year", year);
        query.setParameter("siteId", siteId);
        
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
        	Integer month = (Integer)result[0];
            Long count = (Long)result[1];
            map.put(month, count);
        }
        return map;
    }
    
	@Override
    public Map<Integer, Long> findReleaseArticleFcfChart(final Integer year, final Long siteId){
    	Map<Integer, Long> map = new HashMap<Integer, Long>();
    	
    	for (int i = 1; i <= 12; i++){
    		map.put(i, 0L);
    	}
    	
        String hql = "Select MONTH(a.published) As month , Count(a.id) As total " +
        			 "From ArticleMain As m Left Join m.article As a " +
        		     ", Channel As c Right Join c.site As s " +
        		     " Where c.id=m.channelId And s.id=:siteId " +
        		     " And YEAR(a.published)=:year And a.status=:status And a.delete=false" +
        			 " Group By MONTH(a.published)";
        
        TypedQuery<Object[]> query = em.createQuery(hql, Object[].class);
        query.setParameter("year", year);
        query.setParameter("siteId", siteId);
        query.setParameter("status", Status.RELEASE);
        
        List<Object[]> results = query.getResultList();
        for (Object[] result : results) {
        	Integer month = (Integer)result[0];
            Long count = (Long)result[1];
            map.put(month, count);
        }
        return map;
    }
    
	@Override
    public Map<String, Long> findReleaseArticlePersonFcfChart(final Integer year, final Long siteId){
    	Map<String, Long> map = new HashMap<String, Long>();
    	String hql = "Select a.created As person, Count(a.id) As total " +
    				 "From ArticleMain As m Left Join m.article As a " +
		     		 ", Channel As c Right Join c.site As s " +
    				 " Where c.id=m.channelId And s.id=:siteId " + 
    				 " And YEAR(a.published)=:year And a.status=:status And a.delete=false " +
    				 " Group By a.created";
    	TypedQuery<Object[]> query = em.createQuery(hql, Object[].class);
    	query.setParameter("year", year);
    	query.setParameter("status", Status.RELEASE);
    	query.setParameter("siteId", siteId);
    	List<Object[]> results = query.getResultList();
    	for (Object[] result : results){
    		String person = (String) result[0];
    		Long count = (Long) result[1];
    		map.put(person, count);
    	}
    	return map;
    }
    
	@Override
    public List<ArticleMain> findArticleMainByTitlePrerelease(){
    	String hql = "Select m From ArticleMain As m Left Join m.article As a Where a.type=:type And a.status=:status And a.delete=false And m.reference=false And a.url Is Not Null";
    	
    	TypedQuery<ArticleMain> query = em.createQuery(hql, ArticleMain.class);
    	query.setParameter("type", Article.Genre.TITLE);
    	query.setParameter("status", Article.Status.PRERELEASE);
    	
    	return query.getResultList();
    }
    
	@Override
    public List<ArticleMain> findArticleMainByArticleIdAndReference(Long articleId, Boolean reference){
    	String hql = "Select m From ArticleMain As m Where m.article.id=:articleId And m.reference=:reference";
    	
    	TypedQuery<ArticleMain> query = em.createQuery(hql, ArticleMain.class);
    	query.setParameter("articleId", articleId);
    	query.setParameter("reference", reference);
    	
    	return query.getResultList();
    }
}
