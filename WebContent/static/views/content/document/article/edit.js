var pages = 1;
var currentPage = 1;
var loginName = '';
var insertResourceUrl='';
var voteUrl='';
var mask = 0;

var ArticleEdit = function(options){
	this._options = options;
};

ArticleEdit.prototype.init = function(){
	var options = this._options;
	
	mask = options.mask;
	pages = options.pages;
	currentPage = options.currentPage;
	loginName = options.loginName;
	insertResourceUrl = options.insertResourceUrl;
	voteUrl = options.voteUrl;
	
	if (mask < 4){
		$('#tb-approve').linkbutton('disable');
	}else{
		$('#tb-approve').linkbutton('enable');
	}
	
	window.top.$('#open_article_window_top').panel('setTitle', '文档编辑：' + $('#title').val());
	
	//设置自动保存时长
	//setInterval('ArticleEdit.autoSave(options.saveUrl)', 600000);
	
	ArticleEdit.changeType($('#genre').val());
	
	//初始化页数显示
	for (var i = 1; i < options.pages; i++){
		$('#pageList').append(ArticleEdit.getLiHtml(i + 1));
	}
	
	//标题
	ArticleEdit.showToolbar('title', 'tdTitle', 'FontColor,Bold,Italic,UnderLine,FontFamily,FontSize');
	//短标题
	ArticleEdit.showToolbar('shortTitle', 'tdShortTitle', 'FontColor,Bold,Italic,UnderLine,FontFamily,FontSize');
	//副标题
	ArticleEdit.showToolbar('subTitle', 'tdSubTitle', 'FontColor,Bold,Italic,UnderLine,FontFamily,FontSize');
	
	//短标题不为空，设置短标题选项选中并显示短标题内容
	ArticleEdit.showTitle('shortTitle', 'ShortTitle');
	//副标题不为空，设置副标题选项选中并显示副标题内容
	ArticleEdit.showTitle('subTitle', 'SubTitle');
	
	//读取cookies操作
	for (var i=1;i<=5;i++){
		var ewcms_cookies = $.cookie('ewcms_' + i + '_' + options.loginName);
		if (ewcms_cookies != null){
			$('#ewcms_' + i).attr('checked',true);
			$('#trShowHide_' + i).show();
		}else{
			$('#ewcms_' + i).attr('checked',false);
			$('#trShowHide_' + i).hide();
		}
	}
	
	$('#cc_categories').combobox({
		url: options.categoryUrl,
		valueField:'id',
		textField:'text',
		editable:false,
		multiple:true,
		cascadeCheck:false,
		panelWidth:200
	});
	
	$('#cc_channel').combotree({  
	    url:options.treeUrl,
	    value:$('#channelId').val(), // the initialized value
	    onLoadSuccess:function(){
	    	//$('#cc_channel').combotree('setValue', {id:$('#channelId').val(),text:'图片报道'});
	    },
	    onClick : function(node){
	    	var rootnode = $('#cc_channel').combotree('tree').tree('getRoot');
	    	if (node.id == rootnode.id){
	    		var channelId = $('#channelId').val();
	    		$('#cc_channel').combotree('setValue', channelId);
	    		$.messager.alert('提示', '不能选择根频道', 'info');
				return;
	    	}
	    	$('#channelId').attr('value', node.id);
	    }
	});
	$('#cc_channel').combotree('setValue', $('#channelId').val());
	
	$('#genre').combobox({
		onChange:function(newValue, oldValue){
			ArticleEdit.changeType(newValue);
		}
	});
	
	ArticleEdit.initCookies(options.loginName);
	ArticleEdit.initChannel();
	
	$(window).bind('resize', function(){
		ArticleEdit.windowResize();
	});
	
	//设置短标题选项点击事件
	$('#ShowShortTitle').bind('click', function(){
		if ($('#ShowShortTitle').attr('checked') == 'checked') {
			$('#trShortTitle').show();
		} else {
			$('#trShortTitle').hide();
		}
		ArticleEdit.windowResize();
	});
	//设置副标题选项点击事件
	$('#ShowSubTitle').bind('click', function() {
		if ($('#ShowSubTitle').attr('checked') == 'checked') {
			$('#trSubTitle').show();
		} else {
			$('#trSubTitle').hide();
		}
		ArticleEdit.windowResize();
	});
	
	$('#tb-create').bind('click', function(){
		if ($('#id').val() == ''){
			$.messager.confirm('提示', '文章尚未保存，确认新建文章？', function(r){
				if (r){
					window.top.$('#open_article_window_top').find('iframe').attr('src', options.editUrl);
				}
			});
		}else{
			window.top.$('#open_article_window_top').find('iframe').attr('src', options.editUrl);
		}
	});
	
	$('#tb-save').bind('click', function(){
		if ($.trim($('#title').val())==''){
			$.messager.alert('提示','文章标题不能为空','info');
			return;
		}
		
		var type = $('#genre').val();
		if (type == 'GENERAL'){
			for (var i = 1;i <= pages; i++){
				var contentDetail = tinyMCE.get('_Content_' + i).getContent();
				contentDetail = contentDetail.replace(/<p>\&nbsp;<\/p>/g,'');
				if ($.trim(contentDetail) == ''){
					ArticleEdit.setActivePage(i);
					$.messager.alert('提示','文章内容不能为空!','info');
					return;
				}
				//$('#textAreaContent_' + i).attr('value', contentDetail);
			}
		}else if (type == 'TITLE'){
			var url = $('#url').val();
			if ($.trim(url) == ''){
				$.messager.alert('提示','链接地址不能为空!','info');
				return;
			}
		}
		
		
//		ArticleEdit.loadingEnable();
		if ($('#ShowShortTitle').attr('checked') == false || $.trim($('#shortTitle').val()) == ''){
			$('#shortTitle').attr('value','');
			$('#shortTitleStyle').attr('value','');
		}
		if ($('#ShowSubTitle').attr('checked') == false || $.trim($('#subTitle').val()) == '') {
			$('#subTitle').attr('value','');
			$('#subTitleStyle').attr('value','');
		}
//		var params=$('#articleSave').serialize();
//		$.post(options.saveUrl ,params ,function(data){
//		});
		$('#articleSave').submit();
//		ArticleEdit.loadingDisable();
	});
	
	$('#tb-approve').bind('click', function(){
		if ($('#articleMainId').val() == ''){
			$.messager.alert('提示', '文章未保存之前不能审核', 'info');
			return;
		}
		$.messager.confirm('提示','确定要审核文章吗?',function(r){
			if (r){
				$.post(options.approveUrl, {selections:$('#articleMainId').val()} ,function(data) {
			        if (!data){
			        	$.messager.alert('提示','文章提交审核失败','info');
			        }else{
			        	window.top.$('#open_article_window_top').window('close', false);
			        }
				});
			}
		});
	});
	
	$('#tb-history').bind('click', function(){
		if ($('#genre').val() == 'TITLE'){
			$.messager.alert('提示','标题新闻没有历史记录','info');
			return;
		}
		var articleId = $('#id').val();
		if (articleId == ''){
			$.messager.alert('提示','新增记录没有历史记录','info');
			return;
		}
		$.ewcms.openWindow({windowId:'#pop-window',iframeId:'#editifr_pop', src:options.historyUrl,width:800,height:600,title:'历史内容选择'});
	});
	
	$('#tb-keyword').bind('click', function(){
		if ($('#genre').val() == 'TITLE'){
			$.messager.alert('提示','标题新闻不用提取【关键字】或【摘要】','info');
			return;
		}
		var title = $('#title').val();
		if (title == ''){ 
			$.messager.alert('提示','提取【关键字】或【摘要】时，标题不能为空','info');
			$('#title').focus();
			return;
		}
		
		var contents = '';
		for (var i = 1;i <= pages;i++){
			var contentDetail = tinyMCE.get('_Content_' + i).getContent();
			contents += contentDetail;
	    }
		if (contents=='') {
			$.messager.alert('提示','提取【关键字】或【摘要】时，内容不能为空','info');
			$('#_Content_' + currentPage).focus();
			return;
		}
		
	    var parameter = {};
	    parameter['title'] = title;
	    parameter['contents'] = contents;

		$.post(options.keyWordUrl, parameter ,function(data) {
			if (data != ''){
				$.messager.alert('提示','提取【关键字】成功','info');
				$('#keyword').attr('value',data);
			}else{
				$.messager.alert('提示','提取【关键字】失败,可自行添加','info');
			}
		});
	});
	
	$('#tb-summary').bind('click', function(){
		if ($('#genre').val() == 'TITLE'){
			$.messager.alert('提示','标题新闻不用提取【关键字】或【摘要】','info');
			return;
		}
		var title = $('#title').val();
		if (title == ''){ 
			$.messager.alert('提示','提取【关键字】或【摘要】时，标题不能为空','info');
			$('#title').focus();
			return;
		}
		
		var contents = '';
		for (var i = 1;i <= pages;i++){
			var contentDetail = tinyMCE.get('_Content_' + i).getContent();
			contents += contentDetail;
	    }
		if (contents=='') {
			$.messager.alert('提示','提取【关键字】或【摘要】时，内容不能为空','info');
			$('#_Content_' + currentPage).focus();
			return;
		}
		
	    var parameter = {};
	    parameter['title'] = title;
	    parameter['contents'] = contents;

		$.post(options.summaryUrl, parameter ,function(data) {
			if (data != ''){
				$.messager.alert('提示','提取【摘要】成功','info');
				$('#summary').attr('value',data);
			}else{
				$.messager.alert('提示','提取【摘要】失败,可自行添加','info');
			}
		});
	});
	
	$('#tb-relation').bind('click', function(){
		if ($('#genre').val() == 'TITLE'){
			$.messager.alert('提示','标题新闻没有相关文章','info');
			return;
		}
		var articleId = $('#id').val();
		if (articleId == ''){
			$.messager.alert('提示','在新增状态下不能查看相关文章！','info');
			return;
		}
		$.ewcms.openWindow({windowId:'#pop-window',iframeId:'#editifr_pop',src : options.relationUrl, width:1150,height:600,title:'相关文章选择'});
	});
	
	$('#tb-cookies').bind('click', function(){
		$.ewcms.openWindow({windowId:'#ewcms-cookies',width:300,height:215,title:'设置常用项'});
	});
	
	$('#tb-show').bind('click', function(){
		var showHideLabel_value = $('#showHideLabel').text();
	    if ($.trim(showHideLabel_value) == '展开'){
	        $('#trShowHide_1').show();
	        $('#trShowHide_2').show();
	        $('#trShowHide_3').show();
	        $('#trShowHide_4').show();
	        $('#trShowHide_5').show();
	        //$('#imgShowHide').attr('src', '../../ewcmssource/image/article/hide.gif');
	    	$('#showHideLabel').text('收缩');
	    }else{
	    	for (var i=1;i<=5;i++){
	    		var ewcms_cookies = $.cookie('ewcms_' + i + '_' + loginName);
	    		if (ewcms_cookies != null){
	    			$('#ewcms_' + i).attr('checked',true);
	    			$('#trShowHide_' + i).show();
	    		}else{
	    			$('#ewcms_' + i).attr('checked',false);
	    			$('#trShowHide_' + i).hide();
	    		}
	    	}
	        //$('#imgShowHide').attr('src', '../../ewcmssource/image/article/show.gif');
	    	$('#showHideLabel').text('展开');
	    }
	    ArticleEdit.windowResize();
	});
	
	$('#tb-cancel').bind('click', function(){
		$.messager.confirm('提示','确定要关闭文档编辑器吗?',function(r){
			if (r){
				window.top.$('#open_article_window_top').window('close', false);
			}
		});
	});
	
	$('#tb-referImage').bind('click', function(){
		$('#refence_img').attr('value','true');
		ArticleEdit.openImageWindow(false);
	});
	
	$('#tb-clearimage').bind('click', function(){
		$('#referenceImage').attr('src',options.noImage);
		$('#image').attr('value','');
	});
	
	$('#tb-insertfile').bind('click', function(){
		uploadifr_insert.insert(function(data,success){
			if (success){
				$.each(data, function(index,value){
					if ($('#refence_img').val() == 'true'){
	    				$('#referenceImage').attr('src', value.uri);
	    				$('#image').attr('value', value.uri);
						$('#refence_img').attr('value', 'false');
					}else{
						var html_obj = '';
						var type = value.type;
						if (type=='ANNEX'){
							html_obj = '<a href="' + value.uri + '">' + value.description + '</a>';
						}else if (type=='IMAGE'){
							html_obj = '<p style="text-align: center;"><img border="0" src="' + value.uri + '"/></p><p style="text-align: center;">' + value.description + '</p>';
						}else if (type=='FLASH'){
							html_obj='<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" data="/flvplayer.swf" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" width="480" height="360">' +
						    '<param name="movie" value="flvplayer.swf" />' +
						    '<param name="quality" value="high" />' +
						    '<param name="allowFullScreen" value="true" />' +
						    '<param name="FlashVars" value="vcastr_file=' + value.uri + '&LogoText=www.dean.gov.cn&BufferTime=3" />' +
						    '<embed src="flvplayer.swf" allowfullscreen="true" flashvars="vcastr_file=' + value.uri + '&LogoText=www.dean.gov.cn" quality="high" pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash" width="500" height="400"></embed>' +
						    '</object>';
						}else if (type=='VIDEO'){
							var video_uri = value.uri;
							var extension = video_uri.replace(/^.*(\.[^\.\?]*)\??.*$/,'$1');
							if (extension=='.rm' || extension=='.rmvb'){
								html_obj = writeRealMedia({src:video_uri,width:320,height:240,console:'Clip1',controls:'IMAGEWINDOW,ControlPanel,StatusBar',_ExtentX:11298,_ExtentY:7938,AUTOSTART:0});
							}else if(extension=='.avi' || extension=='.wmv'){
								html_obj = writeWindowsMedia({src:video_uri,width:320,height:240,data:video_uri,autostart:true,controller:true});
							}else if (extension=='.flv'||extension=='.swf'){
								html_obj = writeFlash({src:video_uri,width:480,height:360,movie: 'flvplayer.swf',quality:'high',allowFullScreen:true,flashvars:'vcastr_file=' + video_uri + '&LogoText=www.dean.gov.cn&BufferTime=3'});
							}else if (extension=='.wave'){
								html_obj = writeShockWave({src:video_uri,width:320,height:240});
							}else{
								html_obj = writeQuickTime({src:video_uri,width:320,height:240});
							}
						}
						if (tinyMCE.getInstanceById('_Content_' + currentPage) != null){
							try{
								tinyMCE.execInstanceCommand('_Content_' + currentPage, 'mceInsertContent', false, html_obj);
							}catch(e){
								alert(e);
							}
						}
					}
			   });
			   $('#insert-window').window('close');
			}else{
				$.messager.alert('错误', '插入失败', 'error');
			}
	    });
	});
	
	$('#tb-insertvote').bind('click', function(){
		var rows = editifr_vote.$('#tt').datagrid('getSelections');
		//var host = 'http://' + window.location.host;
		for (var i=0;i<rows.length;i++){
			var html_obj= '<iframe src="/view.vote?id=' + rows[i].id + '" frameborder="0" scrolling="no" style="padding:1px;" width="100%" height="100%" onload="this.height=500"></iframe>';
			if (tinyMCE.getInstanceById('_Content_' + currentPage) != null){
				tinyMCE.execInstanceCommand('_Content_' + currentPage,'mceInsertContent', false, html_obj);
			}
		}
		$('#vote-window').window('close');
	});
};

