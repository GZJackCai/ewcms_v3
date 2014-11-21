<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>相关文章</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="tb-add" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增引用</a>
		  <a id="tb-remove" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-remove">移除引用</a>
		</div>
        <div  style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
            <table width="100%">
              <tr>
                <td width="100%">
                                编号：<input type="text" name="EQ_id" style="width:120px;"/>&nbsp;
                                标题：<input type="text" name="LIKE_title" style="width:120px;"/>&nbsp;
                                状态：<input type="text" name="EQ_articleStatus" style="width:120px;"/>&nbsp;
                  <a href="javascript:void(0);" id="toolbar-arrows" style="text-decoration:none">更多...</a>
                  <a id="tb-query" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',toggle:true">查询</a>
                  <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
                </td>
              </tr>
              <tr>
              	<td>
              	   发布时间：<input type="text" id="publishedStart" name="GTED_published" class="easyui-datebox" style="width:120px"/>至<input type="text" id="publishedEnd" name="LTED_published" class="easyui-datebox" style="width:120px"/>&nbsp;
              	   修改时间：<input type="text" id="modifiedStart" name="GTED_modified" class="easyui-datebox" style="width:120px"/>至<input type="text" id="modifiedEnd" name="LTED_modified" class="easyui-datebox" style="width:120px"/>&nbsp;
              	   类型：<input type="text" name="EQ_articleType" style="width:120px;"/>
              	</td>
              </tr>
            </table>
          </form>
        </div>
      </div>		
    </div>      	
	<div id="pop-window" class="easyui-window" title="弹出窗口" icon="icon-save" closed="true" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
       	  <iframe id="editifr_pop"  name="editifr_pop" class="editifr" frameborder="0" width="100%" height="100%" scrolling="no"></iframe>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="tb-save" class="easyui-linkbutton" icon="icon-save" href="javascript:void(0)">确定</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="javascript:$('#pop-window').window('close');">取消</a>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/document/refer/index.js"></script>
	<script type="text/javascript" src='${ctx}/static/easyui/ext/datagrid-detailview.js"/>'></script>
	<script type="text/javascript">
		var _referIndex = new ReferIndex('#tt');
		$(function(){
			_referIndex.init({
				queryUrl : '${ctx}/content/document/article/query/${channelId}',
				deleteUrl : '${ctx}/content/document/refer/delete/${channelId}',
				referUrl : '${ctx}/content/document/refer/article',
				saveUrl : '${ctx}/content/document/refer/save'
			});
		});
	</script>
  </body>
</html>