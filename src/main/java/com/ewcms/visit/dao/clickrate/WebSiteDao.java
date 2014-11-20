package com.ewcms.visit.dao.clickrate;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.visit.model.clickrate.WebSite;
import com.ewcms.visit.model.clickrate.WebSitePk;

/**
 *
 * @author 吴智俊
 */
public interface WebSiteDao extends JpaRepository<WebSite, WebSitePk>, JpaSpecificationExecutor<WebSite>{

	@Query("select new WebSite( "
			+ "s.webSitePk.webSiteName, "
			+ "sum(s.webSiteCount),"
			+ "(select sum(s1.webSiteCount) from WebSite s1 where s1.webSitePk.visitDate between ?1 and ?2 and s1.webSitePk.siteId=?3) "
			+ ") "
			+ "from WebSite s "
			+ "where s.webSitePk.visitDate between ?1 and ?2 and s.webSitePk.siteId=?3 "
			+ "group by s.webSitePk.webSiteName "
			+ "order by s.webSitePk.webSiteName ")
	List<WebSite> findWebSite(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new WebSite( "
			+ "s.webSitePk.visitDate, "
			+ "sum(s.webSiteCount)"
			+ ") "
			+ "from WebSite s "
			+ "where s.webSitePk.visitDate between ?1 and ?2 and s.webSitePk.siteId=?3 and s.webSitePk.webSiteName=?4 "
			+ "group by s.webSitePk.visitDate "
			+ "order by s.webSitePk.visitDate"
			)
	List<WebSite> findWebSite(Date startVisitDate, Date endVisitDate, Long siteId, String webSiteName);
}
