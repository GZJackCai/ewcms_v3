package com.ewcms.visit.dao.totality;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.totality.Entry;
import com.ewcms.visit.model.totality.EntryPk;

public interface EntryDao extends PagingAndSortingRepository<Entry, EntryPk>, JpaSpecificationExecutor<Entry>{

	@Query("select new Entry( "
			+ "count(v.visitPk.url)"
			+ ") "
			+ "from Visit v "
			+ "where v.visitPk.visitDate=?1 and v.visitPk.siteId=?2 and v.visitPk.url is not null and v.visitPk.url=?3 and v.event=?4")
	Entry findEntry(Date visitDate, Long siteId, String url, String event);
	
	@Query("select new Entry( "
			+ "e.entryPk.url, "
			+ "sum(e.entryCount), "
			+ "(select sum(e1.entryCount) from Entry e1 where e1.entryPk.visitDate between ?1 and ?2 and e1.entryPk.siteId=?3 and e1.entryPk.event=?4) "
			+ ") "
			+ "from Entry e "
			+ "where e.entryPk.visitDate between ?1 and ?2 and e.entryPk.siteId=?3 and e.entryPk.event=?4 "
			+ "group by e.entryPk.url "
			+ "order by sum(e.entryCount) desc")
	Page<Entry> findEntry(Date startVisitDate, Date endVisitDate, Long siteId, String event, Pageable pageRequest);
	
	@Query("select count(distinct e.entryPk.url) "
			+ "from Entry e "
			+ "where e.entryPk.visitDate between ?1 and ?2 and e.entryPk.siteId=?3 and e.entryPk.event=?4")
	Long countEntry(Date startVisitDate, Date endVisitDate, Long siteId, String event);
	
	@Query("select new Entry( "
			+ "e.entryPk.visitDate, "
			+ "sum(e.entryCount)"
			+ ") "
			+ "from Entry e "
			+ "where e.entryPk.visitDate between ?1 and ?2 and e.entryPk.siteId=?3 and e.entryPk.event=?4 and e.entryPk.url=?5 "
			+ "group by e.entryPk.visitDate "
			+ "order by e.entryPk.visitDate")
	List<Entry> findEntry(Date startVisitDate, Date endVisitDate, Long siteId, String event, String url);
}
