package com.ewcms.visit.dao.traffic;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.visit.model.traffic.ArticleClick;
import com.ewcms.visit.model.traffic.ArticleClickPk;

/**
 *
 * @author 吴智俊
 */
public interface ArticleClickDao extends PagingAndSortingRepository<ArticleClick, ArticleClickPk>, JpaSpecificationExecutor<ArticleClick>{

	@Query("select new ArticleClick(" +
			"sum(v.pageView), avg(v.stickTime)) " +
			"from Article a, Channel c, Visit v " +
			"where a.id=v.articleId and c.id=v.channelId and v.visitPk.visitDate=?1 and v.visitPk.siteId=?2 and v.channelId=?3 and v.articleId=?4")
	ArticleClick findArticleClick(Date visitDate, Long siteId, Long channelId, Long articleId);
	
	@Query("select new ArticleClick(" +
			"c.name, a.title, a.url, a.created, sum(t.pageViewSum), sum(t.stickTimeAvg)) " +
			"from Article a, Channel c, ArticleClick t " +
			"where a.id=t.articleClickPk.articleId and c.id=t.articleClickPk.channelId and t.articleClickPk.visitDate between ?1 and ?2 and t.articleClickPk.siteId=?3 and t.articleClickPk.channelId in ?4 " +
			"group by c.name, a.title, a.url, a.created " +
			"order by sum(t.pageViewSum) desc"
			)
	Page<ArticleClick> findArticleClick(Date startVisitDate, Date endVisitDate, Long siteId, List<Long> channelIds, Pageable pageRequest);
	
	@Query("select count(distinct t.articleClickPk.articleId) " +
			"from Article a, Channel c, ArticleClick t " +
			"where a.id=t.articleClickPk.articleId and c.id=t.articleClickPk.channelId and t.articleClickPk.visitDate between ?1 and ?2 and t.articleClickPk.siteId=?3 and t.articleClickPk.channelId in ?4 "
			)
	Long countArticleClick(Date startVisitDate, Date endVisitDate, Long siteId, List<Long> channelIds);
	
	@Query("select new ArticleClick(" +
			"c.name, a.title, a.url, a.created, sum(t.pageViewSum), sum(t.stickTimeAvg)) " +
			"from Article a, Channel c, ArticleClick t " +
			"where a.id=t.articleClickPk.articleId and c.id=t.articleClickPk.channelId and t.articleClickPk.visitDate between ?1 and ?2 and t.articleClickPk.siteId=?3 " +
			"group by c.name, a.title, a.url, a.created " +
			"order by sum(t.pageViewSum) desc"
			)
	Page<ArticleClick> findArticleClick(Date startVisitDate, Date endVisitDate, Long siteId, Pageable pageRequest);
	
	@Query("select count(t.articleClickPk.articleId) " +
			"from Article a, Channel c, ArticleClick t " +
			"where a.id=t.articleClickPk.articleId and c.id=t.articleClickPk.channelId and t.articleClickPk.visitDate between ?1 and ?2 and t.articleClickPk.siteId=?3 "
			)
	Long countArticleClick(Date startVisitDate, Date endVisitDate, Long siteId);
}
