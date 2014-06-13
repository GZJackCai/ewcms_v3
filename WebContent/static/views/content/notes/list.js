var NotesList = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

NotesList.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    pageSize:20,
	    url: options.queryUrl,
		frozenColumns:[[
		    {field:"ck",checkbox:true},
		    {field:"id",title:"序号",width:50}
		]],
		columns:[[
		    {field:'title',title:'标题',width:200},
		    {field:'noteDate',title:'日期',width:67},
		    {field:'warn',title:'提醒',width:33,align:'left',align:'center',
		    	formatter:function(val,rec){
		    		var flag = '否';
		    		if (val){
		    			flag = '是';
		    		}
		    		return flag;
		    	}
		    },
		    {field:'warnTime',title:'提醒时间',width:60},
		    {field:'frequencyDescription',title:'提醒频率',width:80},
		    {field:'beforeDescription',title:'提前时间',width:80},
		    {field:'fireTime',title:'触发时间',width:145},
		    {field:'missRemind',title:'错过提醒',width:55,align:'left', align:'center',
		    	formatter:function(val,rec){
		    		var flag = '否';
		    		if (val){
		    			flag = '是';
		    		}
		    		return flag;
		    	}
		    } 
		]]
	});
	
	 $('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改',
			src : options.editUrl,
			width:420,
			height:330
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
	    
	$('#tb-close').bind('click', function(){
		parent.$('#notes-window').window('close');
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

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};