package com.ewcms.web.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.ewcms.common.convert.ConvertException;
import com.ewcms.common.convert.ConvertFactory;
import com.ewcms.common.convert.Convertable;
import com.ewcms.util.EmptyUtil;

public class SearchFilter {

	public enum Operator {
		EQ, NOTEQ, LIKE, GT, LT, GTE, LTE, IN, GTD, LTD, GTED, LTED, BEQ, NULL;
	}

	public String fieldName;
	public Object value;
	public Operator operator;

	public SearchFilter(String fieldName, Operator operator, Object value) {
		this.fieldName = fieldName;
		this.value = value;
		this.operator = operator;
	}

	/**
	 * searchParams中key的格式为OPERATOR_FIELDNAME
	 */
	public static Map<String, SearchFilter> parse(Map<String, Object> searchParams) {
		Map<String, SearchFilter> filters = new HashMap<String, SearchFilter>();

		for (Entry<String, Object> entry : searchParams.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			
			// 过滤掉空值
			try{
				if (StringUtils.isBlank((String)value)) {
					continue;
				}
			}catch (Exception e){
				try{
					if (EmptyUtil.isCollectionEmpty((Collection<?>) value)){
						continue;
					}
				}catch(Exception e1){
				}
			}

			// 拆分operator与filedAttribute
			String[] names = StringUtils.split(key, "_");
			if (names.length != 2) {
				throw new IllegalArgumentException(key + " is not a valid search filter name");
			}
			String filedName = names[1];
			Operator operator = Operator.valueOf(names[0]);

			// 创建searchFilter
			SearchFilter filter = new SearchFilter(filedName, operator, value);
			filters.put(key, filter);
		}

		return filters;
	}
	
	public static <T> void setSelections(Map<String, Object> searchParams, String key, List<String> selections, Class<T> type){
		if (selections != null && !selections.isEmpty()) {
			String[] selectArr = selections.toArray(new String[0]);
			List<T> result = convertArray(type, selectArr);
			searchParams.put(key, result);
		}
	}
	
	public static Sort getSort(String filedOrder, String filedSort){
		Sort sort = null;
		if (filedOrder != null) {
			if (filedOrder.equals("asc")) {
				sort = new Sort(filedSort);
			}else{
				sort = new Sort(Direction.DESC, filedSort);
			}
		}
		return sort;
	}
	
	 /**
     * 字符串数组转换成指定类型的集合
     * 
     * @param <T>  类型
     * @param type 类型类
     * @param values 转换字符串数组
     * @return
     * @throws ConvertException
     */
    private static <T> List<T> convertArray(final Class<T> type, final String[] values) {
    	List<T> list = new ArrayList<T>();
    	try{
			Convertable<T> handler = ConvertFactory.instance.convertHandler(type);
			for (String value : values) {
				list.add(handler.parse(value));
			}
		} catch (ConvertException e) {
		}
        return list;
    }
}
