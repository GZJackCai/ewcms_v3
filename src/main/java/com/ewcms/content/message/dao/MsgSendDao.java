package com.ewcms.content.message.dao;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.ewcms.content.message.model.MsgSend;
import com.ewcms.content.message.model.MsgSend.Type;

/**
 * @author 吴智俊
 */
public interface MsgSendDao extends PagingAndSortingRepository<MsgSend, Long>, JpaSpecificationExecutor<MsgSend> {
	
	List<MsgSend> findByMsgReceiveUsersUserName(String userName);
	
	MsgSend findByUserNameAndId(String userName, Long id);
	
	MsgSend findByIdAndMsgReceiveUsersUserNameAndType(Long id, String userName, Type type);
	
	@QueryHints(value = {@QueryHint(name = "name", value="value")}, forCounting = false)
	Page<MsgSend> findByType(Type type, Pageable pageable);
}
