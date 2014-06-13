/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.content.document.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.ewcms.content.document.dao.OperateTrackDao;
import com.ewcms.content.document.model.OperateTrack;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author wu_zhijun
 *
 */
@Component
public class OperateTrackService {

	@Autowired
	private OperateTrackDao operateTrackDao;
	
	public void addOperateTrack(Long articleMainId, String statusDesc, String description, String reason, String userName, String userRealName) {
		OperateTrack operateTrack = new OperateTrack();
		
		operateTrack.setArticleMainId(articleMainId);
		operateTrack.setStatusDesc(statusDesc);
		operateTrack.setDescription(description);
		operateTrack.setReason(reason);
		operateTrack.setUserName(userName);
		operateTrack.setUserRealName(userRealName);
		
		operateTrackDao.save(operateTrack);
	}
	
	public void addOperateTrack(Long articleMainId, String statusDesc, String description, String reason){
		String userName = EwcmsContextUtil.getLoginName();
		String userRealName = EwcmsContextUtil.getRealName();
		addOperateTrack(articleMainId, statusDesc, description, reason, userName, userRealName);
	}

	public List<OperateTrack> findOperateTrackByArticleMainId(Long articleMainId) {
		return operateTrackDao.findOperateTrackByArticleMainId(articleMainId);
	}

	public OperateTrack findOperateTrackById(Long operateTrackId) {
		return operateTrackDao.findOne(operateTrackId);
	}

	public void delOperateTrack(Long articleMainId) {
		List<OperateTrack> operateTracks = operateTrackDao.findOperateTrackByArticleMainId(articleMainId);
		operateTrackDao.delete(operateTracks);
	}
	
	public String getArticleOperateTrack(Long trackId){
		OperateTrack track = findOperateTrackById(trackId);
		if (track == null) return "";
		return (track.getReason() == null)? "" : track.getReason();
	}
	
	public Map<String, Object> search(Long articleMainId, QueryParameter params){
		Map<String, Object> parameters = params.getParameters();
		parameters.put("EQ_articleMainId", articleMainId);
		params.setParameters(parameters);
		
		Map<String, Direction> sorts = params.getSorts();
		sorts.put("operateTime",  Direction.DESC);
		params.setSorts(sorts);
		
		return SearchMain.search(params, "IN_id", Long.class, operateTrackDao, OperateTrack.class);
	}
}
