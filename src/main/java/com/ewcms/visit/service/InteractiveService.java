package com.ewcms.visit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.site.dao.OrganDao;
import com.ewcms.site.model.Organ;
import com.ewcms.visit.dao.interactive.InteractiveDao;
import com.ewcms.visit.model.interactive.Interactive;
import com.ewcms.web.vo.TreeNode;

/**
 *
 * @author 吴智俊
 */
@Component
@Transactional(readOnly = true)
public class InteractiveService {

	@Autowired
	private InteractiveDao interactiveDao;
	@Autowired
	private OrganDao organDao;
	
	public List<TreeNode> findZhengMin(Date startDate, Date endDate) {
		List<Organ> rootOrgans = organDao.getOrganChildren(null);
		
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		for (Organ organ : rootOrgans){
			Interactive interactive = interactiveDao.findZhengMin(startDate, endDate, organ.getId());
			if (interactive == null) continue;
			
			TreeNode node = new TreeNode();
			node.setId(String.valueOf(organ.getId()));
			node.setText(organ.getName());
			node.setState("open");
			node.setIconCls("icon-organ");
			node.setData(interactive);
			
			findZhengMinChildNode(node, organ.getId(), startDate, endDate);
			
			nodes.add(node);
		}
		
		return nodes;
	}
	
	private void findZhengMinChildNode(TreeNode parentNode, Long parentId, Date startDate, Date endDate){
		List<TreeNode> treeGridNodes = new ArrayList<TreeNode>();
		
		List<Organ> organs = organDao.getOrganChildren(parentId);
		for (Organ organ : organs){
			Interactive interactive = interactiveDao.findZhengMin(startDate, endDate, organ.getId());
			if (interactive == null) continue;
			
			TreeNode treeGridNode = new TreeNode();

			treeGridNode.setId(String.valueOf(organ.getId()));
			treeGridNode.setText(organ.getName());
			treeGridNode.setState("open");
			treeGridNode.setIconCls("icon-organ");
			treeGridNode.setData(interactive);

			findZhengMinChildNode(treeGridNode, organ.getId(), startDate, endDate);
			
			treeGridNodes.add(treeGridNode);
		}
		parentNode.setChildren(treeGridNodes);
	}

	public List<TreeNode> findAdvisory(Date startDate, Date endDate) {
		List<Organ> rootOrgans = organDao.getOrganChildren(null);
		
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		for (Organ organ : rootOrgans){
			Interactive vo = interactiveDao.findAdvisory(startDate, endDate, organ.getId());
			if (vo == null) continue;
			
			TreeNode node = new TreeNode();
			node.setId(String.valueOf(organ.getId()));
			node.setText(organ.getName());
			node.setState("open");
			node.setIconCls("icon-organ");
			node.setData(vo);
			
			findAdvisoryChildNode(node, organ.getId(), startDate, endDate);
			
			nodes.add(node);
		}
		
		return nodes;
	}
	
	private void findAdvisoryChildNode(TreeNode parentNode, Long parentId, Date start, Date end){
		List<TreeNode> treeGridNodes = new ArrayList<TreeNode>();
		
		List<Organ> organs = organDao.getOrganChildren(parentId);
		for (Organ organ : organs){
			Interactive vo = interactiveDao.findAdvisory(start, end, organ.getId());
			if (vo == null) continue;
			
			TreeNode treeGridNode = new TreeNode();

			treeGridNode.setId(String.valueOf(organ.getId()));
			treeGridNode.setText(organ.getName());
			treeGridNode.setState("open");
			treeGridNode.setIconCls("icon-organ");
			treeGridNode.setData(vo);

			findAdvisoryChildNode(treeGridNode, organ.getId(), start, end);
			
			treeGridNodes.add(treeGridNode);
		}
		parentNode.setChildren(treeGridNodes);
	}
}
