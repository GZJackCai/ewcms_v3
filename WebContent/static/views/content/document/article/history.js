var HistoryArticle = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

HistoryArticle.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
		nowrap:true,
	    pagination:false,
	    rownumbers:true,
	    singleSelect:true,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:"ck",checkbox:true},
		    {field:'historyId',title:'编号',width:60,hidden:true}
		]],
		columns:[[
            {field:'version',title:'版本号',width:100,sortable:true},
            {field:'maxPage',title:'页数',width:200},
	        {field:'historyTime',title:'时间',width:148, 
            	formatter : function(val, rec){
            		var date = new Date(val);
                    return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + ' ' + date.getHours() + ':' + date.getMinutes() + ':' + date.getSeconds();
            	}
	        }
        ]]
	});
	
	$('#td-restore').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
        if(rows.length == 0){
        	$.messager.alert('提示','请选择还原记录','info');
            return;
        }	
        if(rows.length > 1){
			$.messager.alert('提示','只能选择一条记录','info');
			return;
	    }
        
        var details = {};
    	$.post(options.restoreHistoryUrl + '/' + rows[0].historyId, {} ,function(data) {
    		details = data;
    	});
        
        $.messager.confirm("提示","确定要把第 【" + rows[0].version + "】号 版本替换当前的文章吗?",function(r){
    		if (r){
    			if (rows[0].maxPage == 0){
    				parent.$('#table_content').hide();
    				parent.$('#pageBarTable_general').hide();
    				parent.$('#pageBarTable_title').show();
    				parent.$('#tr_url').show();
    				parent.$('#inside').attr('disabled', true);
    				parent.$('#inside').attr('checked', false);
    			}else{
    				parent.$('#table_content').show();
        			parent.$('#pageBarTable_general').show();
        			parent.$('#pageBarTable_title').hide();
        			parent.$('#tr_url').hide();
        			parent.$('#inside').removeAttr('disabled');
        			parent.$('#ShowShortTitle').removeAttr('disabled');
        			parent.$('#ShowSubTitle').removeAttr('disabled');
        			
	    			parent.currentPage = 1;
	    			if (rows[0].maxPage != parent.pages){
	    				if(rows[0].maxPage > parent.pages){
	    					for(var i = parent.pages+1; i <= rows[0].maxPage;i++){
	    						parent.ArticleEdit.addPage();
	    					}
	    				}else{
	    					for (var i = parent.pages; i > rows[0].maxPage; i--){
	    						parent.ArticleEdit.delPage();
	    					}
	    				}
	    			}
	    			for (var i = 1; i <= parent.pages; i++){
	    				if (parent.tinyMCE.getInstanceById('_Content_' + i) != null){
	    					parent.tinyMCE.get('_Content_' + i).setContent(details[i-1]);
	    				}else{
	    					parent.$("#_Content_" + i).val(details[i-1]);
	    				}
	    			}
	    			parent.ArticleEdit.setActivePage(1);
    			}
    			parent.$('#pop-window').window('close');
    		}
    	});
        
	});
	
	$("#tb-query").bind('click', function(){
    	$.ewcms.query({
    		src:options.queryUrl
    	});
    });
	
	$('#tb-clear').bind('click', function(){
    	$('#queryform')[0].reset();
    	$('#startDate').datebox('setValue','');
    	$('#endDate').datebox('setValue','');
    });
	
	$('#tb-cancel').bind('click', function(){
		parent.$('#pop-window').window('close');
	});
};