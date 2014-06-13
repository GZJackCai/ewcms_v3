package com.ewcms.site.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.site.model.Channel;

/**
 * 栏目DAO接口
 * 
 * @author wu_zhijun
 *
 */
public interface ChannelDao extends PagingAndSortingRepository<Channel, Long>, JpaSpecificationExecutor<Channel>, ChannelDaoCustom {

}
