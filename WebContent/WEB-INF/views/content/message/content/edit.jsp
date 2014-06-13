<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>订阅内容</title>
	<%@ include file="../../../taglibs.jsp"%>
	<script type="text/javascript">
		$(function(){
			$('#inputForm').validate({});
			parent.$('#tt').datagrid('reload');
		})
	</script>
  </head>
  <body>
    <%@ include file="../../../alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/content/message/content/save" method="post" modelAttribute="msgContent" class="form-horizontal">
	  <table class="formtable">
		<tr>
		  <td width="20%">标题：</td>
		  <td width="80%"><input type="text" id="title" name="title" size="50" maxlength="200" value="${msgContent.title}" class="required"/></td>
		</tr>
		<tr>
		  <td>内容：</td>
		  <td><textarea cols="46" id="detail" name="detail" class="required">${msgContent.detail}</textarea></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${msgContent.id}"/>
	  <input type="hidden" id="msgSendId" name="msgSendId" value="${msgSendId}"/>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
  </body>
</html>