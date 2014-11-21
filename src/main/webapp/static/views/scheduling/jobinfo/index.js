var JobInfoIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

JobInfoIndex.prototype.init = function(options){
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
            {field:'label',title:'名称',width:200},
	        {field:'version',title:'版本',width:40},
	        {field:'jobClassName',title:'作业名称',width:200,
		       formatter:function(val,rec){
		       		return rec.jobClass.className;
	           }
	        },
	        {field:'state',title:'状态',width:80},
	        {field:'startTime',title:'开始时间',width:145},
	        {field:'previousFireTime',title:'上次执行时间',width:145},
	        {field:'nextFireTime',title:'下次执行时间',width:145},
	        {field:'endTime',title:'结束时间',width:145},
	        {field:'operation',title:'操作',width:50,align:'center',
	        	formatter:function(val,rec){
	        		var button_html = "";
	                if (rec.state=='正常'){
	                	button_html = "<a href='" + options.pauseUrl + "/" + rec.id + "'><img src='../../static/image/scheduling/pause.png' width='13px' height='13px' title='暂停操作'/></a>";
	                }else if (rec.state=='暂停'){
	                	button_html = "<a href='" + options.resumedUrl + "/" + rec.id + "'><img src='../../static/image/scheduling/resumed.png' width='13px' height='13px' title='恢复操作'/></a>";
	                }
	                return button_html;
	             }
	         }
        ]]
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'新增',
			width : 1040,
			height : 510
		});
	});

    $('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改',
			width : 1040,
			height : 510
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
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};