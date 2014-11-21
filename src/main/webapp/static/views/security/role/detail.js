var RoleDetail = function(dataGrid, permissionDataGrid){
	this._dataGrid = dataGrid || '#tt';
	this._permissionDataGrid = permissionDataGrid || '#permission-tt';
};

RoleDetail.prototype.init = function(opts){
	var dataGrid = this._dataGrid;
	var permissionDataGrid = this._permissionDataGrid;
	
	$(dataGrid).datagrid({
		url:opts.queryUrl,
		nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    pageSize:20,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',title:'序号',width:50,hidden:true}
		]],
		columns:[[
		    {field:'name',title:'名称',width:100},
		    {field:'caption',title:'描述',width:200}
		]]
	});
	
    $(permissionDataGrid).datagrid({
        width:511,
        pageSize:5,
        nowrap: false,
        rownumbers:true,
        idField:'id',
        pagination:true,
        pageList:[5],
        url: opts.permissionUrl,
        frozenColumns:[[
             {field:'ck',checkbox:true},
             {field:'id',title:'编号',width:50,hidden:true}
        ]],
        columns:[[
			 {field:'name',title:'权限名称',width:200},
             {field:'caption',title:'说明',width:230}
        ]]
    });
    
    $('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'添加 - 权限',
			width:525,
			height:310
		});
	});
    
    $('#tb-remove').bind('click', function(){
		var rows = $(dataGrid).propertygrid('getSelections');
		if (rows.length == 0){
			$.messager.alert('提示','请选择移除项','info');
			return;
		}
		var parameter = 'isRemove=true';
		for (var i = 0; i < rows.length; i++){
			parameter = parameter + '&permissionIds=' + rows[i].id;
		}
		$.post(opts.editUrl, parameter, function(data){
			$.messager.alert("提示",data,"info");
    		$(datagrid).datagrid("reload");
		});
    });
    
    $('#tb-query').bind('click', function(){
    	$.ewcms.query();
    });
    
    $('#save-button').bind('click', function(){
    	var permissionRows = $(permissionDataGrid).propertygrid('getSelections');
    	var parameter = 'isRemove=false';
    	for (var i = 0; i < permissionRows.length; i++){
    		parameter = parameter + '&permissionIds=' + permissionRows[i].id;
    	}
    	$.post(editUrl, parameter, function(data){
    		$.messager.alert("提示",data,"info");
    		$('#edit-window').window('close');
    		$(datagrid).datagrid("reload");
    	});
    });
    
    $('#cancel-button').bind('click', function(){
    	$('#edit-window').window('close');
    });
};