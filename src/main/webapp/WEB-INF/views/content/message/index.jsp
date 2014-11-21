<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>个人消息</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
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
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/message/index.js"></script>
	<script type="text/javascript">
		var _messageIndex = new MessageIndex();
		$(function(){
			_messageIndex.init({
				sendUrl : '${ctx}/content/message/send/index',
				receiveUrl : '${ctx}/content/message/receive/index'
			});
		});
	</script>  	
  </body>
</html>