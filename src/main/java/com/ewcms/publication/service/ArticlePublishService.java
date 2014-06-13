/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

package com.ewcms.publication.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.ewcms.content.document.dao.ArticleDao;
import com.ewcms.content.document.model.Article;
import com.ewcms.site.dao.ChannelDao;
import com.ewcms.site.model.Channel;

/**
 * 文章加载和操作接口
 * <br>
 * 提供生成文章所需要的数据，并更改以发布文章状态。
 * 
 * @author wangwei
 */
@Component
public class ArticlePublishService {

	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ChannelDao channelDao;
	
    /**
     * 得到指定的文章
     *
     * @param id 文章编号
     * @return
     */
    public Article getArticle(Long id){
    	return articleDao.findOne(id);
    }

    /**
     * 发布文章成功
      * <br>
     * 标示文章为发布状态。
     * 
     * @param id 文章编号
     * @param url 文章链接地址
     */
    public void publishArticleSuccess(Long id, String url){
    	Article article = articleDao.findOne(id);
		Assert.notNull(article);
		url = url.replace("\\", "/").replace("//", "/");
		article.setUrl(url);
		article.setStatus(Article.Status.RELEASE);
		articleDao.save(article);
    }
    
    /**
     * 查询准备发布的文章
     * <br>
     * 再发布时会得到所有要发布的文章（PreRelease和Release）。
     * 
     * @param channelId 频道编号 
     * @param forceAgain 再发布 
     * @param limit 最大文章数
     * @return
     */
    public List<Article> findPublishArticles(Long channelId,Boolean forceAgain, Integer limit){
    	  Channel channel = channelDao.findOne(channelId);
          Assert.notNull(channel);
          return articleDao.findPublishArticles(channelId,forceAgain,limit); 
    }
    
    /**
     * 得到频道指定页面文章
     * 
     * <br>
     * 查询的文章已经发布，按照文章定义的顺序显示，页数从0开始。
     * 
     * @param channelId  频道编号
     * @param page 页数
     * @param row 行数
     * @param top  顶置文章
     * @return
     */
    public List<Article> findArticleReleasePage(Long channelId,Integer page,Integer row,Boolean top){
    	return articleDao.findArticleReleasePage(channelId, page, row, top);
    }
    
    /**
     * 得到指定子频道的页面文章
     * 
     * @param channelId 频道编号
     * @param page 页数
     * @param row 行数
     * @param top 顶置文章
     * @return
     */
    public List<Article> findChildChannelArticleReleasePage(Long channelId,Integer page,Integer row,Boolean top){
    	List<Long> channelIds = new ArrayList<Long>();
		List<Channel> channels = channelDao.getChannelChildren(channelId);
		findChannelChild(channelIds, channels);
		return articleDao.findChildChannelArticleReleasePage(channelIds, page, row, top);
    }
    
    private void findChannelChild(List<Long> channelIds, List<Channel> channels){
		if (channels != null && !channels.isEmpty()){
			for (Channel channel : channels){
				if (channel.getPublicenable()){
					channelIds.add(channel.getId());
				}
				List<Channel> temp = channelDao.getChannelChildren(channel.getId());
				if (temp != null && !temp.isEmpty()){
					findChannelChild(channelIds, temp);
				}
			}
		}
	}

    /**
     * 得到频道已经发布的文章总数
     * <br>
     * 如果已经发布文章总数大于频道最大显示记录数，则返回最大记录数
     * 
     * @param channelId 频道编号
     * @return 
     */
    public int getArticleReleaseCount(Long channelId){
        Channel channel = channelDao.findOne(channelId);
        Assert.notNull(channel);
        int maxSize = channel.getMaxSize();
        long releaseMaxSize = articleDao.findArticleReleseCount(channelId);
        if (maxSize < releaseMaxSize){
            return maxSize;
        }else{
            return (int)releaseMaxSize;
        }
    }
}
