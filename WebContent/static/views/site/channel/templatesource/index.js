var TemplateSourceIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

TemplateSourceIndex.prototype.init = function(opts){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:false,
	    url:opts.queryUrl,
		frozenColumns:[[
		    {field:"ck",checkbox:true},
		    {field:"id",title:"序号",width:50,hidden:true}
		]],
		columns:[[
             {field:'path',title:'资源路径',width:300,align:'left'},
			 {field:'describe',title:'说明',width:120,align:'left'},
			 {field:'parentId',title:'编辑',width:60,align:'center',formatter:function(val,rec){
				 if (rec.path.search(/.js/)>-1 || rec.path.search(/.htm*/)>-1){
					 return '<input type="button" name="Submit" value="编  辑" class="inputbutton" style="height:18px" onClick="_templateSourceIndex.editTemplateContent(\'' + opts.editContentUrl + '\',' + rec.id + ', \'' + rec.path + '\', \'' + rec.describe + '\');">';
				 }
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
    $('#tb-defaultquery').bind('click', function(){
    	$.ewcms.defaultQuery();
    });
};

//编辑
TemplateSourceIndex.prototype.editTemplateContent = function(url, id, path, description){
	var _content = new Content("#tt2");
	_content.show({
		url : url,
		id : id,
		path : path,
		description : description
	});
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};