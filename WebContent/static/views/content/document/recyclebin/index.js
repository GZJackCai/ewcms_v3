var RecyclebinIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

RecyclebinIndex.prototype.init = function(options){
	currentnode = parent.currentnode;
	
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
        ]],
        view : detailview,
		detailFormatter : function(rowIndex, rowData) {
			return '<div id="ddv-' + rowIndex + '"></div>';
		},
		onExpandRow: function(rowIndex, rowData){
			$('#ddv-' + rowIndex).panel({
				border:false,
				cache:false,
				content: '<iframe src="' + options.operateTrackUrl + '/' + rowData.id + '" frameborder="0" width="100%" height="275px" scrolling="auto"></iframe>',
				onLoad:function(){
					$('#tt').datagrid('fixDetailRowHeight',rowIndex);
				}
			});
			$(dataGrid).datagrid('fixDetailRowHeight',rowIndex);
		}
	});
	
	$('#tb-restore').bind('click', function(){
		var rows = $(dataGrid).datagrid('getSelections');
        if(rows.length == 0){
        	$.messager.alert('提示','请选择恢复记录','info');
            return;
        }
        
        var parameters = '';
        for(var i=0;i<rows.length;i++){
        	parameters = parameters + 'selections=' + rows[i].id + '&';
        }
		$.messager.confirm("提示","确定要恢复所选记录吗?",function(r){
			if (r){
	           	$.post(options.restoreUrl,parameters,function(data){
	           		$.messager.alert('成功','恢复文档成功', 'info');
		    		$(dataGrid).datagrid('clearSelections');
		    		$(dataGrid).datagrid('reload');
	    	   	});
			}
		});
	});
	
	$('#tb-remove').bind('click', function(){
		$.ewcms.remove({
    		title:'删除',
    		src : options.deleteUrl
    	});
	});
	
	$('#tb-query').bind('click', function(){
    	$.ewcms.query({
    		src : options.queryUrl
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
    
    if (currentnode.attributes.mask >=3){
		$('#tb-restore').linkbutton('enable');
		$('#tb-remove').linkbutton('enable');
    }else if (currentnode.attributes.mask >= 2) {
		$('#tb-restore').linkbutton('enable');
		$('#tb-remove').linkbutton('disable');
	}else{
		$('#tb-restore').linkbutton('disable');
		$('#tb-remove').linkbutton('disable');
	}
};