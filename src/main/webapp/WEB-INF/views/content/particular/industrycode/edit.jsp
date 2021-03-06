<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>行业编码</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/content/particular/industrycode/save" method="post" modelAttribute="industryCode" class="form-horizontal">
	  <table class="formtable" >
		<tr>
		  <td width="20%">行业编码：</td>
		  <td width="80%"><input type="text" id="code" name="code" maxlength="4" size="4" value="${industryCode.code}"/></td>
		</tr>
		<tr>
		  <td>行业名称：</td>
		  <td><input type="text" id="name" name="name" maxlength="100" size="60" value="${industryCode.name}"/></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${industryCode.id}"/>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/particular/industrycode/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _industryCodeEdit = new IndustryCodeEdit();
		$(function(){
			_industryCodeEdit.init();
	  	});
    </script>
  </body>
</html>