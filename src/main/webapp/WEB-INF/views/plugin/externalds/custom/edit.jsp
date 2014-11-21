<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>Custom数据源设置</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/plugin/externalds/custom/save" method="post" modelAttribute="customDs" class="form-horizontal">
	  <table class="formtable" >
		<tr>
		  <td width="20%">名称：</td>
		  <td width="80%"><input type="text" id="name" name="name" maxlength="10" value="${customDs.name}"/></td>
		</tr>
	    <tr>
		  <td>自定义名称：</td>
		  <td><input type="text" id="customName" name="customName" size="70" maxlength="127" value="${customDs.customName}"/></td>
		</tr>
		<tr>
		  <td>自定义方法：</td>
		  <td><input type="text" id="customMethod" name="customMethod" size="70" maxlength="127" value="${customDs.customMethod}"/></td>
		</tr>
		<tr>
		  <td>描述：</td>
		  <td><textarea cols="46" id="remarks" name="remarks">${customDs.remarks}</textarea></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${customDs.id}"/>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>			
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/externalds/custom/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _customDsEdit = new CustomDsEdit();
		$(function(){
			_customDsEdit.init();
	  	});
    </script>
  </body>
</html>