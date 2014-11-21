var ChartIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

ChartIndex.prototype.init = function(options){
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
            {field:'name',title:'名称',width:100},
			{field:'createDate',title:'创建时间',width:145},
			{field:'updateDate',title:'更新时间',width:145},
			{field:'typeDescription',title:'图型类型',width:200},
			{field:'dsName',title:'数据源名称',width:200,
			   	formatter:function(val,rec){
			  		if (rec.baseDS == null){
			   			return '默认数据源';
			   		}else{
			  			return rec.baseDS.name;
			   		}
			    }
			},
			{field:'remarks',title:'说明',width:600}
        ]],
        view : detailview,
  		detailFormatter : function(rowIndex, rowData) {
  			return '<div id="ddv-' + rowIndex + '"></div>';
  		},
        onExpandRow: function(rowIndex, rowData){
  			var content = '<iframe src="' + options.parameterUrl + '/chart_' + rowData.id + '" frameborder="0" width="100%" height="300px" scrolling="auto"></iframe>';
  			
  			$('#ddv-' + rowIndex).panel({
  				border : false,
  				cache : false,
  				content : content,
  				onLoad : function(){
  					$('#tt').datagrid('fixDetailRowHeight',rowIndex);
  				}
  			});
  			$('#tt').datagrid('fixDetailRowHeight',rowIndex);
  		}
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'新增',
			width:700,
			height:500
		});
	});
	
    $('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改',
			width:700,
			height:500
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
    
    $('#tb-preview').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择预览记录', 'info');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('提示', '只能选择一个预览', 'info');
			return;
		}
		$.ewcms.openWindow({windowId:'#edit-window',iframeId : '#editifr', url : options.previewUrl + '/chart_' + rows[0].id, width:550,height:200,title:'参数选择'});
    });
    
    $('#tb-scheduling').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择记录', 'info');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('提示', '只能选择一条记录', 'info');
			return;
		}
		$.ewcms.openWindow({windowId:'#edit-window',
			iframeId : '#editifr',
			url : options.schedulingUrl + '/chart_' + rows[0].id,
			width : 900,
			height : 500,
			title : '定时器设置'
		});
    });
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};