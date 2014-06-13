<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>Bean数据源设置</title>
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/externalds/bean/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _beanDsEdit = new BeanDsEdit();
		$(function(){
			_beanDsEdit.init();
	  	});
    </script>
  </head>
  <body>
    <%@ include file="../../../alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/plugin/externalds/bean/save" method="post" modelAttribute="beanDs" class="form-horizontal">
	  <table class="formtable" align="center">
		<tr>
		  <td width="20%">名称：</td>
		  <td width="80%"><input type="text" id="name" name="name" value="${beanDs.name}"/></td>
		</tr>
		<tr>
		  <td>Bean名称：</td>
		  <td><input type="text" id="beanName" name="beanName" size="70" maxlength="127" value="${beanDs.beanname}"/></td>
		</tr>
		<tr>
		  <td>Bean名称：</td>
		  <td><input type="text" id="beanMethod" name="beanMethod" size="70" maxlength="127" value="${beanDs.beanMethod}"/></td>
		</tr>
		<tr>
		  <td>描述：</td>
		  <td><textarea cols="46" id="remarks" name="remarks">${beanDs.remarks}</textarea></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${beanDs.id}"/>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>			
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
  </body>
</html>