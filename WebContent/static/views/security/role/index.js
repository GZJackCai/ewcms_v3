var RoleIndex = function(dataGrid){
    this._dataGrid = dataGrid || '#tt';
};

RoleIndex.prototype.init = function(opts){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
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
	        {field:'roleName',title:'角色名称',width:150, sortable:true},
	        {field:'caption',title:'说明',width:400}
        ]],
        url:opts.queryUrl,
        view : detailview,
    	detailFormatter : function(rowIndex, rowData) {
    		return '<div id="ddv-' + rowIndex + '"></div>';
    	},
    	onExpandRow: function(rowIndex, rowData){
    		var content = '<iframe src="' + opts.detailUrl + '?id=' + rowData.id + '" frameborder="0" width="100%" height="350px" scrolling="auto"></iframe>';
    			
    		$('#ddv-' + rowIndex).panel({
    			border : false,
    			cache : false,
    			content : content,
    			onLoad : function(){
    				$(dataGrid).datagrid('fixDetailRowHeight',rowIndex);
    			}
			});
			$(dataGrid).datagrid('fixDetailRowHeight',rowIndex);
		}
	});
    $("#tb-add").bind('click',function(){
		$.ewcms.add({
			title:'新增'
		});
	});
    $("#tb-edit").bind('click',function(){
		$.ewcms.edit({
			title:'修改'
		});
	});
    $("#tb-remove").bind('click', function(){
    	$.ewcms.remove({
    		title:'删除'
    	});
    });
    $("#tb-query").bind('click',function(){
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
}