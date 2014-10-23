<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>修改用户信息</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
    <form:form id="inputForm" action="${ctx}/security/user/saveUserInfo" method="post" modelAttribute="user" class="form-horizontal">
      <input type="hidden" id="loginName" name="loginName" value="${user.loginName}"/>
      <fieldset>
      <table class="formtable">
        <tr>
          <td width="20%">真实姓名：</td>
          <td width="80%"><input id="realName" name="realName" value="${user.realName}"/></td>
        </tr>
        <tr>
          <td>证件编号：</td>
          <td><input id="identification" name="identification" value="${user.identification}"/></td>
        </tr>
        <tr>
          <td>生日：</td>
          <td><input id="birthday" name="birthday" value="${user.birthday}"/></td>
        </tr>
        <tr>
          <td>邮件地址：</td>
          <td><input id="email" name="email" value="${user.email}"/></td>
        </tr>
        <tr>
          <td>电话：</td>
          <td><input id="phone" name="phone" value="${user.phone}"/></td>
        </tr>
        <tr>
          <td>手机：</td>
          <td><input id="mphone" name="mphone" value="${user.mphone}"/></td>
        </tr>
      </table>
      </fieldset>
    </form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript">
        $(function(){
            parent.updateUsername('${user.realName}');
        });
    </script>
  </body>
</html>