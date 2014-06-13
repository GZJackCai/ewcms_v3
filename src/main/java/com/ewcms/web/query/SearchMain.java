package com.ewcms.web.query;

import static org.springframework.data.domain.Sort.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ewcms.web.QueryParameter;

/**
 * 查询主体
 * 
 * @author wu_zhijun
 *
 */
public class SearchMain {
	
	/**
	 * 查询方法
	 * 
	 * @param params 参数
	 * @param key 主关键字组合(表达式：IN_id)
	 * @param keyType 主关键字类型
	 * @param jse 继承了JpaSpecificationExecutor接口的Dao
	 * @param clazz 实体对象
	 * @return Map 包括key=total和key=rows两个对象
	 */
	public static <T, Y> Map<String, Object> search(QueryParameter params, String key, Class<Y> keyType, JpaSpecificationExecutor<T> jse, Class<T> clazz){
		Map<String, Object> searchParams = params.getParameters();
		SearchFilter.setSelections(searchParams, key, params.getSelections(), keyType);
		
		List<Order> orders = new ArrayList<Order>();
		if (params.getSort() != null && !params.getSort().isEmpty()){
			Order order = null;
			Direction direction = Direction.ASC;
			try{
				direction = Direction.fromString(params.getOrder());
			}catch(IllegalArgumentException e){
			}
			order = new Order(direction, params.getSort());
			orders.add(order);
		}else{
			Map<String, Direction> sorts = params.getSorts();
			if (!sorts.isEmpty()){
				Iterator<Entry<String, Direction>> its = sorts.entrySet().iterator();
				while (its.hasNext()){
					Entry<String, Direction> entry = its.next();
					String property = entry.getKey();
					if (property != null && !property.isEmpty()){
						Direction direction = entry.getValue();
						if (direction == null) direction = Direction.ASC;
						Order order = new Order(direction, property);
						orders.add(order);
					}
				}
			}
		}
		
		Sort sort = null;
		if (!orders.isEmpty()){
			sort = new Sort(orders);
		}

		Map<String, SearchFilter> filters = SearchFilter.parse(searchParams);
		Specification<T> spec = DynamicSpecifications.bySearchFilter(filters.values(), clazz);
		
		//记算记录数
		Long count = jse.count(spec);
		Pageable pageable = new PageRequest(params.getPage() - 1, params.getRows(), sort);
		Page<T> pages = jse.findAll(spec, pageable);
		
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		resultMap.put("total", count == null ? 0 : count);
		resultMap.put("rows", pages.getContent());
		return resultMap;
	}
}
