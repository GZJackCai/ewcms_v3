package com.ewcms.visit.dao.traffic;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.traffic.ChannelClick;
import com.ewcms.visit.model.traffic.ChannelClickPk;


/**
 *
 * @author 吴智俊
 */
public interface ChannelClickDao extends PagingAndSortingRepository<ChannelClick, ChannelClickPk>, JpaSpecificationExecutor<ChannelClick>{

	@Query("select new ChannelClick(" +
			"(select sum(v.pageView) from Visit v where lc.id=v.channelId and v.visitPk.visitDate=?1 and v.visitPk.siteId=?2), " +
			"(select avg(v.stickTime) from Visit v where lc.id=v.channelId and v.visitPk.visitDate=?1 and v.visitPk.siteId=?2)," +
			"(select sum(v.pageView) from Visit v, Channel c where v.channelId=c.id and v.visitPk.visitDate=?1 and c.parent.id=lc.id and v.visitPk.siteId=?2)," +
			"(select avg(v.stickTime) from Visit v, Channel c where v.channelId=c.id and v.visitPk.visitDate=?1 and c.parent.id=lc.id and v.visitPk.siteId=?2)" +
			") " +
			"from Channel lc " +
			"where lc.id=?3 "
			)
	ChannelClick findChannelClick(Date visitDate, Long siteId, Long channelId);
	
	@Query("select new ChannelClick(" +
			"lc.id, lc.name, " + 
			"(select sum(v.pageViewSum) from ChannelClick v where lc.id=v.channelClickPk.channelId and v.channelClickPk.visitDate between ?1 and ?2 and v.channelClickPk.siteId=?3)," +
			"(select sum(v.stickTimeAvg) from ChannelClick v where lc.id=v.channelClickPk.channelId and v.channelClickPk.visitDate between ?1 and ?2 and v.channelClickPk.siteId=?3)," +
			"(select sum(v.childPageViewSum) from ChannelClick v, Channel c where v.channelClickPk.channelId=c.id and v.channelClickPk.visitDate between ?1 and ?2 and c.parent.id=lc.id and v.channelClickPk.siteId=?3), " +
			"(select sum(v.stickTimeAvg) from ChannelClick v, Channel c where v.channelClickPk.channelId=c.id and v.channelClickPk.visitDate between ?1 and ?2 and c.parent.id=lc.id and v.channelClickPk.siteId=?3) " +
			") " +
			"from Channel lc " +
			"where lc.parent.id=?4 " +
			"group by lc.id, lc.name")
	List<ChannelClick> findChannelClick(Date startVisitDate, Date endVisitDate, Long siteId, Long channelId);
	
	@Query("select new ChannelClick(c.channelClickPk.visitDate, sum(c.pageViewSum)) from ChannelClick c where c.channelClickPk.visitDate between ?1 and ?2 and c.channelClickPk.siteId=?3 and c.channelClickPk.channelId=?4 group by c.channelClickPk.visitDate order by c.channelClickPk.visitDate")
	List<ChannelClick> findChannelClickTrend(Date startVisitDate, Date endVisitDate, Long siteId, Long channelId);
}
