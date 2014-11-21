var ProcessIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

ProcessIndex.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',title:'编号',width:50,sortable:true},
		]],
		columns:[[
		     {field:'name',title:'名称',width:200},
		     {field:'realNames',title:'用户',width:400},
		     {field:'roleNames',title:'用户组',width:400}     
		]]
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'新增',
			src:options.editUrl,
			width:600,
			height:400
		});
	});
	
	$('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改',
			src:options.editUrl,
			width:600,
			height:400
		});
	});
	
	$('#tb-up').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择移动记录","info");
			return;
		}
		if (rows.length > 1){
			$.messager.alert("提示","只能选择一个记录进行移动","info");
			return;
		}
		$.post(options.upUrl, {selections:rows[0].id}, function(data){
			if (!data){
				$.messager.alert("提示","上移失败","info");
				return;
			}
			$(dataGrid).datagrid('reload');
		});
	});
	
	$('#tb-down').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择移动记录","info");
			return;
		}
		if (rows.length > 1){
			$.messager.alert("提示","只能选择一个记录进行移动","info");
			return;
		}
		$.post(options.downUrl, {selections:rows[0].id}, function(data){
			if (!data){
				$.messager.alert("提示","下移失败","info");
				return;
			}
			$(dataGrid).datagrid('reload');
		});
	});
	
	$('#tb-remove').bind('click', function(){
		$.ewcms.remove({
    		title:'删除',
    		src:options.deleteUrl
    	});
	});
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};