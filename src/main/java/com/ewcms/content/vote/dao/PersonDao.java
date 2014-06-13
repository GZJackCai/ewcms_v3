package com.ewcms.content.vote.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.vote.model.Person;
import com.ewcms.content.vote.model.Record;

/**
 * @author 吴智俊
 */
public interface PersonDao extends PagingAndSortingRepository<Person, Long>, JpaSpecificationExecutor<Person> {
	
	Person findByQuestionnaireIdAndIp(Long questionnaireId, String ip);
	
	@Query("select r from Person p right join p.records r where p.id=?1 and r.subjectName=?2")
	List<Record> findRecordBySubjectTitle(Long personId, String subjectName);
	
	@Query("select r from Person p right join p.records r where p.id=?1 and r.subjectName=?2")
	Record findRecordBySubjectItemTitle(Long personId, String subjectItemName);
}
