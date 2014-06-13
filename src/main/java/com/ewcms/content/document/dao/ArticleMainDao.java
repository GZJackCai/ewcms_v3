package com.ewcms.content.document.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.document.model.ArticleMain;

/**
 * @author 吴智俊
 */
public interface ArticleMainDao extends PagingAndSortingRepository<ArticleMain, Long>, JpaSpecificationExecutor<ArticleMain>, ArticleMainDaoCustom{

}
