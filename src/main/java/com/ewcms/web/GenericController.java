package com.ewcms.web;

import java.io.IOException;  
import java.io.Serializable;  
import javax.servlet.http.HttpServletResponse;  
  
import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
import org.springframework.ui.ModelMap;  
import org.springframework.web.bind.annotation.RequestMapping;  
import org.springframework.web.bind.annotation.RequestMethod;  
import org.springframework.web.bind.annotation.SessionAttributes;

@SessionAttributes(GenericController.SESSIONMESSAGE)
public abstract class GenericController<T, ID extends Serializable> {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public static final String FLASHMESSAGE = "flashMessage";// 信息，重定向后消失
	public static final String SESSIONMESSAGE = "sessionMessage";// 会话信息
	public static final String WARNMESSAGE = "warnMessage";// 警告信息
	protected static final String SHOW = "show";
	protected static final String EDIT = "edit";
	protected static final String CREATE = "create";
	protected static final String LIST = "list";

	/**
	 * 模板目录
	 * 
	 * @return
	 */
	public String getBasePath() {
		return "";
	}

	/**
	 * 查看
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "method=show")
	public String showHandler(ID id, ModelMap model) {
		return getBasePath() + show(id, model);
	}

	public abstract String show(ID id, ModelMap model);

	/**
	 * 创建
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "method=create")
	public String createHandler(ModelMap model) {
		return getBasePath() + create(model);
	}

	public abstract String create(ModelMap model);

	/**
	 * 修改
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "method=edit")
	public String editHandler(ID id, ModelMap model) {
		return getBasePath() + edit(id, model);
	}

	public abstract String edit(ID id, ModelMap model);

	/**
	 * 列表
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "method=list")
	public String listHandler(ModelMap model) {
		return getBasePath() + list(model);
	}

	public abstract String list(ModelMap model);

	/**
	 * 保存
	 * 
	 * @param entity
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "method=save", method = RequestMethod.POST)
	public String saveHandler(T entity, ModelMap model) {
		preSave(entity, model);
		return getBasePath() + save(entity, model);
	}

	public void preSave(T entity, ModelMap model) {

	}

	public abstract String save(T entity, ModelMap model);

	/**
	 * 更新
	 * 
	 * @param entity
	 * @param model
	 * @return
	 */

	@RequestMapping(params = "method=update", method = RequestMethod.POST)
	public String updateHandler(T entity, ModelMap model) {
		preUpdate(entity, model);
		return getBasePath() + update(entity, model);
	}

	public void preUpdate(T entity, ModelMap model) {

	}

	public abstract String update(T entity, ModelMap model);

	/**
	 * 删除
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(params = "method=delete")
	public String deleteHandler(ID id, ModelMap model) {
		return getBasePath() + delete(id, model);
	}

	public abstract String delete(ID id, ModelMap model);

	/**
	 * 信息
	 * 
	 * @param message
	 * @param model
	 */
	public void addFlashMessage(String message, ModelMap model) {
		model.addAttribute(FLASHMESSAGE, message);
	}

	public void addWarnMessage(String message, ModelMap model) {
		model.addAttribute(WARNMESSAGE, message);
	}

	public void addSessionMessage(String message, ModelMap model) {
		model.addAttribute(SESSIONMESSAGE, message);
	}

	/**
	 * 绕过Template,直接输出内容的简便函数.
	 */
	protected String render(String text, String contentType, HttpServletResponse response) {
		try {
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 直接输出字符串.
	 */
	protected String renderText(String text, HttpServletResponse response) {
		return render(text, "text/plain;charset=UTF-8", response);
	}

	/**
	 * 直接输出HTML.
	 */
	protected String renderHtml(String html, HttpServletResponse response) {
		return render(html, "text/html;charset=UTF-8", response);
	}

	/**
	 * 直接输出XML.
	 */
	protected String renderXML(String xml, HttpServletResponse response) {
		return render(xml, "text/xml;charset=UTF-8", response);
	}
}
