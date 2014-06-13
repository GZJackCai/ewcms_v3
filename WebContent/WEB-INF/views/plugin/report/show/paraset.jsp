<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>查询参数设置</title>
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/report/show/edit.js"></script>
	<script type="text/javascript">
		function checkBoxValue(name){
			var strValue = '';
			var list = document.getElementsByName(name);
			for (var i = 0; i < list.length; i++){
				if (list[i].type == 'checkbox'){
					if (list[i].checked == true) {
						listValue = list[i].value;
						if(strValue != '')strValue += ',';
						if (isNumber(listValue)){
							strValue += listValue;
						}
						else{
							strValue += "'" + listValue + "'";
						}
					}
				}
			}
			obName = "paramMap['" + name + "']";
			document.all[obName].value = strValue;
		}
			
		function isNumber(str){
		  var patrn=/^\d*$/;    
		  if(patrn.test(str))   {  
		  	return true;    
		  }else{  
		  	return false;  
		  }   
		}
	</script>
  </head>
  <body>
	<form:form id="inputForm" action="${ctx}/plugin/report/show/build" method="post" modelAttribute="parameterBuilder" class="form-horizontal" target="_blank">
	  <input type="hidden" id="reportType" name="reportType" value="${parameterBuilder.reportType}"/>
	  <input type="hidden" id="reportId" name="reportId" value="${parameterBuilder.reportId}"/>
	  <table class="basetable">
		<tr>
		  <td>
			<table width="100%">
			  <c:forEach items="${pageShowParams}" var="parameter">
			  <tr>
				<td class="texttd">
				<c:choose>
				<c:when test="${not empty parameter.cnName}">
				  ${parameter.cnName}
				</c:when>
				<c:otherwise>
				  ${parameter.enName}
				</c:otherwise>
				</c:choose>
				</td>
				<td class="inputtd">
				  <c:choose>
				  <c:when test="${parameter.type == 'TEXT'}">
				  <input type="text" name="paramMap['${parameter.enName}']" value="${parameter.defaultValue}"/>
				  </c:when>
				  </c:choose>
				  <c:choose>
				  <c:when test="${parameter.type == 'BOOLEAN'}">
				  <input type="checkbox" name="paramMap['${parameter.enName}']" value="${parameter.defaultValue}"/>
				  </c:when>
				  </c:choose>
				  <c:choose>
				  <c:when test="${parameter.type == 'LIST'}">
				  <form:select path="value" name="paramMap['${parameter.enName}']"></form:select>
				  </c:when>
				  </c:choose>
				  <c:choose>
				  <c:when test="${parameter.type == 'CHECK'}">
				  <form:checkboxes items="value" path="name" name="${parameter.enName}" onclick="checkBoxValue('${parameter.enName}')"/>
				  </c:when>
				  </c:choose>
				  <c:choose>
				  <c:when test="${parameter.type == 'DATE'}">
				  <input type="text" name="paramMap['${parameter.enName}']" class="easyui-datebox"/>
				  </c:when>
				  </c:choose>
				  <c:choose>
				  <c:when test="${parameter.type == 'SESSION'}">
				  <input type="text" name="paramMap['${parameter.enName}']" value="<shiro:principal property="loginName"/>" readonly="readonly"/>
				  </c:when>
				  </c:choose>
				  <c:choose>
				  <c:when test="${parameter.type == 'SQL'}">
				  <input type="text" name="paramMap['${parameter.enName}']" value="${parameter.defaultValue}"/>
				  </c:when>
				  </c:choose>
				</td>
			  </tr>
			  </c:forEach>
			  <c:if test="${parameterBuilder.reportType=='text'}">
			  <tr>
				<td class="texttd">报表文件类型：</td>
				<td class="inputtd">
				  <form:select path="textType" items="${textReportTypeMap}" itemLabel="description" cssClass="easyui-combobox"/>
				</td>
			  </tr>
			  </c:if>
			</table>
		  </td>
		</tr>
	  </table>
	</form:form>
    <div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
  </body>
</html>