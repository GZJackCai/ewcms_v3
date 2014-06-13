/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.service;

import static com.ewcms.util.EmptyUtil.isNotNull;
import static com.ewcms.util.EmptyUtil.isCollectionEmpty;
import static com.ewcms.util.EmptyUtil.isCollectionNotEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.content.document.dao.ArticleMainDao;
import com.ewcms.content.document.dao.ReviewProcessDao;
import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.model.ArticleMain;
import com.ewcms.content.document.model.Article.Status;
import com.ewcms.content.document.model.ReviewProcess;
import com.ewcms.content.document.model.ReviewRole;
import com.ewcms.content.document.model.ReviewUser;
import com.ewcms.plugin.BaseException;
import com.ewcms.security.model.User;
import com.ewcms.security.service.AccountService;

@Component
public class ReviewProcessService {

	@Autowired
	private ReviewProcessDao reviewProcessDao;
	@Autowired
	private ArticleMainDao articleMainDao;
	@Autowired
	private OperateTrackService operateTrackService;
	@Autowired
	private AccountService accountService;
	
	public Long addReviewProcess(Long channelId, ReviewProcess reviewProcess, List<String> userNames, List<String> roleNames) throws BaseException {
		ReviewProcess reviewProcess_entity = reviewProcessDao.findIsEntityReviewProcessByChannelAndName(channelId, reviewProcess.getName());
		if (isNotNull(reviewProcess_entity)){
			throw new BaseException("流程名称已定义，请重新输入其他名称！", "流程名称已定义，请重新输入其他名称！");
		}
		if (isCollectionEmpty(userNames) && isCollectionEmpty(roleNames)){
			throw new BaseException("用户组和用户不能同时为空，必须选择一项以上！", "用户组和用户不能同时为空，必须选择一项以上！");
		}
		List<ReviewProcess> vos = reviewProcessDao.findReviewProcessByChannel(channelId);
		reviewProcess.setChannelId(channelId);
		
		if (roleNames != null && !roleNames.isEmpty()){
			Set<ReviewRole> reviewRoles = new HashSet<ReviewRole>();
			ReviewRole reviewRole;
			for (String roleName : roleNames){
				reviewRole = new ReviewRole();
				reviewRole.setRoleName(roleName);
				reviewRoles.add(reviewRole);
			}
			reviewProcess.setReviewRoles(reviewRoles);
		}
		
		if (userNames != null && !userNames.isEmpty()){
			Set<ReviewUser> reviewUsers = new HashSet<ReviewUser>();
			ReviewUser reviewUser;
			for (String userName : userNames){
				reviewUser = new ReviewUser();
				reviewUser.setUserName(userName);
				reviewUser.setRealName(findUserRealNameByUserName(userName));
				reviewUsers.add(reviewUser);
			}
			reviewProcess.setReviewUsers(reviewUsers);
		}
		
		if (vos == null || vos.isEmpty()){
			reviewProcess.setPrevProcess(null);
			reviewProcessDao.save(reviewProcess);
		}else{
			ReviewProcess lastVo = reviewProcessDao.findLastReviewProcessByChannel(channelId);
			reviewProcess.setPrevProcess(lastVo);
			reviewProcess.setNextProcess(null);
			lastVo.setNextProcess(reviewProcess);
			
			reviewProcessDao.save(lastVo);
		}
		return reviewProcess.getId();
	}

