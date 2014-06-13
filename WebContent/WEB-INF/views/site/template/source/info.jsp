<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>资源信息</title>	
	<%@ include file="../../../taglibs.jsp" %>
  </head>
  <body>
    <%@ include file="../../../alertMessage.jsp" %>
	<form:form action="${ctx}/site/template/source/saveInfo" namespace="/site/template/source" enctype="multipart/form-data" modelAttribute="templateSource" class="form-horizontal">
	  <input type="hidden" id="id" name="id" value="${templateSource.id}"/>
	  <fieldset>	
		<table class="formtable" align="center">
		  <tr>
			<td width="20%">资源<c:choose><c:when test="${templateSource.fileType=='DIRECTORY'}">目录</c:when><c:otherwise>文件</c:otherwise></c:choose>路径：</td>
			<td width="80%"><input type="text" id="path" name="path" readonly="readonly" size="60" value="${templateSource.path}"/></td>
		  </tr>						
		  <tr>
			<td>资源<c:choose><c:when test="${templateSource.fileType=='DIRECTORY'}">目录</c:when><c:otherwise>文件</c:otherwise></c:choose>名称：</td>
			<td><input type="text" id="name" name="name" readonly="readonly" size="60" value="${templateSource.name}"/></td>
		  </tr>
		  <c:if test="${templateSource.fileType!='DIRECTORY'}">
		  <tr>
			<td>导入资源文件：</td>
			<td><input type="file" id="templateSourceFile" name="templateSourceFile" size="60"/></td>
		  </tr>
		  </c:if>
		  <tr>
			<td>资源<c:choose><c:when test="${templateSource.fileType=='DIRECTORY'}">目录</c:when><c:otherwise>文件</c:otherwise></c:choose>说明：</td>
			<td><textarea rows="10" cols="56" id="describe" name="describe">${templateSource.describe}</textarea></td>
		  </tr>	
		  <tr>
			<td colspan="2" style="padding:0;">
			  <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
				<a class="easyui-linkbutton" icon="icon-save" href="javascript:document.forms[0].submit();">保存</a>
				<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:document.forms[0].reset();">重置</a>
			  </div>								
			</td>
		  </tr>																							
		</table>
	  </fieldset>
	</form:form>
  </body>
</html>