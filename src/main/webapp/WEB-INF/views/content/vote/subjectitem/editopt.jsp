<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>调查投票</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
	<%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/content/vote/subjectitem/saveopt" method="post" modelAttribute="subjectItem" class="form-horizontal">
	  <table class="formtable" >
		<tr>
		  <td width="17%" height="21px">选项方式：</td>
		  <td width="83%" height="21px"><form:radiobuttons path="status" items="${inputMap}"/></td>
		</tr>
		<tr>
		  <td >票数：</td>
		  <td><input type="text" id="voteNumber" name="voteNumber" maxlength="10" value="${subjectItem.voteNumber}"/></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${subjectItem.id}"/>
	  <input type="hidden" id="sort" name="sort" value="${subjectItem.sort}"/>
	  <input type="hidden" id="subjectId" name="subjectId" value="${subjectItem.subject.id}"/>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
  </body>
</html>