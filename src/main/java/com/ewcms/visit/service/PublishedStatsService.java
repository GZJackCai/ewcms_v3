package com.ewcms.visit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.site.dao.ChannelDao;
import com.ewcms.site.dao.OrganDao;
import com.ewcms.site.model.Channel;
import com.ewcms.site.model.Organ;
import com.ewcms.visit.dao.publishedstats.PublishedStatsDao;
import com.ewcms.visit.model.publishedstats.PublishedStats;
import com.ewcms.web.vo.TreeNode;

/**
 *
 * @author 吴智俊
 */
@Component
@Transactional(readOnly = true)
public class PublishedStatsService {
	
	@Autowired
	private PublishedStatsDao publishedStatsDao;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private OrganDao organDao;
	
	public List<PublishedStats> findPersonPublishedStatsTable(Date startDate, Date endDate, Long siteId, Long channelId){
		return publishedStatsDao.findPerson(startDate, endDate, siteId, channelId);
	}
	
	public List<TreeNode> findChannelPublishedStatsTable(Date startDate, Date endDate, Long siteId){
		Channel rootChannel = channelDao.getChannelRoot(siteId);
		List<Channel> channels = channelDao.getChannelChildren(rootChannel.getId());
		
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		
		for (Channel channel : channels){
			PublishedStats publishedStats = publishedStatsDao.findChannel(startDate, endDate, siteId, channel.getId());
			if (publishedStats == null) continue;
			
			TreeNode node = new TreeNode();
			node.setId(String.valueOf(channel.getId()));
			node.setText(channel.getName());
			node.setState("open");
			findChannelType(channel, node);
			node.setData(publishedStats);
			
			findTreeNode(node, channel.getId(), startDate, endDate, siteId);
			
			nodes.add(node);
		}
		
		return nodes;
	}
	
	private void findChannelType(Channel channel, TreeNode node){
		switch(channel.getType()){
		case ARTICLE: node.setIconCls("icon-channel-article");break;
		case LEADER: node.setIconCls("icon-channel-leader");break;
		case ONLINE : node.setIconCls("icon-channel-online");break;
		case LEADERARTICLE :node.setIconCls("icon-channel-articlerefer");break;
		case RETRIEVAL : node.setIconCls("icon-channel-retrieval");break;
		case PROJECT : node.setIconCls("icon-channel-project");break;
		case PROJECTARTICLE : node.setIconCls("icon-channel-projectarticle");break;
		case ENTERPRISE : node.setIconCls("icon-channel-enterprise");break;
		case ENTERPRISEARTICLE : node.setIconCls("icon-channel-enterprisearticle");break;
		case EMPLOYE : node.setIconCls("icon-channel-employe");break;
		case EMPLOYEARTICLE : node.setIconCls("icon-channel-employearticle");break;
		default : node.setIconCls("icon-channel-note");
		}
	}
	
	private void findTreeNode(TreeNode parentNode, Long parentId, Date startDate, Date endDate, Long siteId){
		List<TreeNode> treeGridNodes = new ArrayList<TreeNode>();
		
		List<Channel> channels = channelDao.getChannelChildren(parentId);
		for (Channel channel : channels){
			PublishedStats publishedStats = publishedStatsDao.findChannel(startDate, endDate, siteId, channel.getId());
			if (publishedStats == null) continue;
			
			TreeNode treeGridNode = new TreeNode();

			treeGridNode.setId(String.valueOf(channel.getId()));
			treeGridNode.setText(channel.getName());
			treeGridNode.setState("open");
			treeGridNode.setData(publishedStats);
			findChannelType(channel, treeGridNode);

			findTreeNode(treeGridNode, channel.getId(), startDate, endDate, siteId);
			
			treeGridNodes.add(treeGridNode);
		}
		parentNode.setChildren(treeGridNodes);
	}
	
	public List<TreeNode> findOrganPublishedStatsTable(Date startDate, Date endDate, Long siteId){
		List<Organ> rootOrgans = organDao.getOrganChildren(null);
		
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		for (Organ organ : rootOrgans){
			PublishedStats vo = publishedStatsDao.findOrgan(startDate, endDate, siteId, organ.getId());
			if (vo == null) continue;
			
			TreeNode node = new TreeNode();
			node.setId(String.valueOf(organ.getId()));
			node.setText(organ.getName());
			node.setState("open");
			node.setIconCls("icon-organ");
			node.setData(vo);
			
			findOrganChildNode(node, organ.getId(), siteId, startDate, endDate);
			
			nodes.add(node);
		}
		
		return nodes;
	}
	
	private void findOrganChildNode(TreeNode parentNode, Long parentId, Long siteId, Date startDate, Date endDate){
		List<TreeNode> treeGridNodes = new ArrayList<TreeNode>();
		
		List<Organ> organs = organDao.getOrganChildren(parentId);
		for (Organ organ : organs){
			PublishedStats vo = publishedStatsDao.findOrgan(startDate, endDate, siteId, organ.getId());
			if (vo == null) continue;
			
			TreeNode treeGridNode = new TreeNode();

			treeGridNode.setId(String.valueOf(organ.getId()));
			treeGridNode.setText(organ.getName());
			treeGridNode.setState("open");
			treeGridNode.setIconCls("icon-organ");
			treeGridNode.setData(vo);

			findOrganChildNode(treeGridNode, organ.getId(), siteId, startDate, endDate);
			
			treeGridNodes.add(treeGridNode);
		}
		parentNode.setChildren(treeGridNodes);
	}
}
