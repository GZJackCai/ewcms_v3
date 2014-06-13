var ShareIndex = function(dataGrid, refenceTree, copyTree){
	this._dataGrid = dataGrid || '#tt';
	this._refenceTree = refenceTree || '#tt2';
	this._copyTree = copyTree || '#tt3';
};

ShareIndex.prototype.init = function(options){
    var dataGrid = this._dataGrid;
    var refenceTree = this._refenceTree;
    var copyTree = this._copyTree;
	
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
	          {field:'created',title:'创建者',width:80,formatter:function(val,rec){return rec.article.created;}},
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
	
	$('#tb-refence').bind('click', function(){
		$(refenceTree).tree( {
			checkbox : true,
			url : options.treeUrl
		});
		var rows = $(dataGrid).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择引用记录', 'info');
			return;
		}
		$.ewcms.openWindow({windowId:'#refence-window',width : 300,height : 400,title : '引用文章选择'});
	});
	
	$('#tb-refenceoperate').bind('click', function(){
		var checkeds = $(refenceTree).tree('getChecked');
		if (checkeds.length == 0) {
			$.messager.alert('提示', '请选择引用到目标的栏目', 'info');
			return;
		}
		var rootnode_tt2 = $(refenceTree).tree('getRoot');

		var parameter = '';
		var rows = $(dataGrid).datagrid('getSelections');
		for ( var i = 0; i < rows.length; i++) {
			parameter = parameter + '&selections=' + rows[i].id;
		}
		for ( var i = 0; i < checkeds.length; i++) {
			if (checkeds[i].id != rootnode_tt2.id) {
				parameter = parameter + '&selectChannelIds=' + checkeds[i].id;
			}
		}
		$.post(options.refenceUrl, parameter, function(data) {
			if (data == 'true') {
				$.messager.alert('成功', '引用文章成功', 'info');
				$('#copy-window').window('close');
			}else{
				$.messager.alert('失败','引用文章失败','error');
				return;
			}
		});
		
	});
	
	$('#tb-copy').bind('click', function(){
		$(copyTree).tree( {
			checkbox : true,
			url : options.treeUrl
		});
		var rows = $(dataGrid).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择复制记录', 'info');
			return;
		}
		$.ewcms.openWindow({widnowId:'#copy-window', width : 300, height : 400,title : '复制文章选择'});
	});
	
	$('#tb-copyoperate').bind('click', function(){
		var checkeds = $(copyTree).tree('getChecked');
		if (checkeds.length == 0) {
			$.messager.alert('提示', '请选择复制到目标的栏目', 'info');
			return;
		}
		var rootnode_tt3 = $(copyTree).tree('getRoot');

		var parameter = '';
		var rows = $(dataGrid).datagrid('getSelections');
		for ( var i = 0; i < rows.length; i++) {
			parameter = parameter + '&selections=' + rows[i].id;
		}

		for ( var i = 0; i < checkeds.length; i++) {
			if (checkeds[i].id != rootnode_tt3.id) {
				parameter = parameter + '&selectChannelIds=' + checkeds[i].id;
			}
		}
		$.post(options.copyUrl, parameter, function(data) {
			if (data == 'true') {
				$.messager.alert('成功', '复制文章成功', 'info');
				$('#copy-window').window('close');
			}else{
				$.messager.alert('失败','复制文章失败','error');
				return;
			}
		});		
	});
	
	$('#tb-preview').bind('click', function(){
		var rows = $(dataGrid).datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择预览记录', 'info');
			return;
		}
		if (rows.length > 1) {
			$.messager.alert('提示', '只能选择一个预览', 'info');
			return;
		}
		window.open(options.previewUrl + '/' + rows[0].channelId + '_' + rows[0].article.id,'popup','width=1280,height=700,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,scrollbars=yes,left=' + (window.screen.width - 1280)/ 2 + ',top=' + (window.screen.height - 700) / 2);
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