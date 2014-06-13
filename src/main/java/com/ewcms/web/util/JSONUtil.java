/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.web.util;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO 只在{com.ewcms.content.resource.web.ResourceController}和{com.ewcms.content.resource.web.ResourceThumbController}中使用
 * 
 * @author wu_zhijun
 */
public class JSONUtil {

	private static Logger logger = LoggerFactory.getLogger(JSONUtil.class);
	private static ObjectMapper objectMapper = new ObjectMapper();

	public static String toJSON(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (IOException e) {
			logger.error(e.toString());
			throw new IllegalArgumentException(e);
		}
	}

    /**
     * 操作成功输出json
     * 
     * @return 输出内容
     */
    public static String renderSuccess(){
       return renderSuccess(null,null);
    }
    
    /**
     * 操作成能输出json
     * <br>
     * 指定操作成能返回值
     * 
     * @param value 返回值
     * @return 输出内容
     */
    public static String renderSuccess(Object value){
        return renderSuccess(null,value);
    }

    /**
     * 操作成能输出json
     * <br>
     * 指定操作成功提示信息，和放回值
     * 
     * @param message 成功信息
     * @param value   返回值
     * @return 输出内容
     */
    public static String renderSuccess(String message,Object value){
        return render(Boolean.TRUE,message,value);
    }

    /**
     * 操作错误输出json
     */
    public static String renderError(){
        return renderError(null);
    }
    
    /**
     * 操作错误输出json
     * <br>
     * 指定错误信息
     * 
     * @param message 错误信息
     * @return 输出内容
     */
    public static String renderError(String message){
        return render(Boolean.FALSE,message,null);
    }
    
    public static String render(Boolean success, String message, Object value) {
		JsonMessage m = new JsonMessage();
		m.success = success;
		m.message = message;
		m.value = value;

		String json = toJSON(m);
		logger.debug("render success output {}", json);

		return json;
	}

	/**
	 * 输出信息对象
	 * 
	 * @author wu_zhijun
	 */
	public static class JsonMessage {

		private Boolean success;
		private String message;
		private Object value;

		public Boolean getSuccess() {
			return success;
		}

		public String getMessage() {
			return message;
		}

		public Object getValue() {
			return value;
		}
	}
}
