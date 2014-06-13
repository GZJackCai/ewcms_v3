package com.ewcms.content.history.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ewcms.content.document.model.Article;
import com.ewcms.content.history.model.HistoryModel;

/**
 * @author 吴智俊
 */
public class HistoryUtil {
	
	protected static final Logger logger = LoggerFactory.getLogger(HistoryUtil.class);
			
	@SuppressWarnings("unchecked")
	public static Map<String, Object> resolve(Map<String, Object> result){
		Long version = (Long) result.get("total");
    	List<HistoryModel> historyModels = (List<HistoryModel>) result.get("rows");
    	
    	List<Map<String,Object>> listValues = new ArrayList<Map<String,Object>>();
    	for(HistoryModel historyModel : historyModels){
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("historyId", historyModel.getId());
    		map.put("version", version);
    		Object obj = conversion(historyModel.getModelObject());
			if (obj == null){
				map.put("maxPage",0);
			}else{
				if (obj instanceof Article){
					Article article = (Article)obj;
					map.put("maxPage",article.getContents().size());
				}
    		}
    		map.put("historyTime", historyModel.getCreateDate());
    		listValues.add(map);
    		
    		version--;
    	}
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>(2);
		resultMap.put("total", historyModels == null ? 0 : historyModels.size());
		resultMap.put("rows", listValues);
		return resultMap;
	}
	
	/**
	 * 转换操作
	 * 
	 * @param bytes 二进制对象
	 * @return 实体对象
	 */
	public static Object conversion(byte[] bytes) {
		ByteArrayInputStream bis = null;
		ObjectInputStream ois = null;
		Object obj = null;
		try {
			bis = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bis);
			obj = ois.readObject();
		} catch (IOException e) {
			logger.error(e.toString());
		} catch (ClassNotFoundException e) {
			logger.error(e.toString());
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
				}
			}
			bis = null;
			ois = null;
		}
		return obj;
	}
}
