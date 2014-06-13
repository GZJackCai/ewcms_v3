package com.ewcms.content.vote.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.vote.model.Subject;

/**
 * @author 吴智俊
 */
public interface SubjectDao extends PagingAndSortingRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {
	
	@Query("select max(s.sort) from Questionnaire q right join q.subjects s where q.id=?1")
	Long findSubjectMaxSort(Long questionnaireId);
	
	@Query("select s from Questionnaire q right join q.subjects s where q.id=?1 and s.sort=?2")
	Subject findSubjectBySort(Long questionnaireId, Long sort);
}
