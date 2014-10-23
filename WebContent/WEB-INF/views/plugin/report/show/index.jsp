<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>文字报表</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
	<div style="padding: 2px;" border="false">
	  <fieldset>
		<table class="formtable" width="100%" height="100%">
		  <c:forEach items="${categoryReportList}" var="categoryReport">
		  <tr>
			<td colspan="4" bgcolor="#a9c9e2" height="20"><font color="#1E4176"><b>${categoryReport.name}-报表集</b></font></td>
		  </tr>
		  <c:choose>
		  <c:when test="${not empty categoryReport.texts}">
		  <tr>
			<td colspan="4"><b>文字报表：</b></td>
		  </tr>
		  <tr>
		    <c:forEach items="${categoryReport.texts}" var="text" varStatus="textStatus">
 			<td width="20%">
			  <a href="javascript:void(0);" onclick="_showIndex.setReportParameter('${ctx}/plugin/report/show/paraset/text_${text.id}');" style="text-decoration:none;" title="${text.remarks}"><span class="ellipsis">${text.name}</span></a>
			</td>
		  <c:choose>
		  <c:when test="${textStatus.index%3==0 && !textStatus.first && !textStatus.last}">
		  </tr>
		  <tr>
		  </c:when>
		  </c:choose>
		    </c:forEach>
		  </tr>
		  </c:when>
		  </c:choose>
		  <c:choose>
		  <c:when test="${not empty categoryReport.charts}">
		  <tr>
			<td colspan="4"><b>图型报表：</b></td>
		  </tr>
		  <tr>
		    <c:forEach items="${categoryReport.charts}" var="chart" varStatus="chartStatus">
 			<td width="20%">
			  <a href="javascript:void(0);" onclick="_showIndex.setReportParameter('${ctx}/plugin/report/show/paraset/chart_${chart.id}');" style="text-decoration:none;" title="${chart.remarks}"><span class="ellipsis">${chart.name}</span></a>
			</td>
		  <c:choose>
		  <c:when test="${chartStatus.index%3==0 && !chartStatus.first && !chartStatus.last}">
		  </tr>
		  <tr>
		  </c:when>
		  </c:choose>
		    </c:forEach>
		  </tr>
		  </c:when>
		  </c:choose>
		  </c:forEach>
		</table>
	  </fieldset>
	</div>
    <div id="edit-window" class="easyui-window" closed="true" title="&nbsp;参数选择">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" style="overflow-x:hidden;overflow-y:scroll"></iframe>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/report/show/index.js"></script>
	<script type="text/javascript">
		var _showIndex = new ShowIndex();
	</script>	
  </body>
</html>