package com.ewcms.plugin.report.manage.dao;

/**
 * @author 吴智俊
 */
public interface ParameterDaoCustom {
	public Boolean findSessionIsEntityByParameterIdAndUserName(final Long parameterId, final String userName);
}
