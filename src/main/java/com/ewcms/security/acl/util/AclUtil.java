package com.ewcms.security.acl.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authz.UnauthorizedException;

import com.ewcms.security.acl.annotation.AclEnum;
import com.ewcms.util.Collections3;
import com.ewcms.util.EwcmsContextUtil;

public class AclUtil {

	public final static String PREFIX = "acl";
	public final static String SEPARATOR = ":";
	public final static String CONNECTOR = ",";

	public static void assertUnauthorized(AclEnum acl, Long channelId) {
		if (!EwcmsContextUtil.isPermitted(PREFIX + SEPARATOR + acl.getExpression() + SEPARATOR + channelId.longValue())) {
			throw new UnauthorizedException();
		}
	}
	
	public static String getPermissionExpression(AclEnum aclEnum, Long id){
		return PREFIX + SEPARATOR + aclEnum.getExpression() + SEPARATOR + id.longValue();
	}
	
	public static String getPermission(Integer mask, Long id){
		List<String> expressions = new ArrayList<String>();

		if (AclEnum.ADMIN_CHANNEL.ordinal() < mask) expressions.add(AclEnum.ADMIN_CHANNEL.getExpression());
		else{
			if (AclEnum.VIEW_ARTICLE.ordinal() < mask) expressions.add(AclEnum.VIEW_ARTICLE.getExpression());
			if (AclEnum.WRITER_ARTICLE.ordinal() < mask) expressions.add(AclEnum.WRITER_ARTICLE.getExpression());
			if (AclEnum.DELETE_ARTICLE.ordinal() < mask) expressions.add(AclEnum.DELETE_ARTICLE.getExpression());
			if (AclEnum.VERIFY_ARTICLE.ordinal() < mask) expressions.add(AclEnum.VERIFY_ARTICLE.getExpression());
			if (AclEnum.PUBLISH_ARTICLE.ordinal() < mask) expressions.add(AclEnum.PUBLISH_ARTICLE.getExpression());
			if (AclEnum.VIEW_CHANNEL.ordinal() < mask) expressions.add(AclEnum.VIEW_CHANNEL.getExpression());
			if (AclEnum.WRITER_CHANNEL.ordinal() < mask) expressions.add(AclEnum.WRITER_CHANNEL.getExpression());
			if (AclEnum.DELETE_CHANNEL.ordinal() < mask) expressions.add(AclEnum.DELETE_CHANNEL.getExpression());
		}
		String expression = "";
		if (!expressions.isEmpty()){
			expression = Collections3.convertToString(expressions, CONNECTOR);
		}
		String value = String.valueOf(id);
//		if (childIds != null && !childIds.isEmpty()) value += CONNECTOR + Collections3.convertToString(childIds, CONNECTOR);
		return PREFIX + SEPARATOR + expression + SEPARATOR + value;
	}
}
