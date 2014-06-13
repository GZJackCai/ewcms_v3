package com.ewcms.visit.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.Visit;
import com.ewcms.visit.vo.traffic.ChannelClickVo;

/**
 * 访问量排行
 * 
 * @author 吴智俊
 */
public interface TrafficDao extends PagingAndSortingRepository<Visit, Long>, JpaSpecificationExecutor<Visit>, TrafficDaoCustom{
	
	@Query("select new com.ewcms.visit.vo.traffic.ChannelClickVo(" +
			"lc.id, lc.name, " +
			"(select sum(v.pageView) from Visit v where lc.id=v.channelId and v.visitDate>=?1 and v.visitDate<=?2 and v.siteId=?4), " +
			"(select avg(v.stickTime) from Visit v where lc.id=v.channelId and v.visitDate>=?1 and v.visitDate<=?2 and v.siteId=?4)," +
			"(select sum(v.pageView) from Visit v, Channel c where v.channelId=c.id and v.visitDate>=?1 and v.visitDate<=?2 and c.parent.id=lc.id and v.siteId=?4)," +
			"(select avg(v.stickTime) from Visit v, Channel c where v.channelId=c.id and v.visitDate>=?1 and v.visitDate<=?2 and c.parent.id=lc.id and v.siteId=?4)," +
			"(case when (select count(c.id) from Channel c where c.parent.id=lc.id)>0 then true else false end)" +
			") " +
			"from Channel lc " +
			"where lc.parent.id=?3 " +
			"group by lc.id, lc.name " +
			"order by lc.sort")
	public List<ChannelClickVo> findChannelClickTable(Date startDate, Date endDate, Long parentChannelId, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"lc.name as channelName, (select sum(v.pageView) from Visit v where lc.id=v.channelId and v.visitDate>=?1 and v.visitDate<=?2 and v.siteId=?4) as levelPv," +
			"(select sum(v.pageView) from Visit v, Channel c where v.channelId=c.id and v.visitDate>=?1 and v.visitDate<=?2 and c.parent.id=lc.id and v.siteId=?4) as pv" +
			") " +
			"from Channel lc " +
			"where lc.parent.id=?3 " +
			"group by lc.id, lc.name")
	public List<Map> findChannelClickReport(Date startDate, Date endDate, Long parentChannelId, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, sum(pageView) as sumPv) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and channelId=?3 and siteId=?4 " +
			"group by visitDate " +
			"order by visitDate")
	public List<Map> findChannelClickTrendReport(Date startDate, Date endDate, Long channelId, Long siteId);
	
//	@Query("select new com.ewcms.visit.vo.traffic.ArticleClickVo(" +
//			"c.name, a.title, v.url, a.created, sum(v.pageView), avg(v.stickTime)) " +
//			"from Article a, Channel c, Visit v " +
//			"where a.id=v.articleId and c.id=v.channelId and v.visitDate>=?1 and v.visitDate<=?2 and c.parent.id=?3 and v.siteId=?4 " +
//			"group by c.name, a.title, v.url, a.created " +
//			"order by sum(v.pageView) desc")
//	public List<ArticleClickVo> findArticleClickTable(Date startDate, Date endDate, Long parentChannelId, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, sum(pageView) as sumPv) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and url=?3 and siteId=?4 " +
			"group by visitDate " +
			"order by visitDate")
	public List<Map> findUrlClickTrendReport(Date startDate, Date endDate, String url, Long siteId);
}
