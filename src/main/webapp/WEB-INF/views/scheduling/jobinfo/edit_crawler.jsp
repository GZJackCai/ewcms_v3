<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>调度器任务</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/scheduling/jobcrawler/save" method="post" modelAttribute="pageDisplay" class="form-horizontal">
	  <table class="formtable" align="center">
		<tr>
		  <td colspan="4" align="left"><font color="#0066FF"><b>任务信息</b></font></td>
		</tr>
		<tr>
		  <td width="10%">采集器名称：</td>
		  <td width="90%"><input type="text" id="labe" name="label" maxlength="50" readonly="readonly" value="${pageDisplay.label}"/></td>
		</tr>
		<tr>
		  <td width="10%">说明：</td>
		  <td colspan="3"><textarea cols="46" id="description" name="description">${pageDisplay.description}</textarea></td>
		</tr>
		<tr>
		  <td colspan="4" align="left"><font color="#0066FF"><b>计划信息</b></font></td>
		</tr>
		<tr>
		  <td>开始时间：</td>
		  <td colspan="3"><input type="text" id="startDate" name="startDate" value="${pageDisplay.startDate}" class="easyui-datetimebox" data-options="showSeconds:false"/></td>
		</tr>
		<tr>
		  <td>调度方式：</td>
		  <td colspan="3">
		    <form:radiobutton path="mode" value="0" label="无" style="vertical-align: middle;"/>&nbsp;&nbsp;&nbsp;
		    <form:radiobutton path="mode" value="1" label="简单" style="vertical-align: middle;"/>&nbsp;&nbsp;&nbsp;
		    <form:radiobutton path="mode" value="2" label="复杂" style="vertical-align: middle;"/>
		  </td>
		</tr>
		<tr id="trSimplicity" style="display: none;">
		  <td>&nbsp;</td>
		  <td colspan="3" style="padding: 1px 1px;">
		    <table class="formtable" align="center">
			  <tr>
				<td>发生：</td>
				<td colspan="3">
				  <form:radiobutton path="occur" value="1" label="无限制" style="vertical-align: middle;"/>&nbsp;&nbsp;&nbsp;
				  <form:radiobutton path="occur" vlaue="2" label="结束时间" style="vertical-align: middle;"/>
				  <label for="occur2"><input type="text" id="endDateSimple" name="endDateSimple" value="${pageDisplay.endDateSimple}" class="easyui-datetimebox" data-options="showSeconds:false"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
				  <form:radiobutton path="occur" value="3" label=" " cssStyle="vertical-align: middle;"/>
				  <input type="text" id="occurrenceCount" name="occurrenceCount" size="4" onkeyup="this.value=this.value.replace(/\D/g,'');" value="${pageDisplay.occurrenceCount}"/>&nbsp;次数&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			  </tr>
			  <tr>
				<td>每隔：</td>
				<td colspan="3">
				  <input type="text" id="recurrenceInterval" name="recurrenceInterval" onkeyup="this.value=this.value.replace(/\D/g,'');" value="${pageDisplay.recurrenceInterval}"/>
				  <select name="recurrenceIntervalUnit">
				    <option value="1">分钟</option>
				    <option value="2">小时</option>
				    <option value="3">天</option>
				    <option value="4">星期</option>
				  </select>
				  执行一次&nbsp;&nbsp;<font color="red" style="vertical-align: middle;">*</font>
				</td>
			  </tr>
			</table>
		  </td>
		</tr>
		<tr id="trComplexity">
		  <td>&nbsp;</td>
		  <td colspan="3" style="padding: 1px 1px;">
			<table class="formtable" align="center">
			  <tr>
				<td>结束时间：</td>
				<td colspan="3"><input type="text" id="endDateCalendar" name="endDateCalendar" value="${pageDisplay.endDateCalendar}" class="easyui-datetimebox" data-options="showSeconds:false"/></td>
			  </tr>
			  <tr>
				<td width="10%">分钟：</td>
				<td width="40%"><input type="text" id="minutes" name="minutes" size="2" maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'');" value="${pageDisplay.minutes}"/>&nbsp;&nbsp;<font color="red" style="vertical-align: middle;">*</font>&nbsp;&nbsp;（在0-59之间）</td>
				<td width="10%">小时：</td>
				<td width="40%"><input type="text" id="hours" name="hours" size="2" maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'');" value="${pageDisplay.hours}"/>&nbsp;&nbsp;<font color="red" style="vertical-align: middle;">*</font>&nbsp;&nbsp;（在0-23之间）</td>
			  </tr>
			  <tr>
				<td>天数：</td>
				<td colspan="3"><form:radiobutton path="days" label="每一天" style="vertical-align: middle;"/></td>
			  </tr>
			  <tr>
				<td></td>
				<td colspan="3"><form:radiobutton path="days" label="一周内" style="vertical-align: middle;"/></td>
			  </tr>
			  <tr>
				<td></td>
				<td colspan="3">
				  <form:checkbox path="weekDays" value="1" label="星期一" style="vertical-align: middle;"/>
				  <form:checkbox path="weekDays" value="2" label="星期二" style="vertical-align: middle;"/>
				  <form:checkbox path="weekDays" value="3" label="星期三" style="vertical-align: middle;"/>
				  <form:checkbox path="weekDays" value="4" label="星期四" style="vertical-align: middle;"/>
				  <form:checkbox path="weekDays" value="5" label="星期五" style="vertical-align: middle;"/>
				  <form:checkbox path="weekDays" value="6" label="星期六" style="vertical-align: middle;"/>
				  <form:checkbox path="weekDays" value="7" label="星期天" style="vertical-align: middle;"/>
				  （<input type="checkbox" id="weekDaysAll" name="weekDaysAll" style="vertical-align: middle;"/><label for="monthAll" style="vertical-align: middle;">&nbsp;全选</label>）
			    </td>
			  </tr>
			  <tr>
				<td></td>
				<td colspan="3">
				  <form:radiobutton path="days" label="一个月内" class="vertical-align: middle;"/>
				  <input id="monthDays" name="monthDays" onkeyup="this.value=this.value.replace(/\D/g,'');" value="${pageDisplay.monthDays}" class="vertical-align: middle;"/>
				</td>
			  </tr>
			  <tr>
				<td>月份：</td>
				<td colspan="3">
				  <form:checkbox path="months" value="1" label="一月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="2" label="二月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="3" label="三月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="4" label="四月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="5" label="五月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="6" label="六月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="7" label="七月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="8" label="八月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="9" label="九月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="10" label="十月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="11" label="十一月" style="vertical-align: middle;"/>
				  <form:checkbox path="months" value="12" label="十二月" style="vertical-align: middle;"/>
				  （<input type="checkbox" name="monthsAll" id="monthsAll" style="vertical-align: middle;"/><label for="monthAll">&nbsp;全选）</label>&nbsp;&nbsp;<font color="red" style="vertical-align: middle;">*</font>
				</td>
			  </tr>
			</table>
		  </td>
		</tr>
	  </table>
      <c:forEach var="selection" items="${selections}">
	    <input type="hidden" name="selections" value="${selection}" />
	  </c:forEach>
	  <input type="hidden" id="jobId" name="jobId" value="${pageDisplay.jobId}"/>
	  <input type="hidden" id="jobVersion" name="jobVersion" value="${pageDisplay.jobVersion}"/>
	  <input type="hidden" id="triggerId" name="triggerId" value="${pageDisplay.triggerId}"/>
	  <input type="hidden" id="triggerVersion" name="triggerVersion" value="${pageDisplay.triggerVersion}"/>
	  <input type="hidden" id="start" name="start" value="2"/>
	  <input type="hidden" id="gatherId" name="gatherId" value="${pageDisplay.gatherId}"/>
	</form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/scheduling/jobinfo/edit.js"></script>
    <script type="text/javascript">
		if ('${close}' == 'true'){
	  		parent.$('#edit-window').window('close');
	  	}
		var _jobInfoEdit = new JobInfoEdit();
		$(function(){
			_jobInfoEdit.init();
		});
    </script>
  </body>
</html>