<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>内容</title>
	<%@ include file="../../../taglibs.jsp"%>
	<script type="text/javascript" src="${ctx}/static/views/content/message/detail/index.js"></script>
	<script type="text/javascript">
		var _messageDetailIndex = new MessageDetailIndex();
		$(function(){
			_messageDetailIndex.init({
				subscribeUrl : '${ctx}/content/message/detail/index/<shiro:principal property="loginName"/>_${id}_${type}',
			})
		});
	</script>	
  </head>
  <body>
	<table class="formtable" width="100%">
	  <tr>
	    <td width="20%">标题：</td>
		<td width="80%">${title}<c:if test="${not empty results}">&nbsp;&nbsp;<a id="tb-subscribe" href="javascript:void(0);" onclick="subscribe();return false;" onfocus="this.blur();">订阅</a></c:if></td>
	  </tr>
	  <tr>
	    <td>内容：</td>
	    <td>
	      <c:choose>
		  <c:when test="${not empty results}">
		    <table class="formatable" width="100%">
		    <c:forEach items="${results}" var="result" varStatus="rowstatus">
		      <tr>
		        <c:choose>
		        <c:when test="${rowstatus.count%2==0}">
		          <td width="100%" style="background: #EEEEFF">${result}</td>
		        </c:when>
		        <c:otherwise>
		          <td width="100%">${result}</td>
		        </c:otherwise>
		        </c:choose>
		      </tr>
		    </c:forEach>
		    </table>
		    </c:when>
		    <c:otherwise>
		    	${detail}
		    </c:otherwise>
		    </c:choose>
		</td>
	  </tr>
	</table>
	<!-- 
	<input type="hidden" id="type" name="type" value="${type}"/>	
	<input type="hidden" id="id" name="id" value="${id}"/>
	 -->
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
  </body>
</html>