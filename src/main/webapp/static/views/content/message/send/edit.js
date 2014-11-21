var MessageSendEdit = function(){};

MessageSendEdit.prototype.init = function(options){
	$('#inputForm').validate({});
	
	$('#userInfo').combobox({
		url: options.userUrl,
		valueField:'id',
        textField:'text',
		editable:false,
		multiple:true,
		cascadeCheck:false,
		panelWidth:200,
		panelHeight:60
	});
	
	$('#type').combobox({
		onChange:function(newValue, oldValue){
			if (newValue != 'GENERAL'){
	    		$('#trUserName').hide();
	    	}else{
	    		$('#trUserName').show();
	    	}
		}
	});
	
	$('#inputForm').submit(function(){
		if ($('#type').combobox('getValue')=='GENERAL'){
			if ($('#userInfo').combobox('getValue') == ''){
				$.messager.alert('提示','收件用户不能为空','info');
				return false;
			}
		}
	});
	
	$('#tb-submit').bind('click', function(){
		$('#inputForm').submit();
	});
	
	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});
	parent.queryNews(selections);
};