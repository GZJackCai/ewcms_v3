package com.ewcms.visit.dao.clientside;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.visit.model.clientside.ClientSide;
import com.ewcms.visit.model.clientside.ClientSidePk;
import com.ewcms.visit.model.clientside.ClientSidePk.ClientSideType;

/**
 *
 * @author 吴智俊
 */
public interface ClientSideDao extends JpaRepository<ClientSide, ClientSidePk>, JpaSpecificationExecutor<ClientSide>{
	
	@Query("select new ClientSide( "
			+ "s.clientSidePk.clientSideName, "
			+ "sum(s.clientSideCount), "
			+ "(select sum(s1.clientSideCount) from ClientSide s1 where s1.clientSidePk.visitDate between ?1 and ?2 and s1.clientSidePk.siteId=?3 and s1.clientSidePk.clientSideType=?4) "
			+ ") "
			+ "from ClientSide s "
			+ "where s.clientSidePk.visitDate between ?1 and ?2 and s.clientSidePk.siteId=?3 and s.clientSidePk.clientSideType=?4 "
			+ "group by s.clientSidePk.clientSideName "
			+ "order by s.clientSidePk.clientSideName ")
	List<ClientSide> findClientSide(Date startVisitDate, Date endVisitDate, Long siteId, ClientSideType clientSideType);
	
	@Query("select new ClientSide( "
			+ "s.clientSidePk.visitDate, "
			+ "sum(s.clientSideCount) "
			+ ") "
			+ "from ClientSide s "
			+ "where s.clientSidePk.visitDate between ?1 and ?2 and s.clientSidePk.siteId=?3 and s.clientSidePk.clientSideType=?4 and s.clientSidePk.clientSideName=?5 "
			+ "group by s.clientSidePk.visitDate "
			+ "order by s.clientSidePk.visitDate"
			)
	List<ClientSide> findClientSide(Date startVisitDate, Date endVisitDate, Long siteId, ClientSideType clientSideType, String clientSideName);

}
