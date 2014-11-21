var CategoryIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

CategoryIndex.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true}
		]],
		columns:[[
		    {field:'id',title:'编号',width:50},
            {field:'name',title:'名称',width:300},
			{field:'remarks',title:'说明',width:800}
        ]],
        view : detailview,
  		detailFormatter : function(rowIndex, rowData) {
  			return '<div id="ddv-' + rowIndex + '"></div>';
  		},
        onExpandRow: function(rowIndex, rowData){
  			var content = '<iframe src="' + options.parameterUrl + '/' + rowData.id + '" frameborder="0" width="100%" height="300px" scrolling="auto"></iframe>';
  			
  			$('#ddv-' + rowIndex).panel({
  				border : false,
  				cache : false,
  				content : content,
  				onLoad : function(){
  					$('#tt').datagrid('fixDetailRowHeight',rowIndex);
  				}
  			});
  			$(dataGrid).datagrid('fixDetailRowHeight',rowIndex);
  		}
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'新增'
		});
	});
	
    $('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改'
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