package com.ewcms.visit.dao.traffic;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.traffic.UrlClick;
import com.ewcms.visit.model.traffic.UrlClickPk;

/**
 *
 * @author 吴智俊
 */
public interface UrlClickDao extends PagingAndSortingRepository<UrlClick, UrlClickPk>, JpaSpecificationExecutor<UrlClick>{

	@Query("select new UrlClick( "
			+ "sum(v.pageView) "
			+ ") "
			+ "from Visit v "
			+ "where v.visitPk.visitDate=?1 and v.visitPk.siteId=?2 and v.visitPk.url is not null and v.visitPk.url=?3")
	UrlClick findUrlClick(Date visitDate, Long siteId, String url);
	
	@Query("select new UrlClick( "
			+ "u.urlClickPk.url, "
			+ "sum(u.urlCount), "
			+ "(select sum(v1.urlCount) from UrlClick v1 where v1.urlClickPk.visitDate between ?1 and ?2 and v1.urlClickPk.siteId=?3)"
			+ ") "
			+ "from UrlClick u "
			+ "where u.urlClickPk.visitDate between ?1 and ?2 and u.urlClickPk.siteId=?3 "
			+ "group by u.urlClickPk.url "
			+ "order by sum(u.urlCount) desc")
	Page<UrlClick> findUrlClick(Date startVisitDate, Date endVisitDate, Long siteId, Pageable pageRequest);
	
	@Query("select count(distinct u.urlClickPk.url) "
			+ "from UrlClick u "
			+ "where u.urlClickPk.visitDate between ?1 and ?2 and u.urlClickPk.siteId=?3")
	Long countUrlClick(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new UrlClick( "
			+ "u.urlClickPk.visitDate, "
			+ "sum(u.urlCount)) "
			+ "from UrlClick u "
			+ "where u.urlClickPk.visitDate between ?1 and ?2 and u.urlClickPk.siteId=?3 and u.urlClickPk.url=?4 "
			+ "group by u.urlClickPk.visitDate "
			+ "order by u.urlClickPk.visitDate")
	List<UrlClick> findUrlClick(Date startVisitDate, Date endVisitDate, Long siteId, String url);
}
