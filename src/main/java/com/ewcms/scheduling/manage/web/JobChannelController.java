package com.ewcms.scheduling.manage.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ewcms.plugin.BaseException;
import com.ewcms.scheduling.generate.job.channel.EwcmsJobChannelFac;
import com.ewcms.scheduling.generate.job.channel.model.EwcmsJobChannel;
import com.ewcms.scheduling.manage.util.ConversionUtil;
import com.ewcms.scheduling.manage.vo.PageDisplay;
import com.ewcms.site.model.Channel;
import com.ewcms.site.service.ChannelService;

/**
 * @author 吴智俊
 */
@Controller
@RequestMapping(value = "/scheduling/job/channel")
public class JobChannelController {

	@Autowired
	private EwcmsJobChannelFac ewcmsJobChannelFac;
	@Autowired
	private ChannelService channelService;

	@RequestMapping(value = "/edit/{channelId}")
	public String edit(@PathVariable(value = "channelId") Long channelId, Model model) {
		EwcmsJobChannel jobChannel = ewcmsJobChannelFac.findJobChannelByChannelId(channelId);
		PageDisplay pageDisplay = new PageDisplay();
		if (jobChannel != null) {
			pageDisplay = ConversionUtil.constructPage(jobChannel);
			pageDisplay.setChannelId(channelId);
			pageDisplay.setSubChannel(jobChannel.getSubChannel());
		} else {
			Channel channel = channelService.getChannel(channelId);
			if (channel != null) {
				pageDisplay.setLabel(channel.getName());
				pageDisplay.setChannelId(channelId);
			}
		}
		model.addAttribute("pageDisplay", pageDisplay);
		return "scheduling/jobinfo/edit_channel";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("pageDisplay") PageDisplay pageDisplay, @RequestParam(required = false) List<Long> selections, Model model,
			RedirectAttributes redirectAttributes) {
		try {
			Long jobId = ewcmsJobChannelFac.saveOrUpdateJobChannel(pageDisplay.getChannelId(), pageDisplay, pageDisplay.getSubChannel());
			if (jobId == null){
				redirectAttributes.addFlashAttribute("message", "操作失败");
			}
			redirectAttributes.addFlashAttribute("message", "操作成功");
		} catch (BaseException e) {
			redirectAttributes.addFlashAttribute("message", "操作失败");
		}
		return "redirect:/scheduling/job/channel/edit/" + pageDisplay.getChannelId();
	}
}
