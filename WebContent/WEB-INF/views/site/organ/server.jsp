<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>站点发布设置</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/organ/server.js"></script>
	<script type="text/javascript"> 
	   	var _organServer = new OrganServer();
	   	$(function(){
	   		_organServer.typeChange();
	   		_organServer.init({
	   			serverTestUrl : '${ctx}/site/setup/siteServerTest'
	   		});
	   	});
	</script>
  </head>
  <body>
    <%@ include file="../../alertMessage.jsp" %>
    <div style="width:100%;height:100%;overflow:auto">
	<h1 class="title">站点发布设置</h1>
	<form:form action="${ctx}/site/setup/saveServer" id="serverForm" method="post" modelAttribute="site" class="form-horizontal">				
	  <fieldset>
		<table class="formtable" align="center">
		  <tr>
	        <td width="20%">站点名称：</td>
			<td width="80%">
			  <input type="text" id="siteName" name="siteName" readonly="readonly" size="40" value="${site.siteName}"/>
			  <span style="color:gray;">(内部编号：${site.id})</span>
			</td>
		  </tr>				
		  <tr>
			<td>发布类型：</td>
			<td>
			  <form:select id="outputType" path="siteServer.outputType" onchange="outputTypeChange(this.value);">
			    <form:option value="" label="------请选择------" />
			    <form:options itemLabel="description"/>
			  </form:select>
			</td>				
		  </tr>
		  <tr id="serverInfo1" style="display:none;">
			<td>服务器IP：</td>
			<td>
			  <input type="text" id="siteServer_hostName" name="siteServer.hostName" size="40" value="${site.siteServer.hostName}"/>
			  <span style="color:gray;">例如：http://www.bbb.cn</span>
			</td>
		  </tr>
		  <tr id="serverInfo2" style="display:none;">
			<td>端口：</td>
			<td><input type="text" id="siteServer_port" name="siteServer.port" value="${site.siteServer.port}"/></td>
		  </tr>	
		  <tr id="serverInfo3" style="display:none;">
			<td>用户名：</td>
			<td><input type="text" id="siteServer_userName" name="siteServer.userName" size="40" value="${site.siteServer.userName}"/></td>
		  </tr>
		  <tr id="serverInfo4" style="display:none;">
			<td>密码：</td>
			<td><input type="password" id="siteServer_password" name="siteServer.password" size="40" value="${site.siteServer.password}"/></td>				
		  </tr>
		  <tr id="serverInfo5">
			<td>发布路径：</td>
			<td>
			  <input type="text" id="siteServer_path" name="siteServer.path" size="40" value="${site.siteServer.path}"/>
			  <span style="color:gray;">比如：e:/resource</span>
			</td>
		  </tr>																												
		</table>
		<input type="hidden" id="siteServer_id" name="siteServer.id" value="${site.siteServer.id}"/>
		<input type="hidden" id="id" name="id" value="${site.id}"/>	
		</fieldset>
	  </form:form>	
	</div>						
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
      <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#serverForm').submit();">保存</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
      <a id="test-button" class="easyui-linkbutton" icon="icon-reload" href="javascript:void(0)" onclick="testServer();">连接测试</a>
    </div>  
  </body>
</html>