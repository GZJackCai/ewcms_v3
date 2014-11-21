var RelationIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

RelationIndex.prototype.init = function(options){
    var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',hidden:true}
		]],
		columns:[[
		    {field:'articleId',title:'编号',width:60,
		    	formatter:function(val,rec){
		    		return rec.relationArticle.id;
		    	}  
		    },
		    {field:'flags',title:'属性',width:60,
		    	formatter:function(val,rec){
		    		var pro = [];
		    		if (rec.comment) pro.push("<img src='../../../../../static/image/article/comment.gif' width='13px' height='13px' title='允许评论'/>");
		    		if (rec.type=="TITLE") pro.push("<img src='../../../../../static/image/article/title.gif' width='13px' height='13px' title='标题新闻'/>");
		    		if (rec.reference) pro.push("<img src='../../../../../static/image/article/reference.gif' width='13px' height='13px' title='引用新闻'/>");
		    		if (rec.inside) pro.push("<img src='../../../../../static/image/article/inside.gif' width='13px' height='13px' title='内部标题'/>");
		    		if (rec.share) pro.push("<img src='../../../../../static/image/article/share.gif' width='13px' height='13px' title='共享' style='border:0'/>");
		    		return pro.join("");
		    	}
		    },
		    {field:'title',title:'标题<span style=\"color:red;\">[分类]</span>',width:500,
		    	formatter:function(val,rec){
		    		var classPro = [];
		    		var categories = rec.relationArticle.categories;
		    		for (var i=0;i<categories.length;i++){
		    			classPro.push(categories[i].categoryName);
		    		}
		    		var classValue = "";
		    		if (classPro.length > 0){
		    			classValue = "<span style='color:red;'>[" + classPro.join(",") + "]</span>";
		    		}
		    		return rec.relationArticle.title + classValue;
		    	}
		    },
		    {field:'statusDescription',title:'状态',width:60,
		    	formatter:function(val,rec){
		    		return rec.relationArticle.statusDescription;
		    	}
		    },
		    {field:'published',title:'发布时间',width:145,
		    	formatter:function(val,rec){
		    		return rec.relationArticle.published;
		    	}
		    },
		    {field:'modified',title:'修改时间',width:145,
		    	formatter:function(val,rec){
		    		return rec.relationArticle.modified;
		    	}
		    },
	        {field:'sort',title:'排序号',width:60,
		    	formatter:function(val,rec){
		    		return rec.relationArticle.sort;
		    	}
		    }
        ]]
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.openWindow({windowId:"#edit-window",iframeId:'#editifr',src:options.addUrl,width:730,height:400,title:'文章选择'});
	});
	
	$('#tb-remove').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
        if(rows.length == 0){
        	$.messager.alert('提示','请选择修改记录','info');
            return;
        }
	    var params = '';
       	for(var i = 0; i < rows.length; i++){
       		params += 'selections=' + rows[i].id + '&';
       	}
        $.post(options.deleteUrl,params,function(data){
            $.messager.alert('成功','删除成功');
            $(dataGrid).datagrid('clearSelections');
            $(dataGrid).datagrid('reload');
        });
	});
	
	$('#tb-up').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择移动记录","info");
			return;
		}
		if (rows.length > 1){
			$.messager.alert("提示","只能选择一个记录进行移动","info");
			return;
		}
		$.post(options.upUrl + '_' + rows[0].relationArticle.id, {}, function(data){
			if (data == "false"){
				$.messager.alert("提示","上移失败","info");
				return;
			}
			$(dataGrid).datagrid('reload');
		});
	});
	
	$('#tb-down').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择移动记录","info");
			return;
		}
		if (rows.length > 1){
			$.messager.alert("提示","只能选择一个记录进行移动","info");
			return;
		}
		$.post(options.downUrl + '_' + rows[0].relationArticle.id, {}, function(data){
			if (data == "false"){
				$.messager.alert("提示","下移失败","info");
				return;
			}
			$(dataGrid).datagrid('reload');
		});
	});
	
	$('#tb-save').bind('click', function(){
		var rows = editifr.getRelationRows();
	    var params = '';
       	for(var i=0;i<rows.length;i++){
       		params += 'selections=' + rows[i].article.id + '&';
       	}
       	alert(params);
		$.post(options.saveUrl, params ,function(data) {
			if (data){
				$(dataGrid).datagrid("reload");
				$("#edit-window").window("close");
			}
		});
	});
	
	$('#tb-cancel').bind('click', function(){
		parent.$('#pop-window').window('close');
	});
	
	$('#tb-query').bind('click', function(){
		$.ewcms.query({
			src : options.queryUrl
		});
	});
};