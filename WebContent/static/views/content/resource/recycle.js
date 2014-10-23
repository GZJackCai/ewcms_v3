/*!
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

operators = {
        _construtSelects : function(datagridId){
            var rows = $(datagridId).datagrid('getSelections');
            var selects = '';
            $.each(rows,function(index,value){
                selects = selects + 'selections=' +value.id +'&';
            });
            return selects;
        },
        reload : function(datagridId){
            $(datagridId).datagrid('reload');
        },
        clearSelect : function(datagridId){
        	$(datagridId).datagrid('clearSelections');
        },
        revert :function(datagridId,url){
            var o = this;
            var selects = this._construtSelects(datagridId);
            $.post(url,selects,function(data){
            	$.messager.alert('成功','资源还原成功','info');
            	o.clearSelect(datagridId);
            	o.reload(datagridId);
            });
        },
        clear : function (datagridId,url){
            var o = this;
            $.messager.confirm('提示', '确定清空所有资源?', function(r){
                if (r){
                    $.post(url,function(data){
                    	$.messager.alert('成功','清空所有资源成功','info');
                    	o.clearSelect(datagridId);
                    	o.reload(datagridId);
                    });
                }
            },'info');
       }
};

var recycle = function(opts){
    opts = opts || {};
    
    this._opts = {};
    this._opts.datagridId = opts.datagridId || "#tt";
    this._opts.menuId = opts.menuId || "#mm";
    this._opts.menuItemTitleId =  opts.menuItemTitleId || "#menu-item-title";
    this._opts.toolbarRevertId = opts.toolbarRevertId || "#toolbar-revert";
    this._opts.toolbarRemoveId = opts.toolbarRemoveId || "#toolbar-remove";
    this._opts.toolbarClearId = opts.toolbarClearId || "#toolbar-clear";
    this._opts.toolbarQueryId = opts.toolbarQueryId || "#toolbar-query";
    this._opts.queryFormId = opts.queryFormId || "#queryform";
};
 
recycle.prototype.init = function(urls){
	$("form table tr").next("tr").hide(); 
	
    var opts = this._opts;
    
    $(opts.datagridId).datagrid({
        idField:"id",
        url:urls.queryUrl,
        fit:true,
        pagination:true,
        columns:[[
           {field:'ck',checkbox:true},
           {field:'thumbUri',title:'引导图',width:180,halign:'left',align:'center',
        	   formatter:function(val,row){
        		   if(row.type=='IMAGE'){
        			   return '<img src="' + ctx + val +'" style="height:48px;"/>';    
        		   }else{
        			   return '无';
        		   }
           }},
           {field:'id',title:'编号',width:120,sortable:true,hidden:true},
           {field:'type',title:'资源类型',width:120,sortable:true,formatter:function(val,row){
               if(val == 'IMAGE'){
                   return '图片';
               }else if(val == 'FLASH'){
                   return 'flash';
               }else if(val == 'VIDEO'){
                   return '视频';
               }else{
                   return '附件';
               }
           }},
           {field:'name',title:'资源名称',width:320,sortable:true},
           {field:'description',title:'描述',width:320,sortable:true},
           {field:'createTime',title:'创建时间',width:145,sortable:true,},
           {field:'updateTime',title:'删除时间',width:145,sortable:true,}
        ]],
        rowStyler: function(index,row){
        	if (row.type == 'IMAGE'){
        		return ';height:50px;';
        	}else{
        		return '';
        	}
        },
        onBeforeLoad:function(param){
             param['removeEvent'] = true;
        },
        onRowContextMenu:function(e, rowIndex, rowData){
            e.preventDefault();
            $(opts.menuId).menu('show', {
                left:e.pageX,
                top:e.pageY
            });
            var menuItem = $(opts.menuId).menu('getItem',opts.menuItemTitleId);
            var title = rowData.name;
            if(title.length > 15){
                title = title.substring(0,12) + "...";
            }
            menuItem.text = "<b>"+title+"</b>";
            menuItem.disabled=true;
            $(opts.menuId).menu('setText',menuItem);
            $(opts.menuId).menu('disableItem',menuItem);
            $(opts.datagridId).datagrid("unselectAll");
            $(opts.datagridId).datagrid("selectRow",rowIndex);
        }
    });
    
    $(opts.menuId).menu({
        onClick:function(item){
            var row = $(opts.datagridId).datagrid("getSelected");
            if(item.iconCls == 'icon-download'){
                window.open(ctx + row.uri);
            }
            if(item.iconCls == 'icon-resume'){
                operators.revert(opts.datagridId,urls.revertUrl);
            }
            if(item.iconCls == 'icon-remove'){
            	$.ewcms.remove({
            		title:'删除'
            	});
            }
        }
    });
    
    $(opts.toolbarRevertId).bind('click',function(){
         operators.revert(opts.datagridId,urls.revertUrl);
    });
    
    $(opts.toolbarRemoveId).bind('click',function(){
    	$.ewcms.remove({
    		title:'删除'
    	});
    });
    
    $(opts.toolbarClearId).bind('click',function(){
    	operators.clear(opts.datagridId,urls.clearUrl);
    });
    
    $(opts.toolbarQueryId).bind('click',function(){
    	$.ewcms.query();
    });
    
    $('#tb-more').bind('click', function(){
       	var showHideLabel_value = $('#showHideLabel').text();
    	$('form table tr').next('tr').toggle();
     	if (showHideLabel_value == '收缩'){
     		$('#showHideLabel').text('更多...');
    	}else{
    		$('#showHideLabel').text('收缩');
    	}
    	$(dataGrid).datagrid('resize');
    });
};