package com.ewcms.visit.dao.loyalty;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.visit.model.loyalty.StickTime;
import com.ewcms.visit.model.totality.Summary;
import com.ewcms.visit.model.totality.SummaryPk;

/**
 *
 * @author 吴智俊
 */
public interface StickTimeDao extends JpaRepository<Summary, SummaryPk>, JpaSpecificationExecutor<Summary>{

	@Query("select new com.ewcms.visit.model.loyalty.StickTime("
			+ "s.summaryPk.visitDate,"
			+ "sum(s.stickTimeSum),"
			+ "sum(s.stickTimeAvg)"
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.visitDate between ?1 and ?2 and s.summaryPk.siteId=?3 "
			+ "group by s.summaryPk.visitDate "
			+ "order by s.summaryPk.visitDate desc")
	Page<StickTime> findStickTime(Date startVisitDate, Date endVisitDate, Long siteId, Pageable pageRequest);
	
	@Query("select count(distinct s.summaryPk.visitDate) from Summary s where s.summaryPk.visitDate between ?1 and ?2 and s.summaryPk.siteId=?3")
	Long countStickTime(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new com.ewcms.visit.model.loyalty.StickTime("
			+ "s.summaryPk.visitDate,"
			+ "sum(s.stickTimeSum),"
			+ "sum(s.stickTimeAvg)"
			+ ") "
			+ "from Summary s "
			+ "where s.summaryPk.visitDate between ?1 and ?2 and s.summaryPk.siteId=?3 "
			+ "group by s.summaryPk.visitDate "
			+ "order by s.summaryPk.visitDate desc")
	List<StickTime> findStickTime(Date startVisitDate, Date endVisitDate, Long siteId);

}
