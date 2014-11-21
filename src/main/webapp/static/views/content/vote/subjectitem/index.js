var SubjectItemIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

SubjectItemIndex.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:true,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',hidden:true}
		]],
		columns:[[
		    {field:'title',title:'选项名称',width:500},
		    {field:'statusDescription',title:'选择方式',width:100},
		    {field:'voteNumber',title:'票数',width:60}
		]]
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'新增',
			src : options.editUrl,
			width:490,
			height:260
		});
	});
	
    $('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改',
			src : options.editUrl,
			width:490,
			height:260
		});
	});
    
    $('#tb-remove').bind('click', function(){
    	$.ewcms.remove({
    		title:'删除',
    		src : options.deleteUrl
    	});
    });
    
    $('#tb-query').bind('click', function(){
    	$.ewcms.query();
    });	

    $('#tb-up').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if(rows.length == 0){
        	$.messager.alert('提示','请选择问卷调查主题列表记录','info');
            return;
        }
        if (rows.length > 1){
			$.messager.alert('提示','只能选择一个问卷调查主题列表记录','info');
			return;
        }
        $.post(options.upUrl + "_" + rows[0].id, {}, function(data){
			$.messager.alert("提示",data,"info");
			$(dataGrid).datagrid('reload');
        });
    });
    
    $('#tb-down').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if(rows.length == 0){
        	$.messager.alert('提示','请选择问卷调查主题列表记录','info');
            return;
        }
        if (rows.length > 1){
			$.messager.alert('提示','只能选择一个问卷调查主题列表记录','info');
			return;
        }
        $.post(options.downUrl + '_' + rows[0].id, {}, function(data){
			$.messager.alert("提示", data, "info");
			$(dataGird).datagrid('reload');
        });
	});	
    
    $('#tb-cancel').bind('click', function(){
    	parent.$('#edit-window').window('close');
    	parent.$('#tt').datagrid('clearSelections');
		parent.$('#tt').datagrid('reload');
    });
    
	$("form table tr").next("tr").hide(); 
    
    $('#toolbar-arrows').bind('click', function(){
    	$('form table tr').next('tr').toggle();
    	if ($(this).html() == '收缩...'){
    		$(this).html('更多...');
    	}else{
    		$(this).html('收缩...');
    	}
    	$(dataGrid).datagrid('resize');
    });
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};