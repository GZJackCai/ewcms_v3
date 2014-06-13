<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>机构设置</title>
	<%@ include file="../../taglibs.jsp" %>
  </head>
  <body>
	<c:choose> 
	  <c:when test="${empty organ}">
		机构管理可对机构进行管理，可以创建机构、修改机构、删除机构、管理机构有关信息、管理机构站点等。
		<br/>
		鼠标双击左边机构，可对机构属性信息编辑。
	  </c:when>
	  <c:otherwise>
		<div class="easyui-tabs"  id="systemtab" border="false" fit="true">
          <div title="机构站点" style="padding: 2px;">
			<iframe src="${ctx}/site/organ/editSite?organId=${organ.id}" id="editsiteifr"  name="editsiteifr" class="editifr" scrolling="no"></iframe>
		  </div>					
		  <div title="机构信息" style="padding: 2px;">
			<iframe src="${ctx}/site/organ/editInfo?organId=${organ.id}" id="editinfoifr"  name="editinfoifr" class="editifr" scrolling="no"></iframe>
		  </div>
		</div>						
	  </c:otherwise>
	</c:choose>			
  </body>
</html>