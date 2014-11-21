var RoleEdit = function(){};

RoleEdit.prototype.init = function(opts){
	if ('${close}' == 'true'){
		parent.$('#edit-window').window('close');
	}
	//聚焦第一个输入框
	$('#name').focus();
	//为inputForm注册validate函数
	$('#inputForm').validate({
		rules: {
			roleName: {
				remote:  opts.checkRoleNameUrl + '?oldRoleName=' + encodeURIComponent('${role.roleName}')
			}
		},
		messages: {
			roleName: {
				remote: '权限组名已存在'
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