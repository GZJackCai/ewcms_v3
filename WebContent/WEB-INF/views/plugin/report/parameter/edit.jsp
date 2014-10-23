<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>参数设置</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/plugin/report/parameter/save" method="post" modelAttribute="parameter" class="form-horizontal">
	  <table class="formtable" >
		<tr>
		  <td width="15%">参数编号：</td>
		  <td width="35%"><input type="text" id="id" name="id" readonly="readonly" value="${parameter.id}"/></td>
		  <td width="15%">参数名：</td>
		  <td width="35%"><input type="text" id="enName" name="enName" readonly="readonly" value="${parameter.enName}"/></td>
		</tr>
		<tr>
          <td>中文名：</td>
		  <td><input type="text" type="text" id="cnName" name="cnName" value="${parameter.cnName}"/></td>
		  <td>默认值：</td>
		  <td>
		    <span id="defaultvalue_span"><input type="text" id="defaultValue" name="defaultVale" value="${parameter.defaultValue}"/></span>
			<span id="userName_span"><input type="text" id="userName" name="sessionValue"/></span>
		  </td>
		</tr>
		<tr>
		  <td>数据输入方式：</td>
		  <td>
		  	<form:select path="type" items="${typeMap}" itemLabel="description"/>
		  </td>
		  <td>辅助数据设置：</td>
		  <td><input type="text" id="value" name="value" value="${parameter.value}"/></td>
		</tr>
	  </table>
	  <input type="hidden" id="className" name="className" value="${parameter.className}"/>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>
	</form:form>
  	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/report/parameter/edit.js"></script>
    <script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
   		var _parameterEdit = new ParameterEdit();
    	$(function(){
    		_parameterEdit.init();
    	});
    </script>
  </body>
</html>