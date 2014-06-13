var ChannelIndex = function(tree){
	this._tree = tree || '#tt2';
	this.cutNode,this.parentNode,this.opobj,this.popNode;
};

ChannelIndex.prototype.init = function(opts){
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
		url: opts.treeUrl,
		onClick:function(node){
			if(node.attributes.mask < 5){
	    		$.messager.alert('提示','您不具有该操作权限','info');
	    		return false;
			}
			$("#editifr").attr('src',opts.editUrl + '?channelId=' + getNodeId(node));
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
		//判断是否选择了专栏
		var node = getSelectNode();
		if(!node) return;
		if(node.attributes.mask < 8){
    		$.messager.alert('提示','您不具有该操作权限','info');
    		return false;
		}    			
    	//添加专栏 			
		$.messager.prompt(node.text, '请输入专栏名', function(r){
			if (r){
	            $.post(opts.addUrl,{'channelParentId':getNodeId(node),'channelName':r},function(data){
		            if(data == 'false'){
	    	    		$.messager.alert('提示','专栏添加失败','info');
	    	    		return;
		            }
					$(tree).tree('append',{
						parent: node.target,
						data:[{
							id:data,
							iconCls:"icon-channel-node",
							text:r,
							attributes:{
								mask:7
							}
						}]
					});	
					$(tree).tree('expand',node.target);	
	    	    });						
			}
		});
	});
	
	$('#rename-button').bind('click', function(){
		//判断是否选择了专栏
		var node = getSelectNode();
		if(!node) return;
		if(node.attributes.mask<5){
    		$.messager.alert('提示','您不具有该操作权限','info');
    		return false;
		}    	    	 		
    	//重命名专栏 			
		$.messager.prompt(node.text, '请修改专栏名', function(r){
			if (r){
	            $.post(opts.renameUrl,{'channelId':getNodeId(node),'channelName':r},function(data){
		            if(data == 'false'){
	    	    		$.messager.alert('提示','专栏重命名失败','info');
	    	    		return;
		            }
		            node.text = r;
					$(tree).tree('update',node);	
	    	    });						
			}
		});
	});
	
	$('#edit-button').bind('click', function(){
		//判断是否选择了操作专栏
		var node = getSelectNode();
		if(!node) return;	
		if(node.attributes.mask<5){
    		$.messager.alert('提示','您不具有该操作权限','info');
    		return false;
		}
		$("#editifr").attr('src',opts.editUrl + '?channelId=' + getNodeId(node));  
	});
	
	$('#delete-button').bind('click', function(){
		//判断是否选择了专栏
		var node = getSelectNode();
		if(!node) return;
		var rootnode = $(tree).tree('getRoot');
		if(rootnode.id == node.id){
			$.messager.alert('提示','不允许删除该专栏','info');
			 return;
		}   
		if(node.attributes.mask<32){
    		$.messager.alert('提示','您不具有该操作权限','info');
    		return false;
		}				  			
		$.messager.confirm('提示', '确认要删除 ' + node.text + ' 专栏?', function(r){
			if (r){
    	    	//删除专栏 			
	            $.post(opts.deleteUrl,{'channelId':getNodeId(node)},function(data){
		            if(data == 'false'){
	    	    		$.messager.alert('提示','专栏不能删除','info');
	    	    		return;
		            }
					$(tree).tree('remove',node.target);	
	    	    });	
			}
		}); 
	});
	
	$('#cut-button').bind('click', function(){
		if(typeof(cutNode) == 'undefined' || cutNode == ''){
			//判断是否选择了操作站点
			var node = getSelectNode();
			if(!node) return false;
			var rootnode = $(tree).tree('getRoot');
			if(rootnode.id == node.id){
				$.messager.alert('提示','不允许剪切该专栏','info');
				return false;
			}         			
			$.messager.confirm('提示', '确认要剪切 ' + node.text + '专栏吗?', function(r){
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
			clearCut(obj);
		}	
	});
	
	$('#parse-button').bind('click', function(){
		//判断是否选择了操作模板
		var node = getSelectNode();
		if(!node) return;
		if(typeof(cutNode) == 'undefined' || cutNode == ''){
    		$.messager.alert('提示','请先剪切专栏','info');		    	    			    	    		
    		return;
		}
    	try{
	    	if(cutNode.id == node.id || parentNode.id == node.id){
	    		$.messager.alert('提示','不能粘到同一 录和父目录下','info');	    	    		
	    		return;
	    	}
	    	//移动专栏 			
            $.post(opts.parseUrl,{'channelId':getNodeId(cutNode),'channelParentId':getNodeId(node)},function(data){
	            if(data == 'false'){
    	    		$.messager.alert('提示','专栏移动失败','info');
    	    		return;
	            }
				$(tree).tree('append',{
					parent: node.target,
					data:[popNode]
				});	
				$(tree).tree('expand',node.target);				            
	            clearCut(opobj);
    	    });	
    	}catch(err){}		
	});
	
	$('up-button').bind('click', function(){
		//判断是否选择了操作模板
		var node = getSelectNode();
		if(!node) return;
		if(node.attributes.mask<5){
    		$.messager.alert('提示','您不具有该操作权限','info');
    		return false;
		}
		var rootnode = $(tree).tree('getRoot');
		if(rootnode.id == node.id){
			$.messager.alert('提示','不允许移动站点','info');
			 return;
		}
		var parentNode = $(tree).tree('getParent',node.target);
	    $.post(opts.upUrl,{'channelId':getNodeId(node),'channelParentId':getNodeId(parentNode)},function(data){
		   	if(data == 'false'){
		    	$.messager.alert('提示','专栏上移一位失败','info');
		    	return;
		   	}else{
	        	$.messager.alert('提示','专栏上移一位成功','info');
	        	$(tree).tree('reload', parentNode.target);
	        }
		});		
	});
	
	$('down-button').bind('click', function(){
		//判断是否选择了操作模板
		var node = getSelectNode();
		if(!node) return;
		if(node.attributes.mask<5){
    		$.messager.alert('提示','您不具有该操作权限','info');
    		return false;
		}
		var rootnode = $(tree).tree('getRoot');
		if(rootnode.id == node.id){
			$.messager.alert('提示','不允许移动站点','info');
			 return;
		}
		var parentNode = $(tree).tree('getParent',node.target);
		  
		$.post(opts.downUrl,{'channelId':getNodeId(node),'channelParentId':getNodeId(parentNode)},function(data){
	    	if(data == 'false'){
    	    	$.messager.alert('提示','专栏下移一位失败','info');
    	    	return;
	    	}else{
	        	$.messager.alert('提示','专栏下移一位成功','info');
	        	$(tree).tree('reload', parentNode.target);
	        }
    	});
	});
	
	$('#move-button').bind('click', function(){
		//判断是否选择了操作模板
		var node = getSelectNode();
		if(!node) return;
		if(node.attributes.mask<5){
    		$.messager.alert('提示','您不具有该操作权限','info');
    		return false;
		}
		var rootnode = $(tree).tree('getRoot');
		if(rootnode.id == node.id){
			$.messager.alert('提示','不允许移动站点','info');
			 return;
		}
		var parentNode = $(tree).tree('getParent',node.target);
		var children = $(tree).tree('getChildren',parentNode.target);
		var len = children.length;
		$.messager.prompt(node.text, '请输入移位数(1-' + len + ')', function(r){  
			if (r){
				var regex = /^[0-9]*[1-9][0-9]*$/;
				if (regex.test(r)){
					if (r > len ){
						$.messager.alert('提示','输入的数不能大于' + len);
					}else{
		    			$.post(opts.moveUrl,{'channelId':getNodeId(node),'channelParentId':getNodeId(parentNode),sort:r},function(data){
					    	if(data == 'false'){
				    	    	$.messager.alert('提示','专栏移动失败','info');
				    	    	return;
					        }else{
					        	$.messager.alert('提示','专栏移动成功','info');
					        	$(tree).tree('reload', parentNode.target);
					        }
				    	});
					}
				}else{
					$.messager.alert('提示','请输入正整数','info');
				}
			}
		});		
	});
	
	$('#export-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;
		window.open(opts.exportUrl + '?channelId=' + getNodeId(node));
	});
	
	$('#treeload-button').bind('click', function(){
		$('#cuttext').html('剪切');
		$('#parse-button').css('display', 'none');
		$(tree).tree('reload');
	});
};
