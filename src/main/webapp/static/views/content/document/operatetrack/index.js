var OperateTrackIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

OperateTrackIndex.prototype.init = function(options){
var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
		singleSelect:true,
		rownumbers:true,
		nowrap:false,
		pagination:true,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'id',title:'编号',width:50,hidden:true}
		]],
		columns:[[
			{field:'userRealName',title:'操作员',width:100},
			{field:'statusDesc',title:'上次状态',width:60},
			{field:'operateTime',title:'操作时间',width:145},
			{field:'description', title:'描述', width:600,
				formatter : function(val, rec) {
					var reason = '&nbsp;&nbsp;';
					if (rec.reason != null && rec.reason.length > 0){
						var reasonUrl = options.reasonUrl + '/' + rec.id;
						reason += '<a href="javascript:void(0);" onclick="_operateTrackIndex.showReason(\'' + reasonUrl + '\');">原因</a>';
					}
					return val + reason; 
				}
			}
		]]
	});
	var pager = $(dataGrid).datagrid('getPager');
	pager.pagination({
		showPageList:false
	});
};

OperateTrackIndex.prototype.showReason = function(reasonUrl){
	$.ewcms.openWindow({windowId:'#reason-window',iframeId:'#editifr_reason', src : reasonUrl, width:600,height:300,title:'原因'});
};