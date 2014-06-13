var RepositoryIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

RepositoryIndex.prototype.init = function(options){
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
		    {field:'name',title:'名称',width:300},
		    {field:'type',title:'类型',width:50},
            {field:'updateDate',title:'更新时间',width:145},
            {field:'publishDate',title:'发布时间',width:145},
            {field:'description',title:'说明',width:300},
			{field:'download',title:'下载',width:30,
			   	formatter:function(val,rec){
			   		return '&nbsp;<a href="javascript:void(0);" onclick="_repositoryIndex.download(\'' + options.downloadUrl + '/' +  rec.id + '\');"><img src="../../../static/css/icons/download.png" width="13px" height="13px" title="下载" style="border:0"/></a>';
			  	}
			}
        ]]
	});
	
    $('#tb-remove').bind('click', function(){
    	$.ewcms.remove({
    		title:'删除'
    	});
    });
    
    $('#tb-query').bind('click', function(){
    	$.ewcms.query();
    });
    
    $('#tb-publish').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择发布的资源记录', 'info');
			return;
		}
		var parameter = '';
		var rows = $(dataGrid).datagrid('getSelections');
		for ( var i = 0; i < rows.length; i++) {
			parameter = parameter + '&selections=' + rows[i].id;
		}
		$.post(options.publishUrl, parameter, function(data) {
			if (data == 'true') {
				$(dataGrid).datagrid('reload');
				$.messager.alert('提示', '资源发布成功', 'info');
			} else if (data == 'notinstate') {
				$.messager.alert('提示', '资源发布失败', 'info');
			}
			return;
		});
		return false;
    });

};

RepositoryIndex.prototype.download = function(url){
	window.open(url,'popup','width=1280,height=700,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,scrollbars=yes,left=' + (window.screen.width - 1280)/ 2 + ',top=' + (window.screen.height - 700) / 2);
};