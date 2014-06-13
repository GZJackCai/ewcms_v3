var SendIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

SendIndex.prototype.init = function(options){
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
			{field:'title',title:'标题',width:800},
			{field:'sendTime',title:'发送时间',width:145},
			{field:'typeDescription',title:'类型',width:40},
			{field:'msgReceiveRealName',title:'接收用户',width:300}       
		]],
		view : detailview,
		detailFormatter : function(rowIndex, rowData) {
			return SendIndex.detailGridData(dataGrid, options.contentDeleteUrl, rowData);
		},
		onExpandRow: function(index,row){  
			$(dataGrid).datagrid('fixDetailRowHeight',index);  
		}
	});
	
	$('#menu-remove').bind('click', function(){
    	$.ewcms.remove({
    		title:'删除'
    	});
    });
	
	$('#menu-add-message').bind('click', function(){
		$.ewcms.add({
			title:'新增-消息',
			width:650,
			height:300
		});
	});
	
	$('#menu-add-subscription').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择新增订阅的记录","info");
			return;
		}
		if (rows.length > 1){
			$.messager.alert("提示","只能选择一个记录进行新增订阅","info");
			return;
		}
		if (rows[0].type == 'SUBSCRIPTION'){
			var url = options.contentUrl + '/' + rows[0].id;
			$.ewcms.openWindow({windowId:'#edit-window', iframeId:'#editifr', src : url, width : 550,height : 200,title : '新增-订阅'});
		}else{
			$.messager.alert('提示','只能是订阅的记录才能再新增订阅内容','info');
		}
	});
	
    $('#tb-query').bind('click', function(){
    	$.ewcms.query({
    		src : options.queryUrl
    	});
    });
    
   $('#tb-clear').bind('click', function(){
    	$('#queryform')[0].reset();
    	$('#sendTimeStart').datebox('setValue','');
    	$('#sendTimeEnd').datebox('setValue','');
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

SendIndex.detailGridData = function(dataGrid, deleteUrl, rowData){
	var htmls = [];
	if (rowData.msgContents.length == 0) {
		htmls.push('<div style="padding:5px 0">没有内容记录!</div>');
	} else {
		htmls.push('<div style="padding:5px 0;"><div class="datagrid-header" style="height:22px;">');
		htmls.push('<div style="float:left;display: block;">');
		htmls.push('<table cellspacing="0" cellpadding="0" border="0" style="height: 23px;">');
		htmls.push('<tr style="height: 21px">');
		htmls.push('<td><div style="margin: 0;overflow: hidden; padding: 3px 4px; word-wrap: normal; width: 20px; text-align: center;"><span></span><span class="datagrid-sort-icon">&nbsp;</span></div></td>');
		htmls.push('<td><div style="margin: 0;overflow: hidden; padding: 3px 4px; word-wrap: normal; width: 1000px; text-align: left;"><span>内容</span><span class="datagrid-sort-icon">&nbsp;</span></div></td>');
		if (rowData.type == 'SUBSCRIPTION'){
			htmls.push('<td><div class="datagrid-cell" style="width: 24px; text-align: center;"><span></span><span class="datagrid-sort-icon">&nbsp;</span></div></td>');
		}
		htmls.push('</tr>');
		htmls.push('</table>');
		htmls.push('</div>');
		htmls.push('</div>');
		htmls.push('<div class="datagrid-body">');
		for ( var i = 0; i < rowData.msgContents.length; i++) {
			htmls.push('<table cellspacing="0" cellpadding="0" border="0"><tr style="height: 21px">'
							+ '<td>'
							+ '<div style="margin: 0;overflow: hidden; padding: 3px 4px; word-wrap: normal; width: 20px; text-align: center;">'
							+ (rowData.msgContents.length - i)
							+ '</td>'
							+ '</div>'
							+ '<td>'
							+ '<div style="margin: 0;overflow: hidden; padding: 3px 4px; word-wrap: normal; width: 1000px; text-align: left;">'
							+ rowData.msgContents[i].detail
							+ '</div>'
							+ '</td>');
			if (rowData.type == 'SUBSCRIPTION'){
				htmls.push('<td><div class="datagrid-cell" style="width: 24px; text-align: center;"><span>'
							+ '<a href="javascript:void(0);" onclick="_sendIndex.delSubscription(\'' + dataGrid + '\',\'' + deleteUrl + '\',' + rowData.msgContents[i].id + ')" style="text-decoration:none;">删除</a>'
							+ '</span></div></td>');
			}
			htmls.push('</tr></table>');
		}
		htmls.push('</div></div>');
	}
	return htmls.join("");
};

SendIndex.prototype.delSubscription = function(dataGrid, deleteUrl, id){
	$.post(deleteUrl, {'selections':id}, function(data){
		if (data){
			$(dataGrid).datagrid('reload');
		}
	});
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};