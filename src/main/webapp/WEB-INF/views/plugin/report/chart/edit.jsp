<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>图形报表设置</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
	<style type="text/css">
	  .CodeMirror {border: 1px solid #eee;height: 105px; width: 400px;}
	  .CodeMirror-scroll {height: 105px;width: 400px;overflow-y: auto;overflow-x: auto;}
    </style>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/plugin/report/chart/save" method="post" modelAttribute="chartReport" class="form-horizontal">
 	  <table class="formtable" >
		<tr>
		  <td width="15%">名称：</td>
		  <td width="35%"><input type="text" id="name" name="name" maxlength="10" vlaue="${chartReport.name}"/></td> 
		  <td width="15%">数据源类型：</td>
		  <td width="35%">
			<form:select path="baseDs.id" items="${baseDsList}" itemLabel="name" itemValue="id" cssClass="easyui-combobox"/>
		  </td>
		</tr>
		<tr>
		  <td>SQL表达式：<a id="regexHelp" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-help" onclick="_chartReportEdit.sqlhelp('${ctx}/plugin/report/chart/sqlHelp')"></a></td>
		  <td colspan="3"><textarea cols="60" name="chartSql">${chartReport.chartSql}</textarea></td>
	    </tr>
		<tr>
		  <td>图表类型：</td>
		  <td>
			<form:select path="type" items="${typeMap}" itemLabel="description" cssClass="easyui-combobox"/>
		  </td>
		  <td>工具提示：</td>
		  <td><form:checkbox path="showTooltips"/></td>
		</tr>
		<tr>
		  <td>图表标题：</td>
		  <td><input type="text" id="chartTitle" name="chartTitle" value="${chartReport.chartTitle}"/></td>
		  <td>图表标题字体：</td>
		  <td>
			<form:select path="fontName" items="${fontNameMap}" cssClass="easyui-combobox"/>&nbsp;
			<form:select path="fontStyle" items="${fontStyleMap}" cssClass="easyui-combobox"/>&nbsp;
			<form:select path="fontSize" items="${fontSizeMap}" cssClass="easyui-combobox"/>
		  </td>
		</tr>
		<tr>
		  <td>横坐标轴标题：</td>
		  <td><input type="text" id="horizAxisLabel" name="horizAxisLabel" value="${chartReport.horizAxisLabel}"/></td>
		  <td>纵坐标轴标题：</td>
		  <td><input type="text" id="vertAxisLabel" name="vertAxisLabel" value="${chartReport.vertAxisLabel}"/></td>
		</tr>
		<tr>
		  <td>数据字体：</td>
		  <td>
		  	<form:select path="dataFontName" items="${fontNameMap}" cssClass="easyui-combobox"/>&nbsp;
		  	<form:select path="dataFontStyle" items="${fontStyleMap}" cssClass="easyui-combobox"/>&nbsp;
		  	<form:select path="dataFontSize" items="${fontSizeMap}" cssClass="easyui-combobox"/>
		  </td>
		  <td>坐标轴字体：</td>
		  <td>
		  	<form:select path="axisFontName" items="${fontNameMap}" cssClass="easyui-combobox"/>&nbsp;
		  	<form:select path="axisFontStyle" items="${fontStyleMap}" cssClass="easyui-combobox"/>&nbsp;
		  	<form:select path="axisFontSize" items="${fontSizeMap}" cssClass="easyui-combobox"/>
		  </td>
		</tr>
		<tr>
		  <td>坐标轴尺值字体：</td>
		  <td>
			<form:select path="axisTickFontName" items="${fontNameMap}" cssClass="easyui-combobox"/>&nbsp;
			<form:select path="axisTickFontStyle" items="${fontStyleMap}" cssClass="easyui-combobox"/>&nbsp;
			<form:select path="axisTickFontSize" items="${fontSizeMap}" cssClass="easyui-combobox"/>
		  </td>
		  <td>数据轴标签角度：</td>
		  <td>
		    <form:select path="tickLabelRotate" items="${rotateMap}" cssClass="easyui-combobox"/>
		  </td>
		</tr>
		<tr>
		  <td>图示说明：</td>
		  <td><form:checkbox path="showLegend" /></td>
		  <td>图示位置：</td>
		  <td>
		    <form:select path="legendPosition" items="${positionMap}" cssClass="easyui-combobox"/>
		  </td>
		</tr>
		<tr>
		  <td>图示字体：</td>
		  <td>
		    <form:select path="legendFontName" items="${fontNameMap}" cssClass="easyui-combobox"/>&nbsp;
		    <form:select path="legendFontStyle" items="${fontStyleMap}" cssClass="easyui-combobox"/>&nbsp;
		    <form:select path="legendFontSize" items="${fontSizeMap}" cssClass="easyui-combobox"/>
		  </td>
		  <td>图表高度：</td>
		  <td><input type="text" id="chartHeight" name="chartHeight" value="${chartReport.chartHeight}"/></td>
		</tr>
		<tr>
		  <td>图表宽度：</td>
		  <td><input type="text" id="chartWidth" name="chartWidth" value="${chartReport.chartWidth}"/></td>
		  <td>RGB背景色：</td>
		  <td>
			<input type="text" id="bgColorR" name="bgColorR" maxLength="3" size="3" value="${chartReport.bgColorR}"/>&nbsp;&nbsp;
			<input type="text" id="bgColorG" name="bgColorG" maxLength="3" size="3" value="${chartReport.bgColorG}"/>&nbsp;&nbsp;
			<input type="text" id="bgColorB" name="bgColorB" maxLength="3" size="3" value="${chartReport.bgColorB}"/>
		  </td>
		</tr>
		<tr>
		  <td>备注：</td>
		  <td colspan="3"><textarea cols="46" id="remarks" name="remarks">${chartReport.remarks}</textarea></td>
		</tr>
	  </table>
	  <input type="hidden" id="id" name="id" value="${chartReport.id}"/>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-save'" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/report/chart/edit.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/codemirror/lib/codemirror.css"/>
    <script type="text/javascript" src="${ctx}/static/codemirror/lib/codemirror.js"></script>
    <script type="text/javascript" src="${ctx}/static/codemirror/mode/plsql/plsql.js"></script>
    <script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
	    var _chartReportEdit = new ChartReportEdit();
		$(function(){
			_chartReportEdit.init();
	  	});
		var editor = CodeMirror.fromTextArea(document.getElementById("chartSql"), {
      		lineNumbers: true,
       		matchBrackets: true,
       		indentUnit: 4,
       		mode: "text/x-plsql"
    	});
    </script>
  </body>
</html>