/**
 * 显示所属标题的工具
 * 
 * @param inputName
 * @param tdName
 * @param type
 */
ArticleEdit.showToolbar = function(inputName, tdName, type){
	var st = new ArticleToolbar(inputName, tdName, type);
	st.show();
	if ($('#' + inputName + 'Style').attr('value') != ''){
		var styleValue = $('#' + inputName + 'Style').attr('value');
		var titleStyle = $('#' + inputName).attr('style') + ';' + styleValue;
		$('#' + inputName).attr('style', titleStyle);
		$('#' + inputName + 'Style').attr('style', styleValue);
		ArticleToolbarStyle(inputName, styleValue);
	}
};

ArticleEdit.showTitle = function(inputName, tdName){
	if ($.trim($('#' + inputName).val()) != ''){
		$('#Show' + tdName).attr('checked', true);
		var styleValue = $('#' + inputName + 'Style').attr('value');
		var titleStyle = $('#' + inputName).attr('style') + ';' + styleValue;
		$('#' + inputName).attr('style',titleStyle);
		$('#tr' + tdName).show();
	}
};

/**
 * 自动保存文章信息
 * 
 * @param saveUrl
 */
ArticleEdit.autoSave = function(saveUrl){
	if ($.trim($('#title').val()=='')) return;
	if ($('#genre').val() == 'GENERAL'){
		var isSave = false;
		for (var i = 1; i <= pages; i++){
			var editor_id = '_Content_' + i;
			var content = tinyMCE.get(editor_id).getContent();
			var notDirty = tinyMCE.get(editor_id);
			content = content.replace(/<br \/>/g, '').replace(/<p>\&nbsp;<\/p>/g, '');
			//$('#textAreaContent_' + i).attr('value', content);
			if ($.trim(content) == '') continue;
			if (tinyMCE.getInstanceById(editor_id).isDirty()){
				isSave = true;
				notDirty.isNotDirty = true;
			}
		}
		if ($('#ShowShortTitle').attr('checked') == false || $.trim('#shortTitle').val() == ''){
			$('#shortTitle').attr('value', '');
			$('#shortTitleStyle').attr('value', '');
		}
		if ($('#ShowSubTitle').attr('checked') == false || $.trim('#subTitle').val() == ''){
			$('#subTitle').attr('value', '');
			$('#subTitleStyle').attr('value', '');
		}
		if (isSave){
			var params = $('#articleSave').serialize();
			$.post(saveUrl, params, function(data){
				$('#articleMainId').attr('value', data.articleMainId);
				$('#saveTime_general').html('<font color="#0000FF">' + data.modified + '</font>');
				$('#saveTime_title').html('<font color="#0000FF">' + data.modified + '</font>');
			});
		}
	}
};

