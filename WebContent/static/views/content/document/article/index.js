/*
 * Article Index JavaScript Library v2.0.0
 * 
 * Licensed under the GPL terms To use it on other terms please contact us
 * 
 * author wu_zhijun
 */
var ArticleIndex = function(dataGrid, tree){
	this._dataGrid = dataGrid || '#tt';
	this._tree = tree || '#tt3';
	this.currentnode, this.rootnode;//当前所选择的节点，父节点
	this.sort = '';//排序值
};

ArticleIndex.prototype.init = function(options){
	currentnode = parent.currentnode;
	rootnode = parent.rootnode;
	
    var dataGrid = this._dataGrid;
	var tree = this._tree;
    
	ArticleIndex.disableButtons();
	ArticleIndex.channelPermission(rootnode, currentnode);
	
	if (currentnode.attributes.type == 'NODE'){
		ArticleIndex.nodeArticleMenu();
	}
	//if ($('#node').val()=='true'){
	//	ArticleIndex.nodeArticleMenu();
	//}
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    url:options.queryUrl,
	    pageSize:30,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',hidden:true}
		]],
		columns:[[
            {field:'articleId',title:'文章编号',width:60,
            	formatter:function(val,rec){
            		return rec.article.id;
                }  
			},
	        {field:'flags',title:'属性',width:60,
	            formatter:function(val,rec){
	            	var pro = [];
	                if (rec.top) pro.push("<img src='../../../../../static/image/article/top.gif' width='13px' height='13px' title='有效期限:永久置顶'/>"); 
	                if (rec.article.isComment) pro.push("<img src='../../../../../static/image/article/comment.gif' width='13px' height='13px' title='允许评论'/>");
	                if (rec.article.genre=="TITLE") pro.push("<img src='../../../../../static/image/article/title.gif' width='13px' height='13px' title='标题新闻'/>");
	                if (rec.idReference) pro.push("<img src='../../../../../static/image/article/reference.gif' width='13px' height='13px' title='引用新闻'/>");
	                if (rec.article.inside) pro.push("<img src='../../../../../static/image/article/inside.gif' width='13px' height='13px' title='内部标题'/>");
	                if (rec.isShare) pro.push("<img src='../../../../../static/image/article/share.gif' width='13px' height='13px' title='共享' style='border:0'/>");
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
        ]],
        onSelect : function(rowIndex, rowData){
			ArticleIndex.adjustMenu(rowData.article.status);
		},
        view : detailview,
		detailFormatter : function(rowIndex, rowData) {
			return '<div id="ddv-' + rowIndex + '"></div>';
		},
		onExpandRow: function(rowIndex, rowData){
			$('#ddv-' + rowIndex).panel({
				border:false,
				cache:false,
				content: '<iframe src="' + options.operateTrackUrl + '/' + rowData.id + '" frameborder="0" width="100%" height="315px" scrolling="auto"></iframe>',
				onLoad:function(){
					$('#tt').datagrid('fixDetailRowHeight',rowIndex);
				}
			});
			$(dataGrid).datagrid('fixDetailRowHeight',rowIndex);
		}
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
    
    $('#menu-preview').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
    	if (rows.length == 0) {
    		$.messager.alert('提示', '请选择预览记录', 'info');
    		return;
    	}
    	if (rows.length > 1) {
    		$.messager.alert('提示', '只能选择一个预览', 'info');
    		return;
    	}
    	window.open(options.previewUrl + '&articleId=' + rows[0].article.id,'popup','width=1280,height=700,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,scrollbars=yes,left=' + (window.screen.width - 1280)/ 2 + ',top=' + (window.screen.height - 700) / 2);	
    });
    
    $('#menu-operate-add').bind('click', function(){
    	if (currentnode.id != 0 && currentnode.id != rootnode.id) {
    		//window.open(options.editUrl,'popup','width=1280,height=700,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,left=' + (window.screen.width - 1280)/ 2 + ',top=' + (window.screen.height - 700) / 2);
    		$.ewcms.openArticleTopWindow({width:1280,height:700,title:'文章编辑', url:options.editUrl + '/?mask=' + currentnode.attributes.mask, position:'', dataGridObj:$(dataGrid)});
    	} else {
    		$.messager.alert('提示', '请选择栏目', 'info');
    	}
    });
    
    $('#menu-operate-upd').bind('click', function(){
    	if (currentnode.id != rootnode.id) {
    		var rows = $(dataGrid).datagrid('getSelections');
    		if (rows.length == 0) {
    			$.messager.alert('提示', '请选择修改记录', 'info');
    			return;
    		}
    		if (rows.length > 1) {
    			$.messager.alert('提示', '只能选择一个修改', 'info');
    			return;
    		}
    		if (rows[0].reference == true) {
    			$.messager.alert('提示', '引用文章不能修改', 'info');
    			return;
    		}
    		var url_param = '/?articleMainId=' + rows[0].id + '&mask=' + currentnode.attributes.mask;
    		$.ewcms.openArticleTopWindow({width:1280,height:700,title:'文章编辑', url:options.editUrl + url_param, position:'', dataGridObj:$(dataGrid)});
    		//window.open(inputUrl + url_param,'popup','width=1280,height=700,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,left=' + (window.screen.width - 1280) / 2 + ',top=' + (window.screen.height - 700)/ 2);
    	} else {
    		$.messager.alert('提示', '请选择栏目', 'info');
    	}
    });
    
    $('#menu-operate-remove').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
    	if (rows.length == 0) {
    		$.messager.alert('提示', '请选择删除记录', 'info');
    		return;
    	}

    	var parameter = 'selections=' + rows[0].id;
    	for ( var i = 1; i < rows.length; i++) {
    		parameter = parameter + '&selections=' + rows[i].id;
    	}
    	$.messager.confirm('提示', '确定要删除所选记录到回收站吗?', function(r) {
    		if (r) {
    			$.post(options.deleteUrl, parameter, function(data) {
    				if (data){
    					$.messager.alert('成功', '删除文档到回收站成功!', 'info');
    				}
    				$(dataGrid).datagrid('clearSelections');
    				$(dataGrid).datagrid('reload');
    			});
    		}
    	});
    });
    
    $('#menu-operate-copy').bind('click', function(){
    	$(tree).tree( {
    		checkbox : true,
    		url : options.treeUrl
    	});
    	var rows = $(dataGrid).datagrid('getSelections');
    	if (rows.length == 0) {
    		$.messager.alert('提示', '请选择复制记录', 'info');
    		return;
    	}
    	$('#tb-move').hide();
    	$('#tb-copy').show();
    	$.ewcms.openWindow({windowId:'#moveorcopy-window', width:600,height:400,title:'复制文章选择'});
    });
    
    $('#tb-copy').bind('click', function(){
    	var checkeds = $(tree).tree('getChecked');
    	if (checkeds.length == 0) {
    		$.messager.alert('提示', '请选择复制到目标的栏目', 'info');
    		return;
    	}
    	var rootnode_tt3 = $(tree).tree('getRoot');

    	var parameter = '';
    	var rows = $(dataGrid).datagrid('getSelections');
    	for ( var i = 0; i < rows.length; i++) {
    		parameter = parameter + '&selections=' + rows[i].id;
    	}
    	for ( var i = 0; i < checkeds.length; i++) {
    		if (checkeds[i].id != rootnode_tt3.id) {
    			parameter = parameter + '&targetChannelIds=' + checkeds[i].id;
    		}
    	}
    	$.post(options.copyArticleUrl, parameter, function(data) {
    		if (data) {
    			$.messager.alert('成功', '复制文章成功', 'info');
    			$(dataGrid).datagrid('clearSelections');
    			$(dataGrid).datagrid('reload');
    		}
    		$('#moveorcopy-window').window('close');
    	});
    });
    
    $('#menu-operate-move').bind('click', function(){
    	$(tree).tree( {
    		checkbox : false,
    		url : options.treeUrl
    	});
    	var rows = $(dataGrid).datagrid('getSelections');
    	if (rows.length == 0) {
    		$.messager.alert('提示', '请选择移动记录', 'info');
    		return;
    	}
    	$('#tb-move').show();
    	$('#tb-copy').hide();
    	$.ewcms.openWindow({windowId:'#moveorcopy-window', width:600,height:400, title:'移动文章选择'});
    });
    
    $('#tb-move').bind('click', function(){
    	var selected = $(tree).tree('getSelected');
    	if (selected == null || typeof (selected) == 'undefined') {
    		$.messager.alert('提示', '请选择移动到目标的栏目', 'info');
    		return;
    	}
    	var rootnode_tt3 = $(tree).tree('getRoot');

    	var parameter = '';
    	var rows = $(dataGrid).datagrid('getSelections');
    	for ( var i = 0; i < rows.length; i++) {
    		parameter = parameter + '&selections=' + rows[i].id;
    	}

    	if (selected.id != rootnode_tt3.id) {
    		parameter = parameter + '&targetChannelIds=' + selected.id;
    	} else {
    		$.messager.alert('提示', '文章不能移动到根栏目', 'info');
    		return;
    	}

    	$.post(options.moveArticleUrl, parameter, function(data) {
    		if (data) {
    			$.messager.alert('成功', '移动文章成功', 'info');
    			$(dataGrid).datagrid('clearSelections');
    			$(dataGrid).datagrid('reload');
    		}
    		$('#moveorcopy-window').window('close');
    	});
    });
    
    $('#menu-top-ok').bind('click', function(){
    	if (currentnode.id != rootnode.id) {
    		var rows = $(dataGrid).datagrid('getSelections');
    		if (rows.length == 0) {
    			$.messager.alert('提示', '请选择记录', 'info');
    			return;
    		}
    		var parameter = 'isTop=true';
    		for ( var i = 0; i < rows.length; i++) {
    			parameter = parameter + '&selections=' + rows[i].id;
    		}
    		$.post(options.topUrl, parameter, function(data) {
    			if (data) {
    				$.messager.alert('提示', '设置文章置顶成功', 'info');
    				$(dataGrid).datagrid('clearSelections');
    				$(dataGrid).datagrid('reload');
    			}
    		});
    	}
    });
    
    $('#menu-top-cancel').bind('click', function(){
    	if (currentnode.id != rootnode.id) {
    		var rows = $(dataGrid).datagrid('getSelections');
    		if (rows.length == 0) {
    			$.messager.alert('提示', '请选择记录', 'info');
    			return;
    		}
    		var parameter = 'isTop=false';
    		for ( var i = 0; i < rows.length; i++) {
    			parameter = parameter + '&selections=' + rows[i].id;
    		}
    		$.post(options.topUrl, parameter, function(data) {
    			if (data) {
    				$.messager.alert('提示', '取消文章置顶成功', 'info');
    				$(dataGrid).datagrid('clearSelections');
    				$(dataGrid).datagrid('reload');
    			}
    		});
    	}
    });
    
    $('#menu-share-ok').bind('click', function(){
    	if (currentnode.id != rootnode.id) {
    		var rows = $(dataGrid).datagrid('getSelections');
    		if (rows.length == 0) {
    			$.messager.alert('提示', '请选择记录', 'info');
    			return;
    		}
    		var parameter = 'isShare=true';
    		for ( var i = 0; i < rows.length; i++) {
    			parameter = parameter + '&selections=' + rows[i].id;
    		}
    		$.post(options.shareUrl, parameter, function(data) {
    			if (data) {
    				$.messager.alert('提示', '设置文章共享成功', 'info');
    				$(dataGrid).datagrid('clearSelections');
    				$(dataGrid).datagrid('reload');
    			}
    		});
    	}
    });
    
    $('#menu-share-cancel').bind('click', function(){
    	if (currentnode.id != rootnode.id) {
    		var rows = $(dataGrid).datagrid('getSelections');
    		if (rows.length == 0) {
    			$.messager.alert('提示', '请选择记录', 'info');
    			return;
    		}
    		var parameter = 'isShare=false';
    		for ( var i = 0; i < rows.length; i++) {
    			parameter = parameter + '&selections=' + rows[i].id;
    		}
    		$.post(options.shareUrl, parameter, function(data) {
    			if (data) {
   					$.messager.alert('提示', '取消文章共享成功', 'info');
    				$(dataGrid).datagrid('clearSelections');
    				$(dataGrid).datagrid('reload');
    			}
    		});
    	}
    });
    
    $('#menu-sort-ok').bind('click', function(){
    	if (currentnode.id != rootnode.id) {
    		var rows = $(dataGrid).datagrid('getSelections');
    		if (rows.length == 0) {
    			$.messager.alert('提示', '请选择排序记录', 'info');
    			return;
    		}
    		if (rows.length > 1) {
    			$.messager.alert('提示', '只能选择一个排序', 'info');
    			return;
    		}
    		$.messager.prompt('是否要对所选中的文章进行排序',	'请输入排序号',function(r) {
    			if (r) {
    				var reg = /^\d+$/;
    				if (reg.test(r)) {
    					var parameter = {};
    					parameter['selections'] = rows[0].id;
    					parameter['isTop'] = rows[0].top;
    					parameter['sort'] = r;
    					$.post(options.isSortUrl, parameter, function(data) {
    						if (data) {//用户输入的排序号与系统中的排序号出现重复，显示是插入还是替换选项页面
    							sort = r;
    							$.ewcms.openWindow({windowId:'#sort-window',width : 550,height : 200,title : '排序'});
    						}else{
    							parameter['isInsert'] = -1;
    							$.post(options.sortUrl, parameter, function(data) {
    								if (data) {
    									$.messager.alert('提示','设置排序号成功','info');
    									$(dataGrid).datagrid('clearSelections');
    									$(dataGrid).datagrid('reload');
    								}
    							});
    						}
    					});
    				}
    			}
    		});
    	}
    });
    
    $('#tb-sort').bind('click', function(){
    	$.post(options.sortUrl, {
    		'selections' : $(dataGrid).datagrid('getSelections')[0].id,
    		'isTop' : $(dataGrid).datagrid('getSelections')[0].top,
    		'isInsert' : $('input[name=\'sortRadio\']:checked').val(),
    		'sort' : sort
    	}, function(data) {
    		$('#sort-window').window('close');
    		if (data) {
    			$.messager.alert('提示', '设置排序号成功', 'info');
    			$(dataGrid).datagrid('clearSelections');
    			$(dataGrid).datagrid('reload');
    		}
    	});
    });
    
    $('#menu-sort-clear').bind('click', function(){
    	if (currentnode.id != rootnode.id) {
    		var rows = $(dataGrid).datagrid('getSelections');
    		if (rows.length == 0) {
    			$.messager.alert('提示', '请选择清除排序记录', 'info');
    			return;
    		}
    		var parameter = '';
    		for ( var i = 0; i < rows.length; i++) {
    			parameter = parameter + '&selections=' + rows[i].id;
    		}
    		$.post(options.clearSortUrl, parameter, function(data) {
    			if (data) {
    				$.messager.alert('提示', '设置消除排序号成功', 'info');
    				$(dataGrid).datagrid('clearSelections');
    				$(dataGrid).datagrid('reload');
    			}
    		});
    	}
    });
    
    $('#menu-approve-submit').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
    	if (rows.length == 0) {
    		$.messager.alert('提示', '请选择提交审核记录', 'info');
    		return;
    	}
    	var parameter = '';
    	var rows = $(dataGrid).datagrid('getSelections');
    	for ( var i = 0; i < rows.length; i++) {
    		parameter = parameter + '&selections=' + rows[i].id;
    	}
    	$.post(options.approveUrl, parameter, function(data) {
    		if (!data) {
    			if (data == 'system-false') {
    				$.messager.alert('提示', '文章提交审核失败', 'info');
    			} else if (data == 'accessdenied') {
    				$.messager.alert('提示', '您没有提交审核文章的权限', 'info');
    			} else if (data == 'notinstate') {
    				$.messager.alert('提示', '文章只有在初稿或重新编辑状态下才能提交审核', 'info');
    			}
    		} else {
    			$.messager.alert('提示', '文章提交审核成功', 'info');
    			$(dataGrid).datagrid('clearSelections');
    			$(dataGrid).datagrid('reload');
    		}
    	});
    });
    
    $('#menu-approve-ok').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
    	if (rows.length == 0) {
    		$.messager.alert('提示', '请选择审核的文章', 'info');
    		return;
    	}
    	if (rows.length > 1) {
    		$.messager.alert('提示', '只能选择一个审核', 'info');
    		return;
    	}
    	if (rows[0].article.status == 'REVIEW') {
    		var parameter = {};
    		parameter['selections'] = rows[0].id;
    		//parameter['channelId'] = $('#channelId').val();
    		$.post(options.effectiveUrl, parameter, function(data) {
    			if (data){
    				$.ewcms.openWindow({windowId:'#approve-window',width:550,height:230,title:'审核'});
    			}else{
    				$.messager.alert('提示', '您没有权限审核此文章', 'info');
    			}
    		});
    	} else {
    		$.messager.alert('提示', '文章只能在审核中状态才能审核', 'info');
    	}
    });
    
    $('#tb-approve').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
    	if (rows.length == 0) {
    		$.messager.alert('提示', '请选择审核记录', 'info');
    		return;
    	}
    	if (rows.length > 1) {
    		$.messager.alert('提示', '只能选择一个审核', 'info');
    		return;
    	}

    	var parameter = {};
    	var review = $('input[name="reviewRadio"]:checked').val();
    	if (review == 1 && $.trim($('#reason').val()) == ''){
    		$.messager.alert('提示', '请输入不通过原因', 'info');
    		return;
    	}
    	parameter['review'] = review;
    	parameter['selections'] = rows[0].id;
    	parameter['reason'] = $('#reason').val();

    	$.post(options.approveArticleUrl, parameter, function(data) {
    		if (data){
	    		$.messager.alert('提示', '文章审核成功', 'info');
	    		$(dataGrid).datagrid('clearSelections');
	    		$(dataGrid).datagrid('reload');
	    		$('#approve-window').window('close');
    		}
    	});
    });
    
    $('#menu-publish-independent ').bind('click', function(){
    	$.post(url, {}, function(data) {
    		if (data == 'system-false') {
    			$.messager.alert('提示', '系统错误', 'error');
    		} else if (data == 'accessdenied') {
    			$.messager.alert('提示', '没有发布权限', 'info');
    		} else if (data != 'true'){
    			$.messager.alert('提示', data, 'info');
    		} else {
    			articleReload();
    			$.messager.alert('提示', '发布正在后台运行中...', 'info');
    		}
    	});
    });
    
    $('menu-publish-relevance').bind('click', function(){
    	$.post(url, {}, function(data) {
    		if (data == 'system-false') {
    			$.messager.alert('提示', '系统错误', 'error');
    		} else if (data == 'accessdenied') {
    			$.messager.alert('提示', '没有发布权限', 'info');
    		} else if (data != 'true'){
    			$.messager.alert('提示', data, 'info');
    		} else {
    			articleReload();
    			$.messager.alert('提示', '发布正在后台运行中...', 'info');
    		}
    	});
    });
    
    $('#tb-publish-back').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
    	if (rows.length == 0) {
    		$.messager.alert('提示', '请选择退回记录', 'info');
    		return;
    	}
