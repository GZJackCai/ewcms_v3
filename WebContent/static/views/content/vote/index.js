var VoteIndex = function(tree){
	this._tree = tree || '#tt2';
};

VoteIndex.prototype.init = function(options){
	var tree = this._tree;
	
	$(tree).tree({
		checkbox: false,
		url: options.channelTreeUrl,
		onClick:function(node){
			if (node.id == $(tree).tree('getRoot')){
				url = '';
			}else{
				if (node.attributes.type == 'ARTICLE'){
					url = options.questionnaireUrl + '/' + node.id;
				}else {
					url = '';
				}
			}
			$('#mainifr').attr('src',url);
			$('#subjectifr').attr('src','');
		}
	});
};