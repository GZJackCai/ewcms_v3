package com.ewcms.site.service;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ewcms.site.dao.TemplateSourceDao;
import com.ewcms.site.model.FileType;
import com.ewcms.site.model.Site;
import com.ewcms.site.model.TemplateSource;
import com.ewcms.site.model.TemplatesrcEntity;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

@Component
@Transactional(readOnly = true)
public class TemplateSourceService {
	
	@Autowired
	private TemplateSourceDao templateSourceDao;
	
	public TemplateSource getTemplateSource(Long id){
		return templateSourceDao.findOne(id);
	}
	
	@Transactional(readOnly = false)
	public Long addTemplateSource(TemplateSource vo){
		templateSourceDao.save(vo);
		return vo.getId();
	}
	
	@Transactional(readOnly = false)
	public Long updTemplateSource(TemplateSource vo){
		updPubPath(vo);
		return vo.getId();
	}
	
	/**
	 * 模板目录发生修改，需要更新模板发布路径，并且包括其子模板路径
	 * 
	 * @param channel
	 *            模板
	 */
	@Transactional(readOnly = false)
	private void updPubPath(final TemplateSource vo) {
		for (TemplateSource child : templateSourceDao.getTemplateSourceChildren(vo.getId(), getCurSite().getId())) {
			child.setPath(null);
			updPubPath(child);
		}
		templateSourceDao.save(vo);
	}


	@Transactional(readOnly = false)
	public void delTemplateSource(Long id){
		delTemplateSourceChildren(id);
		templateSourceDao.delete(id);
	}
	
	private void delTemplateSourceChildren(Long id){
		List<TemplateSource> templateSources = templateSourceDao.getTemplateSourceChildren(id, getCurSite().getId());
		if (templateSources != null && !templateSources.isEmpty()){
			for (TemplateSource templateSource : templateSources){
				List<TemplateSource> templateSourceChildrens = templateSourceDao.getTemplateSourceChildren(templateSource.getId(), getCurSite().getId());
				if (templateSourceChildrens != null && !templateSourceChildrens.isEmpty()){
					delTemplateSourceChildren(templateSource.getId());
				}else{
					templateSourceDao.delete(templateSource);
				}
			}
		}
	}
	
	/**
	 * 获取跟模板资源集
	 * 
	 */ 	
	public List<TemplateSource> getTemplaeSourceTreeList(Boolean channelEnable){
		return getTemplateSourceChildren(null,channelEnable);
	}
	
	/**
	 * 获取模板资源子资源集
	 * 
	 */ 	
	public List<TemplateSource> getTemplaeSourceTreeList(Long parentId,Boolean channelEnable){
		return getTemplateSourceChildren(parentId,channelEnable);
	}	
	
    private List<TemplateSource> getTemplateSourceChildren(Long parentId,Boolean channelEnable){
        List<TemplateSource> srcList = templateSourceDao.getTemplateSourceChildren(parentId,EwcmsContextUtil.getCurrentSite().getId());
        List<TemplateSource> validateList = new ArrayList<TemplateSource>();
        for(TemplateSource vo:srcList){
        	if(!channelEnable&&vo.getName().equals(getSiteSrcName())){
        		continue;
        	}
        	validateList.add(vo);
        }
        return validateList;
    }	
	/**
	 * 获取站点专栏资源根目录
	 * 
	 */    
    public TemplateSource channelSRCRoot(){
    	return channelTemplateSource(null);
    }
    
    @Transactional(readOnly = false)
    public TemplateSource channelTemplateSource(String srcName){
    	if(srcName==null||srcName.length()==0){
        	TemplateSource vo = templateSourceDao.getChannelTemplateSource(getSiteSrcName(),getCurSite().getId(),null);
        	if(vo == null){//没有站点专栏模板节点，就创建
        		vo = new TemplateSource();
        		vo.setDescribe(getCurSite().getSiteName()+"专栏资源目录");
        		vo.setName(getSiteSrcName());
        		vo.setSite(getCurSite());
        		vo.setSize("0KB");
        		vo.setPath(getSiteSrcName());
        		templateSourceDao.save(vo);
        	}
        	return vo;
    	}else{
    		Long parentId = channelSRCRoot().getId();
    		TemplateSource vo = templateSourceDao.getChannelTemplateSource(srcName,getCurSite().getId(),parentId);
        	if(vo == null){//没有站点专栏模板节点，就创建
        		vo = new TemplateSource();
        		vo.setDescribe(srcName+"专栏资源目录");
        		vo.setName(srcName);
        		vo.setSite(getCurSite());
        		vo.setSize("0KB");
        		vo.setParent(channelSRCRoot());
        		vo.setPath(getSiteSrcName()+"/"+srcName);
        		templateSourceDao.save(vo);
        	} 
        	return vo;
    	}    	
    }
    