/**
 * 根据文章类型显示不同的页面
 */
ArticleEdit.changeType = function(type){
	if (type == 'TITLE'){
		$('#table_content').hide();
		$('#pageBarTable_general').hide();
		$('#pageBarTable_title').show();
		$('#tr_url').show();
		$('#inside').attr('disabled', true);
		$('#inside').attr('checked', false);
	}else if (type == 'GENERAL'){
		$('#table_content').show();
		$('#pageBarTable_general').show();
		$('#pageBarTable_title').hide();
		$('#tr_url').hide();
		$('#inside').removeAttr('disabled');
		$('#ShowShortTitle').removeAttr('disabled');
		$('#ShowSubTitle').removeAttr('disabled');
	}
	ArticleEdit.windowResize();
};

/**
 * 页面自适应窗体大小
 */
ArticleEdit.windowResize = function(){
	var height = $(window).height() - $('#buttonBarTable').height() - $('#inputBarTable').height() - $('#pageBarDiv').height() - 10;
	var width = $(window).width() - 60;
	$('div #_DivContainer').css('height', height + 'px');
	try{
		if (tinyMCE.getInstanceById('_Content_' + currentPage) != null){
			tinyMCE.getInstanceById('_Content_' + currentPage).theme.resizeTo(width, (height - 108));
		}else{
			$('#_Content_' + currentPage).css('width', (width + 2) + 'px');
			$('#_Content_' + currentPage).css('height', (height - 41) + 'px');
		}
	}catch (e){}
};

