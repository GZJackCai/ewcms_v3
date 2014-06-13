package com.ewcms.site.dao;

import java.util.List;

import com.ewcms.site.model.TemplateSource;

/**
 * 模板资源自定义DAO
 * 
 * @author wu_zhijun
 *
 */
public interface TemplateSourceDaoCustom {
	/**
	 * 获取子节点模板资源对象集合
	 * 
	 * @param parentTemplateSourceId 父模板资源编号
	 * @param siteId 站点编号
	 * @return List 模板资源对象集合
	 */
	List<TemplateSource> getTemplateSourceChildren(final Long parentTemplateSourceId, final Long siteId);
	
	/**
	 * 获取站点专栏资源节点
	 * 
	 * @param templateSourceName 模板资源名称
	 * @param siteId 站点编号
	 * @param parentTemplateSourceId 父模板资源编号 
	 * @return TemplateSource 模板资源对象
	 */
	TemplateSource getChannelTemplateSource(final String templateSourceName, final Long siteId, final Long parentTemplateSourceId);
	
	/**
	 * 获取需要发布的模板资源对象集合
	 * 
	 * @param siteId 站点编号
	 * @param forceAgain 是否再次发布
	 * @return List 模板资源对象集合
	 */
	List<TemplateSource> getPublishTemplateSources(final Long siteId,final Boolean forceAgain);
	
	/**
	 * 通过模板资源路径获取模板资源对象
	 * 
	 * @param path 模板资源路径
	 * @return TemplateSource 模板资源对象
	 */
	TemplateSource getTemplateSourceByPath(final String path);
	
	/**
	 * 通过栏目编号获取模板资源对象集合
	 * 
	 * @param channelId 栏目编号
	 * @return List 模板资源对象集合
	 */
	List<TemplateSource> getTemplateSourceInChannel(final Long channelId);
	
	/**
	 * 通过模板资源编号和栏目编号获取栏目所属模板资源对象
	 * 
	 * @param templateId 模板资源编号
	 * @param channelId 栏目编号
	 * @return TemplateSource 模板资源对象
	 */
	TemplateSource findByIdAndChannelId(Long templateSourceId, Long channelId);

}
