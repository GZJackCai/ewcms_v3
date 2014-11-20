package com.ewcms.visit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.ewcms.web.vo.TreeNode;

/**
 * @author 吴智俊
 */
public class VisitTreeUtil {
	private static final String ICON_CLS_VISIT = "icon-visit-analysis";
	private static final String ICON_CLS_NOTE = "icon-channel-note";
	private static Map<String, String> oneTreeMap = new TreeMap<String, String>();
	private static Map<String, String> twoTreeMap = new TreeMap<String, String>();
	private static Map<String, String> threeTreeMap = new TreeMap<String, String>();
	private static Map<String, String> fourTreeMap = new TreeMap<String, String>();
	private static Map<String, String> fiveTreeMap = new TreeMap<String, String>();
	private static Map<String, String> sixTreeMap = new TreeMap<String, String>();
	private static Map<String, String> sevenTreeMap = new TreeMap<String, String>();
	
	static{
		oneTreeMap.put("0综合报告", "/totality/summary");
		oneTreeMap.put("1全站点击率", "/totality/siteclick");
		oneTreeMap.put("2访问记录", "/totality/visitrecord");
		oneTreeMap.put("3时段分布", "/totality/timedistributed");
		oneTreeMap.put("4入口分析", "/totality/entrance");
		oneTreeMap.put("5出口分析", "/totality/exit");
		oneTreeMap.put("6被访主机分析", "/totality/host");
		oneTreeMap.put("7区域分布", "/totality/region/country");
		oneTreeMap.put("8在线情况", "/totality/online");
		
		twoTreeMap.put("0栏目点击排行", "/traffic/channel");
		twoTreeMap.put("1文章点击排行", "/traffic/article");
		twoTreeMap.put("2URL点击排行", "/traffic/url");
		
		threeTreeMap.put("0访问深度", "/loyalty/depth");
		threeTreeMap.put("1访问频率", "/loyalty/frequency");
		threeTreeMap.put("2回头率", "/loyalty/back");
		threeTreeMap.put("3停留时间", "/loyalty/sticktime");
		
		fourTreeMap.put("0来源组成", "/clickrate/sourceform");
		fourTreeMap.put("1搜索引擎", "/clickrate/searchengine");
		fourTreeMap.put("2来源网站", "/clickrate/website");
		
		fiveTreeMap.put("0操作系统", "/clientside/os");
		fiveTreeMap.put("1浏览器", "/clientside/browser");
		fiveTreeMap.put("2语言", "/clientside/language");
		fiveTreeMap.put("3屏幕分辨率", "/clientside/screen");
		fiveTreeMap.put("4屏幕色深", "/clientside/colordepth");
		fiveTreeMap.put("5是否支持Apple", "/clientside/javaenabled");
		fiveTreeMap.put("6Flash版本", "/clientside/flashversion");
		fiveTreeMap.put("7是否允许Cookies", "/clientside/cookieenabled");
		
		sixTreeMap.put("0人员发布统计", "/publishedstats/person");
		sixTreeMap.put("1栏目发布统计", "/publishedstats/channel");
		sixTreeMap.put("2机构发布统计", "/publishedstats/organ");
		
		sevenTreeMap.put("0政民互动统计", "/interactive/zhengmin");
		sevenTreeMap.put("1网上咨询统计", "/interactive/advisory");
		//sevenTreeMap.put("2留言审核统计", "audit");
	}
	
	public static List<TreeNode> showTree(){
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
		Integer id = 0;
		TreeNode rootNode = new TreeNode();
		try {
			rootNode.setId((id++).toString());
			rootNode.setText("统计分析");
			rootNode.setState("open");
			rootNode.setIconCls("icon-channel-site");
			
			List<TreeNode> twoNodes = new ArrayList<TreeNode>();
			
			TreeNode twoNode = new TreeNode();
			twoNode.setId((id++).toString());
			twoNode.setText("总体情况");
			twoNode.setState("open");
			twoNode.setIconCls(ICON_CLS_NOTE);
			
			List<TreeNode> threeNodes = getThreeNode(oneTreeMap.entrySet().iterator(), id);
			twoNode.setChildren(threeNodes);
			twoNodes.add(twoNode);
			
			id += oneTreeMap.size();
			
			twoNode = new TreeNode();
			twoNode.setId((id++).toString());
			twoNode.setText("访问量排行");
			twoNode.setState("open");
			twoNode.setIconCls(ICON_CLS_NOTE);
			
			threeNodes = getThreeNode(twoTreeMap.entrySet().iterator(), id); 
			twoNode.setChildren(threeNodes);
			twoNodes.add(twoNode);
			
			id += twoTreeMap.size();
			
			twoNode = new TreeNode();
			twoNode.setId((id++).toString());
			twoNode.setText("忠诚度分析");
			twoNode.setState("open");
			twoNode.setIconCls(ICON_CLS_NOTE);
			
			threeNodes = getThreeNode(threeTreeMap.entrySet().iterator(), id); 
			twoNode.setChildren(threeNodes);
			twoNodes.add(twoNode);
			
			id += threeTreeMap.size();
			
			twoNode = new TreeNode();
			twoNode.setId((id++).toString());
			twoNode.setText("点击量来源");
			twoNode.setState("open");
			twoNode.setIconCls(ICON_CLS_NOTE);
			
			threeNodes = getThreeNode(fourTreeMap.entrySet().iterator(), id); 
			twoNode.setChildren(threeNodes);
			twoNodes.add(twoNode);
			
			id += fourTreeMap.size();
			
			twoNode = new TreeNode();
			twoNode.setId((id++).toString());
			twoNode.setText("客户端情况");
			twoNode.setState("open");
			twoNode.setIconCls(ICON_CLS_NOTE);
			
			threeNodes = getThreeNode(fiveTreeMap.entrySet().iterator(), id); 
			twoNode.setChildren(threeNodes);
			twoNodes.add(twoNode);
			
			id += fiveTreeMap.size();
			
			twoNode = new TreeNode();
			twoNode.setId((id++).toString());
			twoNode.setText("发布统计");
			twoNode.setState("open");
			twoNode.setIconCls(ICON_CLS_NOTE);
			
			threeNodes = getThreeNode(sixTreeMap.entrySet().iterator(), id); 
			twoNode.setChildren(threeNodes);
			twoNodes.add(twoNode);
			
			id += sixTreeMap.size();
			
			twoNode = new TreeNode();
			twoNode.setId((id++).toString());
			twoNode.setText("互动统计");
			twoNode.setState("open");
			twoNode.setIconCls(ICON_CLS_NOTE);
			
			threeNodes = getThreeNode(sevenTreeMap.entrySet().iterator(), id); 
			twoNode.setChildren(threeNodes);
			twoNodes.add(twoNode);
			
			rootNode.setChildren(twoNodes);
		} catch (Exception e) {
		}
		treeNodes.add(rootNode);
		return treeNodes;
	}
	
	private static List<TreeNode> getThreeNode(Iterator<Entry<String, String>> it, Integer id){
		List<TreeNode> threeNodes = new ArrayList<TreeNode>();
		TreeNode threeNode;
		while (it.hasNext()){
			Entry<String, String> entry = it.next();
			threeNode = new TreeNode();
			threeNode.setId((id++).toString());
			threeNode.setText(entry.getKey().substring(1));
			threeNode.setState("open");
			threeNode.setIconCls(ICON_CLS_VISIT);
			Map<String, String> attributes = new HashMap<String, String>();
			attributes.put("url", entry.getValue());
			threeNode.setAttributes(attributes);
			threeNodes.add(threeNode);
		}
		return threeNodes;
	}
}
