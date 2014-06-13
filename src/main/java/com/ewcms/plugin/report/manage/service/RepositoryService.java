/**
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
package com.ewcms.plugin.report.manage.service;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.content.resource.model.Resource;
import com.ewcms.content.resource.service.ResourceService;
import com.ewcms.plugin.report.manage.dao.RepositoryDao;
import com.ewcms.plugin.report.model.Repository;
import com.ewcms.site.model.Site;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.query.SearchMain;

/**
 * 
 * @author wu_zhijun
 *
 */
@Component
public class RepositoryService{

	@Autowired
	private RepositoryDao repositoryDao;
	@Autowired
	private ResourceService resourceService;
	
	public Long addRepository(Repository repository){
		repositoryDao.save(repository);
		return repository.getId();
	}

	public Long updRepository(Repository repository){
		repositoryDao.save(repository);
		return repository.getId();
	}

	public Repository findRepositoryById(Long repositoryId){
		return repositoryDao.findOne(repositoryId);
	}

	public void delRepository(Long repositoryId){
		repositoryDao.delete(repositoryId);
	}

	public void publishRepository(List<Long> repositoryIds, Site site) {
		for (Long repositoryId : repositoryIds) {
			Repository repository = findRepositoryById(repositoryId);
			String type = repository.getType();
			byte[] bytes = repository.getEntity();
			String outputFile = repository.getName() + "." + type;

			Resource.Type resourceType = Resource.Type.ANNEX;
			if (type.toLowerCase().equals("png")) {
				resourceType = Resource.Type.IMAGE;
			}

			File file = null;
			FileOutputStream fileStream = null;
			BufferedOutputStream bufferStream = null;
			try {
				file = new File(outputFile);
				fileStream = new FileOutputStream(file);
				bufferStream = new BufferedOutputStream(fileStream);
				bufferStream.write(bytes);
				
				//TODO 生成资源未实现
				//resourceService.upload(site, file, outputFile, resourceType);
				
				repository.setPublishDate(new Date(Calendar.getInstance().getTime().getTime()));
				repositoryDao.save(repository);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fileStream != null) {
					try {
						fileStream.close();
					} catch (IOException e) {
					}
					fileStream = null;
				}
				if (bufferStream != null) {
					try {
						bufferStream.close();
					} catch (IOException e) {
					}
					bufferStream = null;
				}
				file = null;
			}
		}
	}
	
	public Map<String, Object> search(QueryParameter params){
		return SearchMain.search(params, "IN_id", Long.class, repositoryDao, Repository.class);
	}
}
