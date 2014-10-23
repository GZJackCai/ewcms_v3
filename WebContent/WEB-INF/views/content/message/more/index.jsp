<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>公告/订阅</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
		<table id="tt" fit="true"></table>	
	</div>
    <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="&nbsp;消息" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" onload="iframeFitHeight(this);" scrolling="no"></iframe>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0)" onclick="saveOperator()">保存</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="javascript:$('#edit-window').window('close');">取消</a>
        </div>
      </div>
    </div>	
    <div id="query-window" class="easyui-window" closed="true" icon='icon-search' title="查询"  style="display:none;">
      <div class="easyui-layout" fit="true"  >
        <div region="center" border="false" >
          <form id="queryform">
            <table class="formtable">
              <tr>
                <td class="tdtitle">名称：</td>
                <td class="tdinput"><input type="text" id="title" name="title" class="inputtext"/></td>
              </tr>
            </table>
          </form>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)" onclick="querySearch('');initSubMenu();">查询</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="javascript:$('#query-window').window('close');">取消</a>
        </div>
      </div>
    </div>
    <input type="hidden" id="type" name="type"/>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/easyui/ext/datagrid-detailview.js"></script>
	<script type="text/javascript" src="${ctx}/static/views/content/message/more/index.js"></script>
	<script type="text/javascript">
		var _moreIndex = new MoreIndex('#tt');
		$(function(){
			_moreIndex.init({
				queryUrl : '${ctx}/content/message/more/query/${type}',
				indexUrl : '${ctx}/content/message/more/index'
			});
		});
	</script>
  </body>
</html>