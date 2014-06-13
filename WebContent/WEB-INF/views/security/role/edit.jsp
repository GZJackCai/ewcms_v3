<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>权限组编辑</title>
    <%@ include file="../../taglibs.jsp"%>
	<script src="${ctx}/static/views/security/role/edit.js" type="text/javascript"></script>
	<script type="text/javascript">
		var _roleEdit = new RoleEdit();
		$(function(){
			_roleEdit.init({
				checkRoleNameUrl : '${ctx}/security/role/checkRoleName'
			});		
		});
	</script>
  </head>
  <body>
	<div style="width:100%;height:100%;overflow:auto">
	  <h1 class="title">权限组编辑</h1>
	  <form:form id="inputForm" action="${ctx}/security/role/save" method="post" modelAttribute="role" class="form-horizontal">
	    <input type="hidden" id="id" name="id" value="${role.id}"/>
		<fieldset>
		  <table class="formtable">
			<tr>
			  <td width="20%">名称：</td>
			  <td width="80%"><input type="text" id="roleName" name="roleName" size="30" value="${role.roleName}" class="required"/></td>
			</tr>
			<tr>
			  <td>说明：</td>
			  <td><input type="text" id="caption" name="caption" size="30" value="${role.caption}" class="required"/></td>
			</tr>
		  </table>
		  <c:forEach var="selection" items="${selections}">
			<input type="hidden" name="selections" value="${selection}" />
		  </c:forEach>
		</fieldset>
	  </form:form>
	</div>
    <div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
   	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
  </body>
</html>