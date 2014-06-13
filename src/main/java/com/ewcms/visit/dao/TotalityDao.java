package com.ewcms.visit.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.Visit;
import com.ewcms.visit.vo.totality.HostVo;
import com.ewcms.visit.vo.totality.OnlineVo;
import com.ewcms.visit.vo.totality.RegionVo;
import com.ewcms.visit.vo.totality.SummaryVo;
import com.ewcms.visit.vo.totality.TimeDistributedVo;

/**
 * 总体情况
 * 
 * @author 吴智俊
 */
public interface TotalityDao extends PagingAndSortingRepository<Visit, Long>, JpaSpecificationExecutor<Visit>, TotalityDaoCustom {

	/**
	 * 查询开始统计分析的日期
	 * 
	 * @param siteId 站点编号
	 * @return Date 日期
	 */
	@Query("select min(visitDate) from Visit where siteId=?1")
	public Date findFirstDate(Long siteId);
	
	/**
	 * 查询最后访问的日期
	 * 
	 * @param siteId
	 * @return
	 */
	@Query("select max(visitDate) from Visit where siteId=?1")
	public Date findLastDate(Long siteId);
	
	/**
	 * 当天综合报告
	 * 
	 * @param visitDate 
	 * @param siteId
	 * @return
	 */
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" +
			"'今日', count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
			"from Visit where visitDate=current_date and siteId=?1")
	public SummaryVo findToDaySummary(Long siteId);
	
	/**
	 * 昨日此时综合报告
	 * 
	 * @param siteId
	 * @return
	 */
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" + 
			"'昨日此时', count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
			"from Visit where visitDate=(current_date - 1) and hour(visitTime)=hour(current_time) and siteId=?1")
	public SummaryVo findYesterdayNowSummary(Long siteId);
	
	/**
	 * 今日预计综合报告
	 * 
	 * @param siteId
	 * @return
	 */
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" +
			"'今日预计', count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
			"from Visit where visitDate=current_date and siteId=?1 " +
			"union " +
			"select new com.ewcms.visit.vo.totality.SummaryVo(" + 
			"'今日预计', count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
			"from Visit where visitDate=(current_date - 1) and hour(visitTime)>hour(current_time) and siteId=?1")
	public List<SummaryVo> findToDayForecastSummary(Long siteId);
	
	/**
	 * 昨日综合报告
	 * 
	 * @param siteId
	 * @return
	 */
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" +
			"'昨日', count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
			"from Visit where visitDate=(current_date - 1) and siteId=?1")
	public SummaryVo findYesterdaySummary(Long siteId);

	/**
	 * 本周综合报告
	 * 
	 * @param siteId
	 * @return
	 */
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" +
			"'本周', count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
			"from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3")
	public SummaryVo findThisWeekSummary(Date startDate, Date endDate, Long siteId);
	
	/**
	 * 本月综合报告
	 * 
	 * @param siteId
	 * @return
	 */
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" +
			"'本月', count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
			"from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3")
	public SummaryVo findThisMonthSummary(Date startDate, Date endDate, Long siteId);

	/**
	 * 平均综合报告
	 * 
	 * @param startDate
	 * @param endDate
	 * @param siteId
	 * @return
	 */
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" +
			"'平均', count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
			"from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3")
	public SummaryVo findAvgSummary(Date startDate, Date endDate, Long siteId);
	
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" + 
			"'发生在:'||visitDate, count(distinct ip) as countIp) " +
			"from Visit where siteId=?1 " +
			"group by visitDate " +
			"order by countIp desc")
	public List<SummaryVo> findMaxIpSummary(Long siteId);
	
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" + 
			"'发生在:'||visitDate, count(distinct uniqueId) as countUv) " +
			"from Visit where siteId=?1 " +
			"group by visitDate " +
			"order by countUv desc")
	public List<SummaryVo> findMaxUvSummary(Long siteId);
	
	@Query("select new com.ewcms.visit.vo.totality.SummaryVo(" + 
			"'发生在:'||visitDate, sum(pageView) as sumPv) " +
			"from Visit where siteId=?1 " +
			"group by visitDate " +
			"order by sumPv desc")
	public List<SummaryVo> findMaxPvSummary(Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
	        "visitDate as visitDate, count(distinct ip) as countIp) " +
	        "from Visit " +
	        "where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
	        "group by visitDate " +
	        "order by visitDate")
	public List<Map> findCountIpByVisitDate(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, count(distinct uniqueId) as countUv) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
			"group by visitDate " +
			"order by visitDate")
	public List<Map> findCountUvByVisitDate(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, sum(pageView) as sumPv) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
			"group by visitDate " +
			"order by visitDate")
	public List<Map> findSumPvByVisitDate(Date startDate, Date endDate, Long siteId);
	
