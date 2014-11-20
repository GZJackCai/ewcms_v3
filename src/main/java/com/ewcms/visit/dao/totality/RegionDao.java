package com.ewcms.visit.dao.totality;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.totality.Region;
import com.ewcms.visit.model.totality.RegionPk;

/**
 * 
 * @author wu_zhijun
 *
 */
public interface RegionDao extends PagingAndSortingRepository<Region, RegionPk>, JpaSpecificationExecutor<Region>{
	
	@Query("select new Region("
			+ "count(distinct v.visitPk.ip), count(distinct v.visitPk.uniqueId), sum(v.pageView), count(case when v.rvFlag=true then 1 else null end), count(v.rvFlag), avg(v.stickTime)"
			+ ")"
			+ "from com.ewcms.visit.model.Visit v "
			+ "where v.visitPk.visitDate=?1 and v.visitPk.siteId=?2 and v.country=?3 and v.country is not null and v.province=?4 and v.province is not null and v.city=?5 and v.city is not null")
	Region findRegion(Date visitDate, Long siteId, String country, String province, String city);
	
	@Query("select new Region(r.regionPk.country, sum(r.ipCount), sum(r.uniqueIdCount), sum(r.pageViewSum), sum(r.rvFlagCount), sum(r.rvFlagSum), sum(r.stickTimeAvg)) from Region r where r.regionPk.visitDate between ?1 and ?2 and r.regionPk.siteId=?3 and r.regionPk.country is not null group by r.regionPk.country order by sum(r.pageViewSum) desc")
	List<Region> findRegionCountry(Date startVisitDate, Date endVisitDate, Long siteId);
	
	@Query("select new Region(r.regionPk.visitDate, sum(r.ipCount), sum(r.uniqueIdCount), sum(r.pageViewSum), sum(r.rvFlagCount), sum(r.rvFlagSum), sum(r.stickTimeAvg)) from Region r where r.regionPk.visitDate between ?1 and ?2 and r.regionPk.siteId=?3 and r.regionPk.country=?4 group by r.regionPk.visitDate order by r.regionPk.visitDate")
	List<Region> findVisitDateRegionCountry(Date startVisitDate, Date endVisitDate, Long siteId, String country);

	@Query("select new Region(r.regionPk.country, r.regionPk.province, sum(r.ipCount), sum(r.uniqueIdCount), sum(r.pageViewSum), sum(r.rvFlagCount), sum(r.rvFlagSum), sum(r.stickTimeAvg)) from Region r where r.regionPk.visitDate between ?1 and ?2 and r.regionPk.siteId=?3 and r.regionPk.country=?4 and r.regionPk.country is not null and r.regionPk.province is not null group by r.regionPk.country, r.regionPk.province order by sum(r.pageViewSum) desc")
	List<Region> findRegionProvince(Date startVisitDate, Date endVisitDate, Long siteId, String country);
	
	@Query("select new Region(r.regionPk.visitDate, sum(r.ipCount), sum(r.uniqueIdCount), sum(r.pageViewSum), sum(r.rvFlagCount), sum(r.rvFlagSum), sum(r.stickTimeAvg)) from Region r where r.regionPk.visitDate between ?1 and ?2 and r.regionPk.siteId=?3 and r.regionPk.country=?4 and r.regionPk.province=?5 group by r.regionPk.visitDate order by r.regionPk.visitDate")
	List<Region> findVisitDateRegionProvince(Date startVisitDate, Date endVisitDate, Long siteId, String country, String province);
	
	@Query("select new Region(r.regionPk.country, r.regionPk.province, r.regionPk.city, sum(r.ipCount), sum(r.uniqueIdCount), sum(r.pageViewSum), sum(r.rvFlagCount), sum(r.rvFlagSum), sum(r.stickTimeAvg)) from Region r where r.regionPk.visitDate between ?1 and ?2 and r.regionPk.siteId=?3 and r.regionPk.country=?4 and r.regionPk.country is not null and r.regionPk.province=?5 and r.regionPk.province is not null and r.regionPk.city is not null group by r.regionPk.country, r.regionPk.province, r.regionPk.city order by sum(r.pageViewSum) desc")
	List<Region> findRegionCity(Date startVisitDate, Date endVisitDate, Long siteId, String country, String province);
	
	@Query("select new Region(r.regionPk.visitDate, sum(r.ipCount), sum(r.uniqueIdCount), sum(r.pageViewSum), sum(r.rvFlagCount), sum(r.rvFlagSum), sum(r.stickTimeAvg)) from Region r where r.regionPk.visitDate between ?1 and ?2 and r.regionPk.siteId=?3 and r.regionPk.country=?4 and r.regionPk.province=?5 and r.regionPk.city=?6 group by r.regionPk.visitDate order by r.regionPk.visitDate")
	List<Region> findVisitDateRegionCity(Date startVisitDate, Date endVisitDate, Long siteId, String country, String province, String city);
}
