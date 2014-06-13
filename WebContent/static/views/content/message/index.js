var MessageIndex = function(){
	
};

MessageIndex.prototype.init = function(options){
	$('#msgboxtab').tabs({
		onSelect:function(title){
			if(title=='收件箱'){
				$('#receiveifr').attr('src',options.receiveUrl);
			}else if(title=='发件箱'){
			    $('#sendifr').attr('src',options.sendUrl);
			}
		}
	});
};