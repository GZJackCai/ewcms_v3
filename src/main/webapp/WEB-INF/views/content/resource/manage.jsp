<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>资源管理实现</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <table id="tt" toolbar="#tb"></table>
    <div id="tb" style="padding:5px;height:auto;display:none;">
      <div style="margin-bottom:5px">
        <a href="javascript:void(0);" id="toolbar-upload" class="easyui-linkbutton" iconCls="icon-upload" plain="true">上传</a>
        <a href="javascript:void(0);" id="toolbar-publish" class="easyui-linkbutton" iconCls="icon-publish" plain="true">发布</a>
        <a href="javascript:void(0);" id="toolbar-remove" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
      </div>
      <div style="padding-left:5px;">
        <form id="queryform" style="padding: 0;margin: 0;">
          <table class="formtable" width="100%">
            <tr>
              <td width="6%">资源名称： </td>
              <td width="19%"><input type="text" name="LIKE_name" style="width:120px"/></td>
              <td width="6%">描述： </td>
              <td width="19%"><input type="text" name="LIKE_description" style="width:120px"/></td>
              <td width="6%">创建日期：</td>
              <td width="19%"><input type="text" id="createTime" name="GTED_createTime" class="easyui-datebox" style="width:100px"/>至<input type="text" id="createTime" name="LTED_createTime" class="easyui-datebox" style="width:100px"/></td> 
              <td width="25%" colspan="2">
                <a href="javascript:void(0);" id="toolbar-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                <a href="javascript:void(0);" id="toolbar-clear" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" onclick="javascript:$('#createTime').datebox('setValue','');document.forms[0].reset();">清除</a>
              </td>
            </tr>
          </table>
        </form>
      </div>
    </div>
    <div id="mm" class="easyui-menu" style="width:180px;display:none;">  
      <div id="menu-item-title"><b>操作</b></div>
      <div class="menu-sep"></div>   
      <div iconCls="icon-save">更新资源</div>  
      <div iconCls="icon-image-add">更新引导图</div>  
      <div class="menu-sep"></div>
      <div iconCls="icon-publish">发布</div>
      <div iconCls="icon-remove">删除</div>  
      <div class="menu-sep"></div>   
      <div iconCls="icon-download">下载</div>   
    </div>  
    <div id="resource-upload-window" class="easyui-window" closed="true" icon='icon-upload'>
        <div class="easyui-layout" fit="true" >
          <div region="center" border="false" style="padding: 5px;">
            <iframe src="" name="uploadifr" class="editifr" scrolling="no"></iframe>
          </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="button-save" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#resource-upload-window').window('close');return false;">取消</a>
        </div>
      </div>
    </div>
    <div id="resource-update-window" class="easyui-window" closed="true" icon='icon-save'>
      <div class="easyui-layout" fit="true">
        <div region="center" border="false" style="padding: 5px;">
          <iframe src="" id="updateifr" name="updateifr" class="editifr" scrolling="no"></iframe>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#resource-update-window').window('close');return false;">关闭</a>
        </div>
      </div>
    </div>
    <div id="thumb-update-window" class="easyui-window" closed="true" icon='icon-save'>
      <div class="easyui-layout" fit="true" >
        <div region="center" border="false" style="padding: 5px;">
          <iframe src="" name="thumbifr" class="editifr" scrolling="no"></iframe>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#thumb-update-window').window('close');return false;">关闭</a>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/views/content/resource/manage.js"></script>
    <script type="text/javascript">
    	var _m = new manage('${type}');
        $(function(){
	        _m.init({
            	queryUrl:'${ctx}/content/resource/query',
                resourceUrl:'${ctx}/content/resource/resource',
                thumbUrl:'${ctx}/content/resource/thumb/index',
                publishUrl:'${ctx}/content/resource/manage/publish/${type}',
                removeUrl:'${ctx}/content/resource/manage/delete/${type}'
            });
        });
    </script>
  </body>
</html>