/**
 * 新增一页
 */
ArticleEdit.addPage = function(){
	pages = pages + 1;
	$('#pageList').append(ArticleEdit.getLiHtml(pages));
	var trContentTemp = ArticleEdit.getTrContentHtml(pages).replace(/\&quot;/g, '"').replace(/\&lt;/g, '<').replace(/\nbsp;/g, ' ').replace(/\&amp;/g, '&');
	$('#tableContent').append(trContentTemp);
	try{
		tinyMCE.execCommand('mceAddControl', false, '_Content_' + pages);
	}catch(e){}
	ArticleEdit.setActivePage(pages);
};

/**
 * 删除一页(从后向前)，当只剩下最后一页时不再删除
 */
ArticleEdit.delPage = function(){
	if (pages == 1) return;
	try{
		if (tinyMCE.getInstanceById('_Content_' + pages) != null){
			tinyMCE.execCommand('mceRemoveControl', false, '_Content_' + pages);
		}
	}catch(e){}
	$('#trContent_' + pages).remove();
	$('#p' + pages).remove();
	
	pages = pages - 1;
	if (currentPage > pages) {
		currentPage = pages;
	}
	ArticleEdit.setActivePage(currentPage);
};

/**
 * 设置选中页码与页面
 * 
 * @param page
 */
