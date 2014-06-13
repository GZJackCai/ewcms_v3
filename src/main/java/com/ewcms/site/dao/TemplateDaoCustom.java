package com.ewcms.site.dao;

import java.util.List;

import com.ewcms.site.model.Template;
import com.ewcms.site.model.Template.TemplateType;

/**
 * 模板自定义DAO接口
 * 
 * @author wu_zhijun
 *
 */
public interface TemplateDaoCustom {
	
	/**
	 * 通过站点编号获取所有本站点的模板对象集合
	 * 
	 * @param siteId 站点编号
	 * @return List 模板对象集合
	 */
	List<Template> getTemplateList(final Long siteId);
	
	/**
	 * 获取子栏目的模板对象集合
	 * 
	 * @param parentTemplateId 父模板编号
	 * @param siteId 站点编号
	 * @param channelId 栏目编号
	 * @return List 模板对象集合
	 */
	List<Template> getTemplateChildren(final Long parentTemplateId,final Long siteId,final Long channelId);
	
	/**
	 * 通过模板名称获取当前模板对象
	 * 
	 * @param templateName 模板名称
	 * @param siteId 站点编号
	 * @param parentTemplateId 父模板编号
	 * @return Template 模板对象
	 */
	Template getChannelTemplate(final String templateName,final Long siteId,final Long parentTemplateId);
	
	/**
	 * 通过模板路径获取模板对象
	 * 
	 * @param path 模板路径
	 * @return Template 模板对象
	 */
	Template getTemplateByPath(final String path);
	
	/**
	 * 通过栏目编号获取栏目所有的模板对象集合
	 * 
	 * @param channelId 栏目编号
	 * @return List 模板对象集合
	 */
	List<Template> getTemplatesInChannel(final Long channelId);
	
	/**
	 * 通过栏目编号和模板类型获取模板对象
	 * 
	 * @param channelId 栏目编号
	 * @param templateType 模板类型
	 * @return Template 模板对象
	 */
	Template findTemplateByChannelIdAndTemplateType(final Long channelId, final TemplateType templateType);
	
	/**
	 * 通过模板编号和栏目编号获取栏目所属模板对象
	 * 
	 * @param templateId 模板编号
	 * @param channelId 栏目编号
	 * @return Template 模板对象
	 */
	Template findByIdAndChannelId(Long templateId, Long channelId);
}
