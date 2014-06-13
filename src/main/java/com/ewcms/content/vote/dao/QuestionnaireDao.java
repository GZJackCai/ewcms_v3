package com.ewcms.content.vote.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.vote.model.Questionnaire;

/**
 * @author 吴智俊
 */
public interface QuestionnaireDao extends PagingAndSortingRepository<Questionnaire, Long>, JpaSpecificationExecutor<Questionnaire> {
	
	@Query("select max(q.sort) from Questionnaire q where q.channelId=?1")
	Long findQuestionnaireMaxSort(Long channelId);
	
	@Query("select count(p.id) from Person p where p.questionnaireId=?1")
	Long findPersonCount(final Long questionnaireId);
}
