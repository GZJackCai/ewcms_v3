/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.vote.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.content.vote.dao.PersonDao;
import com.ewcms.content.vote.dao.QuestionnaireDao;
import com.ewcms.content.vote.dao.SubjectDao;
import com.ewcms.content.vote.dao.SubjectItemDao;
import com.ewcms.content.vote.model.Person;
import com.ewcms.content.vote.model.Questionnaire;
import com.ewcms.content.vote.model.Record;
import com.ewcms.content.vote.model.Subject;
import com.ewcms.content.vote.model.SubjectItem;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 投票人员信息Service
 * 
 * @author 吴智俊
 */
@Component
public class PersonService {
	
	protected static final Logger logger = LoggerFactory.getLogger(PersonService.class);
	
	@Autowired
	private QuestionnaireDao questionnaireDao;
	@Autowired
	private PersonDao personDao;
	@Autowired
	private SubjectDao subjectDao;
	@Autowired
	private SubjectItemDao subjectItemDao;
	
	public Long addPerson(Person person) {
		personDao.save(person);
//		personDao.flush(person);
		updVoteNumber(person);
		
		Questionnaire questionnaire = questionnaireDao.findOne(person.getQuestionnaireId());
		questionnaire.setNumberSum(questionnaire.getNumberSum() + 1);
		questionnaireDao.save(questionnaire);
		
		return person.getId();
	}
	
	public Boolean findPersonIsEntity(Long questionnaireId, String ip){
		Person person = personDao.findByQuestionnaireIdAndIp(questionnaireId, ip);
		if (person == null) return false;
		return true;
	}
	
	private void updVoteNumber(Person person){
		List<Record> records = person.getRecords();
		for (Record record : records){
			String subjectName = record.getSubjectName();
			String subjectValue = record.getSubjectValue();
			
			String[] recordNames = subjectName.split("_");
			if (recordNames.length == 2){
				if (recordNames[0].equals("Subject")){
					if (!recordNames[1].equals("") && StringUtils.isNumeric(recordNames[1])){
						Long subjectId = new Long(recordNames[1]);
						Subject subject = subjectDao.findOne(subjectId);
						if (subject == null) continue;
						if (subject.getStatus() == Subject.Status.INPUT){
							SubjectItem subjectItem = subjectItemDao.findSubjectItemBySubjectAndInputStatus(subjectId, Subject.Status.INPUT);
							if (subjectItem == null) continue;
							subjectItem.setVoteNumber(subjectItem.getVoteNumber() + 1);
							subjectItemDao.save(subjectItem);
						}else{
							if (!subjectValue.equals("") && StringUtils.isNumeric(subjectValue)){
								Long subjectItemId = new Long(subjectValue);
								SubjectItem subjectItem = subjectItemDao.findOne(subjectItemId);
								subjectItem.setVoteNumber(subjectItem.getVoteNumber() + 1);
								subjectItemDao.save(subjectItem);
							}							
						}
					}
				}
			}else if (recordNames.length == 4){
				if (recordNames[0].equals("Subject") && recordNames[2].equals("Item")){
					if (!recordNames[3].equals("") && StringUtils.isNumeric(recordNames[3])){
						Long subjectItemId = new Long(recordNames[3]);
						SubjectItem subjectItem = subjectItemDao.findOne(subjectItemId);
						subjectItem.setVoteNumber(subjectItem.getVoteNumber() + 1);
						subjectItemDao.save(subjectItem);
					}
				}
			}
		}
	}
	
	public void delPerson(Long personId) {
		personDao.delete(personId);
	}

	public List<String> getRecordToHtml(Long questionnaireId, Long personId){
		List<String> htmls = new ArrayList<String>();
		Questionnaire questionnaire = questionnaireDao.findOne(questionnaireId);
		Assert.notNull(questionnaire);
		List<Subject> subjects = questionnaire.getSubjects();
		Assert.notNull(subjects);
		if (!subjects.isEmpty()){
			Long number = 1L;
			for (Subject subject : subjects){
				StringBuffer html = new StringBuffer();
				html.append(number + "." + subject.getTitle() + " : ");
				
				String subjectName = "Subject_" + subject.getId();
				
				List<Record> records = personDao.findRecordBySubjectTitle(personId, subjectName);
				if (records == null || records.isEmpty()) continue;
				if (subject.getStatus() != Subject.Status.INPUT){
					for (Record record : records){
						String name = record.getSubjectName();
						String[] names = name.split("_");
						if (names.length == 2){
							if (!record.getSubjectValue().equals("") && StringUtils.isNumeric(record.getSubjectValue())){
								SubjectItem subjectItem = subjectItemDao.findOne(new Long(record.getSubjectValue()));
								if (subjectItem == null) continue;
								html.append("【" + subjectItem.getTitle() + "】 ");
								String subjectItemName = subjectName + "_Item_" + subjectItem.getId();
								Record recordItem = personDao.findRecordBySubjectItemTitle(personId, subjectItemName);
								if (recordItem == null) continue;
								html.append(recordItem.getSubjectValue() + " ");
							}else{
								html.append(record.getSubjectValue() + " ");
							}
						}else if (names.length == 4){
							html.append(record.getSubjectValue() + " ");
						}
					}
				}else{
					Record record = records.get(0);
					html.append(record.getSubjectValue());
				}
				htmls.add(html.toString());
				number++;
			}
		}
		return htmls;
	}
	
	public Map<String, Object> search(QueryParameter params, Long questionnaireId){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_questionnaireId", questionnaireId);
		params.setParameters(parameters);
		
		return SearchMain.search(params, "IN_id", Long.class, personDao, Person.class);
	}
}
