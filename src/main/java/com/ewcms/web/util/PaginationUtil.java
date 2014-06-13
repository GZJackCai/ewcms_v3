package com.ewcms.web.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 吴智俊
 */
public class PaginationUtil {
	
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> pagination(Long count, List list){
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		resultMap.put("total", count == null ? 0 : count);
		resultMap.put("rows", list);
		return resultMap;
	}
}
