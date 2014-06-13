/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.task.impl.event;

import java.util.concurrent.atomic.AtomicInteger;

import com.ewcms.content.document.model.Article;
import com.ewcms.publication.service.ArticlePublishService;

/**
 * 发布文章事件
 * 
 * @author wangwei
 */
public class DetailEvent extends CompleteEvent {

    private final ArticlePublishService articlePublishService;
    private final Article article;
    
    public DetailEvent(AtomicInteger completeNumber,
            ArticlePublishService articlePublishService,
            Article article){
        
        super(completeNumber);
        this.articlePublishService = articlePublishService;
        this.article = article;
    }
    
    @Override
    protected void successAfter(String uri){
        articlePublishService.publishArticleSuccess(article.getId(), uri);
    }
}
