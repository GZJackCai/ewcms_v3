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
	<form:form id="inputForm" action="${ctx}/content/vote/subject/save" method="post" modelAttribute="subject" class="form-horizontal">
	  <table class="formtable" >
		<tr>
		  <td width="17%" height="21px">主题名称：</td>
		  <td width="83%" height="21px"><input type="text" id="title" name="title" value="${subject.title}"/></td>
		</tr>
		<tr>
		  <td>查看方式：</td>
		  <td><form:radiobuttons path="status" items="${statusMap}" itemLabel="description"/></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${subject.id}"/>
	  <input type="hidden" id="sort" name="sort" value="${subject.sort}"/>
	  <input type="hidden" id="questionnaireId" name="questionnaireId" value="${subject.questionnaire.id}"/>
	  <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>	
    </form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/vote/subject/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _subjectEdit = new SubjectEdit();
		$(function(){
			_subjectEdit.init();
	  	});
	</script>
  </body>
</html>