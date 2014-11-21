<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>文字报表设置</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/plugin/report/text/save" method="post" modelAttribute="textReport" enctype="multipart/form-data" class="form-horizontal">
	  <table class="formtable" >
		<tr>
		  <td width="20%">报表名：</td>
		  <td width="80%"><input type="text" id="name" name="name" maxlength="20" value="${textReport.name}"/></td>
		</tr>
		<tr>
		  <td>报表文件：</td>
		  <td><input type="file" accept="jrxml" id="textReportFile" name="textReportFile" onchange="javascript:if(this.value.toLowerCase().lastIndexOf('jrxml')==-1){alert('请选择jrxml文件！');this.value='';}"/></td>
		</tr>
		<tr>
		  <td>数据源类型：</td>
		  <td>
		    <form:select path="baseDs.id" items="${baseDsList}" itemLabel="name" itemValue="id"/>
		  </td>
		</tr>
		<tr>
		  <td>是否隐藏：</td>
		  <td><form:checkbox path="hidden"/></td>
		</tr>
        <tr>
		  <td>备注：</td>
		  <td><textarea rows="10" cols="56" id="remarks" name="remarks">${textReport.remarks}</textarea></td>
		</tr>			
	  </table>
	  <input type="hidden" id="id" name="id" value="${textReport.id}"/>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/report/text/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _textReportEdit = new TextReportEdit();
		$(function(){
			_textReportEdit.init();
	  	});
    </script>
  </body>
</html>