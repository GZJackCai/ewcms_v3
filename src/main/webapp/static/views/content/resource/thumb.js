/*!
 * Copyright (c)2010-2011 Enterprise Website Content Management System(EWCMS), All rights reserved.
 * EWCMS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * http://www.ewcms.com
 */

var thumb = function(id,opts){
    this._id = id;
    this._opts = opts;
};

thumb.prototype.init=function(){
    var id = this._id;
    var opts = this._opts;
    
    $("#upload").uploadify({
        'swf': opts.uploaderUrl,
        'uploader': opts.scriptUrl + ';jsessionid=' + opts.jsessionid,
        'buttonText': '上传引导图文件',
        'fileObjName': 'myUpload',
        'expressInstall':opts.expressInstallUrl,
        'queueID': 'upload_queue',
        'fileTypeDesc': '支持的格式：',
        'fileTypeExts' : '*.jpg;*.gif;*.jpeg;*.png;*.bmp',
        'multi':false,
        'formData': {'resourceId':id},
        'auto':true,
        //'removeCompleted':false,
        'height' : 20,
        'width' : 100,
        'onFallback':function(){
        	$.messager.alert('提示','您未安装FLASH控件，无法上传图片！请安装FLASH控件后再试。');
        },
        'onUploadSuccess' : function(file, data, response) {  
        	if (response){ 
            	var thumbUri = (new Function( "return " + data ))();
                if(thumbUri != null){
                	var uri = ctx + thumbUri + "?_=" + Date.parse(new Date());
                	parent.$('#img_' + id).attr('src',uri);
                }else{
                    $.messager.alert('提示',fileObj['name']+'上传错误');
                }
        	}else{
        		$.messager.alert('提示','上传错误');
        	}
        }
    });
};
