var TemplateIndex = function(tree){
	this._tree = tree || '#tt2';
	this.cutNode,this.parentNode,this.opobj,this.popNode;
};

TemplateIndex.prototype.init = function(options){
	var folderparten = /^[0-9A-Za-z_]*$/;
	var fileparten = /^([a-zA-z0-9])+.(html|htm)$/;
	
	var tree = this._tree;
	
	var getNodeId = function(node){
		return node.id==null?'':node.id;
	};
	
	var getSelectNode = function(){
		var node = $(tree).tree('getSelected');
    	if(node == null || typeof(node) == 'undefined')
    	{
    		$.messager.alert('提示','请选择操作目录');
    		return false;
    	}				
		return node;
	};
	
	var clearCut = function(){
		$('#cuttext').html('剪切');
		$('#parse-button').css('display', 'none');
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
			if(rootnode.id != node.id){
				url= options.editUrl + '?templateId=' + node.id;
			}
			$("#editifr").attr('src',url);
		}
	});
	
	$(tree).bind('contextmenu',function(e){
		$('#treeopmenu').menu('show', {
			left: e.pageX,
			top: e.pageY
		});
		return false;
	});
	
	$('#addtemplate-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;
		if (node.attributes.fileType == 'FILE'){
			$.messager.alert('提示','不能在文件下建立文件','warn');
			return;
		}
    	//添加模板操作			
		$.messager.prompt(node.text, '请输入文件名称', function(r){
			if (r){
				if(!fileparten.exec(r)){
    	    		$.messager.alert('提示','文件名由字母、数字、组成的htm/html文件');
    	    		return;
				}
	            $.post(options.addTemplateUrl,{'templateParentId':getNodeId(node),'templateName':r},function(data){
		            if(data == 'false'){
	    	    		$.messager.alert('提示','新建模板失败');
	    	    		return;
		            }
		            $(tree).tree('append',{
						parent: node.target,
						data:[{
							id:data,
							iconCls:"",
							text:r
						}]
					});	
					$(tree).tree('expand',node.target);				            
	    	    });						
			}
		});
	});
	
	$('#addfolder-button').bind('click',function(){
		var node = getSelectNode();
		if(!node) return;
		if (node.attributes.fileType == 'FILE'){
			$.messager.alert('提示','不能在文件下建立目录','warn');
			return;
		}
		$.messager.prompt(node.text, '请输入文件夹名', function(r){
			if (r){
				if(!folderparten.exec(r)){
    	    		$.messager.alert('提示','目录只能由字母、数字、下划线组成');
    	    		return;
				}						
	            $.post(options.addFolderUrl,{'templateParentId':getNodeId(node),'templateName':r},function(data){
		            if(data == 'false'){
	    	    		$.messager.alert('提示','新建文件夹失败');
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
		$.messager.prompt(node.text, '请修改名称', function(r){
			if (r){
				if(node.attributes.fileType != 'FILE'){
					if(!folderparten.exec(r)){
	    	    		$.messager.alert('提示','目录只能由字母、数字、下划线组成');
	    	    		return;
					}
				}else{
					if(!fileparten.exec(r)){
	    	    		$.messager.alert('提示','文件名由字母、数字、组成的htm/html文件');
	    	    		return;
					}	
				}												
	            $.post(options.renameUrl,{'templateId':getNodeId(node),'templateName':r},function(data){
		            if(data == 'false'){
	    	    		$.messager.alert('提示','重命名失败');
	    	    		return;
		            }
		            node.text = r;
					$(tree).tree('update',node);	
	    	    });						
			}
		});
	});
	
	$('#delete-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;
		var rootnode = $(tree).tree('getRoot');
		if(rootnode.id == node.id){
			$.messager.alert('提示','不允许删除该模板');
			 return;
		}     			
		$.messager.confirm('提示', '确认要删除 ' + node.text + '模板吗?', function(r){
			if (r){
    	    	//删除模板			
	            $.post(options.deleteUrl,{'templateId':getNodeId(node)},function(data){
		            if(data == 'false'){
	    	    		$.messager.alert('提示','模板不能删除');
	    	    		return;
		            }
		            $(tree).tree('remove',node.target);
	    	    });	
			}
		}); 
	});
	
	$('#cut-button').bind('click', function(){
		if(typeof(cutNode) == 'undefined' || cutNode == ''){
			//判断是否选择了操作模板
			var node = getSelectNode();
			if(!node) return;
			var rootnode = $(tree).tree('getRoot');
			if(rootnode.id == node.id){
				$.messager.alert('提示','不允许剪切该模板');
				 return;
			}         			
			$.messager.confirm('提示', '确认要剪切 ' + node.text + '模板吗?', function(r){
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
    		$.messager.alert('提示','请先剪切模板');		    	    			    	    		
    		return;
		}
    	try{
	    	if(cutNode.id == node.id || parentNode.id == node.id){
	    		$.messager.alert('提示','不能粘到同一 录和父目录下');	    	    		
	    		return;
	    	}
	    	//移动专栏 			
            $.post(options.moveUrl,{'templateId':getNodeId(cutNode),'templateParentId':getNodeId(node)},function(data){
	            if(data == 'false'){
    	    		$.messager.alert('提示','粘贴模板失败');
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
	
	$('#import-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;
		if(node.iconCls == "icon-channel-node"){
			$.messager.alert('提示','请选择文件夹目录');	
			return ;
		}
		var url= options.importUrl + getNodeId(node);
		$("#editifr").attr('src',url);
	});
	
	$('#export-button').bind('click', function(){
		var node = getSelectNode();
		if(!node) return;
		window.open(options.exportUrl + '?templateId='+ getNodeId(node));
	});
	
	$('#treeload-button').bind('click', function(){
		$('#cuttext').html('剪切');
		$('#parse-button').css('display', 'none');
		$(tree).tree('reload');
	});
};

