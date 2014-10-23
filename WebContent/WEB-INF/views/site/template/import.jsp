<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>模板导入</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
    <div style="width:100%;height:100%;overflow:auto">
	<h1 class="title">模板文件导入</h1>
	<form:form action="${ctx}/site/template/saveImport" method="post" enctype="multipart/form-data" modelAttribute="template" class="form-horizontal">
	  <input type="hidden" id="id" name="id" value="${template.id}"/>
	  <fieldset>
		<table class="formtable" align="center">
		  <tr>
			<td>模板路径：</td>
			<td><input type="text" id="path" name="path" value="${template.path}" size="50"/></td>
		  </tr>
		  <tr>
			<td width="20%">模板文件：</td>
			<td width="80%"><input type="file" id="templateFile" name="templateFile" size="50"/></td>
		  </tr>
		  <tr>
			<td colspan="2" style="padding:0;">
			  <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
				<a class="easyui-linkbutton" icon="icon-save" href="javascript:document.forms[0].submit();">保存</a>
				<a class="easyui-linkbutton" icon="icon-undo" href="javascript:document.forms[0].reset();">重置</a>
			  </div>								
			</td>
		  </tr>
		</table>
	  </fieldset>
	</form:form>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript">
		<c:if test="${not empty template.id}">
			parent.importLoad();
		</c:if>
	</script>
  </body>
</html>