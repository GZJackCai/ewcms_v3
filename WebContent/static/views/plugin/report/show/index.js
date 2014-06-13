var ShowIndex = function(){};

ShowIndex.prototype.setReportParameter = function(url){
	$.ewcms.openWindow({windowId:"#edit-window",iframeId:'#editifr',src:url,width:400,height:213,title:"参数选择"});
};