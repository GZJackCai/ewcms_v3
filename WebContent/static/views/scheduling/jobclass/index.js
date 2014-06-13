var JobClassIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

JobClassIndex.prototype.init = function(options){
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
            {field:'className',title:'类名称',width:200},
            {field:'classEntity',title:'类实体',width:500},
            {field:'description',title:'描述',width:400}
        ]]
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'新增',
			width : 620,
			height : 210
		});
	});
	
    $('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改',
			width : 620,
			height : 210
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

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};