ArticleEdit.setActivePage = function(page){
	var currentTab = $('#p' + page);
	if (currentPage == page && currentTab.attr('class') == 'current') return;
	for (var i = 0; i < pages; i++){
		var tab = $('#p' + (i + 1));
		var content = $('#trContent_' + (i + 1));
		if (tab.attr('class') == 'current'){
			tab.attr('class', '');
			content.hide();
			break;
		}
	}
	
	currentTab.attr('class', 'current');
	$('#trContent_' + page).show();
	$('#_Content_' + page).focus();
	
	currentPage = page;
	ArticleEdit.windowResize();
};

/**
 * 页码HTML代码，并写入前台显示页面
 * 
 * @param page
 */
ArticleEdit.getLiHtml = function(page){
	var liHtml = '<li onclick="ArticleEdit.changePage(\'p' + page + '\')"'
			   + ' onmouseover="ArticleEdit.onOverPage(\'p' + page + '\')"'
			   + ' onmouseout="ArticleEdit.onOutPage(\'p' + page + '\')"'
			   + ' id="p' + page + '"'
			   + ' name="tabs"><b>页 ' + page + '</b>'
			   + '</li>';
	return liHtml;
};

/**
 * 页面编辑HTML代码，并写入前台显示页面
 * 
 * @param page
 */
