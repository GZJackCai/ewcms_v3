<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>站点设置</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
    <div style="width:100%;height:100%;overflow:auto">
	<h1 class="title">站点设置</h1>
  	<form:form id="inputForm" action="${ctx}/site/setup/saveConfig" method="post" modelAttribute="site" class="form-horizontal">
	  <fieldset>  			
	    <table class="formtable" align="center">
		  <tr>
		  	<td width="20%">站点名称：</td>
			<td width="80%">
			  <input type="text" id="siteName" name="siteName" size="40" value="${site.siteName}" class="required"/>
			  <span style="color:gray;">(内部编号：${site.id})</span>
			</td>
		  </tr>				
		  <tr>
  			<td>站点目录：</td>
			<td>
			  <input type="text" id="siteRoot" name="siteRoot" value="${site.siteRoot}" class="required"/>
			  <span style="color:gray;">只能由数字、字符、下划线组成</span>
			</td>				
		  </tr>
		  <tr>
			<td>访问地址URL：</td>
			<td>
			  <input type="text" id="siteURL" name="siteURL" size="40" value="${site.siteURL}"/>
			  <span style="color:gray;">例如：http://www.bbb.cn</span>
			</td>
          </tr>
		  <tr>
			<td>描述：</td>
			<td><input type="text" id="describe" name="describe" value="${site.describe}"/></td>
          </tr>	
		  <tr>
			<td>meta关键字：</td>
			<td>
			  <input type="text" id="metaKey" name="metaKey" size="40" value="${site.metaKey}"/>
			  <span style="color:gray;">页面中使用关键字，便于SEO</span>
			</td>
		  </tr>
		  <tr>
			<td >meta说明：</td>
			<td><textarea rows="10" cols="30" id="metaDescripe" name="metaDescripe">${site.metaDescripe}</textarea></td>				
		  </tr>
		  <tr>
			<td>生成文件的扩展名：</td>
			<td>
			  <input type="text" id="extraFile" name="extraFile" value="${site.extraFile}"/>
			  <span style="color:gray;">允许htm,html,shtml,jsp，默认shtml</span>
			</td>				
		  </tr>
		  <tr>
			<td>资源发布目录：</td>
			<td>
			  <input type="text" id="resourceDir" name="resourceDir" size="40" value="${site.resourceDir}"/>
			  <span style="color:gray;">比如：e:/resource</span>
			</td>
		  </tr>	
		  <tr>
			<td>是否允许发布：</td>
			<td>
			  <form:checkbox path="publicenable" />
			</td>				
		  </tr>																												
		</table>	
		<input type="hidden" id="id" name="id" value="${site.id}"/>
	  </fieldset>
    </form:form>	
    </div>						
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
  </body>
</html>