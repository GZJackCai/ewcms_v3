/*!
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */
 
//var updateUsername = function(name){
//    $('#user-name').html(name);
//};

var Home = function(refurbish,windowId){
    this._refurbish = refurbish || true;
    this._windowId = windowId || '#edit-window';
};

Home.prototype.init = function(options){
    var windowId = this._windowId;
 
    $('#button-main').bind('click',function(){
        $('#mm').menu('show',{
            left:$(this).offset().left - 80,
            top:$(this).offset().top + 18
        });
    }).hover(function(){
        $(this).addClass('l-btn l-btn-plain m-btn-plain-active');
    },function(){
        $(this).removeClass('l-btn l-btn-plain m-btn-plain-active');
    });
    
    $('#user-menu').bind('click',function(){
        $.ewcms.openWindow({windowId:windowId,iframeId:'#editifr',src:options.user,width:550,height:300,title:'修改用户信息'});
    });
    $('#password-menu').bind('click',function(){
    	$.ewcms.openWindow({windowId:windowId,iframeId:'#editifr',src:options.password,width:550,height:250,title:'修改密码'});
    });
    $('#switch-menu').bind('click',function(){
        var item = $('#mm').menu('getItem','#switch-menu');
        if(!item.disabled){
        	$.ewcms.openWindow({windowId:windowId,iframeId:'#editifr',src:options.siteswitch,width:450,height:246,title:'站点切换'});
        }
    });
    $('#progress-menu').bind('click',function(){
        openWindow(windowId,{width:550,height:300,title:'发布进度',url:options.progress}); 
    });
    $('#exit-menu').bind('click',function(){
        window.location = options.exit;
    });
    
    if(!options.hasSite){
        $('#mm').menu('disableItem','#switch-menu');
    }
    
    $('#menu-visit').bind('click', function(){
	    $('#tt-visit').tree({
	    	checkbox : false,
	        url : options.visitTreeUrl,
	        onClick : function(node) {
	        	var noteUrl = node.attributes.url;
				if (typeof(noteUrl) == 'undefined' || noteUrl == ''){
					return false;
				}else{
					$.ewcms.openTab({id:'#tab-visit', title:node.text,src:options.visitUrl + noteUrl + '/index'});
				}
	        },
	    	onLoadSuccess : function(node, data){
	    		var nodeSel = $('#tt-visit').tree('find', 2);
	    		$('#tt-visit').tree('select', nodeSel.target);
	    		$.ewcms.openTab({id:'#tab-visit', title:nodeSel.text,src:options.visitUrl + nodeSel.attributes.url + '/index'});
	    	}
	    });
    });
	
	$('li[id^="menu-"]').bind('click', function(){
		var id = $(this).get(0).id.substring(5);
		
		$('li[id^="menu-"]').removeClass('tabs-selected');
		$('div[id^="menusub-"]').css('display','none');
		$('div[id^="tab-"]').css('display','none');
		
		$('#menu-' + id).addClass('tabs-selected');
		$('#menusub-' + id).css('display', '');
		$('#tab-' + id).css('display', '');
		
		if (id == 'index'){
			$('#center').css('display','none');
		}else{
			$('#center').css('display','');
			$('#west').panel('setTitle', $(this).text());
			$('#tab-' + id).tabs('resize');
		}
	});
	
	$('a[id="tb-message-more"], #tipMessage').bind('click', function(){
		var id = 'content';
		
		$('li[id^="menu-"]').removeClass('tabs-selected');
		$('div[id^="menusub-"]').css('display','none');
		$('div[id^="tab-"]').css('display','none');
		
		$('#menu-' + id).addClass('tabs-selected');
		$('#menusub-' + id).css('display', '');
		$('#tab-' + id).css('display', '');
		
		$('#center').css('display','');
		$('#west').panel('setTitle', $('#menu-' + id).text());
		$('#tab-' + id).tabs('resize');
		
		$.ewcms.openTab({id:'#tab-content', title:'个人消息',src: options.messageMoreUrl})
	});
	
	var currentDate = new Date();
	this.showPublishArticleChart(currentDate.getFullYear());
	this.showCreateArticleChart(currentDate.getFullYear());
   	this.showPublishPersonChart(currentDate.getFullYear());
};

Home.prototype.showPublishArticleChart = function(year){
	$.post(ctx + "/fcf/publishArticle/" + year, {}, function(result) {
	    var myChart = new FusionCharts(ctx + "/static/fcf/swf/Line.swf?ChartNoDataText=无数据显示", "myChartId", "400", "200");
	    myChart.setDataXML(result);      
	    myChart.render("lineDiv");
	});
};

Home.prototype.showPublishPersonChart = function(year){
	  $.post(ctx + "/fcf/publishPerson/" + year, {}, function(result) {
		var myChart = new FusionCharts(ctx + "/static/fcf/swf/Pie3D.swf?ChartNoDataText=无数据显示", "myChartId", "400", "200");
		myChart.setDataXML(result);
		myChart.render("pieDiv");
	  });  
}
Home.prototype.showCreateArticleChart = function(year){
	  $.post(ctx + "/fcf/createArticle/" + year, {}, function(result) {
	    var myChart = new FusionCharts(ctx + "/static/fcf/swf/Column3D.swf?ChartNoDataText=无数据显示", "myChartId", "400", "200");
	    myChart.setDataXML(result);      
		    myChart.render("chartDiv");
    });  
}