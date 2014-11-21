<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>模板信息</title>			
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
	<%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form action="${ctx}/site/template/saveInfo" enctype="multipart/form-data" method="post" modelAttribute="template" class="form-horizontal">
	  <input type="hidden" id="id" name="id" value="${template.id}"/>
      <fieldset>
        <legend><span style="color:red;">模板基本信息</span></legend>
		<table class="formtable" align="center">
		  <tr>
		    <td width="20%">模板<c:choose><c:when test="${template.fileType=='DIRECTORY'}">目录</c:when><c:otherwise>文件</c:otherwise></c:choose>路径：</td>
			<td width="80%"><input type="text" id="path" name="path" size="60" value="${template.path}"/></td>
		  </tr>						
		  <tr>
			<td>模板<c:choose><c:when test="${template.fileType=='DIRECTORY'}">目录</c:when><c:otherwise>文件</c:otherwise></c:choose>名称：</td>
			<td><input type="text" id="name" name="name" readonly="readonly" size="60" value="${template.name}"/></td>
		  </tr>
		  <c:if test="${template.fileType!='DIRECTORY'}">
		  <tr>
			<td>导入模板文件：</td>
			<td><input type="file" id="templateFile" name="templateFile" size="60"/></td>
		  </tr>		
		  </c:if>						
		  <tr>
			<td>模板<c:choose><c:when test="${template.fileType=='DIRECTORY'}">目录</c:when><c:otherwise>文件</c:otherwise></c:choose>说明：</td>
			<td><textarea rows="10" cols="56" id="describe" name="describe">${template.describe}</textarea></td>
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
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
  </body>
</html>