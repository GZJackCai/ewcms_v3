package com.ewcms.content.document.web;

import static com.ewcms.util.EmptyUtil.isNotNull;
import static com.ewcms.util.EmptyUtil.isStringEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.content.document.model.Article;
import com.ewcms.content.document.model.ArticleMain;
import com.ewcms.content.document.model.Category;
import com.ewcms.content.document.model.Content;
import com.ewcms.content.document.service.ArticleMainService;
import com.ewcms.content.document.service.CategoryService;
import com.ewcms.content.document.util.search.ExtractKeywordAndSummary;
import com.ewcms.content.history.model.HistoryModel;
import com.ewcms.content.history.service.HistoryModelService;
import com.ewcms.content.history.util.HistoryUtil;
import com.ewcms.security.acl.annotation.AclEnum;
import com.ewcms.security.acl.annotation.ChannelAcl;
import com.ewcms.security.service.ServiceException;
import com.ewcms.util.EwcmsContextUtil;
import com.ewcms.util.HtmlStringUtil;
import com.ewcms.web.QueryParameter;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/document/article")
public class ArticleMainController {

	private final static String SUCCESS = "成功";
	private final static String FAILURE = "失败";
	
	@Autowired
	private ArticleMainService articleMainService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private HistoryModelService historyModelService;
	
	@ModelAttribute
	public void init(Model model) {
		model.addAttribute("statusMap", Article.Status.values());
		model.addAttribute("genreMap", Article.Genre.values());
		model.addAttribute("categoryMap", categoryService.findCategoryAll());
	}
	
	@RequestMapping(value = "/tree")
	public String tree(){
		return "content/document/article/tree";
	}
	
	@RequestMapping(value = "/index/{channelId}")
	public String index(@PathVariable(value = "channelId")Long channelId, Model model){
		model.addAttribute("channelId", channelId);
		return "content/document/article/index";
	}
	
	@RequestMapping(value = "/query/{channelId}")
	public @ResponseBody
	Map<String, Object> query(@PathVariable(value = "channelId")Long channelId, @ModelAttribute QueryParameter params) {
		return articleMainService.search(channelId, params);
	}
	
