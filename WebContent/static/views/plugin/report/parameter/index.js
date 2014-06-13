var ParameterIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

ParameterIndex.prototype.init = function(options){
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
			{field:'enName',title:'参数名',width:200},
			{field:'cnName',title:'中文说明',width:200},
			{field:'defaultValue',title:'默认值',width:200},
			{field:'typeDescription',title:'数据输入方式',width:100},
			{field:'value',title:'辅助数据设置',width:200}
        ]]
	});
	
    $('#tb-edit').bind('click', function(){
    	$.ewcms.edit({
    		title:'修改',
    		src : options.editUrl,
    		width: 700,
    		height : 240
    	});
    });
    
    $('#tb-query').bind('click', function(){
    	$.ewcms.query();
    });
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};