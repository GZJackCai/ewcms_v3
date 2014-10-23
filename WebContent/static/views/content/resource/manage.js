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
        publish :function(datagridId,url){
        	if($(datagridId).datagrid('getSelections').length == 0){
                $.messager.alert('提示','请选择发布的记录','info');
                return;
            }
            var o = this;
            var selects = this._construtSelects(datagridId);
            $.post(url,selects,function(data){
           		$.messager.alert('提示',data, 'info');
                o.reload(datagridId);
            });
        },
        remove :function(datagridId,url){
            if($(datagridId).datagrid('getSelections').length == 0){
                $.messager.alert('提示','请选择删除的记录','info');
                return;
            }
            var o = this;
            var selects = this._construtSelects(datagridId);
            $.messager.confirm('提示', '确定删除所选记录?', function(r){
                if (r){
                    $.post(url,selects,function(data){
                    	$.messager.alert('提示',data, 'info');
                        o.reload(datagridId);
                        $(datagridId).datagrid('unselectAll');
                    });
                }
            },'info');
        },
        save : function (ifr,windowId,datagridId){
            var o = this;
            window.frames[ifr].insert(function(data, success){
                if(success){
                    o.reload(datagridId);
                    $(windowId).window('close');
                }else{
                    $.messager.alert('错误','资源上传失败');
                }
           });
       }
};

var manage = function(type,opts){
    this._type = type;

    opts = opts || {};
    
    this._opts = {};
    this._opts.datagridId = opts.datagridId || "#tt";
    this._opts.menuId = opts.menuId || "#mm";
    this._opts.menuItemTitleId =  opts.menuItemTitleId || "#menu-item-title";
    this._opts.resourceUploadWindowId = opts.resourceUploadWindowId || "#resource-upload-window";
    this._opts.resourceUpdateWindowId = opts.resourceUpdateWindowId || "#resource-update-window";
    this._opts.thumbUpdateWindowId = opts.thumbUpdateWindowId || "#thumb-update-window";
    this._opts.toolbarUploadId = opts.toolbarUploadId || "#toolbar-upload";
    this._opts.toolbarPublishId = opts.toolbarPublishId || "#toolbar-publish";
    this._opts.toolbarRemoveId = opts.toolbarRemoveId || "#toolbar-remove";
    this._opts.toolbarQueryId = opts.toolbarQueryId || "#toolbar-query";
    this._opts.queryFormId = opts.queryFormId || "#queryform";
    this._opts.buttonSaveId = opts.buttonSaveId || "#button-save";
    this._opts.iframeUploadName = opts.iframeUploadName || "uploadifr";
    this._opts.iframeUpdateName = opts.iframeUpdateName || "updateifr";
};
 
manage.prototype.init = function(urls){
    var type = this._type;
    var opts = this._opts;
    
    $(opts.datagridId).datagrid({
        idField:"id",
        url:urls.queryUrl + "/" + type,
        fit:true,
        pagination:true,
        columns:[[
           {field:'ck',checkbox:true},
           {field:'thumbUri',title:'引导图',width:180,halign:'left',align:'center',
        	   formatter:function(val, row){
	               if(row.type=='IMAGE'){
	            	   var src = ctx + val + '?_=' + Date.parse(new Date());
	                   return '<img src="' + src + '" style="height:48px;"/>';    
	               }else{
	            	   return '无';
	               }
        	   }
           },
           {field:'id',title:'编号',width:120,sortable:true,hidden:true},
           {field:'name',title:'资源名称',width:320,sortable:true},
           {field:'description',title:'描述',width:320,sortable:true},
           {field:'createTime',title:'创建时间',width:145,sortable:true},
           {field:'publishTime',title:'发布时间',width:145,sortable:true,
        	   formatter:function(val,row){
	               if(val){
	                   return "<font style='font-weight: bold;color:red;'>" + val + "</font>";   
	               }
        	   }
           }
        ]],
        rowStyler: function(index,row){
        	if (type == 'image'){
        		return ';height:50px;';
        	}else{
        		return '';
        	}
        },
        onBeforeLoad:function(param){
             param['type'] = type; 
             if (type != 'image'){
         	 	$(opts.datagridId).datagrid('hideColumn', 'thumbUri');
             }else{
         	 	$(opts.datagridId).datagrid('showColumn', 'thumbUri');
             }
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
            if(item.iconCls == 'icon-save'){
            	$.ewcms.openWindow({windowId:opts.resourceUpdateWindowId,width:600,height:400,title:"更新资源",src : urls.resourceUrl + "?type=" + type +"&multi=false&resourceId="+row.id});
            }
            if(item.iconCls == 'icon-image-add'){
            	$.ewcms.openWindow({windowId:opts.thumbUpdateWindowId,width:450,height:200,title:"更新引导图",src: urls.thumbUrl + "/" + row.id});
            }
            if(item.iconCls == 'icon-publish'){
                operators.publish(opts.datagridId,urls.publishUrl);
            }
            if(item.iconCls == 'icon-remove'){
                operators.remove(opts.datagridId,urls.removeUrl);
            }
        }
    });
    
    $(opts.resourceUpdateWindowId).window({
        onClose:function(){
            updateifr.insert(function(data,success){
                if(success){
                    operators.reload(opts.datagridId);    
                }else{
                    $.messager.alert('错误','更新资源描述错误');
                }
            });
        }
    });
    
    $(opts.thumbUpdateWindowId).window({
        onClose:function(){
           $(opts.datagridId).datagrid('load');
        }
    });
    
    $(opts.toolbarUploadId).bind('click',function(){
        $.ewcms.openWindow({windowId:opts.resourceUploadWindowId,width:600,height:400,title:"上传资源",src:urls.resourceUrl+'?type='+type});
    });
    
    $(opts.toolbarPublishId).bind('click',function(){
        operators.publish(opts.datagridId,urls.publishUrl);
    });
    
    $(opts.toolbarRemoveId).bind('click',function(){
        operators.remove(opts.datagridId,urls.removeUrl);
    });
    
    $(opts.toolbarQueryId).bind('click',function(){
    	$.ewcms.query();
    });
    
    $(opts.buttonSaveId).bind('click',function(){
        operators.save(opts.iframeUploadName, opts.resourceUploadWindowId, opts.datagridId);
    });
};