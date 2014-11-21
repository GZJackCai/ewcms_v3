var ChartReportEdit = function(){};

ChartReportEdit.prototype.init = function(){
	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});
	parent.queryNews(selections);
};

ChartReportEdit.prototype.sqlhelp = function(url){
	window.open(url,'popup','width=900,height=500,resizable=yes,toolbar=no,directories=no,location=no,menubar=no,scrollbars=yes,status=no,left=' + (window.screen.width - 900)/ 2 + ',top=' + (window.screen.height - 500) / 2);
};