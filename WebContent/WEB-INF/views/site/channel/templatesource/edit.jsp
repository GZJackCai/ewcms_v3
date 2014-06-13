<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>资源信息编辑</title>
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/channel/templatesource/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _templateSourceEdit = new TemplateSourceEdit();
		$(function(){
			_templateSourceEdit.init();
	  	});
    </script>
  </head>
  <body>
    <%@ include file="../../../alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/site/channel/templatesource/save" method="post" enctype="multipart/form-data" modelAttribute="templateSource" class="form-horizontal">
	  <table class="formtable" align="center">
		<tr>
		  <td width="20%">资源路径：</td>
		  <td width="80%"><input type="text" id="path" name="path" readonly="readonly" size="60" value="${templateSource.path}"/></td>
		</tr>				
		<tr>
		  <td>资源文件：</td>
		  <td><input type="file" id="templateSourceFile" name="templateSourceFile" size="60"/></td>
		</tr>									
		<tr>
		  <td>说明：</td>
		  <td><input type="text" id="describe" name="describe" value="${templateSource.describe}"/></td>
		</tr>
	  </table>
	  <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>
	  <input type="hidden" id="channelId" name="channelId" value="${templateSource.channelId}"/>
	  <input type="hidden" id="id" name="id" value="${templateSource.id}"/>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
  </body>
</html>