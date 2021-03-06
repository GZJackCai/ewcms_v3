<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>用户填写问卷调查结果</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
	<table class="formtable" width="100%">
	  <c:forEach var="html" items="${results}" varStatus="rowstatus">
	  <tr>
	  <c:choose>
	  	<c:when test="${rowstatus.count%2==0}">
	  	  <td width="100%" style="background: #EEEEFF">${html}</td>
	  	</c:when>
	  	<c:otherwise>
	  	  <td width="100%">${html}</td>
	  	</c:otherwise>
	  </c:choose>
	  </tr>
	  </c:forEach>
	</table>
	<input type="hidden" id="personId" name="personId" value="${personId}"/>
	<input type="hidden" id="questionnaireId" name="questionnaireId" value="${questionnaireId}"/>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
  </body>
</html>