var MessageDetailIndex = function(){};

MessageDetailIndex.prototype.init = function(options){
	$('#tb-subscribe').bind('click', function(){
		$.post(options.subscribeUrl, {}, function(data) {
			if (data == 'own'){
				$.messager.alert('提示','您不能订阅自已发布的信息！','info');
				return;
			}
			if (data == 'exist'){
				$.messager.alert('提示','您已订阅了此信息，不需要再订阅！','info');
				return;
			}
			if (data == 'false'){
				$.messager.alert('提示','订阅信息失败！','info');
				return;
			}
			if (data == 'true'){
				$.messager.alert('提示','订阅成功！','info');
				return;
			}
		});
	});
};