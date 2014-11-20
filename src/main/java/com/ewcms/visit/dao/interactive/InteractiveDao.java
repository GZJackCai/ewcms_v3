package com.ewcms.visit.dao.interactive;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.ewcms.site.model.Organ;
import com.ewcms.visit.model.interactive.Interactive;

/**
 *
 * @author 吴智俊
 */
public interface InteractiveDao extends JpaRepository<Organ, Long>, JpaSpecificationExecutor<Organ>{

	@Query("select new com.ewcms.visit.model.interactive.Interactive( "
			+ "o.name, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=1 and i.state=0 and i.organId=o.id and i.date between ?1 and ?2) as zxblCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=1 and i.state=1 and i.organId=o.id and i.replayDate between ?1 and ?2) as zxhfCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=1 and i.checked=true and i.organId=o.id and i.replayDate between ?1 and ?2) as zxtgCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=1 and i.checked=false and i.organId=o.id and i.date between ?1 and ?2) as zxwtgCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=2 and i.state=0 and i.organId=o.id and i.date between ?1 and ?2) as tsblCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=2 and i.state=1 and i.organId=o.id and i.replayDate between ?1 and ?2) as tshfCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=2 and i.checked=true and i.organId=o.id and i.replayDate between ?1 and ?2) as tstgCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=2 and i.checked=false and i.organId=o.id and i.date between ?1 and ?2) as tswtgCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=3 and i.state=0 and i.organId=o.id and i.date between ?1 and ?2) as jyblCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=3 and i.state=1 and i.organId=o.id and i.replayDate between ?1 and ?2) as jyhfCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=3 and i.checked=true and i.organId=o.id and i.replayDate between ?1 and ?2) as jytgCount, "
			+ "(select count(i.id) from Interaction i "
			+ "    where i.type=3 and i.checked=false and i.organId=o.id and i.date between ?1 and ?2) as jytgCount"
			+ ") "
			+ "from Organ o "
			+ "where o.id=?3 "
			+ "group by o.id, o.name"
			)
	Interactive findZhengMin(Date startDate, Date endDate, Long organId);
	
	@Query("select new com.ewcms.visit.model.interactive.Interactive( "
			+ "o.name, "
			+ "(select count(i.id) from Advisor i "
			+ "    where i.state=1 and i.organ.id=o.id and i.replayDate between ?1 and ?2), "
			+ "(select count(i.id) from Advisor i "
			+ "    where i.state=1 and i.organ.id=o.id and i.date between ?1 and ?2) "
			+ ") "
			+ "from Organ o "
			+ "where o.id=?3 "
			+ "group by o.id, o.name"
			)
	Interactive findAdvisory(Date startDate, Date endDate, Long organId);
}
