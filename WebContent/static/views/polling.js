function Poll(username) {
    var pollingUrl = "/content/message/polling/" + username;
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
            if(data.unreadMessageCount) {
            	$('#tipMessage').empty();
            	var html = '<span id="messageFlash">';
            	if (data.unreadMessageCount > 0){
            	  	html += '<a href="javascript:void(0);" onclick="javascript:_home.addTab(\'个人消息\',\'message/index.do\');return false;" onfocus="this.blur();" style="color:red;font-size:13px;text-decoration:none;" class="message-unread">【&nbsp;&nbsp;&nbsp;&nbsp;新消息(' + data.unreadMessageCount + ')】</a>';
            	}
            	html += '</span>';
            	$(html).appendTo('#tipMessage');
            }
            if(data.notices) {
                //notification.update(data.notifications);
                $('#notice .t-list').empty();
        		var noticesHtml = '<div class="t-list"><table width="100%">';
        	    var pro = [];
        	    $.each(data.notices, function(idx, item){
        	    	pro.push('<tr><td width="80%"><a href="javascript:void(0);" onclick="showRecord(\'\',' + item.id + ');" style="text-decoration:none;" alt="' + item.title + '"><span class="ellipsis">' + item.title + '</span></a></td><td width="10%">[' + item.sendUserName + ']' + '</td><td width="10%" align="right">' + item.date + '</td></tr>');
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
                	pro.push('<tr><td width="80%"><a href="javascript:void(0);" onclick="showRecord(\'\',' + item.id + ');" style="text-decoration:none;"><span class="ellipsis">' + item.title + '</span></a></td><td width="10%">[' + item.sendUserName + ']' + '</td><td width="10%" align="right">' + item.date + '</td></tr>');
                });
                var html = pro.join("");
                subscriptionHtml += html + '</table></div>'
                $(subscriptionHtml).appendTo('#subscription');
            }
            if (data.todos){
            	$('#todo .t-list').empty();
            	var beApprovalHtml = '<div class="t-list"><table width="100%">';
            	var pro = [];
            	$.each(dataObj.todos, function(idx, item){
           			pro.push('<tr><td width="50%"><a href="javascript:void(0);" style="text-decoration:none;">栏目：『' + item.name + '』 共有 ' + item.count + ' 条需要审批</a></td></tr>');
           		});
           		var html = pro.join("");
           		beApprovalHtml += html + '</table></div>'
           		$(beApprovalHtml).appendTo('#todo');
            }
        }
    });
};