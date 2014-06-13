package com.ewcms.content.document.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.document.model.Relation;

/**
 * @author 吴智俊
 */
public interface RelationDao extends PagingAndSortingRepository<Relation, Long>, JpaSpecificationExecutor<Relation>{
	@Query("select r from Relation r where r.article.id=?1 order by r.sort")
	public List<Relation> findRelationByArticle(Long articleId);
	
	@Query("select r from Relation r where r.article.id=?1 and r.sort=?2")
	public Relation findRelationByArticleAndSort(Long articleId, Integer sort);
	
	@Query("select r from Relation r where r.article.id=?1 and r.relationArticle.id=?2")
	public Relation findRelationByArticleAndRelation(Long articleId, Long relationArticleId);

	@Query("select r from Relation r where r.relationArticle.id=?1 or r.article.id=?1")
	public List<Relation> findRelationById(Long articleId);

}