//	/**
//	 * 全站点击率报告
//	 * 
//	 * @param startDate
//	 * @param endDate
//	 * @param siteId
//	 * @return
//	 */
//	@Query("select new com.ewcms.visit.vo.totality.SiteClickVo(" +
//			"visitDate, count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
//			"from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
//			"group by visitDate " +
//			"order by visitDate desc")
//	public List<SiteClickVo> findSiteClick(Date startDate, Date endDate, Long siteId);
	
	
	@Query("select count(visitDate) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 group by visitDate")
	public Long findSiteClickRecordCount(Date startDate, Date endDate, Long siteId);

	@Query("select count(url) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and url is not null group by url")
	public Long findEntranceCount(Date startDate, Date endDate, Long siteId);
	
	@Query("select count(url) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and url is not null and event=com.ewcms.visit.util.VisitUtil.UNLOAD_EVENT group by url")
	public Long findExitCount(Date startDate, Date endDate, Long siteId);
	
	@Query("select new com.ewcms.visit.vo.totality.TimeDistributedVo(" +
	        "hour(visitTime), count(distinct ip), count(distinct uniqueId), sum(pageView), count(case when rvFlag=true then 1 else null end) as countRv, count(rvFlag) as sumRv, avg(stickTime)) " +
	        "from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
			"group by hour(visitTime) " +
	        "order by hour(visitTime)")
	public List<TimeDistributedVo> findTimeDistributed(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"hour(visitTime) as timeDate, count(distinct ip) as countIp) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
			"group by hour(visitTime) " +
			"order by hour(visitTime)")
	public List<Map> findCountIpByVisitTime(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"hour(visitTime) as timeDate, count(distinct uniqueId) as countUv) " +
			"from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
			"group by hour(visitTime) " +
			"order by hour(visitTime)")
	public List<Map> findCountUvByVisitTime(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"hour(visitTime) as timeDate, sum(pageView) as sumPv) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
			"group by hour(visitTime) " +
			"order by hour(visitTime)")
	public List<Map> findSumPvByVisitTime(Date startDate, Date endDate, Long siteId);

//	@Query("select new com.ewcms.visit.vo.totality.EntranceVo(" +
//			"url, count(url), (select count(url) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and url is not null)) " +
//			"from Visit " +
//			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 and url is not null " +
//			"group by url " +
//			"order by count(url) desc")
//	public List<EntranceVo> findEntrance(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, count(url) as countUrl) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and url=?3 and siteId=?4 and url is not null " +
			"group by visitDate " +
			"order by visitDate desc")
	public List<Map> findEntranceTrend(Date startDate, Date endDate, String url, Long siteId);
	
