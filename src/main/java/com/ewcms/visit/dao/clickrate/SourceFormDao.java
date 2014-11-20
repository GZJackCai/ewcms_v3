package com.ewcms.visit.dao.clickrate;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.visit.model.clickrate.SourceForm;
import com.ewcms.visit.model.clickrate.SourceFormPk;

/**
 *
 * @author 吴智俊
 */
public interface SourceFormDao extends JpaRepository<SourceForm, SourceFormPk>, JpaSpecificationExecutor<SourceForm>{

	@Query("select new SourceForm( "
			+ "s.sourceFormPk.visitDate,"
			+ "sum(s.directCount), "
			+ "sum(s.searchCount), "
			+ "sum(s.otherCount) "
			+ ") "
			+ "from SourceForm s "
			+ "where s.sourceFormPk.visitDate between ?1 and ?2 and s.sourceFormPk.siteId=?3 "
			+ "group by s.sourceFormPk.visitDate "
			+ "order by s.sourceFormPk.visitDate desc"
			)
	List<SourceForm> findSourceForm(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new SourceForm( "
			+ "sum(s.directCount), "
			+ "sum(s.searchCount), "
			+ "sum(s.otherCount) "
			+ ") "
			+ "from SourceForm s "
			+ "where s.sourceFormPk.visitDate between ?1 and ?2 and s.sourceFormPk.siteId=?3 "
			)
	SourceForm sumSourceForm(Date startVisitDate, Date endVisitDate, Long siteId);
}
