var SubjectIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

SubjectIndex.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:true,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',hidden:true}
		]],
		columns:[[
            {field:'title',title:'主题名称',width:500,
               	formatter:function(val,rec){
              		return '<a href="javascript:void(0);" onclick="_subjectIndex.showSubjectItem(\'' + options.itemIndexUrl + '\',\'' + options.itemEditUrl + '\',' + rec.id + ',\'' + val +  '\',\'' + rec.statusDescription + '\');">' + val + '</a>';
               	}
            },
            {field:'subjectStatusDescription',title:'主题选择方式',width:100}
        ]]
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'新增',
			src : options.editUrl,
			width:490,
			height:260
		});
	});
	
    $('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改',
			src : options.editUrl,
			width:490,
			height:260
		});
	});
    
    $('#tb-remove').bind('click', function(){
    	$.ewcms.remove({
    		title:'删除',
    		src : options.deleteUrl
    	});
    });
    
    $('#tb-query').bind('click', function(){
    	$.ewcms.query();
    });	

    $('#tb-up').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if(rows.length == 0){
        	$.messager.alert('提示','请选择问卷调查主题记录','info');
            return;
        }
        if (rows.length > 1){
			$.messager.alert('提示','只能选择一个问卷调查主题记录','info');
			return;
        }
        $.post(options.upUrl + "_" + rows[0].id,{},function(data){
			$.messager.alert("提示", data ,"info");
			$(dataGrid).datagrid('reload');
        });
    });
    
    $('#tb-down').bind('click', function(){
    	var rows = $(dataGird).datagrid('getSelections');
		if(rows.length == 0){
	       	$.messager.alert('提示','请选择问卷调查主题记录','info');
	        return;
	    }
	    if (rows.length > 1){
			$.messager.alert('提示','只能选择一个问卷调查主题记录','info');
			return;
		}
	    $.post(options.downUrl + "_" + rows[0].id,{},function(data){
			$.messager.alert("提示",data,"info");
			$(dataGrid).datagrid('reload');
	    });
	});	
    
	$("form table tr").next("tr").hide(); 
    
    $('#toolbar-arrows').bind('click', function(){
    	$('form table tr').next('tr').toggle();
    	if ($(this).html() == '收缩...'){
    		$(this).html('更多...');
    	}else{
    		$(this).html('收缩...');
    	}
    	$(dataGrid).datagrid('resize');
    });
};

SubjectIndex.prototype.showSubjectItem = function(itemIndexUrl, itemEditUrl, subjectId, subjectTitle, optionDescription){
	var title = '[<span style="color:red;">主题编号：</span>' + subjectId + '] [<span style="color:red;">主题名称：</span>' + subjectTitle + '] - 问卷调查主题列表';
	var src = '';
	if (optionDescription == '录入'){
		$('#save_span').attr("style","");
		src = itemEditUrl + '/' + subjectId;
	}else{
		$('#save_span').attr("style","display:none");
		src = itemIndexUrl + '/' + subjectId;
	}
	$.ewcms.openWindow({windowId:'#edit-window', iframeId : '#editifr', width:1400,height:430,title:title, src: src});
};


function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};
