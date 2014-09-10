/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.message.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ewcms.content.message.dao.MsgContentDao;
import com.ewcms.content.message.dao.MsgReceiveDao;
import com.ewcms.content.message.dao.MsgSendDao;
import com.ewcms.content.message.model.MsgContent;
import com.ewcms.content.message.model.MsgReceive;
import com.ewcms.content.message.model.MsgReceiveUser;
import com.ewcms.content.message.model.MsgSend;
import com.ewcms.content.message.model.MsgSend.Type;
import com.ewcms.content.message.model.MsgStatus;
import com.ewcms.content.message.push.PushApiService;
import com.ewcms.security.dao.UserDao;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author wu_zhijun
 * 
 */
@Service
public class MsgSendService {

	@Autowired
	private MsgSendDao msgSendDao;
	@Autowired
	private MsgReceiveDao msgReceiveDao;
	@Autowired
	private MsgContentDao msgContentDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private PushApiService pushApiService;
	@Autowired
	private MsgReceiveService msgReceiveService;

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	public Long addMsgSend(MsgSend msgSend, String content, List<String> ReceiveUserNames) {
		msgSend.setUserName(EwcmsContextUtil.getLoginName());
		msgSend.setStatus(MsgStatus.FAVORITE);
		MsgContent msgContent = new MsgContent();
		msgContent.setTitle(msgSend.getTitle());
		msgContent.setDetail(content);
		List<MsgContent> msgContents = new ArrayList<MsgContent>();
		msgContents.add(msgContent);
		msgSend.setMsgContents(msgContents);
		
		if (msgSend.getType() == Type.GENERAL){
			List<String> receiveUserNames = new ArrayList<String>();
			
			List<MsgReceiveUser> msgReceiveUsers = new ArrayList<MsgReceiveUser>();
			MsgReceiveUser msgReceiveUser;
			for (String receiveUserName : ReceiveUserNames){
				if (receiveUserName.equals(msgSend.getUserName())) continue;
				msgReceiveUser = new MsgReceiveUser();
				msgReceiveUser.setUserName(receiveUserName);
				msgReceiveUser.setRealName(userDao.findByLoginName(receiveUserName).getRealName());
				msgReceiveUsers.add(msgReceiveUser);
				receiveUserNames.add(receiveUserName);
			}
			msgSend.setMsgReceiveUsers(msgReceiveUsers);
			msgSendDao.save(msgSend);
		}else if (msgSend.getType() == Type.NOTICE){
			msgSend.setMsgReceiveUsers(null);
			msgSendDao.save(msgSend);
			
			pushApiService.pushNewNotice(findTopRowNoticesOrSubscription(Type.NOTICE, 10));
		}else if (msgSend.getType() == Type.SUBSCRIPTION){
			msgSend.setMsgReceiveUsers(null);
			msgSendDao.save(msgSend);
			
			pushApiService.pushNewSubscription(findTopRowNoticesOrSubscription(Type.SUBSCRIPTION, 10));
		}
		if (msgSend.getType() == Type.GENERAL){
			for (String receiveUserName : ReceiveUserNames){
				if (receiveUserName.equals(msgSend.getUserName())) continue;
				MsgReceive msgReceive = new MsgReceive();
				msgReceive.setUserName(receiveUserName);
				msgReceive.setSendUserName(EwcmsContextUtil.getLoginName());
				msgReceive.setStatus(MsgStatus.FAVORITE);
				msgReceive.setMsgContent(msgContent);
				msgReceiveDao.save(msgReceive);
				
				pushApiService.pushUnreadMessage(receiveUserName, msgReceiveService.findUnReadMessageCountByUserName(receiveUserName));;
			}		
		}
		
		return msgSend.getId();
	}

	public Long updMsgSend(MsgSend send) {
		return null;
	}

	public void delMsgSend(List<Long> msgSendIds) {
		Boolean isNotice = false;
		Boolean isSubscription = false;
		
		for (Long msgSendId : msgSendIds){
			MsgSend msgSend = findMsgSend(msgSendId);
			if (msgSend.getType() == Type.NOTICE){
				isNotice = true;
			}else if (msgSend.getType() == Type.SUBSCRIPTION){
				isSubscription = true;
			}
			msgSendDao.delete(msgSend);
		}
		
		if (isNotice){
			pushApiService.pushNewNotice(findTopRowNoticesOrSubscription(Type.NOTICE, 10));
		}

		if (isSubscription){
			pushApiService.pushNewSubscription(findTopRowNoticesOrSubscription(Type.SUBSCRIPTION, 10));
		}
	}
	

	public MsgSend findMsgSend(Long msgSendId) {
		return msgSendDao.findOne(msgSendId);
	}

	public List<MsgSend> findMsgSendByUserName() {
		return msgSendDao.findByMsgReceiveUsersUserName(EwcmsContextUtil.getLoginName());
	}

	public Long addSubscription(Long msgSendId, String title, String detail) {
		MsgSend msgSend = msgSendDao.findByUserNameAndId(EwcmsContextUtil.getLoginName(), msgSendId);
		Assert.notNull(msgSend);
		if (msgSend.getType() == Type.SUBSCRIPTION){
			List<MsgContent> msgContents = msgSend.getMsgContents();
			
			MsgContent msgContent = new MsgContent();
			msgContent.setTitle(title);
			msgContent.setDetail(detail);
			
			msgContents.add(msgContent);
			msgSend.setMsgContents(msgContents);
			
			msgSendDao.save(msgSend);
			
			List<MsgReceiveUser> msgReceiveUsers = msgSend.getMsgReceiveUsers();
			for (MsgReceiveUser msgReceiveUser : msgReceiveUsers){
				MsgReceive msgReceive = new MsgReceive();
				msgReceive.setMsgContent(msgContent);
				msgReceive.setSendUserName(EwcmsContextUtil.getLoginName());
				msgReceive.setSubscription(true);
				msgReceive.setUserName(msgReceiveUser.getUserName());
				msgReceive.setStatus(MsgStatus.FAVORITE);
				msgReceiveDao.save(msgReceive);
			}
			return msgSend.getId();
		}
		return null;
	}

