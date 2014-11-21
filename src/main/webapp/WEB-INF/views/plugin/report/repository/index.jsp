<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>报表储存</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
		<div data-options="region:'center',split:true" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
 		  <a id="tb-remove" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove',toggle:true">删除</a>
 		  <a id="tb-publish" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-publish',toggle:true">发布</a>
		</div>
        <div  style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                     名称：<input type="text" name="LIKE_name" style="width:120px;"/>&nbsp;
            <a id="tb-query" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',toggle:true">查询</a>
            <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
          </form>
        </div>
      </div>		
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/plugin/report/repository/index.js"></script>
	<script type="text/javascript">
		var _repositoryIndex = new RepositoryIndex('#tt');
		$(function(){
			_repositoryIndex.init({
				queryUrl : '${ctx}/plugin/report/repository/query',
				deleteUrl : '${ctx}/plugin/report/repository/delete',
				downloadUrl : '${ctx}/plugin/report/repository/download',
				publishUrl : '${ctx}/plugin/report/repository/publish'
			});
		});
	</script>     	
  </body>
</html>