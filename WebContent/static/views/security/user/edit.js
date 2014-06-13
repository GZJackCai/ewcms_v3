var UserEdit = function(){};

UserEdit.prototype.init = function(opts){
	if ('${close}' == 'true'){
  		parent.$('#edit-window').window('close');
  	}
  	//聚焦第一个输入框
  	$('#loginName').focus();
  	//为inputForm注册validate函数
  	$('#inputForm').validate({
	    rules: {
	    	loginName: {
	    		remote: opts.checkLoginNameUrl + '?oldLoginName=' + encodeURIComponent('${user.loginName}')
	  		}
		},
		messages: {
	  		loginName: {
	  			remote: '用户登录名已存在'
			},
			passwordConfirm: {
	  			equalTo: '输入与上面相同的密码'
			}
    	},
  		errorPlacement: function(error, element) {
			error.insertAfter( element );
  		}
	});

	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});
	parent.queryNews(selections);
};
