var ReferIndex = function(dataGrid, refenceTree, copyTree){
	this._dataGrid = dataGrid || '#tt';
};

ReferIndex.prototype.init = function(options){
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
            		return rec.article.id;
                }  
			},
	        {field:'flags',title:'属性',width:60,
	            formatter:function(val,rec){
	            	var pro = [];
	                if (rec.top) pro.push("<img src='../../../../../static/image/article/top.gif' width='13px' height='13px' title='有效期限:永久置顶'/>"); 
	                if (rec.article.comment) pro.push("<img src='../../../../../static/image/article/comment.gif' width='13px' height='13px' title='允许评论'/>");
	                if (rec.article.type=="TITLE") pro.push("<img src='../../../../../static/image/article/title.gif' width='13px' height='13px' title='标题新闻'/>");
	                if (rec.reference) pro.push("<img src='../../../../../static/image/article/reference.gif' width='13px' height='13px' title='引用新闻'/>");
	                if (rec.article.inside) pro.push("<img src='../../../../../static/image/article/inside.gif' width='13px' height='13px' title='内部标题'/>");
	                if (rec.share) pro.push("<img src='../../../../../static/image/article/share.gif' width='13px' height='13px' title='共享' style='border:0'/>");
	                return pro.join("");
	             }
	         },
	         {field:'title',title:'标题<span style=\"color:red;\">[分类]</span>',width:500,
	             formatter:function(val,rec){
	                var classPro = [];
	                var categories = rec.article.categories;
	                for (var i=0;i<categories.length;i++){
	                	classPro.push(categories[i].categoryName);
	                }
	                var classValue = "";
	                if (classPro.length > 0){
	                    classValue = "<span style='color:red;'>[" + classPro.join(",") + "]</span>";
	                }
	                return rec.article.title + classValue;
	             }
	          },
	          {field:'owner',title:'创建者',width:80,formatter:function(val,rec){return rec.article.owner;}},
	          {field : 'statusDescription',title : '状态',width : 120,
	          	  formatter : function(val, rec) {
	          		  var processName = "";
	          		  if (rec.article.status == 'REVIEW' && rec.article.reviewProcess != null){
	          			  processName = "(" + rec.article.reviewProcess.name + ")";
	              	  }
	              	  return rec.article.statusDescription + processName;
	              }
	           }, 
	           {field:'published',title:'发布时间',width:145,formatter:function(val,rec){return rec.article.published;}},
	           {field:'modified',title:'修改时间',width:145,formatter:function(val,rec){return rec.article.modified;}},
	           {field:'sort',title:'排序号',width:60}
        ]]
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.openWindow({windowId:"#pop-window",iframeId:'#editifr_pop',src:options.referUrl, width:600,height:470,title:'新增引用文章'});
	});
	
	$('#tb-remove').bind('click', function(){
		var rows = $(dataGrid).datagrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择删除的文章","info");
			return;
		}
		var parameter = '';
       	for(var i=0;i<rows.length;i++){
       		parameter = parameter + '&articleMainIds=' + rows[i].id;
       	}
		$.post(options.deleteUrl, parameter ,function(data) {
			if (data == 'true'){
				$(dataGrid).datagrid('clearSelections');
				$(dataGrid).datagrid('reload');
				$("#edit-window").window("close");
			}else if (data == 'false'){
				$.messager.alert('失败','删除文章失败','info');
			}
		});	
	});
	
	$('#tb-save').bind('click', function(){
		var rows = editifr_pop.$(dataGrid).datagrid("getSelections");
		if (rows.length == 0){
			$.messager.alert("提示","请选择需引用的文章","info");
			return;
		}
		var parameter = 'channelId=' + $('#channelId').val();
       	for(var i=0;i<rows.length;i++){
       		parameter = parameter + '&articleMainIds=' + rows[i].id;
       	}
		$.post(options.saveUrl, parameter ,function(data) {
			if (data == 'true'){
				$(dataGrid).datagrid('clearSelections');
				$(dataGrid).datagrid('reload');
				$("#pop-window").window("close");
			}else if (data == 'false'){
				$.messager.alert('失败','新增引用失败','info');
			}
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