package com.ewcms.visit.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.Visit;
import com.ewcms.visit.vo.loyalty.DepthVo;
import com.ewcms.visit.vo.loyalty.FrequencyVo;

/**
 * 忠诚度分析
 * 
 * @author 吴智俊
 */
public interface LoyaltyDao extends PagingAndSortingRepository<Visit, Long>, JpaSpecificationExecutor<Visit>, LoyaltyDaoCustom{

	@Query("select new com.ewcms.visit.vo.loyalty.DepthVo(" +
			"(case when depth<30 then depth else 30 end) as level, count(depth), (select count(depth) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and depth is not null)) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 and depth is not null " +
			"group by (case when depth<30 then depth else 30 end) " +
			"order by level desc")
	public List<DepthVo> findDepthTable(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, count(depth) as countDepth) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and (case when depth<30 then depth else 30 end)=?3 and siteId=?4 and depth is not null " +
			"group by visitDate " +
			"order by visitDate")
	public List<Map> findDepthTrendReport(Date startDate, Date endDate, Long depth, Long siteId);

	@Query("select new com.ewcms.visit.vo.loyalty.FrequencyVo(" +
			"(case when frequency<30 then frequency else 30 end) as level, count(frequency), (select count(frequency) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and frequency is not null)) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 and frequency is not null " +
			"group by (case when frequency<30 then frequency else 30 end) " +
			"order by level desc")
	public List<FrequencyVo> findFrequencyTable(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, count(frequency) as countFrequency) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and (case when frequency<30 then frequency else 30 end)=?3 and siteId=?4 and frequency is not null " +
			"group by visitDate " +
			"order by visitDate")
	public List<Map> findFrequencyTrendReport(Date startDate, Date endDate, Long frequency, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, (select count(distinct rvFlag) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and rvFlag=false and rvFlag is not null) as newVisitor," +
			"(select count(distinct rvFlag) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and rvFlag=true and rvFlag is not null) as backVisitor) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
			"group by visitDate " +
			"order by visitDate")
	public List<Map> findBackReport(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, sum(stickTime) as sumSt, avg(stickTime) as avgSt) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
			"group by visitDate " +
			"order by visitDate")
	public List<Map> findStickTimeReport(Date startDate, Date endDate, Long siteId);
}
