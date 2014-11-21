var MoreIndex = function(dataGrid){	
	this._dataGrid = dataGrid || '#tt';
};

MoreIndex.prototype.init = function(options){
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
		    {field:'userName',title:'用户',hidden:true},
			{field:'title',title:'标题',width:800},
	        {field:'sendTime',title:'发送时间',width:145}   
		]],
		view : detailview,
		detailFormatter : function(rowIndex, rowData) {
			return MoreIndex.detailGridData(rowData);
		},
		onExpandRow: function(index,row){  
			$('#tt').datagrid('fixDetailRowHeight',index);  
		}
	});
};

MoreIndex.detailGridData = function(rowData){
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
			htmls.push('<td><div class="datagrid-cell" style="width: 24px; text-align: center;"><span></span><span class="datagrid-sort-icon"><a href="javascript:void(0);" onclick="subscribe(' + rowData.id + ');return false;" onfocus="this.blur();">订阅</a></span></div></td>');
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
							+ '</span></div></td>');
			}
			htmls.push('</tr></table>');
		}
		htmls.push('</div></div>');
	}
	return htmls.join("");
};

MoreIndex.prototype.subscribe = function(subscribeUrl, id){
	var url = '<s:url namespace="/message/detail" action="subscribe"/>';
	$.post(subscribeUrl, {'id':id}, function(data) {
		if (data == 'own'){
			$.messager.alert('提示','您不能订阅自已发布的信息！','info');
			return;
		}
		if (data == 'exist'){
			$.messager.alert('提示','您已订阅了此信息，不需要再订阅！','info');
			return;
		}
		if (data == 'false'){
			$.messager.alert('提示','订阅信息失败！','info');
			return;
		}
		if (data == 'true'){
			$.messager.alert('提示','订阅成功！','info');
			return;
		}
	});
};
