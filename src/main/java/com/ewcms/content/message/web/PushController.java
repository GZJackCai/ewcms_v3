package com.ewcms.content.message.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ewcms.content.document.service.ArticleMainService;
import com.ewcms.content.message.model.MsgSend.Type;
import com.ewcms.content.message.push.PushService;
import com.ewcms.content.message.service.MsgReceiveService;
import com.ewcms.content.message.service.MsgSendService;

@Controller
@RequestMapping(value = "/content/message")
public class PushController {

	@Autowired
	private MsgReceiveService msgReceiveService;
	@Autowired
	private MsgSendService msgSendService;
	@Autowired
	private ArticleMainService articleMainService;
    @Autowired
    private PushService pushService;

    /**
     * 获取页面的提示信息
     * @return
     */
    @RequestMapping(value = "/polling/{username}")
    @ResponseBody
    public Object polling(@PathVariable(value = "username")String username, HttpServletResponse resp) {
        resp.setHeader("Connection", "Keep-Alive");
        resp.addHeader("Cache-Control", "private");
        resp.addHeader("Pragma", "no-cache");

        if(username == null) {
            return null;
        }
        //如果用户第一次来 立即返回
        if(!pushService.isOnline(username)) {
            Long unreadMessageCount = msgReceiveService.findUnReadMessageCountByUserName(username);
            List<Map<String, Object>> notices = msgSendService.findTopRowNoticesOrSubscription(Type.NOTICE, 10);
            List<Map<String, Object>> subscriptions = msgSendService.findTopRowNoticesOrSubscription(Type.SUBSCRIPTION, 10);
            List<Map<String, Object>> todos = articleMainService.findBeApprovalArticleMain(username);

            Map<String, Object> data = new HashMap<String, Object>();
            data.put("unreadMessageCount", unreadMessageCount);
            data.put("notices", notices);
            data.put("subscriptions", subscriptions);
            data.put("todos", todos);
            pushService.online(username);
            return data;
        } else {
            //长轮询
            return pushService.newDeferredResult(username);
        }
    }
}
