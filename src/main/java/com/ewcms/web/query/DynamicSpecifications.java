package com.ewcms.web.query;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import com.ewcms.util.EmptyUtil;
import com.ewcms.web.query.SearchFilter.Operator;

public class DynamicSpecifications {
	private static Logger logger = LoggerFactory.getLogger(DynamicSpecifications.class);
	
	public static <T> Specification<T> bySearchFilter(final Collection<SearchFilter> filters, final Class<T> clazz) {
		return new Specification<T>() {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (EmptyUtil.isCollectionNotEmpty(filters)) {
					List<Predicate> predicates = new ArrayList<Predicate>();
					for (SearchFilter filter : filters) {
						// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
						String[] names = StringUtils.split(filter.fieldName, ".");
						Path expression;
						try{
							//字段为集合类时使用
							expression = root.join(names[0]);
						}catch (Exception e){
							expression = root.get(names[0]);
						}
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}
						if (expression.getJavaType().isEnum() && !filter.value.getClass().isEnum()){
							String enumValue = (String) filter.value;
							if (enumValue == null || enumValue.isEmpty()){
								filter.operator = Operator.NULL;
							}else{
								filter.value = Enum.valueOf(expression.getJavaType(), enumValue);
							}
						}
						// logic operator
						switch (filter.operator) {
						case EQ:
							predicates.add(builder.equal(expression, filter.value));
							break;
						case NOTEQ:
							predicates.add(builder.notEqual(expression, filter.value));
							break;
						case LIKE:
							predicates.add(builder.like(expression, "%" + filter.value + "%"));
							break;
						case GT:
							predicates.add(builder.greaterThan(expression, (Comparable) filter.value));
							break;
						case LT:
							predicates.add(builder.lessThan(expression, (Comparable) filter.value));
							break;
						case GTE:
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case LTE:
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) filter.value));
							break;
						case IN:
							predicates.add(builder.in(expression).value(filter.value));
							break;
						case GTD:
							try{
								predicates.add(builder.greaterThan(expression, Date.valueOf((String)filter.value)));
							} catch (Exception e){
								logger.warn("查询条件不是有效的日期类型:{}", (String)filter.value);
							}
							break;
						case LTD:
							try{
								predicates.add(builder.lessThan(expression, Date.valueOf((String)filter.value)));
							} catch (Exception e){
								logger.warn("查询条件不是有效的日期类型:{}", (String)filter.value);
							}
							break;
						case GTED:
							try{
								predicates.add(builder.greaterThanOrEqualTo(expression, Date.valueOf((String)filter.value)));
							} catch (Exception e){
								logger.warn("查询条件不是有效的日期类型:{}", (String)filter.value);
							}
							break;
						case LTED:
							try{
								predicates.add(builder.lessThanOrEqualTo(expression, Date.valueOf((String)filter.value)));
							} catch (Exception e){
								logger.warn("查询条件不是有效的日期类型:{}", (String)filter.value);
							}
							break;
						case BEQ:
							try{
								boolean temp = Boolean.parseBoolean((String)filter.value);
								predicates.add(builder.equal(expression, temp));
							}catch(Exception e){
								logger.warn("查询条件不是有效的布尔值:{}", (String)filter.value);
							}
							break;
						case NULL:
							break;
						default:
							break;
						}
						
					}

					// 将所有条件用 and 联合起来
					if (predicates.size() > 0) {
						return builder.and(predicates.toArray(new Predicate[predicates.size()]));
					}
					
				}
				return builder.conjunction();
			}
		};
	}
}
