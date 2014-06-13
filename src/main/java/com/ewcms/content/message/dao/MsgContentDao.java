package com.ewcms.content.message.dao;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.message.model.MsgContent;

/**
 * @author 吴智俊
 */
public interface MsgContentDao extends PagingAndSortingRepository<MsgContent, Long>, JpaSpecificationExecutor<MsgContent> {
}
