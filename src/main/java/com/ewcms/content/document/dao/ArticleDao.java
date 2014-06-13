package com.ewcms.content.document.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.document.model.Article;

/**
 * @author 吴智俊
 */
public interface ArticleDao extends PagingAndSortingRepository<Article, Long>, JpaSpecificationExecutor<Article>, ArticleDaoCustom{

}