	public void delReviewProcess(Long reviewProcessId) {
		ReviewProcess vo = reviewProcessDao.findOne(reviewProcessId);
		if (vo.getPrevProcess() == null){
			if (vo.getNextProcess() != null){ 
				ReviewProcess nextVo = reviewProcessDao.findOne(vo.getNextProcess().getId());
				nextVo.setPrevProcess(null);
				reviewProcessDao.save(nextVo);
			}
		}else{
			if (vo.getNextProcess() == null){
				ReviewProcess prevVo = reviewProcessDao.findOne(vo.getPrevProcess().getId());
				prevVo.setNextProcess(null);
				reviewProcessDao.save(prevVo);
			}else{
				ReviewProcess prevVo = reviewProcessDao.findOne(vo.getPrevProcess().getId());
				ReviewProcess nextVo = reviewProcessDao.findOne(vo.getNextProcess().getId());
				prevVo.setNextProcess(nextVo);
				nextVo.setPrevProcess(prevVo);
			
				reviewProcessDao.save(nextVo);
				reviewProcessDao.save(prevVo);
			}
		}
		
		List<ArticleMain> articleMains = reviewProcessDao.findArticleMainByReviewProcess(vo.getChannelId(), reviewProcessId, false);
		if (articleMains != null && !articleMains.isEmpty()){
			for (ArticleMain articleMain : articleMains){
				Article article = articleMain.getArticle();
				if (article == null) continue;
				operateTrackService.addOperateTrack(articleMain.getId(), article.getStatusDescription(), "审核流程【" + vo.getName()  + "】已经被删除。", "");

				article.setReviewProcess(null);
				article.setStatus(Status.REVIEWBREAK);
				
				articleMain.setArticle(article);
				articleMainDao.save(articleMain);
			}
		}
		
		reviewProcessDao.delete(reviewProcessId);
	}

	public void downReviewProcess(Long channelId, Long reviewProcessId) {
		ReviewProcess vo = reviewProcessDao.findOne(reviewProcessId);
		if (vo.getNextProcess() != null){
			ReviewProcess prevVo = vo.getPrevProcess();
			ReviewProcess nextVo = vo.getNextProcess();
			
			ReviewProcess nextnextVo = nextVo.getNextProcess();
			if (nextnextVo != null){
				nextnextVo.setPrevProcess(vo);
				reviewProcessDao.save(nextnextVo);
			}
			if (prevVo != null){
				prevVo.setNextProcess(nextVo);
				reviewProcessDao.save(prevVo);
			}
			
			vo.setPrevProcess(nextVo);
			vo.setNextProcess(nextVo.getNextProcess());
			
			reviewProcessDao.save(vo);
			
			nextVo.setPrevProcess(prevVo);
			nextVo.setNextProcess(vo);
			
			reviewProcessDao.save(nextVo);
		}
	}

	public void upReivewProcess(Long channelId, Long reviewProcessId) {
		ReviewProcess vo = reviewProcessDao.findOne(reviewProcessId);
		if (vo.getPrevProcess() != null){
			ReviewProcess prevVo = vo.getPrevProcess();
			ReviewProcess nextVo = vo.getNextProcess();
			
			ReviewProcess prevprevVo = prevVo.getPrevProcess();
			if (prevprevVo != null){
				prevprevVo.setNextProcess(vo);
				reviewProcessDao.save(prevprevVo);
			}
			
			if (nextVo != null){
				nextVo.setPrevProcess(prevVo);
				reviewProcessDao.save(nextVo);
			}
			
			vo.setPrevProcess(prevVo.getPrevProcess());
			vo.setNextProcess(prevVo);
			
			reviewProcessDao.save(vo);

			prevVo.setPrevProcess(vo);
			prevVo.setNextProcess(nextVo);
			
			reviewProcessDao.save(prevVo);
		}
	}

