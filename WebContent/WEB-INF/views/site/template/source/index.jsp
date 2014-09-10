<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>模板资源管理</title>	
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/template/source/index.js"></script>
	<script type="text/javascript">
		var _templateSourceIndex = new TemplateSourceIndex('#tt2');
		$(function(){
			_templateSourceIndex.init({
				treeUrl : '${ctx}/site/template/source/tree',
				addUrl : '${ctx}/site/template/source/add',
				addFolderUrl : '${ctx}/site/template/source/addFolder',
				editUrl : '${ctx}/site/template/source/edit',
				renameUrl : '${ctx}/site/template/source/rename',
				deleteUrl : '${ctx}/site/template/source/del',
				parseUrl : '${ctx}/site/template/source/moveto',
				importUrl : '${ctx}/site/template/source/import/',
				exportUrl : '${ctx}/site/template/source/exportzip',
				publishUrl : '${ctx}/site/template/source/pubsource'
			});
		});
	</script>		
  </head>
  <body class="easyui-layout">
	<div region="west"  title='<label id="treeload-button" style="vertical-align: middle;cursor:pointer;"><img src="${ctx}/static/image/refresh.png" style="vertical-align: middle;">模板资源库</label>' split="true" style="width:190px;">		
	  <ul id="tt2"></ul>
	</div>
	<div region="center"  style="overflow:auto;">
	  <iframe id="editifr"  name="editifr" class="editifr"></iframe>
	</div>
	<div id="treeopmenu" class="easyui-menu" style="width:100px; padding:4px 0 8px 0;">
	  <div id="add-button" data-option="iconCls:'icon-add'">新建资源</div>
	  <div id="add-folder-button" data-options="iconCls:'icon-folder'">新建文件夹</div>
	  <div id="rename-button" data-options="iconCls:'icon-undo'">重命名</div>
	  <div id="edit-button" data-options="iconCls:'icon-edit'">编辑</div>	
	  <div id="delete-button" data-options="iconCls:'icon-remove'">删除</div>
	  <div class="menu-sep"></div>
	  <div id="cut-button" data-options="iconCls:'icon-cut'"><span id="cuttext">剪切</span></div>									
	  <div id="parse-button" data-options="iconCls:'icon-paste'" style="display:none;">粘贴</div>	
	  <div class="menu-sep"></div>
	  <div id="import-button" data-options="iconCls:'icon-zip-import'">导入</div>	
	  <div id="export-button" data-options="iconCls:'icon-zip-export'">导出ZIP</div>
	  <div class="menu-sep"></div>
	  <div id="publish-button" data-options="iconCls:'icon-table-pub'">发布资源</div>
	</div>
  </body>
</html>