package com.ewcms.visit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ewcms.site.dao.ChannelDao;
import com.ewcms.site.model.Channel;
import com.ewcms.site.service.ChannelService;
import com.ewcms.util.EmptyUtil;
import com.ewcms.visit.dao.VisitDao;
import com.ewcms.visit.dao.clickrate.SearchEngineDao;
import com.ewcms.visit.dao.clickrate.SourceFormDao;
import com.ewcms.visit.dao.clickrate.WebSiteDao;
import com.ewcms.visit.dao.clientside.ClientSideDao;
import com.ewcms.visit.dao.loyalty.AccessDepthDao;
import com.ewcms.visit.dao.loyalty.AccessFrequencyDao;
import com.ewcms.visit.dao.totality.EntryDao;
import com.ewcms.visit.dao.totality.HostDao;
import com.ewcms.visit.dao.totality.OnlineDao;
import com.ewcms.visit.dao.totality.RegionDao;
import com.ewcms.visit.dao.totality.SummaryDao;
import com.ewcms.visit.dao.traffic.ArticleClickDao;
import com.ewcms.visit.dao.traffic.ChannelClickDao;
import com.ewcms.visit.dao.traffic.UrlClickDao;
import com.ewcms.visit.model.Visit;
import com.ewcms.visit.model.VisitPk;
import com.ewcms.visit.model.clickrate.SearchEngine;
import com.ewcms.visit.model.clickrate.SearchEnginePk;
import com.ewcms.visit.model.clickrate.SourceForm;
import com.ewcms.visit.model.clickrate.SourceFormPk;
import com.ewcms.visit.model.clickrate.WebSite;
import com.ewcms.visit.model.clickrate.WebSitePk;
import com.ewcms.visit.model.clientside.ClientSide;
import com.ewcms.visit.model.clientside.ClientSidePk;
import com.ewcms.visit.model.clientside.ClientSidePk.ClientSideType;
import com.ewcms.visit.model.loyalty.AccessDepth;
import com.ewcms.visit.model.loyalty.AccessDepthPk;
import com.ewcms.visit.model.loyalty.AccessFrequency;
import com.ewcms.visit.model.loyalty.AccessFrequencyPk;
import com.ewcms.visit.model.totality.Entry;
import com.ewcms.visit.model.totality.EntryPk;
import com.ewcms.visit.model.totality.Host;
import com.ewcms.visit.model.totality.HostPk;
import com.ewcms.visit.model.totality.Online;
import com.ewcms.visit.model.totality.OnlinePk;
import com.ewcms.visit.model.totality.Region;
import com.ewcms.visit.model.totality.RegionPk;
import com.ewcms.visit.model.totality.SummaryPk;
import com.ewcms.visit.model.totality.Summary;
import com.ewcms.visit.model.traffic.ArticleClick;
import com.ewcms.visit.model.traffic.ArticleClickPk;
import com.ewcms.visit.model.traffic.ChannelClick;
import com.ewcms.visit.model.traffic.ChannelClickPk;
import com.ewcms.visit.model.traffic.UrlClick;
import com.ewcms.visit.model.traffic.UrlClickPk;
import com.ewcms.visit.util.DateTimeUtil;
import com.ewcms.visit.util.SourceUtil;
import com.ewcms.visit.util.VisitUtil;

/**
 * 
 * @author wu_zhijun
 *
 */
@Component("visitService")
public class VisitService {
	
	@Autowired
	private VisitDao visitDao;
	@Autowired
	private ChannelDao channelDao;
	@Autowired
	private SummaryDao summaryDao;
	@Autowired
	private EntryDao entryDao;
	@Autowired
	private HostDao hostDao;
	@Autowired
	private RegionDao regionDao;
	@Autowired
	private OnlineDao onlineDao;
	@Autowired
	private ChannelClickDao channelClickDao;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ArticleClickDao articleClickDao;
	@Autowired
	private UrlClickDao urlClickDao;
	@Autowired
	private AccessDepthDao accessDepthDao;
	@Autowired
	private AccessFrequencyDao accessFrequencyDao;
	@Autowired
	private SourceFormDao sourceFormDao;
	@Autowired
	private SearchEngineDao searchEngineDao;
	@Autowired
	private WebSiteDao webSiteDao;
	@Autowired
	private ClientSideDao clientSideDao;
	
