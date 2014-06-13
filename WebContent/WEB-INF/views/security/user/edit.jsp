<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>人员编辑</title>
    <%@ include file="../../taglibs.jsp"%>
    <script type="text/javascript" src="${ctx}/static/views/security/user/edit.js"></script>
    <script type="text/javascript">
        var _userEdit = new UserEdit();
		$(function(){
			_userEdit.init({
	  			checkLoginNameUrl : '${ctx}/security/user/checkLoginName'
	  		});
	  	});
    </script>
  </head>
  <body>
    <%@ include file="../../alertMessage.jsp" %>
	<div style="width:100%;height:100%;overflow:auto">
	  <h1 class="title">用户编辑</h1>
	  <form:form id="inputForm" action="${ctx}/security/user/save" method="post" modelAttribute="user" class="form-horizontal">
	    <input type="hidden" id="id" name="id" value="${user.id}"/>
	    <c:forEach var="selection" items="${selections}">
  		<input type="hidden" name="selections" value="${selection}" />
		</c:forEach>
	    <fieldset>
		  <table class="formtable">
	        <tr>
			  <td width="20%">登录名：</td>
			  <td width="80%"><input type="text" id="loginName" name="loginName" size="20" value="${user.loginName}" class="required"/></td>
		    </tr> 	
		    <tr>
              <td>用户名：</td>
		  	<td ><input type="text" id="realName" name="realName" size="20" value="${user.realName}" class="required"/></td>
		    </tr>
  		    <tr>
			  <td>密码：</td>
			  <td><input type="password" id="plainPassword" name="plainPassword" size="20" class="required" minlength="3"/></td>
		    </tr>
		    <tr>
  			  <td>确认密码：</td>
			  <td><input type="password" id="passwordConfirm" name="passwordConfirm" size="20" equalTo="#plainPassword"/></td>
		    </tr>
		    <tr>
  			  <td>停用：</td>
			  <td><form:checkbox path="status"/></td>
		    </tr>
		    <tr>
  			  <td>邮箱：</td>
			  <td><input type="text" id="email" name="email" size="20" value="${user.email}" class="email"/></td>
		    </tr>
		  </table>
	    </fieldset>
	  </form:form>
    </div>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
  </body>
</html>