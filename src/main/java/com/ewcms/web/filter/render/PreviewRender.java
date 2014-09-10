/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.web.filter.render;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.publication.PublishException;
import com.ewcms.publication.preview.PreviewServiceable;
import com.ewcms.util.EwcmsContextUtil;

/**
 * 返回指定预览页面
 * 
 * @author wangwei
 */
public class PreviewRender implements Renderable{

    private static final Logger logger = LoggerFactory.getLogger(PreviewRender.class);
    
    private static final String CHANNEL_ID_PARAM = "channelId";
    private static final String TEMPLATE_ID_PARAM = "templateId";
    private static final String ARTICLE_ID_PARAM = "articleId";
    private static final String PAGE_NUMBER_PARAM = "page";
    private static final String MOCK_PARAM = "mock";
    
    private PreviewServiceable previewService;
    
    public PreviewRender(PreviewServiceable previewService){
        this.previewService = previewService;
    }
    
    private Long getChannelId(HttpServletRequest request){
        String value = request.getParameter(CHANNEL_ID_PARAM);
        logger.debug("ChannelId is {}",value);
        return value == null ? null : Long.valueOf(value);
    }
    
    private Long getTemplateId(HttpServletRequest request){
        String value = request.getParameter(TEMPLATE_ID_PARAM);
        logger.debug("TemplateId is {}",value);
        return value == null ? null : Long.valueOf(value);
    }
    
    private Long getArticleId(HttpServletRequest request){
        String value = request.getParameter(ARTICLE_ID_PARAM);
        logger.debug("ArticleId is {}",value);
        return value == null ? null : Long.valueOf(value);
    }
    
    private Integer getPageNumber(HttpServletRequest request){
        String value = request.getParameter(PAGE_NUMBER_PARAM);
        logger.debug("Page number is {}",value);
        return value == null ? new Integer(0) : Integer.valueOf(value);
    }
    
    private boolean isMock(HttpServletRequest request){
        String value = request.getParameter(MOCK_PARAM);
        logger.debug("TemplateId is {}",value);
        if(value == null){
            return true;
        }else{
            return Boolean.valueOf(value);
        }
    }
    
    private boolean skip(HttpServletRequest request,HttpServletResponse response){
        String value = request.getParameter("view");
        return value == null;
    }
    
    /**
     * 输出错误到预览页面
     * 
     * @param response 
     * @param e
     * @throws 
     */
    private void renderError(PrintWriter printWriter, Exception e)throws IOException{
    	printWriter.write(e.getMessage());
    	printWriter.flush();
    }
    
   @Override
    public boolean render(HttpServletRequest request,HttpServletResponse response) throws IOException {
       
        if(skip(request,response)){
            return false;
        }
        
        Long siteId = EwcmsContextUtil.getCurrentSiteId();
        Long channelId = getChannelId(request);
        Long templateId = getTemplateId(request);
        PrintWriter printWriter = response.getWriter();
        try{
            if(templateId != null){
                Boolean mock = isMock(request);
                previewService.viewTemplate(printWriter, siteId, channelId, templateId, mock); 
            }else{
                Long articleId = getArticleId(request);
                Integer pageNumber = getPageNumber(request);
                previewService.viewArticle(printWriter, siteId, channelId, articleId, pageNumber);
            }
            return true;
        }catch(PublishException e){
            logger.warn("Preview is error: {}", e.toString());
            renderError(printWriter, e);
            return false;
        }
    }
}
