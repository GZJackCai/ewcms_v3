<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>任务设置</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div data-options="region:'center',split:true" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="tb-add" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add',toggle:true">新增</a>
		  <a id="tb-edit" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit',toggle:true">修改</a>
 		  <a id="tb-remove" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove',toggle:true">删除</a>
		</div>
        <div  style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                     名称：<input type="text" name="LIKE_label" style="width:120px;"/>&nbsp;
            <a id="tb-query" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',toggle:true">查询</a>
            <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
          </form>
        </div>
      </div>		
	</div>
    <div id="edit-window" class="easyui-window" closed="true" title="编辑">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" width="100%" height="100%" style="overflow-x:hidden;overflow-y:scroll"></iframe>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/scheduling/jobinfo/index.js"></script>
	<script type="text/javascript">
		var _jobInfoIndex = new JobInfoIndex('#tt');
		$(function(){
			_jobInfoIndex.init({
				queryUrl : '${ctx}/scheduling/jobinfo/query',
				editUrl : '${ctx}/scheduling/jobinfo/edit',
				deleteUrl : '${ctx}/scheduling/jobinfo/delete',
				pauseUrl : '${ctx}/scheduling/jobinfo/pause',
				resumedUrl : '${ctx}/scheduling/jobinfo/resumed'
			});
		});
	</script>     	
  </body>
</html>