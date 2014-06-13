package com.ewcms.content.document.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.content.document.model.Category;
import com.ewcms.content.document.service.ArticleMainService;
import com.ewcms.content.document.service.CategoryService;
import com.ewcms.security.service.ServiceException;
import com.ewcms.web.QueryParameter;
import com.ewcms.web.vo.ComboBox;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/content/document/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ArticleMainService articleMainService;

	@RequestMapping(value = "/index")
	public String index() {
		return "content/document/category/index";
	}

	@RequestMapping(value = "/query")
	public @ResponseBody
	Map<String, Object> query(@ModelAttribute QueryParameter params) {
		return categoryService.search(params);
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(@RequestParam(required = false) List<Long> selections, Model model) {
		Category category = new Category();
		if (selections == null || selections.isEmpty()) {
			model.addAttribute("selections", new ArrayList<Long>(0));
		} else {
			category = categoryService.findCategory(selections.get(0));
			model.addAttribute("selections", selections);
		}
		model.addAttribute("category", category);
		return "content/document/category/edit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute(value = "category") Category category, @RequestParam(required = false) List<Long> selections, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Boolean close = Boolean.FALSE;
			if (category.getId() != null && StringUtils.hasText(category.getId().toString())) {
				categoryService.updCategory(category);
				selections.remove(0);
				if (selections == null || selections.isEmpty()) {
					close = Boolean.TRUE;
				} else {
					category = categoryService.findCategory(selections.get(0));
					model.addAttribute("category", category);
					model.addAttribute("selections", selections);
				}
			} else {
				categoryService.addCategory(category);
				selections = selections == null ? new ArrayList<Long>() : selections;
				selections.add(0, category.getId());
				model.addAttribute("category", new Category());
				model.addAttribute("selections", selections);
			}
			model.addAttribute("close", close);
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "content/document/category/edit";
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST, RequestMethod.GET })
	public String remove(@RequestParam("selections") List<Long> selections, RedirectAttributes redirectAttributes) {
		try {
			for (Long id : selections) {
				categoryService.delCategory(id);
			}
		} catch (ServiceException e) {
			redirectAttributes.addFlashAttribute("message", e.getMessage());
		}
		return "redirect:/content/document/category/index";
	}

	@RequestMapping(value = "/all")
	public @ResponseBody List<ComboBox> all(@RequestParam(value = "articleId", required = false) Long articleId) {
		List<ComboBox> comboBoxs = new ArrayList<ComboBox>();

		List<Category> categories = categoryService.findCategoryAll();
		if (categories != null) {
			ComboBox comboBox = null;
			for (Category category : categories) {
				comboBox = new ComboBox();
				comboBox.setId(category.getId());
				comboBox.setText(category.getCategoryName());
				if (articleId != null) {
					Boolean isEntity = articleMainService.findArticleIsEntityByArticleAndCategory(articleId, category.getId());
					if (isEntity)
						comboBox.setSelected(true);
				}
				comboBoxs.add(comboBox);
			}
		}
		
		return comboBoxs;
	}
}
