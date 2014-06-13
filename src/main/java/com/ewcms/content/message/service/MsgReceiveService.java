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
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.content.message.dao.MsgReceiveDao;
import com.ewcms.content.message.model.MsgReceive;
import com.ewcms.util.EmptyUtil;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author wu_zhijun
 *
 */
@Component
public class MsgReceiveService{
	
	@Autowired
	private MsgReceiveDao msgReceiveDao;

	public void delMsgReceive(Long msgReceiveId) {
		msgReceiveDao.delete(msgReceiveId);
	}

	public MsgReceive findMsgReceive(Long msgReceiveId) {
		return msgReceiveDao.findOne(msgReceiveId);
	}

	public List<MsgReceive> findMsgReceiveByUserName() {
		return msgReceiveDao.findByUserName(EwcmsContextUtil.getLoginName());
	}

	public void markReadMsgReceive(Long msgReceiveId, Boolean read) {
		MsgReceive msgReceive = msgReceiveDao.findByUserNameAndId(EwcmsContextUtil.getLoginName(), msgReceiveId);
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
	}

	public List<MsgReceive> findMsgReceiveByUnRead() {
		return msgReceiveDao.findByUserNameAndReadFalseOrderByIdDesc(EwcmsContextUtil.getLoginName());
	}

	public void readMsgReceive(Long msgReceiveId) {
		MsgReceive msgReceive = msgReceiveDao.findByUserNameAndId(EwcmsContextUtil.getLoginName(), msgReceiveId);
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
