package com.ewcms.content.message.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 推送信息服务
 * 
 * @author wu_zhijun
 *
 */
@Service
public class PushApiService {
	
	@Autowired
    private PushService pushService;

	/**
	 * 未读取消息
	 * 
	 * @param userName 登录用户名
	 * @param unreadMessageCount 未读取数
	 */
    public void pushUnreadMessage(final String userName, Long unreadMessageCount) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("unreadMessageCount", unreadMessageCount);
        pushService.push(userName, data);
    }

    /**
     * 公告栏信息
     * 
     * @param notices 公告栏信息列表
     */
    public void pushNewNotice(List<Map<String, Object>> notices){
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("notices", notices);
    	pushService.push(data);
    }
    
    /**
     * 订阅栏信息
     * 
     * @param subscriptions 订阅栏信息列表
     */
    public void pushNewSubscription(List<Map<String, Object>> subscriptions){
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("subscriptions", subscriptions);
    	pushService.push(data);
    }
    
    /**
     * 侍办事件信息
     * 
     * @param userName 登录用户名
     * @param todos 待办事件信息列表
     */
    public void pushTodo(String userName, List<Map<String, Object>> todos){
    	Map<String, Object> data = new HashMap<String, Object>();
    	data.put("todos", data);
    	pushService.push(userName, data);
    }
    
    /**
     * 离线用户，即清空用户的DefferedResult 这样就是新用户，可以即时得到通知
     *
     * 比如刷新主页时，需要offline
     *
     * @param userName
     */
    public void offline(final String userName) {
        pushService.offline(userName);
    }
}
