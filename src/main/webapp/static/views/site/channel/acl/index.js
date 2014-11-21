/*!
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

var ChannelAcl = function(urls){
    this._urls = urls;
};

ChannelAcl.prototype.init = function(opts){
    var urls = this._urls;
    $('#tt').propertygrid({
        height:'auto',
        url:urls.queryUrl,
        showGroup:true,
        scrollbarSize:0,
        singleSelect:true,
        columns:[[
            {field:'ck',checkbox:true},
            {field:'name',title:'名称',width:100},
            {field:'value',title:'权限',width:50,},
            {field:'explain',title:'说明',width:400,
            	formatter : function(val, rec) {
            		if (rec.group == '其他'){
            			if (rec.value == 'true'){
                			return '<font color="blue">继承了上级栏目所有权利</font>。';
                		}else{
                			return '<font color="red">不继承上级栏目所有权利</font>。';
                		}
            		}else{
            			if (rec.value=='1'){
                			return '<font color="blue">具有访问本栏目文章信息列表的权利</font>，<font color="red">但不具有对栏目文章内信息的操作权利</font>。';
                		}else if (rec.value=='2'){
                			return '<font color="blue">具有对本栏目文章信息进行新增、修改操作的权利</font>，<font color="red">但不具有对文章删除、审核、发布的权利。</font>';
                		}else if (rec.value=='3'){
                			return '<font color="blue">具有对本栏目文章信息进行删除操作的权利</font>，<font color="red">但不具有文章审核、发布的权利。</font>';
                		}else if (rec.value=='4'){
                			return '<font color="blue">具有对本栏目文章信息进行审核操作的权利</font>，<font color="red">但不具有文章发布的权利。</font>';
                		}else if (rec.value=='5'){
                			return '<font color="blue">具有对本栏目文章信息所有操作的权利</font>。';
                		}else if (rec.value=='6'){
                			return '<font color="blue">具有对本栏目文章信息所有操作的权利并具有查看栏目的权利</font>，<font color="red">但不具有对栏目的操作权利</font>。';
                		}else if (rec.value=='7'){
                			return '<font color="blue">具有对本栏目信息所有操作的权利并具有栏目创建、修改的权利</font>。';
                		}else if (rec.value=='8'){
                			return '<font color="blue">具有对本栏目信息所有操作的权利并具有栏目所有的权利</font>。';
                		}else if (rec.value=='9'){
                			return '<font color="blue">具有对本栏目进行管理员的权利</font>。';
                		}
            		}
            	}
            }
        ]],
        onSelect:function(rowIndex,data){
            if(data.group == '其他'){
                $('#btnremove').linkbutton('disable');
            }else{
                $('#btnremove').linkbutton('enable');
            }
        },
        groupFormatter : function(fvalue, rows){
        	return fvalue + ' - <span style="color:red">' + rows.length + ' 行</span>';
        },
        onAfterEdit:function(rowIndex,data,changes){
          if(changes){
              var params = {};
              if(data.name == '继承权限'){
            	  if(typeof(changes.value) == 'undefined') return;
            	  
                  params['inherit'] = changes.value;
                  $.post(urls.inheritUrl,params,function(data){
                      if(!data){
                          $.messager.alert('错误','继承权限错误','error');
                      } else {
                    	  $("#tt").propertygrid('reload');
                      }
                   });
              }else{
            	  if(typeof(changes.value) == 'undefined') return;
            	  var type = 'user';
            	  if (data.group == '角色权限'){
            		  type = 'role';
            	  }
                  params['name'] = data.name;
                  params['mask'] = changes.value;
                  params['type'] = type;
                  $.post(urls.saveUrl,params,function(data){
                      if(!data){
                          $.messager.alert('错误','更新权限错误','error');
                      } else{
                    	  $("#tt").propertygrid('reload');
                      }
                   });    
              }
          }
        },
        toolbar:[{
            id:'btnadd',
            text:'添加',
            iconCls:'icon-add',
            handler:function(){
            	$.ewcms.openWindow({windowId:'#edit-window',title:'增加 - 权限',width:400,height:200});
          }
        },'-',{
            id:'btnremove',
            text:'删除',
            iconCls:'icon-remove',
            handler:function(){
                var rows = $('#tt').datagrid('getSelections');
                if(rows.length == 0){
                    $.messager.alert('提示','请选择删除的记录','info');
                    return;
                }
                var type = 'user';
          	  	if (rows[0].group == '角色权限'){
          	  		type = 'role';
          	  	}
                var parameter = 'name=' + rows[0].name + '&type=' + type;
                $.messager.confirm('提示', '确定删除所选记录?', function(r){
                    if (r){
                        $.post(urls.deleteUrl,parameter,function(data){
                            if(data){
                                $("#tt").datagrid('reload');
                            }else{
                                $.messager.alert('错误','删除权限错误');
                            }
                      });
                    }
                },'info');
            }
        },'-',{
           	id:'btnok',
            text:'保存',
            iconCls:'icon-save',
            handler:function(){
            	$.messager.alert('提示','保存成功','info');
            }
        }]
    });    
    
    $('#button-save').bind('click',function(){
        var value = $("#permissionform").serialize();
        $.post(urls.saveUrl,value,function(data){
           if(data){
               $('#tt').propertygrid('reload');
           } else{
               $.messager.alert('错误',data.message,'error');
           }
        });
    });
        
    $('#query-tt').datagrid({
        width:500,
        pageSize:5,
        nowrap: false,
        rownumbers:true,
        idField:'',
        pagination:true,
        pageList:[5],
        singleSelect:true,
        fit:true,
        loadMsg:'',
        frozenColumns:[[
             {field:'ck',checkbox:true}
        ]],
        columns:[[]]
    });
    
    $('#button-query').bind('click',function(){
        var type =  $('#cc').combobox('getValue');
        var idField, columns;
        if(type == 'user'){
            $('#query-label-name').html('用户名:');
            $('#query-label-desc').html('姓名:');
            $('#query-input-name').attr('name','loginName');
            $('#query-input-desc').attr('name','realName');
            columns = [[
                {field:'loginName',title:'用户名',width:200},
                {field:'realName',title:'姓名',width:230}
            ]];
            idField = 'loginName';
        }else{
            $('#query-label-name').html('名称:');
            $('#query-label-desc').html('描述:');
            $('#query-input-name').attr('name','roleName');
            $('#query-input-desc').attr('name','caption');
            columns = [[
               {field:'roleName',title:'名称',width:200},
               {field:'caption',title:'描述',width:230}
            ]];
            idField = 'roleName';
        }
        
        var url = urls[type + 'QueryUrl'];
        $('#query-tt').datagrid({
            idField:idField,
            columns:columns,
            url:url
         });
        
        var title = '权限';
        if(type == 'user'){
            title = "用户";
        }else if(type == 'role'){
            title = "角色";
        }
        
        $.ewcms.openWindow({windowId:'#query-window',title:title,width:523,height:290});        
    });
    
    $('#button-selected').bind("click",function(){
        var type =  $('#cc').combobox('getValue');
        var row = $('#query-tt').datagrid("getSelected");
        if(row.length == 0){
            $.messager.alert('提示','请选择记录','info');
            return;
        }
        var name = '';
        if(type == 'user'){
            name = row.loginName;
        }else{
            name = row.roleName;
        }
        
       $('#input-name').val(name);
       $('#query-window').window('close');
    });
};
     