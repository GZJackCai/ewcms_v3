var ParameterEdit = function(){
};

ParameterEdit.prototype.init = function(){
	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});
	parent.queryNews(selections);
	
	if ($('#parametersType').val() == 'SESSION'){
		$('#userName_span').show();
		$('#defaultvalue_span').hide();
	}else{
		$('#userName_span').hide();
		$('#defaultvalue_span').show();
	}
	$('#parametersType').click(function() {
		if ($('#parametersType').val() == 'SESSION'){
			$('#userName_span').show();
			$('#defaultvalue_span').hide();
		}else{
			$('#userName_span').hide();
			$('#defaultvalue_span').show();
		}
	});
};