//    	if (rows.length > 1) {
//    		$.messager.alert('提示', '只能选择一个退回', 'info');
//    		return;
//    	}

    	var parameter = '';
    	for ( var i = 0; i < rows.length; i++) {
    		parameter = parameter + '&selections=' + rows[i].id;
    	}
    	$.post(url, parameter, function(data) {
    		if (data != 'true') {
    			if (data == 'system-false') {
    				$.messager.alert('提示', '文章退回失败', 'info');
    			} else if (data == 'accessdenied') {
    				$.messager.alert('提示', '没有退回权限', 'info');
    			} else if (data == 'notinstate') {
    				$.messager.alert('提示', '文章只有在审核中断、发布版、已发布版状态下才能退回', 'info');
    			}
    		} else {
    			$(dataGrid).datagrid('clearSelections');
//    			articleReload();
    			$.messager.alert('提示', '文章退回成功', 'info');
    		}
    	});
    });
    
    $('#tb-query').bind('click', function(){
    	$.ewcms.query({
    		src : options.queryUrl
    	});
    });
    
    $('#tb-clear').bind('click', function(){
    	$('#queryform')[0].reset();
    	$('#publishedStart').datebox('setValue','');
    	$('#publishedEnd').datebox('setValue','');
    	$('#modifiedStart').datebox('setValue','');
    	$('#modifiedEnd').datebox('setValue','');
    });
};

