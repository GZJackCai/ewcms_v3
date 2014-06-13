<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>审批备案机关</title>
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/particular/zoningcode/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _zoningCodeEdit = new ZoningCodeEdit();
		$(function(){
			_zoningCodeEdit.init();
	  	});
    </script>
  </head>
  <body>
    <%@ include file="../../../alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/content/particular/zoningcode/save" method="post" modelAttribute="zoningCode" class="form-horizontal">
	  <table class="formtable" >
		<tr>
		  <td width="20%">组织机构代码：</td>
		  <td width="80%"><input type="text" id="code" name="code" maxlength="6" size="6" value="${zoningCode.code}"/></td>
		</tr>
		<tr>
		  <td>机关单位名称：</td>
		  <td><input type="text" id="name" name="name" maxlength="100" size="60" value="${zoningCode.name}"/></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${zoningCode.id}"/>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	</body>
</html>