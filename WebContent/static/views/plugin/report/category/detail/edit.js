var CategoryDetailEdit = function(){};

CategoryDetailEdit.prototype.init = function(options){
	$('#text_categories').combobox({
		url: options.textCategoryUrl,
		valueField:'id',
        textField:'text',
		editable:false,
		multiple:true,
		cascadeCheck:false,
		panelWidth:200,
		panelHeight:70
	});
	$('#chart_categories').combobox({
		url: options.chartCategoryUrl,
		valueField:'id',
        textField:'text',
		editable:false,
		multiple:true,
		cascadeCheck:false,
		panelWidth:200,
		panelHeight:70
	});
	
	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});
	parent.queryNews(selections);
};