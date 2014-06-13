var IndustryCodeEdit = function(){};

IndustryCodeEdit.prototype.init = function(){
	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});
	parent.queryNews(selections);
	
	if ($('#id').val() != ""){
    	$('#code').attr('readonly', true);
    }else{
    	$('#code').attr('readonly', false);
    }
};