var PersonIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

PersonIndex.prototype.init = function(options){
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
            {field:'ip',title:'IP地址',width:120},
            {field:'recordTime',title:'投票时间',width:145},
            {field:'item',title:'填写内容',width:60,
              	formatter:function(val,rec){
              		var url = options.showUrl + '_' + rec.id;
               		return '<a href="javascript:void(0);" onclick="PersonIndex.show(' + url + ');">内容</a>';
               	}
            }
        ]]
	});
	
	PersonIndex.show = function(url){
		$('#editifr_record').attr('src',url);
		$.ewcms.openWindow({windowId:'#record-window',width:400,height:180,title:'内容'});
	};
};