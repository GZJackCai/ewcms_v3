var RecyclebinTree = function(tree){
	this._tree = tree || '#tt2';
};

RecyclebinTree.prototype.init = function(options){
	var tree = this._tree;
	
	$(tree).tree( {
		checkbox : false,
		url : options.treeUrl,
		onClick : function(node) {
			currentnode = node;
			$("#editifr").attr('src', options.indexUrl + '/' + node.id);
		}
	});
	
	$('#treeload-button').bind('click', function(){
		$(tree).tree('reload');
	});
};