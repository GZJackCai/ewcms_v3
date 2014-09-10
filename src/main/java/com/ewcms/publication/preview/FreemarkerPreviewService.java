package com.ewcms.publication.preview;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ewcms.publication.PublishException;
import com.ewcms.publication.dao.ArticlePublishDaoable;
import com.ewcms.publication.dao.ChannelPublishDaoable;
import com.ewcms.publication.dao.SitePublishDaoable;
import com.ewcms.publication.dao.TemplatePublishDaoable;
import com.ewcms.publication.dao.preview.ArticlePreviewDao;
import com.ewcms.publication.dao.preview.ChannelPreviewDao;
import com.ewcms.publication.freemarker.FreemarkerConfigurationFactory;
import com.ewcms.publication.generator.FreemarkerGenerator;
import com.ewcms.publication.generator.Generatorable;
import com.ewcms.publication.module.Article;
import com.ewcms.publication.module.Channel;
import com.ewcms.publication.module.Site;
import com.ewcms.publication.module.Template;
import com.ewcms.publication.module.Template.TemplateType;
import com.ewcms.publication.uri.UriRuleable;
import com.ewcms.publication.uri.UriRules;

import freemarker.template.Configuration;

@Service
public class FreemarkerPreviewService implements PreviewServiceable {
	private static final Logger logger = LoggerFactory.getLogger(FreemarkerPreviewService.class);
	
	@Autowired
	private DataSource dataSource;
	@Autowired
	private SitePublishDaoable sitePublishDao;
	@Autowired
    private TemplatePublishDaoable templatePublishDao;
    
	@Override
	public void viewTemplate(Writer writer,Long siteId, Long channelId, Long templateId, boolean mock) throws PublishException {
		Template template = templatePublishDao.findOne(channelId , templateId);
		if(template == null){
			throw new PublishException("Template not exist");
		}
		ArticlePublishDaoable articlePublishDao = new ArticlePreviewDao(dataSource,mock);
		ChannelPublishDaoable channelPublishDao = new ChannelPreviewDao(dataSource,mock);
		Configuration cfg = createConfigurable(articlePublishDao, channelPublishDao);
		Channel channel = channelPublishDao.findPublishOne(siteId, channelId);
		if(channel == null){
			throw new PublishException("Channel not exist");
		}
		Site site = sitePublishDao.findOne(siteId);
		String taskId = UUID.randomUUID().toString();
		
		Generatorable gen;
		if(isDetail(template)){
			gen = new FreemarkerGenerator.
					Builder(taskId, site, channel, cfg, template.getUniquePath()).
					setArticle(ArticlePreviewDao.newMockArticle(0l)).
					setUriRule(uriRule(template)).
					setPageCount(4).
					setPageNumber(0).
					build();
		}else{
			gen = new FreemarkerGenerator.
					Builder(taskId, site, channel, cfg, template.getUniquePath()).
					setUriRule(uriRule(template)).
					setPageCount(20).
					setPageNumber(0).
					build();	
		}
		
		gen.process(writer);
	}
	
	private boolean isDetail(Template template){
		return template.getType() == TemplateType.DETAIL;
	}
	
	private UriRuleable uriRule(Template t){
		if(StringUtils.isNotBlank(t.getUriPattern())){
			return UriRules.newUriRuleBy(t.getUriPattern());
		}
		
		if(t.getType() == TemplateType.HOME){
			return UriRules.newHome(); 
		}
		
		if(t.getType()== TemplateType.LIST){
			return UriRules.newList();
		}
		
		if(t.getType() == TemplateType.DETAIL){
			return UriRules.newDetail();
		}
		
		return UriRules.newNull();
	}

	@Override
	public void viewArticle(Writer writer,Long siteId, Long channelId, Long articleId, Integer pageNumber) throws PublishException {
		List<Template> templates = templatePublishDao.findInChannel(channelId);
		Template template = getDetailTemplate(templates);		
		ArticlePublishDaoable articleDao = new ArticlePreviewDao(dataSource,false);
		ChannelPublishDaoable channelDao = new ChannelPreviewDao(dataSource,true);
		Configuration cfg = createConfigurable(articleDao, channelDao);
		Channel channel = channelDao.findPublishOne(siteId, channelId);
		if(channel == null){
			throw new PublishException("Channel not exist");
		}
		Site site = sitePublishDao.findOne(siteId);
		String taskId = UUID.randomUUID().toString();
		List<Article> articles = articleDao.findPrePublish(Arrays.asList(articleId));
		Article article = getArticlePage(articles, pageNumber);
		Generatorable gen = new FreemarkerGenerator.
				Builder(taskId, site, channel, cfg, template.getUniquePath()).
				setPageCount(article.getTotalPage()).
				setPageNumber(article.getPage()).
				setArticle(article).
				build();
		gen.process(writer);
	}
	
	private Template getDetailTemplate(List<Template> templates)throws PublishException{
		for(Template t : templates){
			if(isDetail(t)){
				return t;
			}
		}
		throw new PublishException("Channel not exist detail template.");
	}
	
	private Article getArticlePage(List<Article> articles,Integer pageNumber)throws PublishException{
		for(Article article : articles){
			if(article.getPage() == pageNumber){
				return article;
			}
		}
		throw new PublishException("Article not exist");
	}
	
	private Configuration createConfigurable(ArticlePublishDaoable articleDao, ChannelPublishDaoable channelDao)throws PublishException{
		try{
			FreemarkerConfigurationFactory factory = new FreemarkerConfigurationFactory();
			
			factory.setTemplateService(templatePublishDao);
			factory.setArticleService(articleDao);
			factory.setChannelService(channelDao);
			
			return factory.createConfiguration();
		}catch(Exception e){
			logger.error("Create freemarker configuartion error : {}", e.getMessage());
			throw new PublishException("Create freemarker configuartion error");
		}
	}
	
	@Override
	public boolean verifyTemplate(Long siteId, Long channelId, Long templateId) {
		boolean verify = false;
		try{
			Writer writer= new StringWriter();
			this.viewTemplate(writer, siteId, channelId, templateId, true);
			verify = true;
		}catch(PublishException e){
			logger.error("Verify template is exception {}", e.getMessage());
		}
		templatePublishDao.saveVerifyTemplate(templateId, verify);
		return verify;
	}
	
	public void setDataSource(DataSource dataSource){
		this.dataSource = dataSource;
	}

	public void setSitePublishDao(SitePublishDaoable sitePublishDao){
		this.sitePublishDao = sitePublishDao;
	}
	
	public void setTemplatePublishDao(TemplatePublishDaoable templatePublishDao){
		this.templatePublishDao = templatePublishDao;
	}
	
}
