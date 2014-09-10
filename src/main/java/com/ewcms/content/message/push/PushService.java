package com.ewcms.content.message.push;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

@Service
public class PushService {

	private volatile Map<String, Queue<DeferredResult<Object>>> userNameToDeferredResultMap = new ConcurrentHashMap<String, Queue<DeferredResult<Object>>>();

	public boolean isOnline(final String userName) {
		return userNameToDeferredResultMap.containsKey(userName);
	}

	/**
	 * 上线后 创建一个空队列，防止多次判断
	 * 
	 * @param userName
	 */
	public void online(final String userName) {
		Queue<DeferredResult<Object>> queue = userNameToDeferredResultMap.get(userName);
		if (queue == null) {
			queue = new ConcurrentLinkedQueue<DeferredResult<Object>>(); 
			userNameToDeferredResultMap.put(userName, queue);
		}
	}

	public void offline(final String userName) {
		Queue<DeferredResult<Object>> queue = userNameToDeferredResultMap.remove(userName);
		if (queue != null) {
			for (DeferredResult<Object> result : queue) {
				try {
					result.setResult("");
				} catch (Exception e) {
					// ignore
				}
			}
		}
	}

	public DeferredResult<Object> newDeferredResult(final String userName) {
		final DeferredResult<Object> deferredResult = new DeferredResult<Object>();
		deferredResult.onCompletion(new Runnable() {
			@Override
			public void run() {
				Queue<DeferredResult<Object>> queue = userNameToDeferredResultMap.get(userName);
				if (queue != null) {
					queue.remove(deferredResult);
					deferredResult.setResult("");
				}
			}
		});
		deferredResult.onTimeout(new Runnable() {
			@Override
			public void run() {
				deferredResult.setErrorResult("");
			}
		});
		Queue<DeferredResult<Object>> queue = userNameToDeferredResultMap.get(userName);
		if (queue == null) {
			queue = new ConcurrentLinkedQueue<DeferredResult<Object>>();
			userNameToDeferredResultMap.put(userName, queue);
		}
		queue.add(deferredResult);

		return deferredResult;
	}

	/**
	 * 发送给指定用户
	 * 
	 * @param userName
	 * @param data
	 */
	public void push(final String userName, final Object data) {
		Queue<DeferredResult<Object>> queue = userNameToDeferredResultMap.get(userName);
		if (queue != null){
			for (DeferredResult<Object> deferredResult : queue) {
				if (!deferredResult.isSetOrExpired()) {
					try {
						deferredResult.setResult(data);
					} catch (Exception e) {
						queue.remove(deferredResult);
					}
				}
			}
		}
	}

	/**
	 * 发送给所有在线人员
	 * 
	 * @param data
	 */
	public void push(final Object data) {
		for (String loginUsername : userNameToDeferredResultMap.keySet()) {
			push(loginUsername, data);
		}
	}

	/**
	 * 定期清空队列 防止中间推送消息时中断造成消息丢失
	 */
	@Scheduled(fixedRate = 5L * 60 * 1000)
	public void sync() {
		Map<String, Queue<DeferredResult<Object>>> oldMap = userNameToDeferredResultMap;
		userNameToDeferredResultMap = new ConcurrentHashMap<String, Queue<DeferredResult<Object>>>();
		for (Queue<DeferredResult<Object>> queue : oldMap.values()) {
			if (queue == null) {
				continue;
			}

			for (DeferredResult<Object> deferredResult : queue) {
				try {
					deferredResult.setResult("");
				} catch (Exception e) {
					queue.remove(deferredResult);
				}
			}

		}
	}

}