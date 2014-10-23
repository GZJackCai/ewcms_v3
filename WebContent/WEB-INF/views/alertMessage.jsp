<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<c:if test="${not empty message}">
  <script type="text/javascript">$.messager.alert("提示","${message}");</script>
</c:if>