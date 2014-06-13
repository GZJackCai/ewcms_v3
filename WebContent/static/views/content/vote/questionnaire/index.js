var QuestionnaireIndex = function(dataGrid){
	this._dataGrid = dataGrid || '#tt';
};

QuestionnaireIndex.prototype.init = function(options){
	var dataGrid = this._dataGrid;
	
	$(dataGrid).datagrid({
	    nowrap:true,
	    pagination:true,
	    rownumbers:true,
	    singleSelect:true,
	    url:options.queryUrl,
		frozenColumns:[[
		    {field:'ck',checkbox:true},
		    {field:'id',hidden:true}
		]],
		columns:[[
            {field:'title',title:'调查标题',width:600},
            {field:'questionnaireStatusDescription',title:'查看方式',width:200},
            {field:'number',title:'投票人数',width:160},
            {field:'verifiCode',title:'验证码',width:100, halign:'left', align:'center',
              	formatter:function(val,rec){
               		return val ? '是' : '否';
               	}
            },
            {field:'startTime',title:'开始时间',width:160},
            {field:'endTime',title:'结束时间',width:160},
            {field:'voteEnd',title:'结束投票',width:100,halign:'left', align:'center',
               	formatter:function(val,rec){
               		var flag = '否';
               		var nowDate = new Date();
               		if (val){
                   		flag = '是';
               		}else if (rec.endTime < nowDate.toLocaleString()){
               			flag = '是';
               		}
               		return flag;
               	}
            }
        ]],
        view : detailview,
		detailFormatter : function(rowIndex, rowData) {
			return '<div id="ddv-' + rowIndex + '"></div>';
		},
		onExpandRow: function(rowIndex, rowData){
			$('#ddv-' + rowIndex).panel({
				border:false,
				cache:false,
				content: '<iframe src="' + options.subjectIndexUrl + '/' + rowData.id + '" frameborder="0" width="100%" height="500px" scrolling="auto"></iframe>',
				onLoad:function(){
					$('#tt').datagrid('fixDetailRowHeight',rowIndex);
				}
			});
			$(dataGrid).datagrid('fixDetailRowHeight',rowIndex);
		}
//		,
//        onSelect:function(rowIndex,rowData){
//			parent.$('#subjectifr').attr('src',options.subjectIndexUrl + '/' + rowData.id + '_' + rowData.title);
//		},
//		onUnselect:function(rowIndex,rowData){
//			$(dataGrid).datagrid('unselectRow', rowIndex);
//			parent.$('#subjectifr').attr('src','');
//		}
	});
	
	$('#tb-add').bind('click', function(){
		$.ewcms.add({
			title:'新增',
			src : options.editUrl,
			width:690,
			height:400
		});
	});
	
    $('#tb-edit').bind('click', function(){
		$.ewcms.edit({
			title:'修改',
			src : options.editUrl,
			width:690,
			height:400
		});
	});
    
    $('#tb-remove').bind('click', function(){
    	$.ewcms.remove({
    		title:'删除',
    		src : options.deleteUrl
    	});
    });
    
    $('#tb-query').bind('click', function(){
    	$.ewcms.query();
    });	
    
    $('#tb-prview').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if(rows.length == 0){
        	$.messager.alert('提示','请选择预览记录','info');
            return;
        }
        if (rows.length > 1){
			$.messager.alert('提示','只能选择一个预览','info');
			return;
        }
		window.open(options.viewVoteUrl + '?id=' + rows[0].id + '','popup','width=1280,height=700,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,left=' + (window.screen.width - 1280)/2 + ',top=' + (window.screen.height - 700)/2 + '');
    });
    
    $('#tb-result').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if(rows.length == 0){
        	$.messager.alert('提示','请选择结果查看记录','info');
            return;
        }
        if (rows.length > 1){
			$.messager.alert('提示','只能选择一个结果查看','info');
			return;
        }
		window.open(options.resultVoteUrl + '/' + rows[0].id + '','popup','width=1280,height=700,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,status=no,left=' + (window.screen.width - 1280)/2 + ',top=' + (window.screen.height - 700)/2 + '');
    });
    
    $('#tb-person').bind('click', function(){
    	var rows = $(dataGrid).datagrid('getSelections');
		if(rows.length == 0){
        	$.messager.alert('提示','请选择结果查看记录','info');
            return;
        }
        if (rows.length > 1){
			$.messager.alert('提示','只能选择一个结果查看','info');
			return;
        }
		$('#editifr_person').attr('src', options.personUrl + '/' + rows[0].id);
		$.ewcms.openWindow({windowId : '#person-window', width:500,height:265,title:'人员'});
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

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};