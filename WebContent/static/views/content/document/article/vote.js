var VoteArticle = function(dataGrid, tree){
	this._dataGrid = dataGrid || '#tt';
	this._tree = tree || '#tt2';
};

VoteArticle.prototype.init = function(options){
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
            {field:'title',title:'调查主题',width:500},
            {field:'questionnaireStatusDescription',title:'查看方式',width:100},
            {field:'number',title:'投票人数',width:60},
            {field:'verifiCode',title:'验证码',width:43,
              	formatter:function(val,rec){
               		return val ? '&nbsp;&nbsp;是' : '&nbsp;&nbsp;否';
               	}
            },
            {field:'startTime',title:'开始时间',width:145},
            {field:'endTime',title:'结束时间',width:145},
            {field:'voteEnd',title:'结束投票',width:55,
               	formatter:function(val,rec){
               		var flag = '&nbsp;&nbsp;&nbsp;否';
               		var nowDate = new Date();
               		if (val){
                   		flag = '&nbsp;&nbsp;&nbsp;是';
               		}else if (rec.endTime < nowDate.toLocaleString()){
               			flag = '&nbsp;&nbsp;&nbsp;是';
               		}
               		return flag;
               	}
            }
        ]]
	});
	
	$(tree).tree({
		checkbox: false,
		url: options.treeUrl,
		onClick:function(node){
			if (node == $(tree).tree('getRoot')) return;
			$(dataGrid).datagrid({
				url : options.queryUrl + '/' + node.id
			}); 
		}
	});
};