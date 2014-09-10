package com.ewcms.content.document.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.document.model.OperateTrack;

/**
 * @author 吴智俊
 */
public interface OperateTrackDao extends PagingAndSortingRepository<OperateTrack, Long>, JpaSpecificationExecutor<OperateTrack>{
	
//	@Query("select t from OperateTrack t where t.articleMainId=?1 order by t.id desc")
	public List<OperateTrack> findByArticleMainIdOrderByIdDesc(Long articleMainId);
}