//	@Query("select new com.ewcms.visit.vo.totality.EntranceVo(" +
//			"url, count(url), (select count(url) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and url is not null and event=com.ewcms.visit.util.VisitUtil.UNLOAD_EVENT)) " +
//			"from Visit " +
//			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 and url is not null and event=com.ewcms.visit.util.VisitUtil.UNLOAD_EVENT " +
//			"group by url order by " +
//			"count(url) desc")
//	public List<EntranceVo> findExit(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, count(url) as countUrl) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and url=?3 and siteId=?4 and url is not null and event=com.ewcms.visit.util.VisitUtil.UNLOAD_EVENT " +
			"group by visitDate " +
			"order by visitDate desc")
	public List<Map> findExitTrend(Date startDate, Date endDate, String url, Long siteId);
	
	@Query("select new com.ewcms.visit.vo.totality.HostVo(" +
			"host, sum(pageView), (select sum(pageView) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and host is not null)) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 and host is not null " +
			"group by host " +
			"order by sum(pageView) desc")
	public List<HostVo> findHost(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"visitDate as visitDate, sum(pageView) as sumPv) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and host=?3 and siteId=?4 and host is not null " +
			"group by visitDate " +
			"order by visitDate desc")
	public List<Map> findHostTrend(Date startDate, Date endDate, String host, Long siteId);
	
	@Query("select new com.ewcms.visit.vo.totality.RegionVo(" +
			"country, count(distinct ip), count(distinct uniqueId), sum(pageView), avg(stickTime), (select sum(pageView) from Visit where visitDate>=?1 and visitDate<=?2 and siteId=?3 and country is not null)) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 and country is not null " +
			"group by country " +
			"order by sum(pageView) desc")
	public List<RegionVo> findRegionCountryTable(Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"country as country, sum(pageView) as sumPv) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 and country is not null " +
			"group by country")
	public List<Map> findRegionCountryReport(Date startDate, Date endDate, Long siteId);
	
	@Query("select new com.ewcms.visit.vo.totality.RegionVo(" +
			"province, count(distinct ip), count(distinct uniqueId), sum(pageView), avg(stickTime), (select sum(pageView) from Visit where country=?1 and visitDate>=?2 and visitDate<=?3 and siteId=?4 and province is not null))" +
			"from Visit where country=?1 and visitDate>=?2 and visitDate<=?3 and siteId=?4 and province is not null " +
			"group by province " +
			"order by sum(pageView) desc")
	public List<RegionVo> findRegionProvinceTable(String country, Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"province as province, sum(pageView) as sumPv) " +
			"from Visit " +
			"where country=?1 and visitDate>=?2 and visitDate<=?3 and siteId=?4 and province is not null " +
			"group by province")
	public List<Map> findRegionProvinceReport(String country, Date startDate, Date endDate, Long siteId);

	@Query("select new com.ewcms.visit.vo.totality.RegionVo(" +
			"city, count(distinct ip), count(distinct uniqueId), sum(pageView), avg(stickTime), (select sum(pageView) from Visit where country=?1 and province=?2 and visitDate>=?3 and visitDate<=?4 and siteId=?5 and city is not null ))" +
			"from Visit " +
			"where country=?1 and province=?2 and visitDate>=?3 and visitDate<=?4 and siteId=?5 and city is not null " +
			"group by city order " +
			"by sum(pageView) desc")
	public List<RegionVo> findRegionCityTable(String country, String province, Date startDate, Date endDate, Long siteId);
	
	@SuppressWarnings("rawtypes")
	@Query("select new map(" +
			"city as city, sum(pageView) as sumPv) " +
			"from Visit " +
			"where country=?1 and province=?2 and visitDate>=?3 and visitDate<=?4 and siteId=?5 and city is not null " +
			"group by city")
	public List<Map> findRegionCityReport(String country, String province, Date startDate, Date endDate, Long siteId);

	@Query("select new com.ewcms.visit.vo.totality.RegionVo(" +
			"visitDate, count(distinct ip), count(distinct uniqueId), sum(pageView)) " +
			"from Visit " +
			"where country=?1 and visitDate>=?2 and visitDate<=?3 and siteId=?4 " +
			"group by visitDate " +
			"order by visitDate")
	public List<RegionVo> findRegionCountryTrendReport(String country, Date startDate, Date endDate, Long siteId);
	
	@Query("select new com.ewcms.visit.vo.totality.RegionVo(" +
			"visitDate, count(distinct ip), count(distinct uniqueId), sum(pageView)) " +
			"from Visit " +
			"where country=?1 and province=?2 and visitDate>=?3 and visitDate<=?4 and siteId=?5 " +
			"group by visitDate " +
			"order by visitDate")
	public List<RegionVo> findRegionProvinceTrendReport(String country, String province, Date startDate, Date endDate, Long siteId);

	@Query("select new com.ewcms.visit.vo.totality.RegionVo(" +
			"visitDate, count(distinct ip), count(distinct uniqueId), sum(pageView)) " +
			"from Visit " +
			"where country=?1 and province=?2 and city=?3 and visitDate>=?4 and visitDate<=?5 and siteId=?6 " +
			"group by visitDate " +
			"order by visitDate")
	public List<RegionVo> findRegionCityTrendReport(String country, String province, String city, Date startDate, Date endDate, Long siteId);
	
	@Query("select new com.ewcms.visit.vo.totality.OnlineVo(" +
			"hour(visitTime), " +
			"sum(case when stickTime<=5*60 then 1 else 0 end), " +
			"sum(case when stickTime>5*60 and stickTime<=10*60 then 1 else 0 end), " +
			"sum(case when stickTime>10*60 then 1 else 0 end)) " +
			"from Visit " +
			"where visitDate>=?1 and visitDate<=?2 and siteId=?3 " +
			"group by hour(visitTime) " +
			"order by hour(visitTime)")
	public List<OnlineVo> findOnline(Date startDate, Date endDate, Long siteId);
}
