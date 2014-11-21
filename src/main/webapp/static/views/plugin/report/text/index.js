var TextReportIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

TextReportIndex.prototype.init = function(options){
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
            {field:'name',title:'名称',width:200},
			{field:'createDate',title:'创建时间',width:145},
			{field:'updateDate',title:'更新时间',width:145},
			{field:'dsName',title:'数据源名称',width:200,
				formatter:function(val,rec){
			    	if (rec.baseDs == null){
			    		return '默认数据源';
			    	}else{
			    		return rec.baseDs.name;
			    	}
			    }
			},
			{field:'remarks',title:'说明',width:400},
			{field:'download',title:'下载',width:30,
			   	formatter:function(val,rec){
			   		return '&nbsp;<a href="javascript:void(0);" onclick="_textReportIndex.download(\'' + options.downloadUrl + '/' +  rec.id + '\');"><img src="../../../static/css/icons/download.png" width="13px" height="13px" title="下载" style="border:0"/></a>';
			  	}
			}
        ]],
        view : detailview,
  		detailFormatter : function(rowIndex, rowData) {
  			return '<div id="ddv-' + rowIndex + '"></div>';
  		},
        onExpandRow: function(rowIndex, rowData){
  			var content = '<iframe src="' + options.parameterUrl + '/text_' + rowData.id + '" frameborder="0" width="100%" height="300px" scrolling="auto"></iframe>';
  			
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
    	$.ewcms.openWindow({windowId:'#edit-window',iframeId : '#editifr', src : options.previewUrl + '/text_' + rows[0].id, width:550,height:200,title:'应用栏目'});
    });
    
    $('#tb-scheduling').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择记录', 'info');
			return;
		}
		var parameters = "?reportType=text";
		for ( var i = 0; i < rows.length; i++) {
			parameters += '&selections=' + rows[i].id;
		}
		
		$.ewcms.openWindow({windowId:'#edit-window',
			iframeId : '#editifr',
			src : options.schedulingUrl + parameters,
			width : 900,
			height : 500,
			title : '定时器设置'
		});
    });
};

TextReportIndex.prototype.download = function(url){
	window.open(url,'popup','width=1280,height=700,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,scrollbars=yes,left=' + (window.screen.width - 1280)/ 2 + ',top=' + (window.screen.height - 700) / 2);
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};