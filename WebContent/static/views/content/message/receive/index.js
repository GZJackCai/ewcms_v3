var MsgReceiveIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

MsgReceiveIndex.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',title:'编号',width:80}
		]],
		columns:[[
		     {field:'userName',title:'用户',hidden:true},
		     {field:'read',title:'标记 ',width:32,
		    	 formatter : function(val, rec) {
		    		 if (val){
		    			 return "&nbsp;<img src='" + ctx + "/static/image/msg/msg_read.gif' width='13px' height='13px' title='接收消息，已读'/>";
		    		 }else{
		    			 return "&nbsp;<img src='" + ctx + "/static/image/msg/msg_unread.gif' width='13px' height='13px' title='接收消息，未读'/>";
		    		 }
		    	 }
		     },
		     {field:'title',title:'标题',width:800,
		    	 formatter : function(val, rec){
		    		 return '<a href="javascript:void(0);" onclick="_msgReceiveIndex.showRecord(\'' + options.detailUrl + '\',\'' + options.unReadUrl + '\',' + rec.id + ')" onfocus="this.blur();">' + rec.msgContent.title + '</a>';
		    	 }
		     },
		     {field:'readTime',title:'读取时间',width:145},
		     {field:'subscription',title:'订阅',width:32,
		    	 formatter : function(val, rec) {
		    		 if (val){
		    			 return "&nbsp;<img src='" + ctx + "/static/theme/icons/ok.png' width='13px' height='13px'/>";
		    		 }else{
		    			 return "";
		    		 }
		    	 }
		     },
		     {field:'sendUserName',title:'发送用户',width:80}        
		]]
	});
	
	$('#menu-mark-read').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择记录","info");
			return;
		}
		if (rows.length > 1){
			$.messager.alert("提示","只能选择一条记录进行标记","info");
			return;
		}
		$.post(options.markReadUrl, {'selections':rows[0].id,'read':true}, function(data){
			if (!data){
				$.messager.alert("提示","标记失败","info");
				return;
			}
			$(dataGrid).datagrid('reload');
		});
	});
	
	$('#menu-mark-unread').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择记录","info");
			return;
		}
		if (rows.length > 1){
			$.messager.alert("提示","只能选择一条记录进行标记","info");
			return;
		}
		$.post(options.markReadUrl, {'selections':rows[0].id,'read':false}, function(data){
			if (!data){
				$.messager.alert("提示","标记失败","info");
				return;
			}
			$(dataGrid).datagrid('reload');
		});
	});
	
	$('#menu-remove').bind('click', function(){
    	$.ewcms.remove({
    		title:'删除'
    	});
    });
	
    $('#tb-query').bind('click', function(){
    	$.ewcms.query({
    		src : options.queryUrl
    	});
    });
    
    $('#tb-clear').bind('click', function(){
    	$('#queryform')[0].reset();
    	$('#readTimeStart').datebox('setValue','');
    	$('#readTimeEnd').datebox('setValue','');
    });
    
    $("form table tr").next("tr").hide(); 
    
    $('#tb-more').bind('click', function(){
       	var showHideLabel_value = $('#showHideLabel').text();
    	$('form table tr').next('tr').toggle();
     	if (showHideLabel_value == '收缩'){
     		$('#showHideLabel').text('更多...');
    	}else{
    		$('#showHideLabel').text('收缩');
    	}
    	$(dataGrid).datagrid('resize');
    });
};

MsgReceiveIndex.prototype.showRecord = function(detailUrl, unReadUrl, id){
	url = detailUrl + '/' + id + '_' + 'message';
	$.ewcms.openWindow({windowId:'#edit-window',iframeId:'#editifr', src : url,width:700,height:400,title:'内容'});
};