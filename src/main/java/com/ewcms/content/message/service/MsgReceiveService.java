/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.message.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ewcms.content.message.dao.MsgReceiveDao;
import com.ewcms.content.message.model.MsgReceive;
import com.ewcms.content.message.push.PushApiService;
import com.ewcms.util.EmptyUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author wu_zhijun
 *
 */
@Service
public class MsgReceiveService{
	
	@Autowired
	private MsgReceiveDao msgReceiveDao;
	@Autowired
	private PushApiService pushApiService;

	public void delMsgReceive(String userName, List<Long> msgReceiveIds) {
		for (Long msgReceive : msgReceiveIds){
			msgReceiveDao.delete(msgReceive);
		}
		pushApiService.pushUnreadMessage(userName, findUnReadMessageCountByUserName(userName));
	}

	public MsgReceive findMsgReceive(Long msgReceiveId) {
		return msgReceiveDao.findOne(msgReceiveId);
	}

	public List<MsgReceive> findMsgReceiveByUserName() {
		return msgReceiveDao.findByUserName(EwcmsContextUtil.getLoginName());
	}

	public void markReadMsgReceive(String userName, Long msgReceiveId, Boolean read) {
		MsgReceive msgReceive = msgReceiveDao.findByUserNameAndId(userName, msgReceiveId);
		Assert.notNull(msgReceive);
		if (msgReceive.getRead() != read){
			msgReceive.setRead(read);
			if (read){
				msgReceive.setReadTime(new Date(Calendar.getInstance().getTime().getTime()));
			}else{
				msgReceive.setReadTime(null);
			}
			msgReceiveDao.save(msgReceive);
		}
		pushApiService.pushUnreadMessage(userName, findUnReadMessageCountByUserName(userName));
	}

	public List<MsgReceive> findMsgReceiveByUnRead(String userName) {
		return msgReceiveDao.findByUserNameAndReadFalseOrderByIdDesc(userName);
	}

	public void readMsgReceive(String userName, Long msgReceiveId) {
		MsgReceive msgReceive = msgReceiveDao.findByUserNameAndId(userName, msgReceiveId);
		Assert.notNull(msgReceive);
		msgReceive.setRead(true);
		msgReceive.setReadTime(new Date(Calendar.getInstance().getTime().getTime()));
		msgReceiveDao.save(msgReceive);
	}

	public Long findUnReadMessageCountByUserName(String userName) {
		List<MsgReceive> receives = msgReceiveDao.findByUserNameAndReadFalseOrderByIdDesc(userName);
		Long count = 0L;
		if (EmptyUtil.isCollectionNotEmpty(receives)){
			count = (long) receives.size();
		}
		return count;
	}
	
	public Map<String, Object> search(QueryParameter params){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_userName", EwcmsContextUtil.getLoginName());
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("read",  Direction.ASC);
		sorts.put("readTime", Direction.DESC);
		sorts.put("id", Direction.DESC);
		params.setSorts(sorts);

		return SearchMain.search(params, "IN_id", Long.class, msgReceiveDao, MsgReceive.class);
	}
}
