package com.ewcms.scheduling.generate.job.channel.dao;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.scheduling.generate.job.channel.model.EwcmsJobChannel;

/**
 * @author 吴智俊
 */
public interface EwcmsJobChannelDao extends PagingAndSortingRepository<EwcmsJobChannel, Long>, EwcmsJobChannelDaoCustom {

}
