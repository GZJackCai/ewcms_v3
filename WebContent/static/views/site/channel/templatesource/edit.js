var TemplateSourceEdit = function(){};

TemplateSourceEdit.prototype.init = function(){
	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});
	parent.queryNews(selections);
};