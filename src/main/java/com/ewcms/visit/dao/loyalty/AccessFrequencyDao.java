package com.ewcms.visit.dao.loyalty;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.visit.model.loyalty.AccessFrequency;
import com.ewcms.visit.model.loyalty.AccessFrequencyPk;

/**
 *
 * @author 吴智俊
 */
public interface AccessFrequencyDao extends JpaRepository<AccessFrequency, AccessFrequencyPk>, JpaSpecificationExecutor<AccessFrequency>{

	@Query("select new AccessFrequency("
			+ "count(frequency)"
			+ ") "
			+ "from Visit "
			+ "where visitPk.visitDate=?1 and visitPk.siteId=?2 and frequency=?3 and frequency is not null")
	AccessFrequency findFrequency(Date visitDate, Long siteId, Long frequency);
	
	@Query("select new AccessFrequency("
			+ "(case when accessFrequencyPk.frequency<30 then accessFrequencyPk.frequency else 30 end) as level, "
			+ "sum(frequencyCount), "
			+ "(select sum(frequencyCount) from AccessFrequency where accessFrequencyPk.visitDate between ?1 and ?2 and accessFrequencyPk.siteId=?3)"
			+ ") "
			+ "from AccessFrequency "
			+ "where accessFrequencyPk.visitDate between ?1 and ?2 and accessFrequencyPk.siteId=?3 "
			+ "group by (case when accessFrequencyPk.frequency<30 then accessFrequencyPk.frequency else 30 end) "
			+ "order by level desc")
	List<AccessFrequency> findFrequency(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new AccessFrequency("
			+ "accessFrequencyPk.visitDate, "
			+ "sum(frequencyCount)"
			+ ") "
			+ "from AccessFrequency "
			+ "where accessFrequencyPk.visitDate between ?1 and ?2 and accessFrequencyPk.siteId=?3 and (case when accessFrequencyPk.frequency<30 then accessFrequencyPk.frequency else 30 end)=?4 "
			+ "group by accessFrequencyPk.visitDate "
			+ "order by accessFrequencyPk.visitDate")
	List<AccessFrequency> findFrequency(Date startVisitDate, Date endVisitDate, Long siteId, Long frequency);
}
