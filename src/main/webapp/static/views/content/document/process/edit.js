var ProcessEdit = function(userComboBox, roleComboBox){
	this._userComboBox = userComboBox || '#userInfo';
	this._roleComboBox = roleComboBox || '#roleInfo';
};

ProcessEdit.prototype.init = function(options){
	var userComboBox = this._userComboBox;
	var roleComboBox = this._roleComboBox;
	
	$(userComboBox).combobox({
		url: options.userUrl,
		valueField:'id',
        textField:'text',
		editable:false,
		multiple:true,
		cascadeCheck:false,
		panelWidth:380,
		panelHeight:100
	});
	$(roleComboBox).combobox({
		url: options.roleUrl,
		valueField:'id',
        textField:'text',
		editable:false,
		multiple:true,
		cascadeCheck:false,
		panelWidth:380,
		panelHeight:100
	});
	
	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});
	parent.queryNews(selections);
};