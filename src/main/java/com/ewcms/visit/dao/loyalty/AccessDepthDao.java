package com.ewcms.visit.dao.loyalty;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.visit.model.loyalty.AccessDepth;
import com.ewcms.visit.model.loyalty.AccessDepthPk;

/**
 *
 * @author 吴智俊
 */
public interface AccessDepthDao extends JpaRepository<AccessDepth, AccessDepthPk>, JpaSpecificationExecutor<AccessDepth>{

	@Query("select new AccessDepth("
			+ "count(depth)"
			+ ") "
			+ "from Visit "
			+ "where visitPk.visitDate=?1 and visitPk.siteId=?2 and depth=?3 and depth is not null")
	AccessDepth findDepth(Date visitDate, Long siteId, Long depth);
	
	@Query("select new AccessDepth("
			+ "(case when accessDepthPk.depth<30 then accessDepthPk.depth else 30 end) as level, "
			+ "sum(depthCount), "
			+ "(select sum(depthCount) from AccessDepth where accessDepthPk.visitDate between ?1 and ?2 and accessDepthPk.siteId=?3)"
			+ ") "
			+ "from AccessDepth "
			+ "where accessDepthPk.visitDate between ?1 and ?2 and accessDepthPk.siteId=?3 "
			+ "group by (case when accessDepthPk.depth<30 then accessDepthPk.depth else 30 end) "
			+ "order by level desc")
	List<AccessDepth> findDepth(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new AccessDepth("
			+ "accessDepthPk.visitDate, "
			+ "sum(depthCount)"
			+ ") "
			+ "from AccessDepth "
			+ "where accessDepthPk.visitDate between ?1 and ?2 and accessDepthPk.siteId=?3 and (case when accessDepthPk.depth<30 then accessDepthPk.depth else 30 end)=?4 "
			+ "group by accessDepthPk.visitDate "
			+ "order by accessDepthPk.visitDate")
	List<AccessDepth> findDepth(Date startVisitDate, Date endVisitDate, Long siteId, Long depth);
}
