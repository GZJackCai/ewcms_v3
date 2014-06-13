var OrganSite = function(tree){
	this._tree = tree;
	this.cutNode,this.parentNode,this.opobj,this.popNode;
};

OrganSite.prototype.init = function(options){
	var tree = this._tree;
	
	var getNodeId = function(node){
		return node.id == null ? '' : node.id;
	};
	
	var getSelectNode = function(){
		var node = $(tree).tree('getSelected');
    	if(node == null || typeof(node) == 'undefined')
    	{
    		$.messager.alert('提示','请选择要操作的站点');
    		return false;
    	}	
		var rootnode = $(tree).tree('getRoot');
		if(rootnode.id == node.id){
			$.messager.alert('提示','非机构站点不能操作');
			 return false;
		}     	    				
		return node;
	};
	
	var clearCut = function(){
		$('#parse-button').css('display', 'none');
		$('#cuttext').html('剪切');
		cutNode = "";
		parentNode = "";
		opobj = ""; 
		popNode = ""; 
	};
	
	var loadMainSite = function(id){
		try{
			var node = $(tree).tree('find',id);
			$('#homeSiteId').val(id);
			document.getElementById('mainsitelabel').innerHTML = node.text;
		}catch(err){}
	};
	
	$(tree).tree({
		checkbox: false,
		url: options.treeUrl,
		onDblClick:function(node){
			var rootnode = $(tree).tree('getRoot');
			if(rootnode.id == node.id){
				$(tree).tree('reload');     			
			}
		}					
	});

	$(tree).bind('contextmenu',function(e){
		$('#treeopmenu').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
		return false;
	});	
	
	$('#add-button').bind('click', function(){
		var node = $(tree).tree('getSelected');
    	if(node == null || typeof(node) == 'undefined'){
    		$.messager.alert('提示','请选择要操作的站点');
    		return false;
    	}
		$.messager.prompt(node.text, '请输入站点名', function(r){
			if (r){
	            $.post(options.addUrl,{'siteId':getNodeId(node),'siteName':r},function(data){
		            if(data == null){
	    	    		$.messager.alert('提示','站点创建失败');
	    	    		return;
		            }
					$(tree).tree('append',{
						parent: node.target,
						data:[{
							id:data,
							iconCls:"icon-folder",
							text:r
						}]
					});	
					$(tree).tree('expand',node.target);	
	    	    });						
			}
		});
	});
	
	$('#set-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return; 
		if(node.id == $('#homeSiteId').val()){
    		$.messager.alert('提示','该机构已是主站');
    		return;        			
		}    						
	    $.post(options.setUrl,{'siteId':getNodeId(node)},function(data){
	    	if(data == false){
	    		$.messager.alert('提示','主站设置失败！');
	    	    return;
		    }else{
		       	$.messager.alert('提示','主站设置成功！');
		       	loadMainSite(node.id);
		    }
	    });
	});
	
	$('#rename-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;
		$.messager.prompt(node.text, '请修改站点名', function(r){
			if (r){
	            $.post(options.renameUrl,{'siteId':getNodeId(node),'siteName':r},function(data){
		            if(data == false){
	    	    		$.messager.alert('提示','站点重命名失败');
	    	    		return;
		            }
		            
		            node.text = r;
					$(tree).tree('update',node);	
					if(node.id == $('#homeSiteId').val()){
						loadMainSite(node.id);
					}
	    	    });						
			}
		});
	});
	
	$('#del-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;   
		if(node.id == $('#homeSiteId').val()){
    		$.messager.alert('提示','机构主站不能删除');
    		return;        			
		}
		$.messager.confirm('提示', '确认要删除 ' + node.text + ' 站点吗?', function(r){
			if (r){
    	    	//删除专栏 			
	            $.post(options.delUrl,{'siteId':getNodeId(node)},function(data){
		            if(data == false){
	    	    		$.messager.alert('提示','站点不能删除');
	    	    		return;
		            }
					$(tree).tree('remove',node.target);	
	    	    });	
			}
		}); 
	});
	
	$('#cut-button').bind('click', function(){
		if(typeof(cutNode) == 'undefined' || cutNode == ''){
			var node = getSelectNode();
			if(!node) return;         			
			$.messager.confirm('提示', '确认要剪切 ' + node.text + '站点吗?', function(r){
				if (r){
	    			parentNode = $(tree).tree('getParent',node.target);
	    			cutNode = node;
	    			popNode = $(tree).tree('pop',node.target);
	    			$('#parse-button').css('display', '');
	    			$('#cuttext').html('撤消');
				}
			}); 
		}
		else{
			$(tree).tree('append',{
				parent: parentNode.target,
				data:[popNode]
			});	
			clearCut();
		}	
	});
	
	$("#parse-button").bind('click', function(){
		var node = $(tree).tree('getSelected');
    	if(node == null || typeof(node) == 'undefined')
    	{
    		$.messager.alert('提示','请选择要操作的站点');
    		return false;
    	}
		if(typeof(cutNode) == 'undefined' || cutNode == ''){
    		$.messager.alert('提示','请先剪切站点');		    	    			    	    		
    		return;
		}
    	try{
	    	if(cutNode.id == node.id || parentNode.id == node.id){
	    		$.messager.alert('提示','不能粘到同一 录和父目录下');	    	    		
	    		return;
	    	}
	    	//移动专栏 			
            $.post(options.moveUrl,{'siteId':getNodeId(cutNode),'siteParentId':getNodeId(node)},function(data){
	            if(data == false){
    	    		$.messager.alert('提示','站点移动失败');
    	    		return;
	            }
				$(tree).tree('append',{
					parent: node.target,
					data:[popNode]
				});	
				$(tree).tree('expand',node.target);				            
	            clearCut();
    	    });	
    	}catch(err){}	
	});
	
	$('#config-button').bind('click', function(){
		var node = getSelectNode();
    	if(!node) return;	  			
		var url= options.configUrl + getNodeId(node);
		$.ewcms.openWindow({windowId:'#edit-window',iframeId:'#editifr',src:url,width:620,height:430,title:'属性设置'});	
	});
	
	
	$('#server-button').bind('click', function(){
		var node = getSelectNode();
    	if(!node) return;	  			
		var url=options.serverUrl + getNodeId(node);
		$.ewcms.openWindow({windowId:'#edit-window',iframeId:'#editifr',src:url,width:600,height:400,title:'发布设置'});	
	});
};