//根据权限显示相应的菜单
ArticleIndex.channelPermission = function(rootnode, currentnode) {
//	if (rootnode.id == currentnode.id) {
//		ArticleIndex.disableButtons();
//		if (currentnode.attributes.mask > 5) {
//			var menuPublish = '#menu-publish';
//			var menuPublishSubIds = [
//			                         menuPublish + '-independent',
//			                         menuPublish + '-relevance'
//			                        ];
//			ArticleIndex.menuButtons('enable', menuPublish, menuPublishSubIds);
//			$('#menu-preview').menubutton('disable');
//		}
//	}else{
		if (currentnode.attributes.mask >= 2 && currentnode.attributes.mask <= 4) {
			ArticleIndex.enableButtons();
			if (currentnode.attributes.mask == 2){
				var menuOperate = '#menu-operate';
				var menuOperateSubIds = [menuOperate + '-remove'];
				ArticleIndex.menuButtons('disable', menuOperate,  menuOperateSubIds);
				$('#menu-approve').menubutton('disable');
				$('#menu-publish').menubutton('disable');
			}else if (currentnode.attributes.mask == 3){
				$('#menu-approve').menubutton('disable');
				$('#menu-publish').menubutton('disable');
			}else if (currentnode.attributes.mask == 4){
				$('#menu-publish').menubutton('disable');
			}
		}else if (currentnode.attributes.mask == 9){
			ArticleIndex.enableButtons();
		}else {
			ArticleIndex.disableButtons();
		}
//	}
};

