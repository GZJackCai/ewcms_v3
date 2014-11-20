package com.ewcms.visit.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.Visit;
import com.ewcms.visit.model.VisitPk;

public interface VisitDao extends PagingAndSortingRepository<Visit, VisitPk>, JpaSpecificationExecutor<Visit>{
	
	Page<Visit> findByVisitPkSiteId(Long siteId, Pageable pageable);
}
