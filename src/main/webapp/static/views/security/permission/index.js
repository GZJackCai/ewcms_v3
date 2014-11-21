var PermissionIndex = function(dataGrid){
    this._dataGrid = dataGrid || '#tt';
};

PermissionIndex.prototype.init = function(opts){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
		url:opts.queryUrl,
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:true,
	    pageSize:20,
		frozenColumns:[[
		    {field:'id',title:'序号',width:50,hidden:true}
		]],
		columns:[[
	        {field:'permissionName',title:'名称',width:150, sortable:true},
	        {field:'expression',title:'表达式',width:300,sortable:true},
	        {field:'caption',title:'说明',width:400,sortable:true}
        ]]
	});
    $("#tb-query").click(function(){
    	$.ewcms.query();
    });
    $('#tb-defaultquery').bind('click', function(){
    	$.ewcms.defaultQuery();
    });
};


function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};