	@ChannelAcl(acl = { AclEnum.DELETE_ARTICLE }, position = 0)
	@RequestMapping(value = "/delete/{channelId}")
	public String remove(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections")List<Long> selections){
		for (Long articleMainId : selections){
			articleMainService.delArticleMainToRecycleBin(articleMainId, channelId);
		}
		return "redirect:/content/document/article/index/" + channelId;
	}
	
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/edit/{channelId}",method = RequestMethod.GET)
	public String edit(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "articleMainId", required = false) Long articleMainId, @RequestParam(value="mask") Integer mask, Model model) {
		Article article = new Article();
		Integer pages = 1;
		if (articleMainId == null) {
			List<Content> contents = new ArrayList<Content>();
			Content content = new Content();
			content.setDetail("");
			contents.add(content);
			article.setContents(contents);
		} else {
			ArticleMain articleMain = articleMainService.findArticleMainByArticleMainAndChannel(articleMainId, channelId);
			article = articleMain.getArticle();
			List<Content> contents = articleMain.getArticle().getContents();
			if (contents != null && !contents.isEmpty()){
				pages = contents.size();
			}
			model.addAttribute("articleMainId", articleMain.getId());
		}
		model.addAttribute("mask", mask);
		model.addAttribute("pages", pages);
		model.addAttribute("channelId", channelId);
		model.addAttribute("article", article);
		return "content/document/article/edit";
	}

	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 3)
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "article")Article article, @RequestParam(value = "categoryList", required = false) List<Long> categoryList, @RequestParam(value = "articleMainId", required = false)Long articleMainId, @RequestParam(value = "channelId") Long channelId, @RequestParam(value = "mask") Integer mask, RedirectAttributes redirectAttributes){
		try{
			if (isNotNull(categoryList)) {
				List<Category> articleCategories = new ArrayList<Category>();
				Category category = null;
				for (Long categoryId : categoryList) {
					category = categoryService.findCategory(categoryId);
					if (category == null)
						continue;
					articleCategories.add(category);
				}
				article.setCategories(articleCategories);
			}
			
			if (isStringEmpty(article.getAuthor())){
				article.setAuthor(EwcmsContextUtil.getRealName());
			}
			
			if (article.getId() != null && StringUtils.hasText(article.getId().toString())){
				articleMainId = articleMainService.updArticleMain(article, articleMainId, channelId, article.getPublished());
			}else{
				articleMainId = articleMainService.addArticleMain(article, channelId, article.getPublished());
			}
			redirectAttributes.addFlashAttribute("message", "文章保存成功!");
		}catch(ServiceException e){
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/document/article/edit/" + channelId + "?articleMainId=" + articleMainId + "&mask=" + mask;	
	}
	
	/**
	 * 文章审核
	 * 
	 * @param channelId 频道编号
	 * @param selections 文章主体编号
	 * @return Boolean
	 */
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/approve/{username}_{channelId}")
	public @ResponseBody Boolean approve(@PathVariable(value = "username")String username, @PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections")List<Long> selections){
		Boolean result = false;
		try {
			articleMainService.submitReviewArticleMain(username, selections, channelId);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}
	
	@ChannelAcl(acl = { AclEnum.PUBLISH_ARTICLE }, position = 0)
	@RequestMapping(value = "/pubArticle/{channelId}")
	public String pubArticle(@PathVariable(value = "channelId")Long channelId, RedirectAttributes redirectAttributes){
		String message = "独立发布文章";
		try{
			articleMainService.pubArticleMainByChannel(channelId, false, false);
			message += SUCCESS;
		}catch(Exception e){
			message += FAILURE;
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/content/document/articlemain/index/" + channelId;
	}
	
	@ChannelAcl(acl = { AclEnum.PUBLISH_ARTICLE }, position = 0)
	@RequestMapping(value = "/associateRelease/{channelId}")
	public String associateRelease(@PathVariable(value = "channelId")Long channelId, RedirectAttributes redirectAttributes){
		String message = "关联发布文章";
		try{
			articleMainService.associateRelease(channelId);
			message += SUCCESS;
		}catch(Exception e){
			message += FAILURE;
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/content/document/articlemain/index/" + channelId;
	}
	
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/copy/{channelId}")
	public @ResponseBody Boolean copy(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections")List<Long> selections, @RequestParam(value = "targetChannelIds") List<Long> targetChannelIds){
		Boolean result = false;
		try{
			result = articleMainService.copyArticleMainToChannel(selections, targetChannelIds, channelId);
		}catch(Exception e){
		}
		return result;
	}
	
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/move/{channelId}")
	public @ResponseBody Boolean move(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections") List<Long> selections, @RequestParam(value = "targetChannelIds") List<Long> targetChannelIds){
		Boolean result = false;
		try{
			result = articleMainService.moveArticleMainToChannel(selections, targetChannelIds, channelId);
		}catch(Exception e){
		}
		return result;
	}
	
	@ChannelAcl(acl = { AclEnum.VERIFY_ARTICLE }, position = 0)
	@RequestMapping(value = "/approveArticle/{username}_{channelId}")
	public @ResponseBody Boolean approveArticle(@PathVariable(value = "username")String username, @PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections") List<Long> selections, @RequestParam(value = "review") Integer review, @RequestParam(value = "reason", required = false) String reason){
		Boolean result = false;
		try{
			articleMainService.approveArticleMain(username, selections.get(0), channelId, review, reason);
			result = true;
		}catch(Exception e){
		}
		return result;
	}
	
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/sort/{channelId}")
	public @ResponseBody Boolean sortArticle(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections")List<Long> selections, @RequestParam(value = "sort")Long sort, @RequestParam(value = "isTop")Boolean isTop, @RequestParam(value = "isInsert")Integer isInsert){
		Boolean result = false;
		try{
			articleMainService.moveArticleMainSort(selections.get(0), channelId, sort, isInsert, isTop);
			result = true;
		}catch(Exception e){
		}
		return result;
	}
	
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/isSort/{channelId}")
	public @ResponseBody Boolean isSortArticle(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "sort")Long sort, @RequestParam(value = "isTop")Boolean isTop){
		Boolean isSort = false;
		try{
			isSort = articleMainService.findArticleMainByChannelAndEqualSort(channelId, sort, isTop);
		}catch(Exception e){
		}
		return isSort;
	}
	
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/clearSort/{channelId}")
	public @ResponseBody Boolean clearSortArticle(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections")List<Long> selections){
		Boolean result = false;
		try{
			articleMainService.clearArticleMainSort(selections, channelId);
			result = true;
		}catch(Exception e){
		}
		return result;
	}
	
	@ChannelAcl(acl = { AclEnum.PUBLISH_ARTICLE }, position = 0)
	@RequestMapping(value = "/break/{username}_{channelId}")
	public String breakArticle(@PathVariable(value = "username")String username, @PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections")List<Long> selections, RedirectAttributes redirectAttributes){
		String message = "文章退回到重新编辑状态";
		try{
			articleMainService.breakArticleMain(username, selections, channelId);
			message += SUCCESS;
		}catch(Exception e){
			message += FAILURE;
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/content/document/articlemain/index/" + channelId;
	}
	
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/top/{channelId}")
	public @ResponseBody Boolean topArticle(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections")List<Long> selections, @RequestParam(value = "isTop")Boolean isTop){
		Boolean result = false;
		try{
			articleMainService.topArticleMain(selections, isTop);
			result = true;
		}catch(Exception e){
		}
		return result;
	}
	
	@ChannelAcl(acl = { AclEnum.WRITER_ARTICLE }, position = 0)
	@RequestMapping(value = "/share/{channelId}")
	public @ResponseBody Boolean shareArticle(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections")List<Long> selections, @RequestParam(value = "isShare")Boolean isShare){
		Boolean result = false;
		try{
			articleMainService.shareArticleMain(selections, isShare);
			result = true;
		}catch(Exception e){
		}
		return result;
	}
	
	@RequestMapping(value = "/reviewEffective/{channelId}")
	public @ResponseBody Boolean reviewEffective(@PathVariable(value = "channelId")Long channelId, @RequestParam(value = "selections")List<Long> selections){
		Boolean isEffective = false;
		try{
			isEffective = articleMainService.reviewArticleMainIsEffective(selections.get(0), channelId);
		}catch(Exception e){
		}
		return isEffective;
	}
	
	@RequestMapping(value = "/keyword")
	public @ResponseBody String keyword(@RequestParam(value = "title") String title, @RequestParam(value = "contents") String contents) {
		if (title != null && title.length() > 0 && contents != null && contents.length() > 0) {
			return HtmlStringUtil.join(ExtractKeywordAndSummary.getKeyword(title + " " + contents), " ");
		}else{
			return "";
		}
	}

	@RequestMapping(value = "/summary")
	public @ResponseBody String summary(@RequestParam(value = "title") String title, @RequestParam(value = "contents") String contents) {
		if (title != null && title.length() > 0 && contents != null && contents.length() > 0) {
			return ExtractKeywordAndSummary.getTextAbstract(title, contents);
		}else{
			return "";
		}
	}

	@RequestMapping(value = "/history/index/{articleId}")
	public String historyArticle(@PathVariable(value = "articleId") Long articleId, Model model){
		model.addAttribute("articleId", articleId);
		return "content/document/article/history";
	}
	
	@RequestMapping(value = "/history/query/{articleId}")
	public @ResponseBody Map<String, Object> queryHistory(@ModelAttribute QueryParameter params, @PathVariable(value = "articleId") Long articleId){
		return articleMainService.searchArticleHistory(articleId, params);
	}
	
	@RequestMapping(value = "/history/restore/{historyId}")
	public @ResponseBody List<String> restoreArticleHistory(@PathVariable(value = "historyId")Long historyId){
		HistoryModel historyModel = historyModelService.findByHistoryModel(historyId);
		Object obj = HistoryUtil.conversion(historyModel.getModelObject());
		List<String> details = new ArrayList<String>();
		if (obj != null) {
			Article article = (Article) obj;
			List<Content> contents = article.getContents();
			if (contents != null && contents.size() > 0) {
				for (int i = 0; i < contents.size(); i++) {
					details.add(contents.get(i).getDetail());
				}
			}
		}
		return details;
	}
	
	@RequestMapping(value = "/vote")
	public String vote(){
		return "content/document/article/vote";
	}
}
