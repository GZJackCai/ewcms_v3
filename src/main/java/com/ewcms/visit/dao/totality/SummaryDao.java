package com.ewcms.visit.dao.totality;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.totality.SummaryPk;
import com.ewcms.visit.model.totality.Summary;

public interface SummaryDao extends PagingAndSortingRepository<Summary, SummaryPk>, JpaSpecificationExecutor<Summary>{
	
	@Query("select new Summary("
			+ "count(distinct v.visitPk.ip), "
			+ "count(distinct v.visitPk.uniqueId), "
			+ "sum(v.pageView), "
			+ "count(case when v.rvFlag=true then 1 else null end), "
			+ "count(v.rvFlag), "
			+ "avg(v.stickTime),"
			+ "sum(v.stickTime)"
			+ ") "
			+ "from com.ewcms.visit.model.Visit v "
			+ "where v.visitPk.visitDate=?1 and hour(v.visitTime)=?2 and v.visitPk.siteId=?3")
	Summary findSummary(Date visitDate, Integer hour, Long siteId);

	@Query("select min(s.summaryPk.visitDate) from Summary s where s.summaryPk.siteId=?1")
	Date findMinVisitDate(Long siteId);
	
	@Query("select max(s.summaryPk.visitDate) from Summary s where s.summaryPk.siteId=?1")
	Date findMaxVisitDate(Long siteId);
	
	@Query("select new Summary( "
			+ "sum(s.ipCount), "
			+ "sum(s.uniqueIdCount), "
			+ "sum(s.pageViewSum), "
			+ "sum(s.rvFlagCount), "
			+ "sum(s.rvFlagSum), "
			+ "sum(s.stickTimeAvg)"
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.visitDate between ?1 and ?2 and s.summaryPk.siteId=?3")
	Summary findSumSummary(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new Summary( "
			+ "s.ipCount, "
			+ "s.uniqueIdCount, "
			+ "s.pageViewSum, "
			+ "s.rvFlagCount, "
			+ "s.rvFlagSum, "
			+ "s.stickTimeAvg "
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.visitDate=?1 and s.summaryPk.hour=?2 and s.summaryPk.siteId=?3")
	Summary findYesterdayNowSummary(Date visitDate, Integer hour, Long siteId);
	
	@Query("select new Summary( "
			+ "sum(s.ipCount), "
			+ "sum(s.uniqueIdCount), "
			+ "sum(s.pageViewSum), "
			+ "sum(s.rvFlagCount), "
			+ "sum(s.rvFlagSum), "
			+ "sum(s.stickTimeAvg) "
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.visitDate=?1 and s.summaryPk.hour>?2 and s.summaryPk.siteId=?3")
	Summary findTodayForecastSummary(Date visitDate, Integer hour, Long siteId);
	
	@Query("select new Summary( "
			+ "'发生在:'||s.summaryPk.visitDate, "
			+ "s.ipCount "
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.siteId=?1 and s.ipCount=(select max(s1.ipCount) from Summary s1)")
	Summary findMaxIpCount(Long siteId);
	
	@Query("select new Summary( "
			+ "'发生在:'||s.summaryPk.visitDate, "
			+ "s.uniqueIdCount "
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.siteId=?1 and s.uniqueIdCount=(select max(s1.uniqueIdCount) from Summary s1)")
	Summary findMaxUniqueIdCount(Long siteId);
	
	@Query("select new Summary( "
			+ "'发生在:'||s.summaryPk.visitDate, "
			+ "s.pageViewSum "
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.siteId=?1 and s.pageViewSum=(select max(s1.pageViewSum) from Summary s1)")
	Summary findMaxPageViewSum(Long siteId);
	
	@Query("select new Summary( "
			+ "s.summaryPk.visitDate, "
			+ "sum(s.ipCount), "
			+ "sum(s.uniqueIdCount), "
			+ "sum(s.pageViewSum), "
			+ "sum(s.rvFlagCount), "
			+ "sum(s.rvFlagSum), "
			+ "sum(s.stickTimeAvg) "
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.visitDate between ?1 and ?2 and s.summaryPk.siteId=?3 "
			+ "group by s.summaryPk.visitDate "
			+ "order by s.summaryPk.visitDate")
	List<Summary> findVisitDateSummary(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new Summary( "
			+ "s.summaryPk.visitDate, "
			+ "sum(s.ipCount), "
			+ "sum(s.uniqueIdCount), "
			+ "sum(s.pageViewSum), "
			+ "sum(s.rvFlagCount), "
			+ "sum(s.rvFlagSum), "
			+ "sum(s.stickTimeAvg) "
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.visitDate between ?1 and ?2 and s.summaryPk.siteId=?3 "
			+ "group by s.summaryPk.visitDate "
			+ "order by s.summaryPk.visitDate")
	Page<Summary> findVisitDateSummary(Date startVisitDate, Date endVisitDate, Long siteId, Pageable pageRequest);
	
	@Query("select new Summary( "
			+ "s.summaryPk.hour, "
			+ "sum(s.ipCount), "
			+ "sum(s.uniqueIdCount), "
			+ "sum(s.pageViewSum), "
			+ "sum(s.rvFlagCount), "
			+ "sum(s.rvFlagSum), "
			+ "sum(s.stickTimeAvg) "
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.visitDate between ?1 and ?2 and s.summaryPk.siteId=?3 "
			+ "group by s.summaryPk.hour "
			+ "order by s.summaryPk.hour")
	List<Summary> findHourSummary(Date startDate, Date endDate, Long siteId);
}
