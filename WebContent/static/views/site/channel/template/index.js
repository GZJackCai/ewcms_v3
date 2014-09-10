var TemplateIndex = function(dataGrid, tree){
	this._dataGrid = dataGrid || '#tt';
	this._tree = tree || '#tt2';
};

TemplateIndex.prototype.init = function(opts){
	var dataGrid = this._dataGrid;
	var tree = this._tree;
	
	$(tree).tree({
		checkbox: false,
		url: opts.tplTreeUrl
	});
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    pageSize:20,
	    url:opts.queryUrl,
		frozenColumns:[[
		    {field:"ck",checkbox:true},
		    {field:"id",title:"序号",width:50,hidden:true}
		]],
		columns:[[
             {field:'path',title:'模板路径',width:300,align:'left'},
             {field:'typeDescription',title:'模板类型',width:120},
             {field:'describe',title:'说明',width:120,align:'left'},
             {field:'parentId',title:'预览',width:70,align:'center',formatter:function(val,rec){
				return '<input id="preview-button" type="button" name="Submit" value="预  览" onclick="_templateIndex.preview(\'' + opts.previewUrl + '\',' + rec.id + ');" class="inputbutton" style="height:18px">';
			 }},
             {field:'size',title:'编辑',width:70,align:'center',formatter:function(val,rec){
				return '<input id="edit-button" type="button" name="Submit" value="编  辑" onclick="_templateIndex.editTemplateContent(\'' + opts.editContentUrl + '\',' + rec.id + ', \'' + rec.path + '\', \'' + rec.describe + '\');" class="inputbutton" style="height:18px">';
			 }},
			 {field:'history',title:'历史',width:70,align:'center',formatter:function(val,rec){
				 return '<input id="history-button" type="button" name="Submit" value="历  史" onclick="_templateIndex.history(\'' + opts.historyUrl + '\',' + rec.id + ');" class="inputbutton" style="height:18px">';
			 }},
			 {field:'isVerify',title:'校验',width:70,align:'center',formatter:function(val,rec){
				 var result = '不通过';
				 if (val == null) result = '未校验';
				 else if (val) result = '通  过';
				 return '<input id="verify-button" type="button" name="Submit" value="' + result + '" onclick="_templateIndex.verify(\'' + opts.verifyUrl + '\',' + rec.id + ',\'' + rec.describe + '\');" class="inputbutton" style="height:18px">';
			 }}
        ]]
	});
	
	$("#tb-add").bind('click', function(){
		$.ewcms.add({
			title:"新增",
			src:opts.editUrl
		});
	});
	
    $("#tb-edit").bind('click', function(){
		$.ewcms.edit({
			title:"修改",
			src:opts.editUrl
		});
	});
    
    $("#tb-remove").bind('click', function(){
    	$.ewcms.remove({
    		title:"删除",
    		src:opts.deleteUrl
    	});
    });
    
    $("#tb-query").bind('click', function(){
    	$.ewcms.query();
    });
    
    $('#tb-import').bind('click', function(){
    	$.ewcms.openWindow({windowId:"#template-window"});
    });
    
    $('#tb-appchild').bind('click', function(){
    	var rows = $('#tt').datagrid('getSelections');
		if (rows.length == 0) {
			$.messager.alert('提示', '请选择应用栏目的模板记录', 'info');
			return;
		}
		$.ewcms.openWindow({windowId:'#appchildren-window',width:550,height:200,title:'应用栏目'});
    });
    
    $('#tb-force').bind('click', function(){
    	$.ewcms.openWindow({windowId:'#force-window',width : 550,height : 200,title : '强制发布'});
    });
    
    $('#select-button').bind('click', function(){
    	var node = $(tree).tree('getSelected');
	   	if(node == null || typeof(node) == 'undefined'){
	   		$.messager.alert('提示','请选择模板文件');
	   		return false;
	   	}
        $.post(opts.importTplUrl,{'templateId':node.id,'templateName':node.id},function(data){
            if(data == false){
	    		$.messager.alert('提示','模板导入失败');
	    		return;
            }
	    }); 
    });
    
    $('#appchild-button').bind('click', function(){
    	var rows = $(dataGrid).datagrid("getSelections");
		if(rows.length == 0){
         	$.messager.alert('提示','请选择模板记录','info');
            return;
        }
		var parameters = "";
		parameters += "&cover=" + $('input[name=\'appchildrenRadio\']:checked').val();
		for ( var i = 0; i < rows.length; i++) {
			parameters += '&selections=' + rows[i].id;
		}
		var url = opts.appChildUrl + parameters;
		$.ajax({
	        type:'post',
	        async:false,
	        datatype:'json',
	        cache:false,
	        url:url,
	        data: '',
	        success:function(message, textStatus){
	        	//loadingDisable();
	        	$.messager.alert('提示', message, 'info');
	        },
	        beforeSend:function(XMLHttpRequest){
	        	//loadingEnable();
	        },
	        complete:function(XMLHttpRequest, textStatus){
	        	//loadingDisable();
	        },
	        error:function(XMLHttpRequest, textStatus, errorThrown){
	        }
	    });
    });
    
    $('#force-button').bind('click', function(){
    	$.post(opts.forceUrl, {children : $('input[name=\'channelRadio\']:checked').val()}, function(data) {
			$.messager.alert('提示', data, 'info');
		});
		$('#force-window').window('close');
    });
    
    $('#tb-defaultquery').bind('click', function(){
    	$.ewcms.defaultQuery();
    });
};

//编辑
TemplateIndex.prototype.editTemplateContent = function(url, id, path, description){
	var _content = new Content("#tt2");
	_content.show({
		url : url,
		id : id,
		path : path,
		description : description
	});
};

//预览
TemplateIndex.prototype.preview = function(url, id){
	window.open(url + '/' + id + '/true', "previewwin", "height=600, width=800, top=0, left=0, toolbar=no, menubar=no, scrollbars=yes, resizable=1, location=no, status=no");
};

//历史
TemplateIndex.prototype.history = function(url, id){
	$.ewcms.openWindow({windowId:'#pop-window', iframeId : '#editifr_pop', src : url + '/' + id, width : 550,height : 350,title : '历史记录选择'});
};

//校验
TemplateIndex.prototype.verify = function(url, id, description){
	var dataGrid = this._dataGrid;
	if (description.indexOf('请选择类型') != -1){
		$.messager.alert('提示', '请先选择模板类型，才能进行模板校验', 'info');
	}else{
		alert(url);
		$.post(url, {templateId:id}, function(data){
			$.messager.alert('提示', '模板校验完成', 'info');
			$(dataGrid).datagrid('clearSelections');
			$(dataGrid).datagrid('reload');
		});
	}
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};