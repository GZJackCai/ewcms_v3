var ChannelInfo = function(){};

ChannelInfo.prototype.init = function(options){
	$('#referImage').bind("click", function(){
		$.ewcms.openWindow({windowId:"#insert-window",width:600,height:500,title:"图片选择",src:options.referImageUrl});
	});
	
	$('#clearImage').bind('click', function(){
		$("#viewImage").attr("src", options.clearImageUrl);
   		$("#iconUrl").attr("value","");
	});
	
	$('#pinyin').bind('click', function(){
		var channelName = $('#name').val();
   		$.post(options.pinyinUrl, {'channelName':channelName} ,function(data) {
   			$('#dir').val(data);
   		});
	});
	
	$('#insertIcon').bind('click', function(){
		uploadifr_insert.insert(function(data,success){
   			if (success){
   				$.each(data, function(index,value){
   	    			$("#viewImage").attr("src", value.uri);
   	    			$("#iconUrl").attr("value", value.uri);
   			   });
   			   $("#insert-window").window("close");
   			}else{
   				$.messager.alert('错误', '插入失败', 'error');
   			}
   	    });
	});
	
	$('#connect').bind('click', function(){
		$.post(options.connectUrl, {}, function(data) {
			if (data){
				$.messager.alert('提示', '重新计算本栏目被其他引用成功', 'info');
				$('#span-connect').html('已建立');
				$('#span-viewconnect').show();
			}else{
				$.messager.alert('提示', '重新计算本栏目被其他引用失败', 'info');
			}
		});
	});
	
	$('#disConnect').bind('click', function(){
		$.post(options.disConnectUrl, {}, function(data) {
			if (data){
				$.messager.alert('提示', '清除本栏目被其他引用成功', 'info');
				$('#span-connect').html('已断开');
				$('#span-viewconnect').hide();
			} else {
				$.messager.alert('提示', '清除本栏目被其他引用失败', 'info');
			}
		});
	});
	
	$('#viewConnect').bind('click', function(){
		$.ewcms.openWindow({windowId:"#connect-window",src:options.viewConnectUrl, width:750,height:370,url:url,title:"查看被引用栏目"});
	});
};