package com.ewcms.content.message.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.message.model.MsgReceive;

/**
 * @author 吴智俊
 */
public interface MsgReceiveDao extends PagingAndSortingRepository<MsgReceive, Long>, JpaSpecificationExecutor<MsgReceive> {
	
	List<MsgReceive> findByUserName(String userName);
	
	MsgReceive findByUserNameAndId(String userName, Long id);
	
	List<MsgReceive> findByUserNameAndReadFalseOrderByIdDesc(String userName);
}