ArticleEdit.getTrContentHtml = function(page){
	var trContentHtml = '<tr id="trContent_' + page + '">'
				      + '  <td>'
				      + '    <textarea id="_Content_' + page + '" name="contents[' + (page - 1) + '].detail" class="mceEditor"></textarea>'
				      + '    <input type="hidden" id="contents' + page + '.id" name="contents[' + (page - 1) + '].id"/>'
				      + '    <input type="hidden" id="contents' + page + '.page" name="contents[' + (page - 1) + '].page" value="' + page + '"/>'
				      + '  </td>'
				      + '</tr>';
	return trContentHtml;
};

/**
 * 改变页码与页面
 * 
 * @param id
 */
ArticleEdit.changePage = function(id){
	ArticleEdit.setActivePage(parseInt(id.substr(1)));
};

/**
 * 鼠标进入页码选项显示效果
 * 
 * @param id
 */
ArticleEdit.onOverPage = function(id){
	var liObj = $('#' + id);
	if (liObj.attr('class') == ''){
		liObj.attr('class', 'pagetabOver');
	}
};

/**
 * 鼠标离开页码选项显示效果
 * 
 * @param id
 */
ArticleEdit.onOutPage = function(id){
	var liObj = $('#' + id);
	if (liObj.attr('class') == 'pagetabOver'){
		liObj.attr('class', '');
	}
};

ArticleEdit.initCookies = function(){
	for (var i=1;i<=5;i++){
		var ewcms_cookies = $.cookie('ewcms_' + i + '_' + loginName);
		if (ewcms_cookies != null){
			$('#ewcms_' + i).attr('checked',true);
			$('#trShowHide_' + i).show();
		}else{
			$('#ewcms_' + i).attr('checked',false);
			$('#trShowHide_' + i).hide();
		}
	}
};

ArticleEdit.setCookies = function(obj,trId){
	var id = obj.id;
	if ($('#' + id).attr('checked') == 'checked'){
		$.cookie(id + '_' + loginName,'true',{expires:14});
		$('#' + trId).show();
	}else{
		$.cookie(id + '_' + loginName, null);
		$('#' + trId).hide();
	}
	ArticleEdit.windowResize();
};

ArticleEdit.initChannel = function(){
	if ($('#articleMainId').val() != ''){
		$('#cc_channel').combotree('disable');
	}else{
		$('#cc_channel').combotree('enable');
	}
};

/**
 * 打开图片界面
 * 
 * @param multi Boolean(true:多条,false:单条)
 */
ArticleEdit.openImageWindow = function(multi){
	$.ewcms.openWindow({windowId:'#insert-window',iframeId:'#uploadifr_insert_id',src:insertResourceUrl + '/image/?multi=' + multi,width:600,height:500,title:'图片选择'});
};

ArticleEdit.openAnnexWindow = function(){
	$.ewcms.openWindow({windowId:'#insert-window',iframeId:'#uploadifr_insert_id',src:insertResourceUrl + '/annex',width:600,height:500,title:'附件选择'});
};

ArticleEdit.openFlashWindow = function(){
	$.ewcms.openWindow({windowId:'#insert-window',iframeId:'#uploadifr_insert_id',src:insertResourceUrl + '/flash',width:600,height:500,title:'Flash选择'});
};

ArticleEdit.openVideoWindow = function(){
	$.ewcms.openWindow({windowId:'#insert-window',iframeId:'#uploadifr_insert_id',src:insertResourceUrl + '/video',width:600,height:500,title:'视频选择'});
};

ArticleEdit.openVoteWindow = function(){
	$.ewcms.openWindow({windowId:'#vote-window',iframeId:'#editifr_vote',src:voteUrl,width:900,height:500,title:'问卷调查选择'});
};

ArticleEdit.loadingEnable = function(){
	$('<div class="datagrid-mask"></div>').css({display:'block',width:'100%',height:$(window).height()}).appendTo('body');
	$('<div class="datagrid-mask-msg"></div>').html('<font size="9">正在处理，请稍候。。。</font>').appendTo('body').css({display:'block',left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
};

ArticleEdit.loadingDisable = function(){
	$('.datagrid-mask-msg').remove();
	$('.datagrid-mask').remove();
};
