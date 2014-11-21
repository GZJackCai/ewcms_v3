var NotesEdit = function(){};

NotesEdit.prototype.init = function(){
	if ($('#warn').attr('checked') == 'checked') {
        $('#tr_warn').show();
    } else {
        $('#tr_warn').hide();
    }
	
	$('#warn').bind('click', function(){
		if ($('#warn').attr('checked') == 'checked') {
            $('#tr_warn').show();
        } else {
            $('#tr_warn').hide();
        }
	});
	
	var selections = [];
	$.each($('input[name="selections"]'),function(index,o){
  		selections[index] = $(o).val();
	});

	try{
		parent.queryNews(selections);
	}catch(e){}
};