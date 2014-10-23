<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>备忘录</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
	<%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/content/notes/save" method="post" modelAttribute="memoranda" class="form-horizontal"> 
	  <table class="formtable">
		<tr>
		  <td width="60">标题：</td>
		  <td><input type="text" id="title" name="title" size="25" maxlength="25" value="${memoranda.title}"/></td>
		</tr>
		<tr>
		  <td>内容：</td>
		  <td><textarea cols="46" id="content" name="content">${memoranda.content}</textarea></td>
		</tr>
		<tr>
		  <td>提醒：</td>
		  <td><form:checkbox id="warn" path="warn"/></td>
		</tr>
		<tr id="tr_warn" >
		  <td>&nbsp;</td>
		  <td>
			<table class="formtable">
			  <tr>
			    <td>时间：</td>
			    <td><input type="text" id="warnTime" name="warnTime" class="easyui-timespinner" style="width:80px;" value="${memoranda.warnTime}"/></td>
              </tr>
              <tr>
                <td>重复频率：</td>
                <td><form:select id="frequency" path="frequency" items="${frequencyStatusMap}" itemLabel="description" cssClass="easyui-combobox"/></td>
              </tr>
              <tr>
                <td>提前：</td>
                <td><form:select id="before" path="before" items="${beforeStatusMap}" itemLabel="description" cssClass="easyui-combobox"/></td>
              </tr>
              <tr>
                <td>错过提醒：</td>
                <td><form:checkbox path="missRemind"/></td>
              </tr>
			</table>
		  </td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${memoranda.id}"/>
	  <input type="hidden" id="noteDate" name="noteDate" value="${memoranda.noteDate}"/>
	  <input type="hidden" id="userName" name="userName" value="${memoranda.userName}"/>
	  <input type="hidden" id="year" name="year" value="${year}"/>
	  <input type="hidden" id="month" name="month" value="${month}"/>
	  <input type="hidden" id="day" name="day" value="${day}"/>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/notes/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _notesEdit = new NotesEdit();
		$(function(){
			_notesEdit.init();
	  	});
	</script>
  </body>
</html>