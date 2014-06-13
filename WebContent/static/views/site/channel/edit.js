var ChannelEdit = function(){
	this._template = false;
	this._templateSource = false;
	this._process = false;
};

ChannelEdit.prototype.init = function(options){
	$('#systemtab').tabs({
		onSelect:function(title){
				if(title=="专栏模板" && !this._template){
					$("#edittplifr").attr('src', options.templateUrl);
					this._template = true;
				}
				if(title=="专栏资源" && !this._templateSource){
					$("#editsrcifr").attr('src', options.templateSourceUrl);
					this._templateSource = true;
				}
				if(title=="访问控制"){
					$("#editauthifr").attr('src', options.aclUrl);
				}
				if(title=="发布设置" ){
					$("#editquartzifr").attr('src', options.schedulingUrl);
				}	
				if(title=="基本设置"){
					$("#editinfoifr").attr('src', options.infoUrl);
				}		
				if (title=="审核流程" && !this._process){
					$("#editprocessifr").attr('src', options.processUrl);
					this._process = true;
				}												
		}
	});
};
