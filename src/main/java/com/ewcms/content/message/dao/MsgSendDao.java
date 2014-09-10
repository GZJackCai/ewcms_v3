package com.ewcms.content.message.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
	
	/**
	 * 查询所有公告栏信息
	 * 
	 * @param type Type.NOTICES
	 * @return 
	 */
	List<MsgSend> findByTypeOrderBySendTimeDesc(Type type);
	
	@Query("Select s From MsgSend s Left Join s.msgReceiveUsers r Where r.userName=?1 Order By s.sendTime Desc")
	List<MsgSend> findByTypeAndReceiveUserNameOrderBySendTimeDesc(Type type, String userName);
}
