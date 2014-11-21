<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>资源管理回收站</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <table id="tt" toolbar="#tb"></table>
    <div id="tb" style="padding:5px;height:auto;display:none;">
      <div style="margin-bottom:5px">
        <a href="javascript:void(0);" id="toolbar-revert" class="easyui-linkbutton" iconCls="icon-resume" plain="true">还原</a>
        <a href="javascript:void(0);" id="toolbar-remove" class="easyui-linkbutton" iconCls="icon-remove" plain="true">永久删除</a>
        <a href="javascript:void(0);" id="toolbar-clear" class="easyui-linkbutton" iconCls="icon-clear" plain="true">清空</a>
      </div>
      <div style="padding-left:5px;">
        <form id="queryform" style="padding: 0;margin: 0;">
          <table class="formtable" width="100%">
            <tr>
              <td width="6%">资源名称：</td>
              <td width="19%"><input type="text" name="LIKE_name" style="width:120px"/></td>
	          <td width="6%">描述：</td>
	          <td width="19%"><input type="text" name="LIKE_description" style="width:120px"/></td>
		      <td width="6%">资源类型：</td>
		      <td width="19%"><form:select id="type" name="EQ_type" path="typeMap" cssClass="easyui-combobox">
					            <form:option value="" label="------请选择------"/>
					            <form:options items="${typeMap}" itemLabel="description"/>
					          </form:select>
			   </td>
			   <td width="25%" colspan="2">
		          <a href="javascript:void(0);" id="toolbar-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		          <a href="javascript:void(0);" id="toolbar-clear" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" onclick="javascript:document.forms[0].reset();$('#createTime').datebox('setValue','');$('#type').combobox('setValue','');">清除</a>
		          <a id="tb-more" href="javascript:void(0);" class="easyui-linkbutton"><span id="showHideLabel">更多...</span></a>
		        </td>
             </tr>
             <tr>
               <td>创建日期：</td>
               <td><input type="text" id="createTime" name="GTED_createTime" class="easyui-datebox" style="width:100px"/>至<input type="text" id="createTime" name="LTED_createTime" class="easyui-datebox" style="width:100px"/></td>
            </td>
          </tr>
          </table> 
        </form>
       </div>
     </div>
     <div id="mm" class="easyui-menu" style="width:180px;display:none;">  
       <div id="menu-item-title"><b>操作</b></div>
       <div class="menu-sep"></div>   
       <div iconCls="icon-resume">还原</div>  
       <div iconCls="icon-remove">永久删除</div>  
       <div class="menu-sep"></div>   
       <div iconCls="icon-download">下载</div>   
     </div>
     <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/views/content/resource/recycle.js"></script>
    <script type="text/javascript">
	    var _r = new recycle();
        $(function(){
	        _r.init({
    	        queryUrl:'${ctx}/content/resource/recycle/query',
                deleteUrl:'${ctx}/content/resource/recycle/delete',
                clearUrl:'${ctx}/content/resource/recycle/clear',
                revertUrl:'${ctx}/content/resource/recycle/revert'
            });
        });
    </script>  
  </body>
</html>