	public Long updReviewProcess(ReviewProcess reviewProcess, List<String> userNames, List<String> roleNames) throws BaseException {
		ReviewProcess entity = reviewProcessDao.findIsEntityReviewProcessByChannelAndName(reviewProcess.getChannelId(), reviewProcess.getName());
		if (isNotNull(entity) && reviewProcess.getId().longValue() != entity.getId().longValue()){
			throw new BaseException("流程名称已定义，请重新输入其他名称！", "流程名称已定义，请重新输入其他名称！");
		}
		if (isCollectionEmpty(userNames) && isCollectionEmpty(roleNames)){
			throw new BaseException("用户组和用户不能同时为空，必须选择一项以上！", "用户组和用户不能同时为空，必须选择一项以上！");
		}
		
		if (isCollectionNotEmpty(roleNames)){
			Set<ReviewRole> reviewRoles = new HashSet<ReviewRole>();
			Set<ReviewRole> oldReviewRoles = entity.getReviewRoles();
			for (String roleName : roleNames){
				Boolean result = true;
				for (ReviewRole oldReviewRole : oldReviewRoles){
					if (oldReviewRole.getRoleName().equals(roleName)){
						reviewRoles.add(oldReviewRole);
						result = false;
						break;
					}
				}
				if (result){
					ReviewRole reviewRole = new ReviewRole();
					reviewRole.setRoleName(roleName);
					reviewRoles.add(reviewRole);
				}
			}
			entity.setReviewRoles(reviewRoles);
		}else{
			entity.setReviewRoles(null);
		}
		
		if (isCollectionNotEmpty(userNames)){
			Set<ReviewUser> reviewUsers = new HashSet<ReviewUser>();
			Set<ReviewUser> oldReviewUsers = entity.getReviewUsers();
			for (String userName : userNames){
				Boolean result = true;
				for (ReviewUser oldReviewUser : oldReviewUsers){
					if (oldReviewUser.getUserName().equals(userName)){
						reviewUsers.add(oldReviewUser);
						result = false;
						break;
					}
				}
				if (result){
					ReviewUser reviewUser = new ReviewUser();
					reviewUser.setUserName(userName);
					reviewUser.setRealName(findUserRealNameByUserName(userName));
					reviewUsers.add(reviewUser);
				}
			}
			entity.setReviewUsers(reviewUsers);
		}else{
			entity.setReviewUsers(null);
		}
		entity.setName(reviewProcess.getName());
		
		reviewProcessDao.save(entity);
		return entity.getId();
	}

	public ReviewProcess findReviewProcess(Long reviewProcessId) {
		return reviewProcessDao.findOne(reviewProcessId);
	}

	public List<ReviewProcess> findReviewProcessByChannel(Long channelId) {
		return reviewProcessDao.findReviewProcessByChannel(channelId);
	}

	public ReviewProcess findFirstReviewProcessByChannel(Long channelId) {
		return reviewProcessDao.findFirstReviewProcessByChannel(channelId);
	}

	public Long findReviewProcessCountByChannel(Long channelId) {
		return reviewProcessDao.findReviewProcessCountByChannel(channelId);
	}

	private String findUserRealNameByUserName(String userName){
		User user = accountService.findUserByLoginName(userName);
		return user.getRealName();
	}

	public Boolean findReviewUserIsEntityByProcessIdAndUserName(Long reviewProcessId, String userName) {
		Long result = reviewProcessDao.findReviewUserIsEntityByProcessIdAndUserName(reviewProcessId, userName);
		if (result == 0) return false;
		else return true;
	}

	public Boolean findReviewRoleIsEntityByProcessIdAndRoleName(Long reviewProcessId, String goupName) {
		Long result = reviewProcessDao.findReviewRoleIsEntityByProcessIdAndRoleName(reviewProcessId, goupName);
		if (result == 0) return false;
		else return true;
	}
	
	public Map<String, Object> search(Long channelId){
		ReviewProcess reviewProcess = reviewProcessDao.findFirstReviewProcessByChannel(channelId);
		Long count = reviewProcessDao.findReviewProcessCountByChannel(channelId);
		
		List<ReviewProcess> reviewProcesses = new ArrayList<ReviewProcess>();
		for (int i = 0; i < count; i++){
			if (reviewProcess == null) break;
			reviewProcesses.add(reviewProcess);
			reviewProcess = reviewProcess.getNextProcess();
		}
		Map<String, Object> map = new HashMap<String, Object>(2);
		map.put("total", reviewProcesses.size());
		map.put("rows", reviewProcesses);
		return map;
	}
}