	public void delSubscription(Long msgContentId) {
		msgContentDao.delete(msgContentId);
	}
	
	public List<Map<String, Object>> findTopRowNoticesOrSubscription(Type type, Integer row) {
		QueryParameter queryParameter = new QueryParameter();
		queryParameter.setRows(row);
		
		Map<String, Object> parameters = queryParameter.getParameters();
		parameters.put("EQ_type", type);
		queryParameter.setParameters(parameters);
		
		Map<String, Direction> sorts = queryParameter.getSorts();
		sorts.put("sendTime",  Direction.DESC);
		sorts.put("id", Direction.DESC);
		queryParameter.setSorts(sorts);

		Map<String, Object> seachMap = SearchMain.search(queryParameter, "IN_id", Long.class, msgSendDao, MsgSend.class);
		
		@SuppressWarnings("unchecked")
		List<MsgSend> msgSends = (List<MsgSend>) seachMap.get("rows");
		
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (MsgSend msgSend : msgSends){
			Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", msgSend.getId());
            map.put("title", msgSend.getTitle());
            map.put("date", simpleDateFormat.format(msgSend.getSendTime()));
            map.put("sendUserName", msgSend.getUserName());
            dataList.add(map);
		}
		return dataList;
	}
	
//	public List<Map<String, Object>> findTopRowGeneral(String userName, Integer row){
//		QueryParameter queryParameter = new QueryParameter();
//		queryParameter.setRows(row);
//		
//		Map<String, Object> parameters = queryParameter.getParameters();
//		parameters.put("EQ_type", Type.GENERAL);
//		parameters.put("EQ_msgReceiveUsers.userName", userName);
//		queryParameter.setParameters(parameters);
//		
//		Map<String, Direction> sorts = queryParameter.getSorts();
//		sorts.put("sendTime",  Direction.DESC);
//		sorts.put("id", Direction.DESC);
//		queryParameter.setSorts(sorts);
//
//		Map<String, Object> seachMap = SearchMain.search(queryParameter, "IN_id", Long.class, msgSendDao, MsgSend.class);
//		
//		@SuppressWarnings("unchecked")
//		List<MsgSend> msgSends = (List<MsgSend>) seachMap.get("rows");
//		
//		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
//		for (MsgSend msgSend : msgSends){
//			Map<String, Object> map = new HashMap<String, Object>();
//            map.put("id", msgSend.getId());
//            map.put("title", msgSend.getTitle());
//            map.put("date", simpleDateFormat.format(msgSend.getSendTime()));
//            map.put("sendUserName", msgSend.getUserName());
//            dataList.add(map);
//		}
//		return dataList;
//	}

	public String subscribeMsg(Long msgSendId) {
		MsgSend msgSend = msgSendDao.findOne(msgSendId);
		if (msgSend.getType() == Type.SUBSCRIPTION){
			String receiveUserName = EwcmsContextUtil.getLoginName();
			String realName = EwcmsContextUtil.getRealName();
			String sendUserName = msgSend.getUserName();
			if (receiveUserName.equals(sendUserName)){
				return "own";
			}
			
			MsgSend msgSendSub = msgSendDao.findByIdAndMsgReceiveUsersUserNameAndType(msgSendId, receiveUserName, Type.SUBSCRIPTION);
			if (msgSendSub == null){
				List<MsgReceiveUser> msgReceiveUsers = msgSend.getMsgReceiveUsers();
				MsgReceiveUser msgReceiveUser = new MsgReceiveUser();
				msgReceiveUser.setUserName(receiveUserName);
				msgReceiveUser.setRealName(realName);
				msgReceiveUsers.add(msgReceiveUser);
				msgSend.setMsgReceiveUsers(msgReceiveUsers);
				msgSendDao.save(msgSend);
				return "true";
			}else{
				return "exist";
			}
		}else{
			return "false";
		}
	}
	
	public Map<String, Object> search(QueryParameter params){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_userName", EwcmsContextUtil.getLoginName());
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("sendTime",  Direction.DESC);
		sorts.put("id", Direction.DESC);
		params.setSorts(sorts);

		return SearchMain.search(params, "IN_id", Long.class, msgSendDao, MsgSend.class);
	}
	
	public Map<String, Object> searchMore(QueryParameter params, MsgSend.Type type){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_type", type);
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("sendTime",  Direction.DESC);
		sorts.put("id", Direction.DESC);
		params.setSorts(sorts);

		return SearchMain.search(params, "IN_id", Long.class, msgSendDao, MsgSend.class);
	}
	
	public List<String> findMsgReceiveUser(Long msgSendId){
		List<String> results = new ArrayList<String>();
		MsgSend msgSend = msgSendDao.findOne(msgSendId);
		if (msgSend == null) return results;
		List<MsgReceiveUser> msgReceiveUsers = msgSend.getMsgReceiveUsers();
		if (msgReceiveUsers == null || msgReceiveUsers.isEmpty()) return results;
		for (MsgReceiveUser msgReceiveUser : msgReceiveUsers){
			results.add(msgReceiveUser.getUserName());
		}
		return results;
	}
}