	public void addVisitByLoadEvent(Visit visit) {
		if (visit == null) return;
		if (visit.getVisitPk() == null) return;
		
		Visit dbVisit = visitDao.findOne(visit.getVisitPk());
		
		if (dbVisit == null) {
			visit.setPageView(1L);
			visit.setDepth(findVisitDepth(1L, visit.getChannelId()));
			
			visitDao.save(visit);
			
			saveOtherTable(true, visit);
		} else {
			dbVisit.setPageView(dbVisit.getPageView() + 1);
			dbVisit.setRvFlag(visit.getRvFlag());
			
			visitDao.save(dbVisit);
			
			saveOtherTable(false, dbVisit);
		}
		
	}
	
	public void addVisitByKeepAliveEvent(Visit visit) {
		if (visit == null) return;
		if (visit.getVisitPk() == null) return;
		
		Visit dbVisit = visitDao.findOne(visit.getVisitPk());
		
		if (dbVisit != null){
			dbVisit.setStickTime(visit.getStickTime() + dbVisit.getStickTime());
			dbVisit.setDepth(findVisitDepth(1L, visit.getChannelId()));
			visitDao.save(dbVisit);
			
			saveOtherTable(false, dbVisit);
		}
	}
	
	public void addVisitByUnloadEvent(Visit visit){
		if (visit == null) return;
		if (visit.getVisitPk() == null) return;
		
		Visit dbVisit = visitDao.findOne(visit.getVisitPk());
		
		if (dbVisit != null){
			dbVisit.setEvent(VisitUtil.UNLOAD_EVENT);
			dbVisit.setDepth(findVisitDepth(1L, visit.getChannelId()));
			visitDao.save(dbVisit);
			
			saveOtherTable(false, dbVisit);
		}
	}
	
	private void saveOtherTable(Boolean isNew, Visit visit){
		saveSummary(visit);
		saveEntry(visit);
		saveRegion(visit);
		saveOnline(visit);
		saveChannelClick(visit);
		saveArticleClick(visit);
		saveDepth(visit);
		
		if (isNew){
			saveHost(visit);
			saveUrlClick(visit);
			saveFrequency(visit);
			saveSourceFormAndSearchEngine(visit);
			saveClientSide(visit);
		}
	}
	
	private Long findVisitDepth(Long depth, Long channelId){
		if (channelId == null) return depth; 
		Channel channel = channelDao.findOne(channelId);
		if (channel == null)  return depth;
		Channel parent = channel.getParent();
		if (parent == null) return depth; 
		depth = depth + 1;
		return findVisitDepth(depth, parent.getId());
	}
	
	private void saveSummary(Visit visit){
		VisitPk pk = visit.getVisitPk();

		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		Integer hour = DateTimeUtil.getHour(visit.getVisitTime());
		
		Summary summary = summaryDao.findSummary(visitDate, hour, siteId);
		if (summary != null){
			SummaryPk summaryPk = new SummaryPk(visitDate, hour, siteId);
			summary.setSummaryPk(summaryPk);
			summaryDao.save(summary);
		}
	}
	
	private void saveEntry(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		String url = pk.getUrl();
		String event = visit.getEvent();
		
		if (EmptyUtil.isStringEmpty(event)){
			event = "";
		}else{
			event = VisitUtil.UNLOAD_EVENT;
		}
		
		Entry entry = entryDao.findEntry(visitDate, siteId, url, event);
		if (entry != null){
			EntryPk entryPk = new EntryPk(visitDate, siteId, url, event);
			entry.setEntryPk(entryPk);
			entryDao.save(entry);
		}
	}
	
	private void saveHost(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		String host = visit.getHost();
		
		Host entity = hostDao.findHost(visitDate, siteId, host);
		if (entity != null){
			HostPk hostPk = new HostPk(visitDate, siteId, host);
			entity.setHostPk(hostPk);
			hostDao.save(entity);
		}
	}
	
	private void saveRegion(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		String country = visit.getCountry();
		String province = visit.getProvince();
		String city = visit.getCity();
		
		Region region = regionDao.findRegion(visitDate, siteId, country, province, city);
		if (region != null){
			RegionPk regionPk = new RegionPk(visitDate, siteId, country, province, city);
			region.setRegionPk(regionPk);
			regionDao.save(region);
		}
	}
	
