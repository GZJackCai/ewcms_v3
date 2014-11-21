<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>站点专栏</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="west" title='<label id="treeload-button" style="cursor:pointer;"><img src="${ctx}/static/image/refresh.png" style="vertical-align: middle;">专栏库</label>' split="true" style="width:180px;">		
	  <ul id="tt2"></ul>
	</div>
	<div region="center"  style="overflow:auto;">
	  <iframe id="editifr"  name="editifr" class="editifr"></iframe>
	</div>
	<div id="treeopmenu" class="easyui-menu" style="width:110px; padding:4px 0 8px 0;">
	  <div id="add-button" data-options="iconCls:'icon-add'">创建</div>
	  <div id="rename-button" data-options="iconCls:'icon-undo'">重命名</div>
	  <div id="edit-button" data-options="iconCls:'icon-edit'">编辑</div>	   
	  <div id="delete-button" data-options="iconCls:'icon-remove'">删除</div>   
	  <div id="cut-button" data-options="iconCls:'icon-cut'"><span id="cuttext">剪切</span></div>
	  <div id="parse-button" data-options="iconCls:'icon-paste'" style="display:none;">粘贴</div>	
	  <div class="menu-sep"></div>
	  <div id="up-button" data-options="iconCls:'icon-up'" onclick="upChannel();">上移</div>
	  <div id="down-button" data-options="iconCls:'icon-down'" onclick="downChannel();">下移</div>
	  <div id="move-button" data-options="iconCls:'icon-up-down'" onclick="moveChannel();">移动</div>
	  <div class="menu-sep"></div>
	  <div id="export-button" data-options="iconCls:'icon-zip-export'" onclick="exportZip();">导出ZIP</div>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/site/channel/index.js"></script>
	<script type="text/javascript">
		var _channelIndex = new ChannelIndex("#tt2");
		$(function(){
			_channelIndex.init({
				treeUrl : '${ctx}/site/channel/treePub',
				addUrl : '${ctx}/site/channel/add',
				editUrl : '${ctx}/site/channel/edit',
				renameUrl : '${ctx}/site/channel/rename',
				deleteUrl : '${ctx}/site/channel/delete',
				parseUrl : '${ctx}/site/channel/moveto',
				upUrl : '${ctx}/site/channel/up',
				downUrl : '${ctx}/site/channel/down',
				moveUrl : '${ctx}/site/channel/moveSort',
				exportUrl : '${ctx}/site/channel/exportzip'
			});
		});
	</script>	    	
  </body>
</html>