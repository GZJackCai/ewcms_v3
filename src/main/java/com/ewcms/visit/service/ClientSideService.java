package com.ewcms.visit.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.visit.dao.clientside.ClientSideDao;
import com.ewcms.visit.model.clientside.ClientSide;
import com.ewcms.visit.model.clientside.ClientSidePk.ClientSideType;
import com.ewcms.visit.util.ChartVisitUtil;
import com.ewcms.visit.util.DateTimeUtil;

/**
 *
 * @author 吴智俊
 */
@Component
@Transactional(readOnly = true)
public class ClientSideService {

	@Autowired
	private ClientSideDao clientSideDao;
	
	public List<ClientSide> findClientSideTable(Date startVisitDate, Date endVisitDate, Long siteId, ClientSideType clientSideType){
		return clientSideDao.findClientSide(startVisitDate, endVisitDate, siteId, clientSideType);
	}
	
	public String findClientSideReport(Date startVisitDate, Date endVisitDate, Long siteId, ClientSideType clientSideType){
		Map<String, Long> dataSet = new LinkedHashMap<String, Long>();
		List<ClientSide> clientSides = clientSideDao.findClientSide(startVisitDate, endVisitDate, siteId, clientSideType);
		for (ClientSide clientSide : clientSides){
			dataSet.put(clientSide.getClientSidePk().getClientSideName(), clientSide.getClientSideCount());
		}
		return ChartVisitUtil.getPie3DChart(dataSet);
	}
	
	public String findClientSideTrendReport(Date startVisitDate, Date endVisitDate, ClientSideType clientSideType, String clientSideName, Integer labelCount, Long siteId){
		Map<String, Map<String, Long>> dataSet = new LinkedHashMap<String, Map<String, Long>>();
		
		Map<String, Long> sumPvMap = DateTimeUtil.getDateAreaLongMap(startVisitDate, endVisitDate);
		List<ClientSide> clientSides = clientSideDao.findClientSide(startVisitDate, endVisitDate, siteId, clientSideType, clientSideName);
		for (ClientSide clientSide : clientSides){
			Date visitDate = clientSide.getClientSidePk().getVisitDate();
			Long clientSideCount = clientSide.getClientSideCount();
			sumPvMap.put(DateTimeUtil.getDateToString(visitDate), clientSideCount == null ? 0L : clientSideCount);
		}
		dataSet.put("PV", sumPvMap);
		
		List<String> dateAreaList = DateTimeUtil.getDateAreaList(startVisitDate, endVisitDate);
		return ChartVisitUtil.getLine2DChart(dateAreaList, dataSet, labelCount);
	}
}
