<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>模板管理</title>			
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/template/index.js"></script>
	<script type="text/javascript">
		var _templateIndex = new TemplateIndex('#tt2');
		$(function(){
			_templateIndex.init({
				treeUrl : '${ctx}/site/template/tree',
				editUrl : '${ctx}/site/template/edit',
				addTemplateUrl : '${ctx}/site/template/add',
				addFolderUrl : '${ctx}/site/template/addFolder',
				renameUrl : '${ctx}/site/template/rename',
				deleteUrl : '${ctx}/site/template/delete',
				moveUrl : '${ctx}/site/template/moveto',
				importUrl : '${ctx}/site/template/import/',
				exportUrl : '${ctx}/site/template/exportzip'
			});
		});
	</script>		
  </head>
  <body class="easyui-layout">
	<div region="west"  title='<label id="treeload-button" style="vertical-align: middle;cursor:pointer;"><img src="${ctx}/static/image/refresh.png" style="vertical-align: middle;">模板库</label>' split="true" style="width:190px;">		
	  <ul  id="tt2" fit="true"></ul>
	</div>
	<div region="center"  style="overflow:auto;">
	  <iframe id="editifr"  name="editifr" class="editifr"></iframe>
	</div>	
	<div id="treeopmenu" class="easyui-menu" style="width:100px; padding:4px 0 8px 0;">
	  <div id="addtemplate-button" data-options="iconCls:'icon-add'">新建模板</div>
	  <div id="addfolder-button" data-options="iconCls:'icon-folder'">新建文件夹</div>
	  <div id="rename-button" data-options="iconCls:'icon-undo'">重命名</div>	
	  <div id="delete-button" data-options="iconCls:'icon-remove'">删除</div>
	  <div class="menu-sep"></div>
	  <div id="cut-button" data-options="iconCls:'icon-cut'"><span id="cuttext">剪切</span></div>									
	  <div id="parse-button" data-options="iconCls:'icon-paste'" style="display:none;">粘贴</div>	
	  <div class="menu-sep"></div>
	  <div id="import-button" data-options="iconCls:'icon-zip-import'" onclick="importTemplate();">导入</div>	
	  <div id="export-button" data-options="iconCls:'icon-zip-export'" onclick="exportTemplate();">导出ZIP</div>
	</div>	  	
  </body>
</html>