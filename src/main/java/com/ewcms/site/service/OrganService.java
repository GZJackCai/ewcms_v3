package com.ewcms.site.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.site.dao.OrganDao;
import com.ewcms.site.model.Organ;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;
import com.ewcms.web.vo.TreeNode;

/**
 * 组织管理
 * 
 * @author wu_zhijun
 *
 */
@Component
@Transactional(readOnly = true)
public class OrganService {
	
	@Autowired
	private OrganDao organDao;
	
	@Transactional(readOnly = false)
	public Long addOrgan(Long parentId, String organName){
		Organ vo = new Organ();
		if(parentId != null) vo.setParent(getOrgan(parentId));
		vo.setName(organName);
		organDao.save(vo);
		return vo.getId();
	}
	
	@Transactional(readOnly = false)
	public Long updOrgan(Organ vo) {
		organDao.save(vo);
		return vo.getId();
	}
	
	@Transactional(readOnly = false)
	public void delOrgan(Long id) {
		organDao.delete(id);
	}

	public Organ getOrgan(Long id) {
		return organDao.findOne(id);
	}

	@Transactional(readOnly = false)
	public Long saveOrganInfo(Organ vo) {
		Organ oldVo = getOrgan(vo.getId());
		oldVo.setOrganInfo(vo.getOrganInfo());
		organDao.save(oldVo);
		return oldVo.getOrganInfo().getId();
	}

	public List<TreeNode> getOrganTreeList(){
		return getOrganChildren(null);
	}

	public List<TreeNode> getOrganTreeList(Long parentId){
		return getOrganChildren(parentId);
	}	
	
	public Map<String, Object> searchOrgan(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, organDao, Organ.class);
	}
	
	private List<TreeNode> getOrganChildren(Long parentId) {
		List<TreeNode> tnList = new ArrayList<TreeNode>();
		List<Organ> organList = organDao.getOrganChildren(parentId);
		for (Organ vo : organList) {
			TreeNode tnVo = new TreeNode();
			tnVo.setId(vo.getId().toString());
			tnVo.setText(vo.getName());
			tnVo.setIconCls("icon-organ");
			if (vo.hasChildren()) {
				tnVo.setState("closed");
			} else {
				tnVo.setState("open");
			}

			tnList.add(tnVo);
		}
		return tnList;
	}
}
