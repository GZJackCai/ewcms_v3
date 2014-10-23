/*!
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

UploadUtils = {
    getInputElementId:function(row){
        return "input_discription_"+row.id;
    } ,
    getInputElementName:function(row){
        return "descriptions["+row.id+"]";
    },
    getImageElementId:function(row){
        return "img_" + row.id;
    },
    getImageFileElementId:function(row){
        return "image_upload_" + row.id;
    },
    getImageQueueElementId:function(row){
        return "image_queue_" + row.id;
    },
    initThumbUpload:function(options,row){
        var utils = this;
        $("#"+this.getImageFileElementId(row)).uploadify({
        	'swf': options.uploaderUrl,
            'uploader': options.thumbScriptUrl + ";jsessionid=" + options.jsessionid,
        	'buttonText': '上传引导图文件',
        	'fileObjName': 'myUpload',
            'fileTypeDesc': '支持的格式：',
            'fileTypeExts' : '*.jpg;*.gif;*.jpeg;*.png;*.bmp',
            'expressInstall' : options.expressInstallUrl,
            'multi':  false,
            'formData':  {'resourceId':row.id},
            'progressData':'speed',
            'queueID':  this.getImageQueueElementId(row),
            'height' : 20,
            'width' : 100,
            'onFallback':function(){
            	$.messager.alert('提示','您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。');
            },
            'onUploadSuccess' : function(file, data, response) {  
            	if (response){ 
	            	var thumbUri = (new Function( "return " + data ))();
	                if(thumbUri != null){
	                	var src = ctx + thumbUri + "?_=" + Date.parse(new Date());
	                	$("#"+utils.getImageElementId(row)).attr("src",src);    
	                }else{
	                    $.messager.alert('提示',fileObj['name']+'上传错误');
	                }
            	}else{
            		$.messager.alert('提示','上传错误');
            	}
            }
        });
    }
};

var Upload = function(saveaction, options){
    this._saveaction = saveaction;
    this._options = options;
};

Upload.prototype.init = function(){
    var utils = UploadUtils;
    var options = this._options;
    
    $('#tt').datagrid({
        //nowrap: false,
        width:540,
        height:280,
        columns:[[
        	{field:'ck',checkbox:true,width:20},
        	{field:'id',hidden:true},
        	{field:'thumbUri',title:'引导图',width:60,align:'center',
            	formatter:function(value,row){
                    if(value){
                       	value = ctx + value;
                    }else{
                        value = "" ;
                    }
                    return '<img id="'+utils.getImageElementId(row)+'" src="' + value + '" style="width:40px;height:30px;margin:0;padding: 0;"/>';
                }
            }, 
            {field:'description',title:'描述',width:180,
             	formatter:function(value,row){
             		return "<input id='" + utils.getInputElementId(row) + "' type='text' name='"+ utils.getInputElementName(row) + "' value='" + value + "' style='width:170px;' onchange=" + options.objectName + ".descChange('" + utils.getInputElementId(row) + "') />";
             	}
         	}, 
         	{field:'uploadImage',title:'引导图上传',width:120,align:'center',
            	formatter:function(value,row){
                	return '<input type="file" id="'+utils.getImageFileElementId(row)+'"/><div id="'+utils.getImageQueueElementId(row)+'"></div>' ;
            	}
         	}
        ]],
        onSelect:function(rowIndex, rowData){
        	$("#"+utils.getInputElementId(rowData)).attr("name",utils.getInputElementName(rowData));
        },
        onUnselect:function(rowIndex, rowData){
        	$("#"+utils.getInputElementId(rowData)).attr("name","");
        },
        onBeforeLoad:function(param){
        	if (options.type != 'image'){
            	$('#tt').datagrid('hideColumn', 'thumbUri');
            }else{
            	$('#tt').datagrid('showColumn', 'thumbUri');
            }
        	return true;
        }
    });
    
    $("#upload").uploadify({
        'swf': options.uploaderUrl,
        'uploader': options.scriptUrl + ";jsessionid=" + options.jsessionid,
    	'buttonText': '上传文件',
    	'fileObjName': 'myUpload',
        'fileTypeDesc': '支持的格式：',
        'fileTypeExts' : options.fileExt,
        'expressInstall' : options.expressInstallUrl,
        'multi':  options.multi,
        'formData': {'resourceId':options.resourceId, 'type':options.type},
        'progressData':'speed',
        'queueID': 'upload_queue',
        'height' : 20,
        'width' : 100,
        'onFallback':function(){
        	$.messager.alert('提示','您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。');
        },
        'onUploadStart' : function(file) {
        	$("#upload_queue").css('display','');
        },
        'onUploadComplete' : function(file){
        	$("#upload_queue").css('display','none');
        },
        'onUploadSuccess' : function(file, data, response) {
        	if (response){
	        	var res = (new Function( "return " + data ))();
	            if(res != null){
	                $('#tt').datagrid("appendRow",res);
	                utils.initThumbUpload(options, res);
	                
	                
	            }else{
	                $.messager.alert('提示',fileObj['name']+'上传错误');
	            }
        	}else{
        		$.messager.alert('提示','上传错误');
        	}
        },
        'onQueueComplete':function(queueData){
            $("#resource_infos").css('display','');
            $('#tt').datagrid("selectAll");
            $.each($('td'), function(index, td){
                $(td).bind('click',function(e){
                    return false;
                });
            });
        }
    });
};

//Upload.prototype.descChange = function(id){
//	$('#' + id).bind("propertychange",function(){
//		$('#' + id).val($('#' + id).val);
//	})
//};

Upload.prototype.insert=function(callback,message){
    var params = $('#resource_infos').serialize();
    params = decodeURIComponent(params, true);
    params= encodeURI(encodeURI(params));
    $.post(this._saveaction,params,function(data){
        if(callback){
            callback(data.value, true);
        }else{
            if(data == null){
                if(message){
                    $.messager.alert('提示',message, 'info');
                }else{
                    $.messager.alert('提示','插入资源错误', 'info');    
                }
            }
        }
     },'json');
};