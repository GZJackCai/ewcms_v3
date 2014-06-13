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

import com.ewcms.content.vote.dao.SubjectDao;
import com.ewcms.content.vote.dao.SubjectItemDao;
import com.ewcms.content.vote.model.Subject;
import com.ewcms.content.vote.model.SubjectItem;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 问卷调查主题明细Service
 * 
 * @author 吴智俊
 */
@Component
public class SubjectItemService {
	
	@Autowired
	private SubjectDao subjectDao;
	@Autowired
	private SubjectItemDao subjectItemDao;

	public Long addSubjectItem(Long subjectId, SubjectItem subjectItem) {
		Assert.notNull(subjectId);
		Subject subject = subjectDao.findOne(subjectId);
		Assert.notNull(subject);
		
		Long maxSort = subjectItemDao.findSubjectItemMaxSort(subjectId);
		if (maxSort == null) maxSort = 0L;
		subjectItem.setSort(maxSort + 1);
		
		subjectItem.setSubject(subject);
		subjectItemDao.save(subjectItem);
		
		return subjectItem.getId();
	}

	public void delSubjectItem(Long subjectItemId) {
		subjectItemDao.delete(subjectItemId);
	}

	public SubjectItem findSubjectItem(Long subjectItemId) {
		return subjectItemDao.findOne(subjectItemId);
	}

	public Long updSubjectItem(Long subjectId, SubjectItem subjectItem) {
		Subject subject = subjectDao.findOne(subjectId);
		subjectItem.setSubject(subject);
		subjectItemDao.save(subjectItem);
		return subjectItem.getId();
	}

	public SubjectItem findSubjectItemBySubjectAndInputStatus(Long subjectId) {
		return subjectItemDao.findSubjectItemBySubjectAndInputStatus(subjectId, Subject.Status.INPUT);
	}

	public void downSubjectItem(Long subjectId, Long subjectItemId) {
		Assert.notNull(subjectId);
		SubjectItem subjectItem = subjectItemDao.findOne(subjectItemId);
		Assert.notNull(subjectItem);
		Long sort = subjectItem.getSort();
		if (sort == null){
			sort = subjectItemDao.findSubjectItemMaxSort(subjectItemId);
			subjectItem.setSort(sort + 1);
			subjectItemDao.save(subjectItem);
		}else{
			SubjectItem subjectItem_next = subjectItemDao.findSubjectItemBySort(subjectId, sort + 1);
			if (subjectItem_next != null){
				subjectItem_next.setSort(sort);
				subjectItemDao.save(subjectItem_next);
				subjectItem.setSort(sort + 1);
				subjectItemDao.save(subjectItem);
			}
		}
	}

	public void upSubjectItem(Long subjectId, Long subjectItemId) {
		Assert.notNull(subjectId);
		SubjectItem subjectItem = subjectItemDao.findOne(subjectItemId);
		Assert.notNull(subjectItem);
		Long sort = subjectItem.getSort();
		if (sort == null){
			sort = subjectItemDao.findSubjectItemMaxSort(subjectId);
			subjectItem.setSort(sort + 1);
			subjectItemDao.save(subjectItem);
		}else{
			SubjectItem subjectItem_prv = subjectItemDao.findSubjectItemBySort(subjectId, sort - 1);
			if (subjectItem_prv != null){
				subjectItem_prv.setSort(sort);
				subjectItemDao.save(subjectItem_prv);
				subjectItem.setSort(sort - 1);
				subjectItemDao.save(subjectItem);
			}
		}
	}
	
	public Map<String, Object> search(QueryParameter params, Long subjectId){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_subject.id", subjectId);
		params.setParameters(parameters);
		
		return SearchMain.search(params, "IN_id", Long.class, subjectItemDao, SubjectItem.class);
	}

}