	private Site getCurSite(){
		return EwcmsContextUtil.getCurrentSite();
	} 
	
	private String getSiteSrcName(){
		return getCurSite().getId()+"src";
	}

	public List<TemplateSource> findPublishTemplateSources(Long siteId, Boolean forceAgain) {
		return templateSourceDao.getPublishTemplateSources(siteId, forceAgain);
	}

	public List<TemplateSource> getTemplateSourceChildren(Long id) {
		return templateSourceDao.getTemplateSourceChildren(id, getCurSite().getId());
	}

	@Transactional(readOnly = false)
	public void publishTemplateSourceSuccess(Long id) {
		TemplateSource vo = getTemplateSource(id);
		vo.setRelease(true);
		templateSourceDao.save(vo);
	}

	public TemplateSource getTemplateSourceByUniquePath(String path) {
		return templateSourceDao.getTemplateSourceByPath(getCurSite().getId()+path);
	}

	public void exportTemplateSourceZip(Long templateSourceId, ZipOutputStream zos, String templateSourcePath) {
		try{
			TemplateSource templateSource = getTemplateSource(templateSourceId);
			if (templateSource == null) return;

			TemplatesrcEntity templatesrcEntity = templateSource.getSourceEntity();
			
			String filePath = templateSourcePath + templateSource.getName();
			ZipEntry zipEntry;
			if (templatesrcEntity == null){
				filePath += "/";
				
				//创建栏目目录
				zipEntry = new ZipEntry(filePath);
				zipEntry.setUnixMode(755);
				zos.putNextEntry(zipEntry);
				
				List<TemplateSource> templateSourceChildrens = templateSourceDao.getTemplateSourceChildren(templateSource.getId(), getCurSite().getId());
				
				for (TemplateSource templateSourceChildren : templateSourceChildrens){
					exportTemplateSourceZip(templateSourceChildren.getId(), zos, filePath);
				}
			}else{
				zipEntry = new ZipEntry(filePath);
				zipEntry.setUnixMode(644);
				zos.putNextEntry(zipEntry);
				
				BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(templatesrcEntity.getSrcEntity()));
				int b;
				while((b = bis.read())!=-1){  
					zos.write(b);  
		        }  
				
				zos.closeEntry();
				bis.close();
			}
		}catch(Exception e){
			
		}
	}	
	
	public Map<String, Object> searchTemplate(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, templateSourceDao, TemplateSource.class);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = false)
	public void unZipTemplateSourceFile(Long templateSourceId, File file){
		try {
			
			ZipFile zfile = new ZipFile(file);
			Enumeration<ZipEntry> zList = zfile.getEntries();
			Map<String, Long> dirMap = new HashMap<String, Long>();
			ZipEntry ze = null;
			String[] pathArr;
			while (zList.hasMoreElements()) {
				try {
					ze = (ZipEntry) zList.nextElement();
					TemplateSource vo = new TemplateSource();
					vo.setSite(EwcmsContextUtil.getCurrentSite());
					pathArr = ze.getName().split("/");
					vo.setFileType(FileType.FILE);
					vo.setName(pathArr[pathArr.length - 1]);
					String pathKey = ze.getName().substring(0, ze.getName().lastIndexOf(pathArr[pathArr.length - 1]));
					if (pathKey == null || pathKey.length() == 0) {
						if (templateSourceId == null) {
							vo.setParent(null);
						} else {
							vo.setParent(getTemplateSource(templateSourceId));
						}
					} else {
						vo.setParent(getTemplateSource(dirMap.get(pathKey)));
					}
					if (ze.isDirectory()) {
						vo.setFileType(FileType.DIRECTORY);
						dirMap.put(ze.getName(), addTemplateSource(vo));
						continue;
					}
					InputStream in = new BufferedInputStream(zfile.getInputStream(ze));
					byte[] buffer = new byte[Integer.parseInt(String.valueOf(ze.getSize()))];
					in.read(buffer);

					TemplatesrcEntity tplEntityVo = new TemplatesrcEntity();
					tplEntityVo.setSrcEntity(buffer);
					vo.setSourceEntity(tplEntityVo);
					addTemplateSource(vo);
					in.close();
				} catch (Exception e) {
				}
			}
			zfile.close();
		} catch (Exception e) {
		}
	}
	
	public TemplateSource findByIdAndChannelId(Long templateSourceId, Long channelId){
		return templateSourceDao.findByIdAndChannelId(templateSourceId, channelId);
	}
}
