<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>机构管理</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/organ/index.js"></script>
	<script type="text/javascript">
		var _organIndex = new OrganIndex('#tt2');
		$(function(){
			_organIndex.init({
				treeUrl : '${ctx}/site/organ/query',
				editUrl : '${ctx}/site/organ/edit',
				addUrl : '${ctx}/site/organ/add',
				renameUrl : '${ctx}/site/organ/rename',
				delUrl : '${ctx}/site/organ/delete',
				moveUrl : '${ctx}/site/organ/move'
			});
		});
	</script>		
  </head>
  <body class="easyui-layout">
	<div region="west"  title='<label id="treeload-button" style="cursor:pointer;"><img src="${ctx}/static/image/refresh.png" style="vertical-align: middle;">机构库</label>' split="true" style="width:190px;">		
	  <ul id="tt2" fit="true"></ul>
	</div>
	<div region="center"  style="overflow:hidden;">
	  <iframe id="editifr"  name="editifr" class="editifr"></iframe>
	</div>
	<div id="treeopmenu" class="easyui-menu" style="width:100px; padding:4px 0 8px 0;">
	  <div id="add-button" data-options="iconCls:'icon-add'">添加</div>
	  <div id="rename-button" data-options="iconCls:'icon-undo'">重命名</div>	   	
	  <div id="del-button" data-options="iconCls:'icon-remove'">删除</div>
	  <div class="menu-sep"></div>
	  <div id="cut-button" data-options="iconCls:'icon-cut'"><span id="cuttext">剪切</span></div>	
	  <div id="parse-button" data-options="iconCls:'icon-ok'" style="display:none;">粘贴</div>	
	</div>	  
  </body>
</html>