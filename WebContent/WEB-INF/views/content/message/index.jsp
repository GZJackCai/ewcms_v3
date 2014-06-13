<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>个人消息</title>	
	<%@ include file="../../taglibs.jsp"%>
	<script type="text/javascript" src="${ctx}/static/views/content/message/index.js"></script>
	<script type="text/javascript">
		var _messageIndex = new MessageIndex();
		$(function(){
			_messageIndex.init({
				sendUrl : '${ctx}/content/message/send/index',
				receiveUrl : '${ctx}/content/message/receive/index'
			});
		});
		  function showRecord(id){
			
		  }
		  function refreshTipMessage(){
			  $.ajax({
				  type:'post',
				  datatype:'json',
				  cache:false,
				  url:'<s:url namespace="/message/receive" action="unRead"/>',
				  data: '',
				  success:function(message, textStatus){
					  parent.$('#tipMessage').empty();
				      var html = '<span id="messageFlash">';
				      if (message != 'false'){
				      	var tiplength = message.length;
				        html += '<a href="javascript:void(0);" onclick="javascript:_home.addTab(\'个人消息\',\'message/index.do\');return false;" onfocus="this.blur();" style="color:red;font-size:13px;text-decoration:none;">【<img src="./ewcmssource/image/msg/msg_new.gif"/>新消息(' + tiplength + ')】</a>';
				      }
				      html += '</span>';
				      $(html).appendTo('#tipMessage');
				  }
			  });
		   }
		</script>		
	</head>
	<body>
	  <div class="easyui-tabs" id="msgboxtab" border="false" fit="true">
	    <div title="收件箱">
		  <iframe id="receiveifr"  name="receiveifr" class="editifr" scrolling="no"></iframe>	
		</div>			
		<div title="发件箱">
		  <iframe id="sendifr" name="sendifr" class="editifr" scrolling="no"></iframe>
		</div>
	  </div>	
	</body>
</html>