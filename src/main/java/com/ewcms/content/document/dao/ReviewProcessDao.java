package com.ewcms.content.document.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.document.model.ArticleMain;
import com.ewcms.content.document.model.ReviewProcess;

/**
 * @author 吴智俊
 */
public interface ReviewProcessDao extends PagingAndSortingRepository<ReviewProcess, Long>, JpaSpecificationExecutor<ReviewProcess>{
	@Query("select count(p.id) from ReviewProcess p where p.channelId=?1")
	public Long findReviewProcessCountByChannel(final Long channelId);
	
	@Query("select p from ReviewProcess p where p.channelId=?1")
	public List<ReviewProcess> findReviewProcessByChannel(final Long channelId);
	
	@Query("select p from ReviewProcess p where p.id=?1 and p.channelId=?2")
	public ReviewProcess findReviewProcessByIdAndChannel(final Long reviewProcessid, final Long channelId);
	
	@Query("select p from ReviewProcess p where p.channelId=?1 and p.nextProcess is null")
	public ReviewProcess findLastReviewProcessByChannel(final Long channelId);
	
	@Query("select p from ReviewProcess p where p.channelId=?1 and p.prevProcess is null")
	public ReviewProcess findFirstReviewProcessByChannel(final Long channelId);
	
	@Query("select count(p.id) from ReviewProcess p left join p.reviewUsers u where p.id=?1 and u.userName=?2")
	public Long findReviewUserIsEntityByProcessIdAndUserName(final Long reviewProcessId, final String userName);
	
	@Query("select count(p.id) from ReviewProcess p left join p.reviewRoles g where p.id=?1 and g.roleName=?2")
	public Long findReviewRoleIsEntityByProcessIdAndRoleName(final Long reviewProcessId, final String roleName);
	
	@Query("select p from ReviewProcess p where p.channelId=?1 and p.name=?2")
	public ReviewProcess findIsEntityReviewProcessByChannelAndName(final Long channelId, final String name);
	
	@Query("select m from ArticleMain m left join m.article a left join a.reviewProcess r where m.channelId=?1 and r.id=?2 and m.isReference=?3")
	public List<ArticleMain> findArticleMainByReviewProcess(final Long channelId, final Long reviewProcessId, final Boolean reference);
}
