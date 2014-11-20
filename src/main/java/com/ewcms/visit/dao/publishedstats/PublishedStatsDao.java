package com.ewcms.visit.dao.publishedstats;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.security.model.User;
import com.ewcms.visit.model.publishedstats.PublishedStats;

/**
 *
 * @author 吴智俊
 */
public interface PublishedStatsDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>{
	@Query("select new com.ewcms.visit.model.publishedstats.PublishedStats( "
			+ "o.name, "
			+ "u.realName, "
			+ "u.loginName, "
			+ "(select count(a.id) from ArticleMain m left join m.article a, Channel As c Left Join c.site As s "
			+ "    where m.isReference=false and a.isDelete=false and a.status='DRAFT' and a.created=u.loginName and m.channelId=c.id "
			+ "        and a.createTime between ?1 and ?2 and s.id=?3 and c.id=?4) as draftCount, "
			+ "(select count(a.id) from ArticleMain m left join m.article a, Channel As c Left Join c.site As s "
			+ "    where m.isReference=false and a.isDelete=false and a.status='REEDIT' and a.created=u.loginName and m.channelId=c.id "
			+ "        and a.createTime between ?1 and ?2 and s.id=?3 and c.id=?4) as reeditCount, "
			+ "(select count(a.id) from ArticleMain m left join m.article a, Channel As c Left Join c.site As s "
			+ "    where m.isReference=false and a.isDelete=false and a.status='REVIEW' and a.created=u.loginName and m.channelId=c.id "
			+ "        and a.modified between ?1 and ?2 and s.id=?3 and c.id=?4) as reviewCount, "
			+ "(select count(a.id) from ArticleMain m left join m.article a, Channel As c Left Join c.site As s "
			+ "    where m.isReference=false and a.isDelete=false and a.status='RELEASE' and a.created=u.loginName and m.channelId=c.id "
			+ "        and a.published between ?1 and ?2 and s.id=?3 and c.id=?4) as releaseCount"
			+ ") "
			+ "from User u left join u.organ o "
			+ "group by o.name, u.realName, u.loginName "
			+ "order by releaseCount Desc, reviewCount Desc, reeditCount Desc, draftCount Desc"
			)
	List<PublishedStats> findPerson(Date startDate, Date endDate, Long siteId, Long channelId);
	
	@Query("select new com.ewcms.visit.model.publishedstats.PublishedStats( "
			+ "c.name, "
			+ "(select count(a.id) from ArticleMain m left join m.article a "
			+ "    where m.isReference=false and a.isDelete=false and a.status='DRAFT' and m.channelId=c.id "
			+ "        and a.createTime between ?1 and ?2) as draftCount, "
			+ "(select count(a.id) from ArticleMain m left join m.article a "
			+ "    where m.isReference=false and a.isDelete=false and a.status='REEDIT' and m.channelId=c.id "
			+ "        and a.modified between ?1 and ?2) as reeditCount, "
			+ "(select count(a.id) from ArticleMain m left join m.article a "
			+ "    where m.isReference=false and a.isDelete=false and a.status='REVIEW' and m.channelId=c.id "
			+ "        and a.modified between ?1 and ?2) as reviewCount, "
			+ "(select count(a.id) from ArticleMain m left join m.article a "
			+ "    where m.isReference=false and a.isDelete=false and a.status='RELEASE' and m.channelId=c.id "
			+ "        and a.published between ?1 and ?2) as releaseCount "
			+ ") "
			+ "from Channel c left join c.site s "
			+ "where c.id=?4 and s.id=?3 "
			+ "group by c.id, c.name"
			)
	PublishedStats findChannel(Date startDate, Date endDate, Long siteId, Long channelId);
	
	@Query("select new com.ewcms.visit.model.publishedstats.PublishedStats( "
			+ "o.name, "
			+ "(select count(a.id) from ArticleMain m left join m.article a, Channel c left join c.site s "
			+ "    where m.isReference=false and a.isDelete=false and a.status='DRAFT' and a.created=u.loginName and s.id=?3 and m.channelId=c.id "
			+ "        and a.createTime between ?1 and ?2) as draftCount, "
			+ "(select count(a.id) from ArticleMain m left join m.article a, Channel c left join c.site s "
			+ "    where m.isReference=false and a.isDelete=false and a.status='REEDIT' and a.created=u.loginName and s.id=?3 and m.channelId=c.id "
			+ "        and a.modified between ?1 and ?2) as reeditCount, "
			+ "(select count(a.id) from ArticleMain m left join m.article a, Channel c left join c.site s "
			+ "    where m.isReference=false and a.isDelete=false and a.status='REVIEW' and a.created=u.loginName and s.id=?3 and m.channelId=c.id "
			+ "        and a.modified between ?1 and ?2) as reviewCount, "
			+ "(select count(a.id) from ArticleMain m left join m.article a, Channel c left join c.site s "
			+ "    where m.isReference=false and a.isDelete=false and a.status='RELEASE' and a.created=u.loginName and s.id=?3 and m.channelId=c.id "
			+ "        and a.published between ?1 and ?2) as releaseCount "
			+ ") "
			+ "from User u right join u.organ o "
			+ "where o.id=?4 "
			+ "group by o.id, o.name, u.loginName "
			)
	PublishedStats findOrgan(Date startDate, Date endDate, Long siteId, Long organId);
}
