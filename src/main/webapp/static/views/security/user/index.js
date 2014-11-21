var UserIndex = function(dataGrid){
    this._dataGrid = dataGrid || '#tt';
};

UserIndex.prototype.init = function(opts){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    pageSize:20,
	    url:opts.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',title:'序号',width:50,hidden:true}
		]],
		columns:[[
		    {field:'loginName',title:'登录名',width:200,sortable:true},
	        {field:'realName',title:'用户名',width:200},
	        {field:'email',title:'邮箱',width:250},
	        {field:'registerDate',title:'注册日期',width:200},
	        {field:'status',title:'停用',width:30,
	        	formatter:function(val,rec){
               		return val ? '&nbsp;是' : '&nbsp;否';
               	}	
	        }
        ]],
        view : detailview,
    	detailFormatter : function(rowIndex, rowData) {
    		return '<div id="ddv-' + rowIndex + '"></div>';
    	},
    	onExpandRow: function(rowIndex, rowData){
   			var content = '<iframe src="' + opts.detailUrl + '?id=' + rowData.id + '" frameborder="0" width="100%" height="400px" scrolling="auto"></iframe>';
  			
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
    $('#tb-defaultquery').bind('click', function(){
    	$.ewcms.defaultQuery();
    });
};


function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};