package com.ewcms.site.dao;

import java.util.List;

import com.ewcms.site.model.Channel;

/**
 * 栏目自定义DAO接口
 * 
 * @author wu_zhijun
 *
 */
public interface ChannelDaoCustom {
	
	/**
	 * 通过父栏目获取子栏目
	 * 
	 * @param parentId 父栏目编号 
	 * @return List 子栏目对象集合
	 */
	List<Channel> getChannelChildren(final Long parentId);
	
	/**
	 * 通过站点编号获取栏目
	 * 
	 * @param siteId 站点编号
	 * @return Channel 栏目对象
	 */
	Channel getChannelRoot(final Long siteId);
	
	/**
	 * 通过路径和站点编号获取栏目
	 * 
	 * @param siteId 站点编号
	 * @param path 栏目路径
	 * @return Channel 栏目对象
	 */
	Channel getChannelByURL(final Long siteId,final String path);
	
	/**
	 * 通过父栏目编号获取最大排序号
	 * 
	 * @param parentId 父栏目编号
	 * @return Long 最大排序号
	 */
	Long findMaxSiblingChannel(final Long parentId);
	
	/**
	 * 通过父栏目编号和排序号获取栏目
	 * 
	 * @param parentId 父栏目编号
	 * @param sort 排序号
	 * @return Channel 栏目对象
	 */
	Channel findChannelByParentIdAndSort(final Long parentId, final Long sort);
	
	/**
	 * 通过父栏目编号获取大于等于排序号和小于原排序号并剔除当前栏目编号的栏目对象集合
	 * 
	 * @param channelId 当前栏目编号
	 * @param parentId 父栏目编号
	 * @param sort 排序号
	 * @param oldSort 原排序号
	 * @return List 栏目对象集合
	 */
	List<Channel> findChannelByParentIdAndGtSort(final Long channelId, final Long parentId, final Long sort, final Long oldSort);
	
	/**
	 * 通过父栏目编号获取小于等于排序号和大于原排序号并剔除当前栏目编号的栏目对象集合
	 * 
	 * @param channelId 当前栏目编号
	 * @param parentId 父栏目编号
	 * @param sort 排序号
	 * @param oldSort 原排序号 
	 * @return List 栏目对象集合
	 */
	List<Channel> findChannelByParentIdAndLtSort(final Long channelId, final Long parentId, final Long sort, final Long oldSort);
	
	/**
	 * 通过父栏目编号获取大于排序号的栏目对象集合
	 * 
	 * @param parentId 父栏目编号
	 * @param sort 排序号
	 * @return List 栏目对象集合
	 */
	List<Channel> findChannelByGreaterThanSort(final Long parentId, final Long sort);
	
	/**
	 * 通过父栏目编号分组获取父栏目编号集合
	 * 
	 * @return List 父栏目编号集合
	 */
	List<Long> findChannelParent();
}
