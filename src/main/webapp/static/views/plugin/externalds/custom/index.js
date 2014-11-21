var CustomDsIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

CustomDsIndex.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',title:'编号',width:50}
		]],
		columns:[[
            {field:'name',title:'名称',width:100},
			{field:'customName',title:'自定义类名称',width:200},
			{field:'customMethod',title:'自定义方法',width:200},
			{field:'remarks',title:'说明',width:300},
			{field:'connectTest',title:'测试',width:30,
				formatter : function(val, rec) {
					return '&nbsp;<a href="javascript:void(0);" onclick="_customDsIndex.isConnect(\'' + options.isConnectUrl + '/' + rec.id + '\');"><img src="../../../static/image/ds/connect_test.png" width="13px" height="13px" title="测试" style="border:0"/></a>';
			    }
			}
        ]]
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'新增'
		});
	});
	
    $('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改'
		});
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

CustomDsIndex.prototype.isConnect = function(url){
	$.post(url, {}, function(data) {
		$.messager.alert('提示', data, 'info');
	});
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};