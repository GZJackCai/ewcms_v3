function Poll(pollingUrl, messageDetailUrl, todoUrl) {
    var longPolling = function(url, callback) {
        $.ajax({
            url: url,
            async: true,
            cache: false,
            global: false,
            timeout: 30 * 1000,
            dataType : "json",
            success: function (data, status, request) {
                callback(data);
                data = null;
                status = null;
                request = null;
                setTimeout(
                    function () {
                        longPolling(url, callback);
                    },
                    10
                );
            },
            error: function (xmlHR, textStatus, errorThrown) {
                xmlHR = null;
                textStatus = null;
                errorThrown = null;

                setTimeout(
                    function () {
                        longPolling(url, callback);
                    },
                    30 * 1000
                );
            }
        });
    };
    longPolling(pollingUrl, function(data) {
        if(data) {
            if(data.unreadMessageCount > 0) {
            	$('#tipMessage').empty();
            	var html = '<span id="messageFlash">';
            	html += '<a id="tb-unreadmessage" href="javascript:void(0);" onclick="showUnreadMessage()" onfocus="this.blur();" style="color:red;font-size:13px;" class="message-unread">【&nbsp;&nbsp;&nbsp;&nbsp;新消息(' + data.unreadMessageCount + ')】</a>';
            	html += '</span>';
            	$(html).appendTo('#tipMessage');
            }else{
            	$('#tipMessage').empty();
            }
            if(data.notices) {
                $('#notice .t-list').empty();
        		var noticesHtml = '<div class="t-list"><table width="100%">';
        	    var pro = [];
        	    $.each(data.notices, function(idx, item){
        	    	pro.push('<tr><td width="75%"><a href="javascript:void(0);" onclick="showMessageDetail(\'' + messageDetailUrl + '\','  +  item.id +  ',\'notice\');" alt="' + item.title + '"><span class="ellipsis">' + item.title + '</span></a></td><td width="10%">[' + item.sendUserName + ']' + '</td><td width="15%" align="right">' + item.date + '</td></tr>');
        	    });
        	    var html = pro.join("");
        	    noticesHtml += html + '</table></div>';
        	    $(noticesHtml).appendTo('#notice');
            }
            if(data.subscriptions){
            	$('#subscription .t-list').empty();
            	var subscriptionHtml = '<div class="t-list"><table width="100%">';
            	var pro = [];
            	$.each(data.subscriptions, function(idx, item){
                	pro.push('<tr><td width="75%"><a href="javascript:void(0);" onclick="showMessageDetail(\'' + messageDetailUrl + '\','  +  item.id +  ',\'subscription\');" alt="' + item.title + '"><span class="ellipsis">' + item.title + '</span></a></td><td width="10%">[' + item.sendUserName + ']' + '</td><td width="15%" align="right">' + item.date + '</td></tr>');
                });
                var html = pro.join("");
                subscriptionHtml += html + '</table></div>'
                $(subscriptionHtml).appendTo('#subscription');
            }
            if (data.todos){
            	$('#todo .t-list').empty();
            	var beApprovalHtml = '<div class="t-list"><table width="100%">';
            	var pro = [];
            	$.each(data.todos, function(idx, item){
           			pro.push('<tr><td width="50%"><a href="javascript:void(0);" onclick="showTodo(\'' + todoUrl + '\',' + item.id + ');">栏目：『' + item.name + '』 共有 ' + item.count + ' 条需要审批</a></td></tr>');
           		});
           		var html = pro.join("");
           		beApprovalHtml += html + '</table></div>'
           		$(beApprovalHtml).appendTo('#todo');
            }
            if (data.pops){
            	$.each(data.pops, function(idx, item){
            		$.messager.show({title:item.title,msg:item.content,width:'350',height:'200',timeout:5000,showType:'fade'});
            	});
            }
        }
    });
};

var showUnreadMessage = function(){
	$('#tipMessage').trigger('click');
};

var showMessageDetail = function(url, id, type){
	var detailUrl = url + '_' + id + '_' + type;
	$.ewcms.openWindow({windowId:'#edit-window',iframeId:'#editifr',src:detailUrl, width:700,height:400,title:'内容'});
};

var showTodo = function(url, channelId){
//	var id = 'content';
//		
//	$('li[id^="menu-"]').removeClass('tabs-selected');
//	$('div[id^="menusub-"]').css('display','none');
//	$('div[id^="tab-"]').css('display','none');
//	
//	$('#menu-' + id).addClass('tabs-selected');
//	$('#menusub-' + id).css('display', '');
//	$('#tab-' + id).css('display', '');
//	
//	$('#center').css('display','');
//	$('#west').panel('setTitle', $('#menu-' + id).text());
//	$('#tab-' + id).tabs('resize');
//	
//	$.ewcms.openTab({id:'#tab-content', title:'内容编辑',src: url})
};