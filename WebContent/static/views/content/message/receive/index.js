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
		    			 return "&nbsp;<img src='../../../../../static/image/msg/msg_read.gif' width='13px' height='13px' title='接收消息，已读'/>";
		    		 }else{
		    			 return "&nbsp;<img src='../../../../../static/image/msg/msg_unread.gif' width='13px' height='13px' title='接收消息，未读'/>";
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
		    			 return "&nbsp;<img src='../../../../../static/theme/icons/ok.png' width='13px' height='13px'/>";
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
			MsgReceiveIndex.refreshTipMessage(options.unReadUrl);
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
			MsgReceiveIndex.refreshTipMessage(options.unReadUrl);
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

MsgReceiveIndex.prototype.showRecord = function(detailUrl, unReadUrl, id){
	url = detailUrl + '/' + id + '_' + 'message';
	$.ewcms.openWindow({windowId:'#edit-window',iframeId:'#editifr', src : url,width:700,height:400,title:'内容'});
	MsgReceiveIndex.refreshTipMessage(unReadUrl);
};

MsgReceiveIndex.refreshTipMessage = function(unReadUrl){
	$.ajax({
		  type:'post',
		  datatype:'json',
		  cache:false,
		  url:unReadUrl,
		  data: '',
		  success:function(message, textStatus){
			  parent.parent.$('#tipMessage').empty();
		      var html = '<span id="messageFlash">';
		      if (message != 'false'){
		      	var tiplength = message.length;
		        html += '<a href="javascript:void(0);" onclick="javascript:_home.addTab(\'个人消息\',\'message/index.do\');return false;" onfocus="this.blur();" style="color:red;font-size:13px;text-decoration:none;">【<img src="./ewcmssource/image/msg/msg_new.gif"/>新消息(' + tiplength + ')】</a>';
		      }
		      html += '</span>';
		      parent.parent.$('#tipMessage').append(html);
		  }
	  });
};