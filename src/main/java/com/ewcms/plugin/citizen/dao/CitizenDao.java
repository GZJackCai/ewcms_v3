package com.ewcms.plugin.citizen.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.plugin.citizen.model.Citizen;

/**
 * @author 吴智俊
 */
public interface CitizenDao extends PagingAndSortingRepository<Citizen, Long>, JpaSpecificationExecutor<Citizen>{
	Citizen findByName(String name);
	
	@Query("select c from Citizen c where c.id!=?1 and c.name=?2")
	Citizen findByNotIdAndName(Long id, String name);
}
