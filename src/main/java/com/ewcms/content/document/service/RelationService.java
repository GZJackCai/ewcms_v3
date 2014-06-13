/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.service;

import static com.ewcms.util.EmptyUtil.isNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.ewcms.content.document.dao.ArticleDao;
import com.ewcms.content.document.dao.RelationDao;
import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.model.Relation;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 *
 * @author 吴智俊
 */
@Component
public class RelationService {
	
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private RelationDao relationDao;

	public void deleteRelation(Long articleId, List<Long> relationIds) {
		if (articleId != null && relationIds != null && relationIds.size() > 0){
			for (Long relationId : relationIds){
				relationDao.delete(relationId);
			}
			
			Article article = articleDao.findOne(articleId);
			List<Relation> relations = article.getRelations();
			if (relations != null && !relations.isEmpty()){
				Integer sort = 1;
				List<Relation> relations_new = new ArrayList<Relation>();
				for (Relation relation : relations){
					relation.setSort(sort);
					relations_new.add(relation);
					sort++;
				}
				articleDao.save(article);
			}
		}
	}
	

	public void downRelation(Long articleId, Long relationArticleId) {
		if (articleId != null && relationArticleId != null){
			Article article = articleDao.findOne(articleId);
			List<Relation> relations_old = relationDao.findRelationByArticle(articleId);
			for (Relation relation : relations_old){
				Long relation_article_id = relation.getRelationArticle().getId();
				if (relation_article_id.longValue() == relationArticleId.longValue()){
					Integer sort = relation.getSort();
					if (sort.longValue() < relations_old.size()){
						sort = sort + 1;
						Relation relation_prev = relationDao.findRelationByArticleAndSort(articleId, sort);
						relation_prev.setSort(sort - 1);
						
						relation.setSort(sort);
						relations_old.add(relation);
						relations_old.add(relation_prev);
						break;
					}
				}
			}
			article.setRelations(relations_old);
			
			articleDao.save(article);
		}
	}

	public void saveRelation(Long articleId, List<Long> relationArticleIds) {
		if (articleId != null && relationArticleIds != null && relationArticleIds.size() > 0){
			Article article = articleDao.findOne(articleId);
			List<Relation> relations = article.getRelations();
			if (relations.isEmpty()){
				relations = new ArrayList<Relation>();
			}
			Integer relation_count = relations.size();
			Relation relation = null;
			for (Long relationArticleId : relationArticleIds){
				relation = relationDao.findRelationByArticleAndRelation(articleId, relationArticleId);
				if (isNotNull(relation)) continue;
				relation_count++;
				relation = new Relation();
				Article relation_article = articleDao.findOne(relationArticleId);
				relation.setSort(relation_count);
				relation.setArticle(article);
				relation.setRelationArticle(relation_article);
				relations.add(relation);
			}
			article.setRelations(relations);
			
			articleDao.save(article);
		}
	}

	public void upRelation(Long articleId, Long relationArticleId) {
		if (articleId != null && relationArticleId != null){
			Article article = articleDao.findOne(articleId);
			List<Relation> relations_old = relationDao.findRelationByArticle(articleId);
			for (Relation relation : relations_old){
				Long relation_article_id = relation.getRelationArticle().getId();
				if (relation_article_id.longValue() == relationArticleId.longValue()){
					Integer sort = relation.getSort();
					if (sort.longValue() > 1){
						sort = sort - 1;
						Relation relation_prev = relationDao.findRelationByArticleAndSort(articleId, sort);
						relation_prev.setSort(sort + 1);
						
						relation.setSort(sort);
						relations_old.add(relation);
						relations_old.add(relation_prev);
						break;
					}
				}
			}
			article.setRelations(relations_old);
			
			articleDao.save(article);
		}
	}

	public List<Relation> findRelationByArticle(Long articleId) {
		return relationDao.findRelationByArticle(articleId);
	}
	
	public void delRelations(Long articleId){
		List<Relation> relations = relationDao.findRelationById(articleId);
		relationDao.delete(relations);
	}
	
	public Map<String, Object> searchArticleRelation(Long articleId, QueryParameter params){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_article.isDelete", false);
		parameters.put("EQ_article.id", articleId);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("sort", Direction.ASC);
		
		return SearchMain.search(params, "IN_id", Long.class, relationDao, Relation.class);
	}
}
