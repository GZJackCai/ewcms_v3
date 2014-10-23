<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>Jdbc数据源设置</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/plugin/externalds/jdbc/save" method="post" modelAttribute="jdbcDs" class="form-horizontal">
	  <table class="formtable" >
		<tr>
		  <td width="20%">名称：</td>
		  <td width="80%"><input type="text" id="name" name="name" maxlength="10" value="${jdbcDs.name}"/></td>
		</tr>
		<tr>
		  <td>驱动名：</td>
		  <td><input type="text" id="driver" name="driver" size="70" maxlength="127" value="${jdbcDs.driver}"/></td>
		</tr>
		<tr>
		  <td>数据库连接URL：</td>
		  <td><input type="text" id="connUrl" name="connUrl" size="70" maxlength="127" value="${jdbcDs.connUrl}"/></td>
		</tr>
		<tr>
		  <td>用户名：</td>
		  <td><input type="text" id="userName" name="userName" size="70" maxlength="127" value="${jdbcDs.userName}"/></td>
		</tr>
		<tr>
		  <td>密码：</td>
		  <td><input type="text" id="passWord" name="passWord" size="70" maxlength="127" value="${jdbcDs.passWord}"/></td>
        </tr>
		<tr>
		  <td>描述：</td>
		  <td><textarea cols="46" id="remarks" name="remarks">${jdbcDs.remarks}</textarea></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${jdbcDs.id}"/>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>			
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/externalds/jdbc/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _jdbcDsEdit = new JdbcDsEdit();
		$(function(){
			_jdbcDsEdit.init();
	  	});
    </script>
  </body>
</html>