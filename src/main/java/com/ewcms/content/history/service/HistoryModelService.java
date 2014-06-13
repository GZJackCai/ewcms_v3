/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.history.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.content.document.model.Article;
import com.ewcms.content.history.dao.HistoryModelDao;
import com.ewcms.content.history.model.HistoryModel;
import com.ewcms.site.model.Template;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * @author 吴智俊
 */
@Component
@Transactional(readOnly = true)
public class HistoryModelService {

	@Autowired
	private HistoryModelDao historyModelDao;
	
	public HistoryModel findByHistoryModel(Long historyId) {
		return historyModelDao.findOne(historyId);
	}

	@Transactional(readOnly = false)
	public void delHistoryModel(Long historyId) {
		historyModelDao.delete(historyId);
	}

	@Transactional(readOnly = false)
	public void delHistoryModelBeforeDate() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -3);
		date = calendar.getTime();
		
		historyModelDao.delByBeforeDate(date);
	}
	
	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, historyModelDao, HistoryModel.class);
	}
	
	public Map<String, Object> searchTemplate(QueryParameter params, Long templateId){
		String className = Template.class.getName().toString();
		String idName = "id";
		String idValue = templateId.toString();
		
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_className", className);
		parameters.put("EQ_idName", idName);
		parameters.put("EQ_idValue", idValue);
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("createDate",  Direction.DESC);
		params.setSorts(sorts);
		
		return SearchMain.search(params, "IN_id", Long.class, historyModelDao, HistoryModel.class);
	}
	
	public Map<String, Object> searchArticle(QueryParameter params, Long articleId){
		String className = Article.class.getName().toString();
		String idName = "id";
		String idValue = articleId.toString();
		
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_className", className);
		parameters.put("EQ_idName", idName);
		parameters.put("EQ_idValue", idValue);
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("id",  Direction.DESC);
		params.setSorts(sorts);

		return SearchMain.search(params, "IN_id", Long.class, historyModelDao, HistoryModel.class);
	}
}
