<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>发送消息</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/content/message/send/save" method="post" modelAttribute="msgSend" class="form-horizontal">
	<table class="formtable">
      <tr>
        <td width="20%">标题：</td>
		<td width="80%"><input type="text" id="title" name="title" size="50" maxlength="200" value="${msgSend.title}" class="required"/></td>
	  </tr>
	  <tr>
  		<td>内容：</td>
		<td><textarea cols="46" id="content" name="content" class="required">${msgContent.content}</textarea></td>
	  </tr>
	  <tr>
  		<td>类型：</td>
		<td><form:select id="type" path="type" items="${sendTypeMap}" itemLabel="description" cssClass="easyui-combobox"/></td>
	  </tr>
	  <tr id="trUserName">
 		<td>收信人：</td>
		<td><input id="userInfo" name="userNames"/></td>
	  </tr>
	</table>
	<input type="hidden" id="id" name="id" value="${msgSend.id}"/>
    <c:forEach var="selection" items="${selections}">
  	  <input type="hidden" name="selections" value="${selection}" />
	</c:forEach>
    </form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a id="tb-submit" class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);">提交</a>
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/message/send/edit.js"></script>
    <script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
    	var _messageSendEdit = new MessageSendEdit();
    	$(function(){
    		_messageSendEdit.init({
    			userUrl : '${ctx}/content/message/send/user?msgSendId=${msgSend.id}'
    		});
    	});
    </script>
  </body>
</html>