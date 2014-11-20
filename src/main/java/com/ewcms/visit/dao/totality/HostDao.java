package com.ewcms.visit.dao.totality;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.totality.Host;
import com.ewcms.visit.model.totality.HostPk;

public interface HostDao extends PagingAndSortingRepository<Host, HostPk>, JpaSpecificationExecutor<Host>{
	
	@Query("select new Host( "
			+ "sum(v.pageView)"
			+ ") "
			+ "from Visit v "
			+ "where v.visitPk.visitDate=?1 and v.visitPk.siteId=?2 and v.host is not null and v.host=?3")
	Host findHost(Date visitDate, Long siteId, String host);
	
	@Query("select new Host( "
			+ "h.hostPk.hostName, "
			+ "sum(h.hostCount), "
			+ "(select sum(h1.hostCount) from Host h1 where h1.hostPk.visitDate between ?1 and ?2 and h1.hostPk.siteId=?3) "
			+ ") "
			+ "from Host h "
			+ "where h.hostPk.visitDate between ?1 and ?2 and h.hostPk.siteId=?3 "
			+ "group by h.hostPk.hostName "
			+ "order by sum(h.hostCount) desc")
	List<Host> findHostName(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new Host( "
			+ "h.hostPk.visitDate, "
			+ "sum(h.hostCount) "
			+ ") "
			+ "from Host h "
			+ "where h.hostPk.visitDate between ?1 and ?2 and h.hostPk.siteId=?3 and h.hostPk.hostName=?4 "
			+ "group by h.hostPk.visitDate "
			+ "order by h.hostPk.visitDate")
	List<Host> findHostVisitDate(Date startVisitDate, Date endVisitDate, Long siteId, String host);

}
