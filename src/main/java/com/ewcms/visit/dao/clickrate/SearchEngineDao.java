package com.ewcms.visit.dao.clickrate;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.visit.model.clickrate.SearchEngine;
import com.ewcms.visit.model.clickrate.SearchEnginePk;

/**
 *
 * @author 吴智俊
 */
public interface SearchEngineDao extends JpaRepository<SearchEngine, SearchEnginePk>, JpaSpecificationExecutor<SearchEngine>{

	@Query("select new SearchEngine( "
			+ "s.searchEnginePk.engineName, "
			+ "sum(s.engineCount),"
			+ "(select sum(s1.engineCount) from SearchEngine s1 where s1.searchEnginePk.visitDate between ?1 and ?2 and s1.searchEnginePk.siteId=?3) "
			+ ") "
			+ "from SearchEngine s "
			+ "where s.searchEnginePk.visitDate between ?1 and ?2 and s.searchEnginePk.siteId=?3 "
			+ "group by s.searchEnginePk.engineName "
			+ "order by s.searchEnginePk.engineName ")
	List<SearchEngine> findSearchEngine(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new SearchEngine( "
			+ "s.searchEnginePk.visitDate, "
			+ "sum(s.engineCount)"
			+ ") "
			+ "from SearchEngine s "
			+ "where s.searchEnginePk.visitDate between ?1 and ?2 and s.searchEnginePk.siteId=?3 and s.searchEnginePk.engineName=?4 "
			+ "group by s.searchEnginePk.visitDate "
			+ "order by s.searchEnginePk.visitDate"
			)
	List<SearchEngine> findSearchEngine(Date startVisitDate, Date endVisitDate, Long siteId, String engineName);
}
