<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>调查投票</title>
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/vote/questionnaire/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _questionnaireEdit = new QuestionnaireEdit();
		$(function(){
			_questionnaireEdit.init();
			$('#inputForm').validate();
	  	});
	</script>
  </head>
  <body>
	<%@ include file="../../../alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/content/vote/questionnaire/save" method="post" modelAttribute="questionnaire" class="form-horizontal"> 
	  <table class="formtable" align="center">
		<tr>
		  <td width="17%">问卷名称：</td>
		  <td width="83%"><input type="text" id="title" name="title" value="${questionnaire.title}" class="required"/></td>
		</tr>
		<tr>
		  <td>查看方式：</td>
		  <td><form:radiobuttons path="status" items="${statusMap}" itemLabel="description" class="required"/></td>
		</tr>
		<tr>
		  <td>开始时间：</td>
		  <td><input type="text" id="startTime" name="startTime" class="easyui-datetimebox" value="${questionnaire.startTime}"/></td>
		</tr>
		<tr>
		  <td>结束时间：</td>
		  <td><input type="text" id="endTime" name="endTime" class="easyui-datetimebox" value="${questionnaire.endTime}"/></td>
		</tr>
		 
		<tr>
		  <td>验证码：</td>
		  <td><form:checkbox id="verifiCode" path="verifiCode"/></td>
		</tr>
		<tr>
		  <td>结束投票：</td>
		  <td><form:checkbox id="voteEnd" path="voteEnd"/></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${questionnaire.id}"/>
	  <input type="hidden" id="sort" name="sort" value="${questionnaire.sort}"/>
	  <input type="hidden" id="channelId" name="channelId" value="${questionnaire.channelId}"/>
	  <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>	
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" iconCls="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
  </body>
</html>