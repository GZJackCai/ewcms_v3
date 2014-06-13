<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>调查投票</title>
    <%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/vote/subjectitem/edit.js"></script>
	<script type="text/javascript">
        var _subjectItemEdit = new SubjectItemEdit();
		$(function(){
			_subjectItemEdit.init();
	  	});
	</script>
  </head>
  <body>
	<%@ include file="../../../alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/content/vote/subjectitem/save" method="post" modelAttribute="subjectItem" class="form-horizontal">
      <table class="formtable" >
		<tr>
		  <td width="17%" height="21px;">选项名称：</td>
		  <td width="83%" height="21px"><input type="text" id="title" name="title" value="${subjectItem.title}"/></td>
		</tr>
		<tr>
		  <td>选项方式：</td>
		  <td><form:radiobuttons path="status" items="${statusMap}" itemLabel="description"/></td>
		</tr>
		<tr>
		  <td>票数：</td>
		  <td><input type="text" id="voteNumber" name="voteNumber" maxlength="10" value="${subjectItem.voteNumber}"/></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${subjectItem.id}"/>
	  <input type="hidden" id="sort" name="sort" value="${subjectItem.sort}"/>
	  <input type="hidden" id="subjectId" name="subjectId" value="${subjectItem.subject.id}"/>
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