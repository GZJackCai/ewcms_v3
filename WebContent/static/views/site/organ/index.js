var OrganIndex = function(tree){
	this._tree = tree || '#tt2';
	this.cutNode,this.parentNode,this.opobj,this.popNode;
};

OrganIndex.prototype.init = function(options){
	var tree = this._tree;
	
	var getNodeId = function(node){
		return node.id == null ? '' : node.id;
	};
	
	var getSelectNode = function(){
		var node = $(tree).tree('getSelected');
		if(node == null || typeof(node) == 'undefined'){
			$.messager.alert('提示','请选择要操作的机构');
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
	
	$(tree).tree({
		checkbox: false,
		url: options.treeUrl,
		onClick:function(node){
			var rootnode = $(tree).tree('getRoot');
			var url = '';
			if(rootnode.id != node.id)    	    	{
	    		url= options.editUrl + '?organId=' + getNodeId(node);
	    	}	
			$('#editifr').attr('src',url);
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
		var node = getSelectNode();
		if(!node) return;
		$.messager.prompt(node.text, '请输入机构名', function(r){
			if (r){
	            $.post(options.addUrl,{'organId':getNodeId(node),'organName':r},function(data){
		            if(data == null){
	    	    		$.messager.alert('提示','机构添加失败');
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
	
	$('#rename-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;
		var rootnode = $(tree).tree('getRoot');
		if(rootnode.id == node.id)    	    	{
    		$.messager.alert('提示','不能重命名客户机构');
    		return false;
    	}   	    	 		
		$.messager.prompt(node.text, '请修改机构名', function(r){
			if (r){
	            $.post(options.renameUrl,{'organId':getNodeId(node),'organName':r},function(data){
		            if(data == null){
	    	    		$.messager.alert('提示','机构重命名失败');
	    	    		return;
		            }
		            node.text = r;
					$(tree).tree('update',node);	
	    	    });						
			}
		});
	});
	
	$('#del-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;  	
		var rootnode = $(tree).tree('getRoot');
		if(rootnode.id == node.id)    	    	{
    		$.messager.alert('提示','不能删除客户机构');
    		return false;
    	}    					
		$.messager.confirm('提示', '确认要删除 ' + node.text + ' 机构吗?', function(r){
			if (r){
	            $.post(options.delUrl, {'organId':getNodeId(node)},function(data){
		            if(data == false){
	    	    		$.messager.alert('提示','机构不能删除');
	    	    		return;
		            }
		            
					$(tree).tree('remove',node.target);	
	    	    });	
			}
		}); 
	});
	
	$('#treeload-button').bind('click', function(){
		$('#cuttext').html('剪切');
		$('#parse-button').css('display', 'none');
		$(tree).tree('reload');
	});
	
	$('#cut-button').bind('click', function(){
		if(typeof(cutNode) == 'undefined' || cutNode == ''){
			var node = getSelectNode();
			if(!node) return;   
			var rootnode = $(tree).tree('getRoot');
			if(rootnode.id == node.id)    	    	{
	    		$.messager.alert('提示','不能剪切客户机构');
	    		return false;
	    	}        			      			
			$.messager.confirm('提示', '确认要剪切 ' + node.text + '机构吗?', function(r){
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
	
	$('#parse-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;
		if(typeof(cutNode) == 'undefined' || cutNode == ''){
    		$.messager.alert('提示','请先剪切机构');		    	    			    	    		
    		return;
		}
    	try{
	    	if(cutNode.id == node.id || parentNode.id == node.id){
	    		$.messager.alert('提示','不能粘到同一 录和父目录下');	    	    		
	    		return;
	    	}
            $.post(options.moveUrl, {'organId':getNodeId(cutNode),'organParentId':getNodeId(node)},function(data){
	            if(data == false){
    	    		$.messager.alert('提示','机构移动失败');
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
};
