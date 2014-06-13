package com.ewcms.security.dao;

/**
 * 用户自定义DAO
 * 
 * @author wu_zhijun
 *
 */
public interface UserDaoCustom {
	/**
     * 更新用户密码
     * 
     * @param username 用户名
     * @param password 用户密码
     */
    void updatePassword(String username,String password);
}
