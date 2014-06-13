package com.ewcms.content.notes.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.notes.model.Memoranda;

/**
 * @author 吴智俊
 */
public interface MemorandaDao extends PagingAndSortingRepository<Memoranda, Long>, JpaSpecificationExecutor<Memoranda> {
	
	@Query("select m from Memoranda m where m.noteDate>=?1 and m.noteDate<?2 and m.userName=?3 order by m.warnTime desc, m.noteDate desc, m.id desc")
	List<Memoranda> findMemorandaByDate(Date beginDate, Date endDate, String userName);
	
	@Query("select m from Memoranda m where m.userName=?1 and m.warn=true and m.warnTime Is not null and m.noteDate is not null")
	List<Memoranda> findMemorandaByWarn(String userName);
	
	@Query("select m from Memoranda m where m.userName=?1 order by m.noteDate desc, m.warnTime desc, m.id desc")
	List<Memoranda> findMemorandaByUserName(String userName);
}