	private void saveOnline(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		Integer hour = DateTimeUtil.getHour(visit.getVisitTime());
		
		Online online = onlineDao.findOnline(visitDate, siteId, hour);
		if (online != null){
			OnlinePk onlinePk = new OnlinePk(visitDate, hour, siteId);
			online.setOnlinePk(onlinePk);
			onlineDao.save(online);
		}
	}
	
	private void saveChannelClick(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		Long channelId = visit.getChannelId();
		
		channelId = channelService.findRootChannelId(channelId, siteId);
		ChannelClick channelClick = channelClickDao.findChannelClick(visitDate, siteId, channelId);
		if (channelClick != null){
			ChannelClickPk channelClickPk = new ChannelClickPk(visitDate, siteId, channelId);
			channelClick.setChannelClickPk(channelClickPk);
			channelClickDao.save(channelClick);
		}
	}
	
	private void saveArticleClick(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Long articleId = visit.getArticleId();
		if (articleId == null || articleId.longValue() == 0) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		Long channelId = visit.getChannelId();
		
		ArticleClick articleClick = articleClickDao.findArticleClick(visitDate, siteId, channelId, articleId);
		if (articleClick != null){
			ArticleClickPk articleClickPk = new ArticleClickPk(visitDate, siteId, channelId, articleId);
			articleClick.setArticleClickPk(articleClickPk);
			articleClickDao.save(articleClick);
		}
	}
	
	private void saveUrlClick(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		String url = pk.getUrl();
		if (EmptyUtil.isStringEmpty(url)) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();

		UrlClick urlClick = urlClickDao.findUrlClick(visitDate, siteId, url);
		if (urlClick != null){
			UrlClickPk urlClickPk = new UrlClickPk(visitDate, siteId, url);
			urlClick.setUrlClickPk(urlClickPk);
			urlClickDao.save(urlClick);
		}
	}
	
	private void saveDepth(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		Long depth = visit.getDepth();
		if (depth == null) return;
		
		AccessDepth accessDepth = accessDepthDao.findDepth(visitDate, siteId, depth);
		if (accessDepth != null){
			AccessDepthPk accessDepthPk = new AccessDepthPk(visitDate, siteId, depth);
			accessDepth.setAccessDepthPk(accessDepthPk);
			accessDepthDao.save(accessDepth);
		}
	}
	
	private void saveFrequency(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		Long frequency = visit.getFrequency();
		if (frequency == null) return;
		
		AccessFrequency accessFrequency = accessFrequencyDao.findFrequency(visitDate, siteId, frequency);
		if (accessFrequency != null){
			AccessFrequencyPk accessFrequencyPk = new AccessFrequencyPk(visitDate, siteId, frequency);
			accessFrequency.setAccessFrequencyPk(accessFrequencyPk);
			accessFrequencyDao.save(accessFrequency);
		}
	}
	
	private void saveSourceFormAndSearchEngine(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		
		SourceFormPk sourceFormPk = new SourceFormPk(visitDate, siteId);
		SourceForm sourceForm = sourceFormDao.findOne(sourceFormPk);
		if (sourceForm == null) sourceForm = new SourceForm(sourceFormPk);
		String referer = visit.getReferer();
		if (EmptyUtil.isStringEmpty(referer)){
			sourceForm.setDirectCount(sourceForm.getDirectCount() + 1);
		}else if (EmptyUtil.isNotNull(SourceUtil.getDomainName(referer))){
			sourceForm.setSearchCount(sourceForm.getSearchCount() + 1);
			
			String engineName = SourceUtil.getDomainName(referer);
			if (engineName != null){
				SearchEnginePk searchEnginePk = new SearchEnginePk(visitDate, siteId, engineName);
				SearchEngine searchEngine = searchEngineDao.findOne(searchEnginePk);
				if (searchEngine == null) searchEngine = new SearchEngine(searchEnginePk);
				searchEngine.setEngineCount(searchEngine.getEngineCount() + 1);
				searchEngineDao.save(searchEngine);
			}
		}else{
			sourceForm.setOtherCount(sourceForm.getOtherCount() + 1);
			
			String webSiteName = SourceUtil.getWebSiteUrl(referer);
			if (webSiteName != null){
				WebSitePk webSitePk = new WebSitePk(visitDate, siteId, webSiteName);
				WebSite webSite = webSiteDao.findOne(webSitePk);
				if (webSite == null) webSite = new WebSite(webSitePk);
				webSite.setWebSiteCount(webSite.getWebSiteCount() + 1);
				webSiteDao.save(webSite);
			}
		}
		sourceFormDao.save(sourceForm);
	}
	
