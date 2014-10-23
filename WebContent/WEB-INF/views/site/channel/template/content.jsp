<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>模板编辑</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/codemirror/lib/codemirror.css"/>
  </head>
  <body>
	<%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form action="${ctx}/site/channel/template/saveContent" modelAttribute="template" method="post" class="form-horizontal">
	  <input type="hidden" id="id" name="id" value="${template.id}"/>
		<table class="formtable" >
		  <tr>
			<td style="padding:0px;"><textarea id="templateContent" name="templateContent" style="width:99%;height:280px;padding-right: 10px;">${templateContent}</textarea></td>
		  </tr>	
		</table>
	</form:form>
	<div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
		<a class="easyui-linkbutton" icon="icon-save" href="javascript:document.forms[0].submit();">保存</a>
		<a class="easyui-linkbutton" icon="icon-undo" href="javascript:document.forms[0].reset();">重置</a>
		<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:parent.$('#open_window_top').window('close')">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/codemirror/lib/codemirror.js"></script>
    <script type="text/javascript" src="${ctx}/static/codemirror/mode/xml/xml.js"></script>
    <script type="text/javascript" src="${ctx}/static/codemirror/mode/javascript/javascript.js"></script>
    <script type="text/javascript" src="${ctx}/static/codemirror/mode/css/css.js"></script>
    <script type="text/javascript">
		var modeName = "xml";
		var editor = CodeMirror.fromTextArea(document.getElementById("templateContent"), {
 			mode: modeName,
  			lineNumbers: true,
  			lineWrapping: true,
            matchBrackets: true,
            indentUnit: 4
		});
		editor.focus();
   	</script>
  </body>
</html>