var Content = function(parentTree){
	this._parentTree = parentTree || '#tt2';
};

Content.prototype.show = function(options){
	var parentTree = this._parentTree;
	
	var position="";
	var currentNode = parent.parent.$(parentTree).tree('getSelected');
	if (currentNode){
		var rootNode = parent.parent.$(parentTree).tree('getRoot');
		if (rootNode && (currentNode.id == rootNode.id)){
			position += rootNode.text + " >> ";
		}else{
			var text = [];
			if (rootNode){
				position += rootNode.text + " >> ";
				$.each(currentNode , function(){
					if (currentNode && currentNode.id != rootNode.id){
						text.push(currentNode.text);
						currentNode = parent.parent.$(parentTree).tree('getParent',currentNode.target);
					}
				});
			}
			for (var i = text.length - 1; i > 0; i--){
				if (typeof(text[i])!="undefined"){
					position += text[i] + " >> ";
				}
			}
			position += text[i] + " >> ";
		}
		position += "(" + options.id + ")" + options.path + "(" + options.description + ")" ;
    }
	position = "<span style='color:red;'>当前位置：" + position + "</span>";
	$.ewcms.openTopWindow({width:800,height:385,title:"模板编辑",url: options.url + '/'+ options.id,position:position});
};