ArticleIndex.nodeArticleMenu = function(){
	ArticleIndex.disableButtons();
	if (currentnode.attributes.mask >= 3) {
		var menuPublish = '#menu-publish';
		ArticleIndex.menuButtons('enable', menuPublish, [menuPublish + '-independent', menuPublish + '-relevance']);
	}
};

ArticleIndex.menuButtons = function(operate, menuId, menuSubIds){
	var menu = $(menuId).menubutton('options').menu;
	$.each(menuSubIds, function(i, l){
		$(menu).menu(operate + 'Item', $(l)[0]);
	});
	$(menuId).menubutton(operate);
};

//子菜单不可用
ArticleIndex.disableButtons = function() {
	var menuOperate = '#menu-operate';
	var menuOperateSubIds = [
	                         menuOperate + '-add', 
	                         menuOperate + '-upd', 
	                         menuOperate + '-remove', 
	                         menuOperate + '-copy', 
	                         menuOperate + '-move'
	                        ];
	ArticleIndex.menuButtons('disable', menuOperate,  menuOperateSubIds);
	
	var menuTop = '#menu-top';
	var menuTopSubIds = [
	                     menuTop + '-ok', 
	                     menuTop + '-cancel'
	                    ];
	ArticleIndex.menuButtons('disable', menuTop, menuTopSubIds);
	
	var menuShare = '#menu-share';
	var menuShareSubIds = [
	                       menuShare + '-ok', 
	                       menuShare + '-cancel'
	                      ];
	ArticleIndex.menuButtons('disable', menuShare, menuShareSubIds);
	
	var menuSort = '#menu-sort';
	var menuSortSubIds = [
	                      menuSort + '-ok',
	                      menuSort + '-clear'
	                     ];
	ArticleIndex.menuButtons('disable', menuSort, menuSortSubIds);
	
	var menuApprove = '#menu-approve';
	var menuApproveSubIds = [
	                         menuApprove + '-submit',
	                         menuApprove + '-ok'
	                        ];
	ArticleIndex.menuButtons('disable', menuApprove, menuApproveSubIds);
	
	var menuPublish = '#menu-publish';
	var menuPublishSubIds = [
	                         menuPublish + '-independent',
	                         menuPublish + '-relevance',
	                         menuPublish + '-back'
	                        ];
	ArticleIndex.menuButtons('disable', menuPublish, menuPublishSubIds);
};
//主菜单/子菜单可用
ArticleIndex.enableButtons = function() {
	var menuOperate = '#menu-operate';
	var menuOperateSubIds = [
	                         menuOperate + '-add', 
	                         menuOperate + '-upd', 
	                         menuOperate + '-remove', 
	                         menuOperate + '-copy', 
	                         menuOperate + '-move'
	                        ];
	ArticleIndex.menuButtons('enable', menuOperate,  menuOperateSubIds);
	
	var menuTop = '#menu-top';
	var menuTopSubIds = [
	                     menuTop + '-ok', 
	                     menuTop + '-cancel'
	                    ];
	ArticleIndex.menuButtons('enable', menuTop, menuTopSubIds);
	
	var menuShare = '#menu-share';
	var menuShareSubIds = [
	                       menuShare + '-ok', 
	                       menuShare + '-cancel'
	                      ];
	ArticleIndex.menuButtons('enable', menuShare, menuShareSubIds);
	
	var menuSort = '#menu-sort';
	var menuSortSubIds = [
	                      menuSort + '-ok',
	                      menuSort + '-clear'
	                     ];
	ArticleIndex.menuButtons('enable', menuSort, menuSortSubIds);
	
	var menuApprove = '#menu-approve';
	var menuApproveSubIds = [
	                         menuApprove + '-submit',
	                         menuApprove + '-ok'
	                        ];
	ArticleIndex.menuButtons('enable', menuApprove, menuApproveSubIds);
	
	var menuPublish = '#menu-publish';
	var menuPublishSubIds = [
	                         menuPublish + '-independent',
	                         menuPublish + '-relevance',
	                         menuPublish + '-back'
	                        ];
	ArticleIndex.menuButtons('enable', menuPublish, menuPublishSubIds);
};
//根据文章不同的状态,调整子菜单的显示
ArticleIndex.adjustMenu = function(status){
	var menuApprove = $('#menu-approve').menubutton('options').menu;
	if (status == 'REVIEW'){
		$(menuApprove).menu('disableItem', $('#menu-approve-submit')[0]);
		$(menuApprove).menu('enableItem', $('#menu-approve-ok')[0]);
	}else if (status == 'DRAFT' || status == 'REEDIT'){
		$(menuApprove).menu('enableItem', $('#menu-approve-submit')[0]);
		$(menuApprove).menu('disableItem', $('#menu-approve-ok')[0]);
	}else if (status == 'PRERELEASE' || status == 'RELEASE' || status == 'REVIEWBREAK'){
		$(menuApprove).menu('disableItem', $('#menu-approve-submit')[0]);
		$(menuApprove).menu('disableItem', $('#menu-approve-ok')[0]);
	}
};