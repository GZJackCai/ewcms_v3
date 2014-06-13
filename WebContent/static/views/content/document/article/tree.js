var ArticleTree = function(tree){
	this._tree = tree || '#tt2';
};

ArticleTree.prototype.init = function(options){
	var tree = this._tree;
	$(tree).tree( {
		checkbox : false,
		url : options.queryUrl,
		onClick : function(node) {
			rootnode = $(tree).tree('getRoot');
			currentnode = node;
			var url = '';
			if (rootnode.id == node.id){
				url = options.articleUrl + '/' + node.id;;
			}else{
				if (node.attributes.type == 'ARTICLE' || node.attributes.type == 'RETRIEVAL'){
					url = options.articleUrl + '/' + node.id;
				}else if (node.attributes.type == 'LEADER'){
					url = options.articleUrl + '/' + node.id;
				}else if (node.attributes.type == 'LEADERARTICLE'){
					url = options.referUrl + '/' + node.id;
				}else if (node.attributes.type == 'ONLINE'){
					url = options.workingbodyUrl + '/' + node.id;
				}else if (node.attributes.type == 'INTERACTION'){
					url = options.interactionUrl;
				}else if (node.attributes.type == 'SPEAK'){
					url = options.speakUrl;
				}else if (node.attributes.type == 'ADVISOR'){
					url = options.advisorUrl;
				}else if (node.attributes.type == 'PROJECT'){
					url = options.pbUrl + '/' + node.id;
				}else if (node.attributes.type == 'PROJECTARTICLE'){
					url = options.paUrl + '/' + node.id;
				}else if (node.attributes.type == 'ENTERPRISE'){
					url = options.ebUrl + '/' + node.id;
				}else if (node.attributes.type == 'ENTERPRISEARTICLE'){	
					url = options.eaUrl + '/' + node.id;
				}else if (node.attributes.type == 'EMPLOYE'){
					url = options.mbUrl + '/' + node.id;
				}else if (node.attributes.type == 'EMPLOYEARTICLE'){	
					url = options.maUrl + '/' + node.id;
				}else if (node.attributes.type == 'NODE'){
					url =  options.articleUrl + '/' + node.id + '?node=true';
				}
			}
			$("#editifr").attr('src', url);
		}
	});
	
	$('#treeload-button').bind('click', function(){
		$(tree).tree('reload');
	});
};