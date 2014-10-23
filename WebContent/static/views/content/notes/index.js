var NotesIndex = function(options){
	this._addUrl = options.addUrl;
	this._editUrl = options.editUrl;
};

NotesIndex.prototype.init = function(options){
	$('.a_notes_value').not('span.span_title').draggable({
		revert:true,
		proxy:'clone'
	});
	
	$('.div_notes').not('span.span_title').droppable({
		onDragEnter:function(e, source){},
	    onDragLeave:function(e, source){},
	    onDrop:function(e, source){
	    	$(this).append(source);
		    var divMemoId = $(source).attr('id');    
		    var targetDivId = $(source).parents('div:first').attr('id');
		    var memorandaId = divMemoId.split('_')[3];
		    var dropDay = targetDivId.split('_')[2];
		    $.post(options.dropUrl + '/' + memorandaId + '_' + $('#year').val() + '_' + $('#month').val() + '_' + dropDay,{},function(data){
		    	if (!data){}
		    });
	     }
	 });
	
	$('#tb-today').bind('click', function(){
		$.post(options.toDayUrl,{},function(data) {
	    	$('tr').remove('.notes_tr');
	    	$('#result').append(data);
	    });
	});
	
	$('#tb-notedetial').bind('click', function(){
		$.ewcms.openWindow({windowId:'#edit-window',iframeId:'#editifr', src: options.noteDetailUrl, width:1450,height:500,title:'备访录列表'});
	});
	
	var monthSelect = function(year, month, weight){
		loadingEnable();
		var monthValue = parseInt(month) + weight;
	    if (monthValue >= 13){
	        year = parseInt(year) + 1;
	        monthValue = monthValue - 12;
	    }else if (monthValue <= 0){
	        year = parseInt(year) - 1;
	        monthValue = monthValue + 12;
	    }
	    $('#year').attr('value',year);
	    $('#month').attr('value',monthValue);
	    $.post(options.changeDateUrl + '/' + year + '_' + monthValue,{},function(data) {
	        $('tr').remove('.notes_tr');
	        $('#result').append(data);
	        loadingDisable();
	    });
	};
	
	$('#tb-prevMonth').bind('click', function(){
		monthSelect($('#year').val(), $('#month').val(), -1);
	});
	
	$('#tb-nextMonth').bind('click', function(){
		monthSelect($('#year').val(), $('#month').val(), 1);
	});
};

NotesIndex.prototype.addNotes = function(i){
	var title = '新增备忘(' + $('#year').val() + '年' + $('#month').val() + '月' + i + '日)';
	$('#bntRemove').linkbutton('disable');
	$.ewcms.openWindow({windowId:'#edit-window',iframeId:'#editifr',src : this._addUrl + '/' + $('#year').val() + '_' + $('#month').val() + '_' + i, width:420,height:330,title:title});
};

NotesIndex.prototype.editNotes = function(){
	$('#bntRemove').linkbutton('enable');
	$.ewcms.openWindow({windowId:'#edit-window',iframeId:'#editifr',src : this._editUrl, width:420,height:330,title:'修改备忘'});
};

function loadingEnable(){
	$('<div class="datagrid-mask"></div>').css({display:'block',width:'100%',height:$(window).height()}).appendTo('body');
	$('<div class="datagrid-mask-msg"></div>').html('<font size="9">正在处理，请稍候。。。</font>').appendTo('body').css({display:'block',left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2}); 
};
 
function loadingDisable(){
    $('.datagrid-mask-msg').remove();
    $('.datagrid-mask').remove();
};

function queryNews(selections){
	$.ewcms.query({
		selections:selections
	});
};