	private void saveClientSide(Visit visit){
		VisitPk pk = visit.getVisitPk();
		if (pk == null) return;
		Date visitDate = pk.getVisitDate();
		Long siteId = pk.getSiteId();
		
		List<ClientSide> clientSides = new ArrayList<ClientSide>();
		
		String osName = visit.getOs();
		ClientSidePk osPk = new ClientSidePk(visitDate, siteId, ClientSideType.OS, osName);
		ClientSide os = clientSideDao.findOne(osPk);
		if (os == null) os = new ClientSide(osPk);
		os.setClientSideCount(os.getClientSideCount() + 1);
		clientSides.add(os);
		
		String browserName = visit.getBrowser();
		ClientSidePk browserPk = new ClientSidePk(visitDate, siteId, ClientSideType.BROWSER, browserName);
		ClientSide browser = clientSideDao.findOne(browserPk);
		if (browser == null) browser = new ClientSide(browserPk);
		browser.setClientSideCount(browser.getClientSideCount() + 1);
		clientSides.add(browser);
		
		String languageName = visit.getLanguage();
		ClientSidePk languagePk = new ClientSidePk(visitDate, siteId, ClientSideType.LANGUAGE, languageName);
		ClientSide language = clientSideDao.findOne(languagePk);
		if (language == null) language = new ClientSide(languagePk);
		language.setClientSideCount(language.getClientSideCount() + 1);
		clientSides.add(language);
		
		String screenName = visit.getScreen();
		ClientSidePk screenPk = new ClientSidePk(visitDate, siteId, ClientSideType.SCREEN, screenName);
		ClientSide screen = clientSideDao.findOne(screenPk);
		if (screen == null) screen = new ClientSide(screenPk);
		screen.setClientSideCount(screen.getClientSideCount() + 1);
		clientSides.add(screen);
		
		String colorDepthName = visit.getColorDepth();
		ClientSidePk colorDepthPk = new ClientSidePk(visitDate, siteId, ClientSideType.COLORDEPTH, colorDepthName);
		ClientSide colorDepth = clientSideDao.findOne(colorDepthPk);
		if (colorDepth == null) colorDepth = new ClientSide(colorDepthPk);
		colorDepth.setClientSideCount(colorDepth.getClientSideCount() + 1);
		clientSides.add(colorDepth);
		
		String javaEnabledName = "支持Applet";
		if (!visit.getJavaEnabled()){
			javaEnabledName = "不" + javaEnabledName;
		}
		ClientSidePk javaEnabledPk = new ClientSidePk(visitDate, siteId, ClientSideType.JAVAENABLED, javaEnabledName);
		ClientSide javaEnabled = clientSideDao.findOne(javaEnabledPk);
		if (javaEnabled == null) javaEnabled = new ClientSide(javaEnabledPk);
		javaEnabled.setClientSideCount(javaEnabled.getClientSideCount() + 1);
		clientSides.add(javaEnabled);
		
		String flashVersionName = visit.getFlashVersion();
		ClientSidePk flashVersionPk = new ClientSidePk(visitDate, siteId, ClientSideType.FLASHVERSION, flashVersionName);
		ClientSide flashVersion = clientSideDao.findOne(flashVersionPk);
		if (flashVersion == null) flashVersion = new ClientSide(flashVersionPk);
		flashVersion.setClientSideCount(flashVersion.getClientSideCount() + 1);
		clientSides.add(flashVersion);
		
		String cookieEnabledName = "允许Cookies";
		if (!visit.getCookieEnabled()){
			cookieEnabledName = "不" + cookieEnabledName;
		}
		ClientSidePk cookieEnabledPk = new ClientSidePk(visitDate, siteId, ClientSideType.COOKIEENABLED, cookieEnabledName);
		ClientSide cookieEnabled = clientSideDao.findOne(cookieEnabledPk);
		if (cookieEnabled == null) cookieEnabled = new ClientSide(cookieEnabledPk);
		cookieEnabled.setClientSideCount(cookieEnabled.getClientSideCount() + 1);
		clientSides.add(cookieEnabled);
		
		clientSideDao.save(clientSides);
	}
}
