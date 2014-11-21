var HistoryTemplate = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

HistoryTemplate.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
		nowrap:true,
	    pagination:false,
	    rownumbers:true,
	    singleSelect:true,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:"ck",checkbox:true},
		    {field:'historyId',title:'编号',width:60,hidden:true}
		]],
		columns:[[
            {field:'version',title:'版本号',width:100,sortable:true},
	        {field:'historyTime',title:'时间',width:148, 
            	formatter : function(val, rec){
            		var date = new Date(val);
                    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
            	}
	        }
        ]]
	});
	
	$('#td-restore').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
        if(rows.length == 0){
        	$.messager.alert('提示','请选择还原记录','info');
            return;
        }	
        $.messager.confirm("提示","确定要把第 【" + rows[0].version + "】号 版本替换当前的模板吗?",function(r){
    		if (r){
    			$.post(options.restoreUrl, {historyId : rows[0].historyId}, function(data) {
					if (data){
						$.messager.alert('提示', '还原模板成功', 'info');
					}else{
						$.messager.alert('提示', '还原模板失败', 'info');
					}
				});
    		}
    	});
	});
	
	$('#td-cancel').bind('click', function(){
		parent.$('#pop-window').window('close');
	});
	
	$("#tb-query").bind('click', function(){
    	$.ewcms.query({
    		src:options.queryUrl
    	});
    });
	
	$('#tb-clear').bind('click', function(){
    	$('#queryform')[0].reset();
    	$('#startDate').datebox('setValue','');
    	$('#endDate').datebox('setValue','');
    });
};