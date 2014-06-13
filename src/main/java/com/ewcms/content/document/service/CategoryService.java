/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.content.document.dao.CategoryDao;
import com.ewcms.content.document.model.Category;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * @author 吴智俊
 */
@Component
public class CategoryService {
	
	@Autowired
	private CategoryDao categoryDao;
	
	public Long addCategory(Category category) {
		Assert.notNull(category);
		categoryDao.save(category);
		return category.getId();
	}

	public Long updCategory(Category category) {
		Assert.notNull(category);
		categoryDao.save(category);
		return category.getId();
	}

	public void delCategory(Long categoryId) {
		categoryDao.delete(categoryId);
	}

	public Category findCategory(Long categoryId) {
		return categoryDao.findOne(categoryId);
	}

	public List<Category> findCategoryAll() {
		return (List<Category>) categoryDao.findAll();
	}
	
	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, categoryDao, Category.class);
	}

}
