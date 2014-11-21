var UserDetail = function(propertyGrid, roleDataGrid, permissionDataGrid){
	this._propertyGrid = propertyGrid || '#tt';
	this._roleDataGrid = roleDataGrid || '#role-tt';
	this._permissionDataGrid = permissionDataGrid || '#permission-tt';
};

UserDetail.prototype.init = function(opts){
	var propertyGrid = this._propertyGrid;
	var roleDataGrid = this._roleDataGrid;
	var permissionDataGrid = this._permissionDataGrid;
	
	$(propertyGrid).propertygrid({
        width:500,
        url:opts.queryUrl,
        showGroup:true,
        scrollbarSize:0,
        singleSelect:false,
        frozenColumns:[[
			{field:"ck",checkbox:true}
		]],
        columns:[[
            {field:'name',title:'名称',width:150},
            {field:"value",title:'描述',width:260}
        ]]
	});
	
    $(permissionDataGrid).datagrid({
        width:500,
        pageSize:5,
        nowrap: false,
        rownumbers:true,
        idField:'name',
        pagination:true,
        pageList:[5],
        url: opts.permissionUrl,
        frozenColumns:[[
             {field:'ck',checkbox:true},
             {field:"name",title:'权限名称',width:200}
        ]],
        columns:[[
             {field:"caption",title:'说明',width:230}
        ]]
    });
    
    $(roleDataGrid).datagrid({
        width:500,
        idField:'roleName',
        pageSize:5,
        pagination:true,
        nowrap: false,
        rownumbers:true,
        pageList:[5],
        url: opts.roleQueryUrl,
        frozenColumns:[[
             {field:'ck',checkbox:true},
             {field:"roleName",title:'用户组名称',width:200}
        ]],
        columns:[[
             {field:"caption",title:'说明',width:230}
        ]]
    });
    
    $("#tb-add").bind('click',function(){
		$.ewcms.add({
			title:"添加 - 角色/权限",
			width:525,
			height:310
		});
	});
    
    $("#tb-remove").bind('click', function(){
		var rows = $(propertyGrid).propertygrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择移除项","info");
			return;
		}
		var parameter = "isRemove=true";
		for (var i = 0; i < rows.length; i++){
			if (rows[i].group == "角色"){
				parameter = parameter + "&roleNames=" + rows[i].name;
			}else{
				parameter = parameter + "&permissionNames=" + rows[i].name;
			}
		}
		$.post(opts.editUrl, parameter, function(data){
			$.messager.alert("提示",data,"info");
    		$(propertyGrid).propertygrid("reload");
		});
    });
    
    $("#tb-query").bind('click',function(){
    	$(propertyGrid).propertygrid({
			onBeforeLoad:function(param){
				param['parameters']=$('#queryform').serializeObject();
			}
		});
    });
    
    $('#save-button').bind('click', function(){
    	var roleRows = $(roleDataGrid).propertygrid("getSelections");
    	var permissionRows = $(permissionDataGrid).propertygrid("getSelections");
    	var parameter = "isRemove=false";
    	for (var i = 0; i < roleRows.length; i++) {
    		parameter = parameter + "&roleNames=" + roleRows[i].roleName;
    	}
    	for (var i = 0; i < permissionRows.length; i++){
    		parameter = parameter + "&permissionNames=" + permissionRows[i].name;
    	}
    	$.post(opts.editUrl, parameter, function(data){
			$.messager.alert("提示",data,"info");
    		$('#edit-window').window('close');
    		$(propertyGrid).propertygrid("reload");
    	});
    });
    
    $('#cancel-button').bind('click', function(){
    	$('#edit-window').window('close');
    });
};