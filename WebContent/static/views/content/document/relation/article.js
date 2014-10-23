var RelationArticle = function(dataGrid, tree){
	this._dataGrid = dataGrid || '#tt';
	this._tree = tree || '#tt2';
};

RelationArticle.prototype.init = function(options){
    var dataGrid = this._dataGrid;
    var tree = this._tree;
	
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
	                if (rec.top) pro.push("<img src='" + ctx + "/static/image/article/top.gif' width='13px' height='13px' title='有效期限:永久置顶'/>"); 
	                if (rec.article.comment) pro.push("<img src='" + ctx + "/static/image/article/comment.gif' width='13px' height='13px' title='允许评论'/>");
	                if (rec.article.type=="TITLE") pro.push("<img src='" + ctx + "/static/image/article/title.gif' width='13px' height='13px' title='标题新闻'/>");
	                if (rec.reference) pro.push("<img src='" + ctx + "/static/image/article/reference.gif' width='13px' height='13px' title='引用新闻'/>");
	                if (rec.article.inside) pro.push("<img src='" + ctx + "/static/image/article/inside.gif' width='13px' height='13px' title='内部标题'/>");
	                if (rec.share) pro.push("<img src='" + ctx + "/static/image/article/share.gif' width='13px' height='13px' title='共享' style='border:0'/>");
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
	
	$(tree).tree({
		checkbox: false,
		url: options.treeUrl,
		onClick:function(node){
			channelId = node.id;
			var rootnode = $(tree).tree('getRoot');
			if(rootnode.id == node.id) return;

			var url = options.queryUrl + "/" + node.id;
			$(dataGrid).datagrid({
            	pageNumber:1,
                url:url
            });
		}
	});
	
	$('#treeload-button').bind('click', function(){
		$(tree).tree('reload');
	});
};