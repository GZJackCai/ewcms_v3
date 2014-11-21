var CategoryDetailIndex = function(propertyGrid){
	this._propertyGrid = propertyGrid || '#tt';
};

CategoryDetailIndex.prototype.init = function(options){
	var propertyGrid = this._propertyGrid;
	
	$(propertyGrid).propertygrid({
        width:500,
        url:options.queryUrl,
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
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'添加',
			src : options.editUrl,
			width : 700,
			height : 200
		});
	});
	
    $('#tb-remove').bind('click', function(){
    	$.ewcms.remove({
    		title:'移除'
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