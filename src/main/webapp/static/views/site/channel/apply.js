var ChannelApply = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

ChannelApply.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',title:'栏目编号',width:60}
		]],
		columns:[[
            {field:'name',title:'栏目名称',width:200},
            {field:'absUrl',title:'相对地址',width:300}
        ]]
	});
	
	$('#td-remove').bind('click', function(){
		var parentRefer = false;
		var rows = $(dataGrid).datagrid('getSelections');
	    if(rows.length == 0){
	        $.messager.alert('提示','请选择移除记录','info');
	        return ;
	    }
	    if (rows.length == $(dataGrid).datagrid('getData').total){
	    	parentRefer = true;
	    }
	    var ids = '';
	    for(var i=0;i<rows.length;++i){
	        ids =ids + 'selections=' + rows[i].id +'&';
	    }
	    $.messager.confirm("提示","确定要移除所选记录吗?",function(r){
	        if (r){
	            $.post(options.removeUrl,ids,function(data){          	
	            	$.messager.alert('成功','删除成功','info');
	            	$(dataGrid).datagrid('clearSelections');
	                $(dataGrid).datagrid('reload');
	                if (parentRefer){
	                	parent.$('#span-connect').html('已断开');
	                	parent.$('#span-viewconnect').hide();
	                }
	            });
	        }
	    });	
	});
};