<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>站点切换</title>
		<%@ include file="taglibs.jsp" %>
        <script type="text/javascript" src="${ctx}/static/views/siteswitch.js"></script>
		<script type="text/javascript">
    		var _siteSwitch = new SiteSwitch({
    		    queryUrl:'${ctx}/siteSwitchQuery',
    		    switchUrl:'${ctx}/home'
    		});
    		
    		$(function(){
    		    _siteSwitch.init();
    		});
    		
    		function pageSubmit(){
    		    _siteSwitch.switchSite();
    		}
		</script>
	</head>
	<body>
        <table id="tt"></table>
        <input type="hidden" id="siteId" name="siteId" value="${siteId}"/>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
			<a class="easyui-linkbutton" icon="icon-save" href="javascript:pageSubmit();">切换</a>
			<a class="easyui-linkbutton" icon="icon-undo" href="javascript:document.forms[0].reset();">重置</a>
		</div>
	</body>
</html>