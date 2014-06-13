package com.ewcms.content.vote.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.vote.model.Subject;
import com.ewcms.content.vote.model.SubjectItem;

/**
 * @author 吴智俊
 */
public interface SubjectItemDao extends PagingAndSortingRepository<SubjectItem, Long>, JpaSpecificationExecutor<SubjectItem> {
	
	@Query("select max(i.sort) from Subject s right join s.subjectItems i Where s.id=?1")
	Long findSubjectItemMaxSort(Long subjectId);
	
	@Query("select i from Subject s right join s.subjectItems i where s.id=?1 and s.status=?2")
	SubjectItem findSubjectItemBySubjectAndInputStatus(Long subjectId, Subject.Status status);
	
	@Query("select i from Subject s right join s.subjectItems i where s.id=?1 and i.sort=?2")
	SubjectItem findSubjectItemBySort(Long subjectId, Long sort);
}
