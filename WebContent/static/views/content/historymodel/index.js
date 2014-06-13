var HistoryModelIndex = function(dataGrid){
    this._dataGrid = dataGrid || '#tt';
};

HistoryModelIndex.prototype.init = function(opts){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    pageSize:20,
	    url: opts.queryUrl,
		frozenColumns:[[
		    {field:"ck",checkbox:true},
		    {field:"id",title:"序号",width:50}
		]],
		columns:[[
            {field:'className',title:'类名',width:400},
			{field:'methodName',title:'方法名',width:100},
			{field:'createDate',title:'保存时间',width:125},
			{field:'idName',title:'关键字名称',width:100},
			{field:'idValue',title:'关键字值',width:100},
			{field:'idType',title:'关键字类型',width:120},
			{field:'userName',title:'操作员',width:80},
			{field:'download',title:'下载',width:30,
			   	formatter:function(val,rec){
			   		if (rec.className=='com.ewcms.site.model.Template'){
			     		return '&nbsp;<a href="javascript:void(0);" onclick="_historyModelIndex.downloadTemplate(\'' + opts.downloadTemplateUrl + '\',' +  rec.id + ');"><img src="../../static/css/icons/download.png" width="13px" height="13px" title="下载" style="border:0"/></a>';
			   		}
			   	}
			}
        ]]
	});
	
	$('#tb-remove').bind('click', function(){
		$.ewcms.remove({
       		title:'删除'
       	});
	});
	
	$('#tb-query').bind('click', function(){
		$.ewcms.query();
	});
};

HistoryModelIndex.prototype.downloadTemplate = function(url, id){
	window.open(url + '?historyId=' + id);
};
