<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>作业设置</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/scheduling/jobclass/save" method="post" modelAttribute="jobClass" class="form-horizontal">
	  <table class="formtable" >
	    <tr>
	      <td width="20%">名称：</td>
		  <td width="80%"><input type="text" id="className" name="className" size="70" maxlength="127" value="${jobClass.className}"/></td>
		</tr>
		<tr>
		  <td>类实体：</td>
		  <td><input type="text" id="classEntity" name="classEntity" size="70" maxlength="127" value="${jobClass.classEntity}"/></td>
		</tr>
		<tr>
		  <td>描述：</td>
		  <td><textarea cols="46" id="description" name="description">${jobClass.description}</textarea></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${jobClass.id}"/>
	  <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>		
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/scheduling/jobclass/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _jobClassEdit = new JobClassEdit();
		$(function(){
			_jobClassEdit.init();
	  	});
    </script>
  </body>
</html>