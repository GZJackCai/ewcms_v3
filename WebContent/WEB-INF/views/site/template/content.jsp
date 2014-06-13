<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>模板编辑</title>	
	<%@ include file="../../taglibs.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/codemirror/lib/codemirror.css"/>
    <script type="text/javascript" src="${ctx}/static/codemirror/lib/codemirror.js"></script>
    <script type="text/javascript" src="${ctx}/static/codemirror/mode/xml/xml.js"></script>
    <script type="text/javascript" src="${ctx}/static/codemirror/mode/javascript/javascript.js"></script>
    <script type="text/javascript" src="${ctx}/static/codemirror/mode/css/css.js"></script>
  </head>
  <body>
	<script type="text/javascript">
		var currentNode = parent.parent.$('#tt2').tree('getSelected');
		if (currentNode){
			var position = "";
			var rootNode = parent.parent.$('#tt2').tree('getRoot');
			var text = [];
			if (rootNode){
				position += rootNode.text + " >> ";
				$.each(currentNode , function(){
					if (currentNode && currentNode.id != rootNode.id){
						text.push(currentNode.text);
						currentNode = parent.parent.$('#tt2').tree('getParent',currentNode.target);
					}
				});
			}
			for (var i = text.length - 1; i > 0; i--){
				position += text[i] + " >> ";
			}
			position += text[i];
			document.write("<span style='color:red;'>当前位置：" + position + "</span>");
		}
	</script>
	<%@ include file="../../alertMessage.jsp" %>
	<form:form action="${ctx}/site/template/saveContent" modelAttribute="template" method="post" class="form-horizontal">
	  <input type="hidden" id="id" name="id" value="${template.id}"/>
	  <fieldset>
		<table class="formtable" >
		  <tr>
			<td style="padding:0px;"><textarea id="templateContent" name="templateContent" style="width:99%;height:280px;padding-right: 10px;">${templateContent}</textarea></td>
		  </tr>	
		  <tr>
			<td style="padding:0;">
			  <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
				<a class="easyui-linkbutton" icon="icon-save" href="javascript:document.forms[0].submit();">保存</a>
				<a class="easyui-linkbutton" icon="icon-cancel" href="javascript:document.forms[0].reset();">重置</a>
			  </div>								
			</td>
		  </tr>																							
		</table>
	  </fieldset>
	</form:form>
	<script type="text/javascript">
		//var fileName = parent.parent.$('#tt2').tree('getSelected').text;
		var modeName = "xml";
	    //if(fileName.lastIndexOf(".") > 0){
		//    var modeName = fileName.substring(fileName.lastIndexOf(".")+1) ;
		//    if (modeName == "htm" || modeName == "html"){
		//    	modeName = "xml";
		//    }else if (modeName == "js"){
		//    	modeName = "javascript";
		//    }
	    //}
		var editor = CodeMirror.fromTextArea(document.getElementById("templateContent"), {
 			mode: modeName,
  			lineNumbers: true,
  			lineWrapping: true,
            matchBrackets: true,
            indentUnit: 4
		});
   	</script>
  </body>
</html>