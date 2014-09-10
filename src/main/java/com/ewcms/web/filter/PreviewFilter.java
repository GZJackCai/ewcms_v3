/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.web.filter;

import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.service.ArticleMainService;
import com.ewcms.site.model.Channel;
import com.ewcms.site.model.Template;
import com.ewcms.site.service.ChannelService;
import com.ewcms.site.service.TemplateService;

public class PreviewFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(PreviewFilter.class);
    
    private static final Format format = new SimpleDateFormat("yyyy-MM-dd");
    private static final String PREVIEW_PATH = "/template/preview";
    private static final String CHANNEL_ID_PARAM = "channelId";
    private static final String TEMPLATE_ID_PARAM = "templateId";
    private static final String ARTICLE_ID_PARAM = "articleId";
    
    @Autowired
    private ChannelService channelService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private ArticleMainService articleMainService ;
    
    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig config) throws ServletException {
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
    
    @SuppressWarnings("rawtypes")
    private String getRedirectUri(HttpServletRequest request,Long articleId,Long templateId){
        
        StringBuilder builder = new StringBuilder();
        
        if(templateId == null){
            Article article = articleMainService.findArticle(articleId);
            if(article == null){
                return null;
            }
            builder.append("/document/")
                   .append(format.format(article.getCreateTime()))
                   .append("/").append(article.getId()).append(".html");
        }else{
            Template template = templateService.getTemplate(templateId);
            if(template == null){
                return null;
            }
            Channel channel = channelService.getChannel(template.getChannelId());
            builder.append(channel.getAbsUrl())
                   .append("/")
                   .append(template.getName());
        }
        
        builder.append("?view=true");
        for(Enumeration e = request.getParameterNames();e.hasMoreElements();){
            String name = (String)e.nextElement();
            String value = request.getParameter(name);
            builder.append("&").append(name).append("=").append(value);
        }
        
        return builder.toString();
    }
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse rep,FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String uri = request.getRequestURI();
        if(!StringUtils.endsWith(uri, PREVIEW_PATH)){
            logger.debug("Uri is not preview address");
            chain.doFilter(req, rep);
            return ;
        }
        
        HttpServletResponse response = (HttpServletResponse) rep;
        try{
            Long channelId = getChannelId(request);
            Long templateId = getTemplateId(request);
            Long articleId = getArticleId(request);
            if(templateId == null 
                    &&(channelId == null || articleId == null )){
                
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return ;
            }

            String redirectUri = getRedirectUri(request,articleId,templateId);
            if(redirectUri == null){
                logger.debug("Redirect's address is null");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }else{
                logger.debug("Redirect's address is {}",redirectUri);
                response.sendRedirect(redirectUri);
            }
        }catch(Exception e){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,e.toString());
        }
    }
}
