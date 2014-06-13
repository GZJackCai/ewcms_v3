/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.vote.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.content.vote.dao.QuestionnaireDao;
import com.ewcms.content.vote.dao.SubjectDao;
import com.ewcms.content.vote.model.Questionnaire;
import com.ewcms.content.vote.model.Subject;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 问卷调查主题Service
 * 
 * @author 吴智俊
 */
@Component
public class SubjectService {

	@Autowired
	private QuestionnaireDao questionnaireDao;
	@Autowired
	private SubjectDao subjectDao;
	
	public Long addSubject(Long questionnaireId, Subject subject) {
		Assert.notNull(questionnaireId);
		Long maxSort = subjectDao.findSubjectMaxSort(questionnaireId);
		if (maxSort == null) maxSort = 0L;
		Questionnaire questionnaire = questionnaireDao.findOne(questionnaireId);
		Assert.notNull(questionnaire);
		
		subject.setQuestionnaire(questionnaire);
		subject.setSort(maxSort + 1);
		
		subjectDao.save(subject);
		return subject.getId();
	}

	public void delSubject(Long subjectId) {
		subjectDao.delete(subjectId);
	}

	public Subject findSubject(Long subjectId) {
		return subjectDao.findOne(subjectId);
	}

	public Long updSubject(Long questionnaireId, Subject subject) {
		Subject subject_old = subjectDao.findOne(subject.getId());
		if (subject.getStatus() != Subject.Status.INPUT){
			subject.setSubjectItems(subject_old.getSubjectItems());
		}
		subjectDao.save(subject);
		return subject.getId();
	}

	public void downSubject(Long questionnaireId, Long subjectId) {
		Assert.notNull(questionnaireId);
		Subject subject = subjectDao.findOne(subjectId);
		Assert.notNull(subject);
		Long sort = subject.getSort();
		if (sort == null){
			sort = subjectDao.findSubjectMaxSort(questionnaireId);
			subject.setSort(sort + 1);
			subjectDao.save(subject);
		}else{
			Subject subject_next = subjectDao.findSubjectBySort(questionnaireId, sort + 1);
			if (subject_next != null){
				subject_next.setSort(sort);
				subjectDao.save(subject_next);
				subject.setSort(sort + 1);
				subjectDao.save(subject);
			}
		}
	}

	public void upSubject(Long questionnaireId, Long subjectId) {
		Assert.notNull(questionnaireId);
		Subject subject = subjectDao.findOne(subjectId);
		Assert.notNull(subject);
		Long sort = subject.getSort();
		if (sort == null){
			sort = subjectDao.findSubjectMaxSort(questionnaireId);
			subject.setSort(sort + 1);
			subjectDao.save(subject);
		}else{
			Subject subject_prv = subjectDao.findSubjectBySort(questionnaireId, sort - 1);
			if (subject_prv != null){
				subject_prv.setSort(sort);
				subjectDao.save(subject_prv);
				subject.setSort(sort - 1);
				subjectDao.save(subject);
			}
		}
	}
	
	public Map<String, Object> search(QueryParameter params, Long questionnaireId){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_questionnaire.id", questionnaireId);
		params.setParameters(parameters);
		
		return SearchMain.search(params, "IN_id", Long.class, subjectDao, Subject.class);
	}
}
