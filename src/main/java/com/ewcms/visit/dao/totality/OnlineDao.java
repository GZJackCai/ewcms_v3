package com.ewcms.visit.dao.totality;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.totality.Online;
import com.ewcms.visit.model.totality.OnlinePk;

public interface OnlineDao extends PagingAndSortingRepository<Online, OnlinePk>, JpaSpecificationExecutor<Online>{

	@Query("select new Online(sum(case when v.stickTime<=5*60 then 1 else 0 end), sum(case when v.stickTime>5*60 and v.stickTime<=10*60 then 1 else 0 end), sum(case when v.stickTime>10*60 then 1 else 0 end)) from Visit v where v.visitPk.visitDate=?1 and v.visitPk.siteId=?2 and hour(v.visitTime)=?3")
	Online findOnline(Date visitDate, Long siteId, Integer hour);
	
	@Query("select new Online(o.onlinePk.hour, sum(o.fiveCount), sum(o.tenCount), sum(fifteenCount)) from Online o where o.onlinePk.visitDate between ?1 and ?2 and o.onlinePk.siteId=?3 group by o.onlinePk.hour")
	List<Online> findOnline(Date startVisitDate, Date endVisitDate, Long siteId);
}
