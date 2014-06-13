<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>文字报表设置</title>
	<%@ include file="../../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/report/category/detail/edit.js"></script>
	<script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
        var _categoryDetailEdit = new CategoryDetailEdit();
		$(function(){
			_categoryDetailEdit.init({
				textCategoryUrl : '${ctx}/plugin/report/categorydetail/text/${categoryId}',
				chartCategoryUrl : '${ctx}/plugin/report/categorydetail/chart/${categoryId}'
			});
	  	});
    </script>
  </head>
  <body>
    <%@ include file="../../../../alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/plugin/report/categorydetail/save" method="post" modelAttribute="categoryReport" class="form-horizontal">
	  <table class="formtable" >
		<tr>
		  <td width="10%">分类编号：</td>
		  <td width="40%"><input type="text" id="id" name="id" readonly="readonly" value="${categoryReport.id}"/></td>
		  <td width="10%">分类名称：</td>
		  <td width="40%"><input type="text" id="name" name="name" readonly="readonly" value="${categoryReport.name}"/></td>
		</tr>
		<tr>
		  <td>文字报表：</td>
		  <td colspan="3"><input id="text_categories" name="textReportIds" style="width:200px;"/></td>
	    </tr>
	    <tr>
		  <td>图型报表：</td>
		  <td colspan="4"><input id="chart_categories" name="chartReportIds" style="width:200px;"/></td>
		</tr>
	  </table>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
  </body>
</html>