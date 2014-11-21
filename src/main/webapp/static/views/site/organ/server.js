var OrganServer = function(){};

OrganServer.prototype.init = function(options){
	var organServer = this;
	
	$('#test-button').bind('click', function(){
		var formValue = $('#serverForm').serialize();
	    $.post(options.serverTestUrl,formValue,function(data){
	    	$.messager.alert('提示',data);
	    });
	});
	
	$('#outputType').bind('change', function(){
		organServer.typeChange();
	});
	
};

OrganServer.prototype.typeChange = function(){
	var value = $("#outputType").val();
	$('#serverInfo1').hide();
	$('#serverInfo2').hide();
	$('#serverInfo3').hide();
	$('#serverInfo4').hide();
	if(value=="SFTP" || value=="FTP" || value=="FTPS"){
		$('#serverInfo1').show();
		$('#serverInfo2').show();
		$('#serverInfo3').show();
		$('#serverInfo4').show();
	};
};