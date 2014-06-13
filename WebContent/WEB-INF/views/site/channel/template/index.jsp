<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>专栏模板管理</title>
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/channel/template/index.js"></script>
	<script type="text/javascript" src="${ctx}/static/views/site/content.js"></script>
	<script type="text/javascript"> 
		var _templateIndex = new TemplateIndex('#tt','#tt2');
		$(function(){
			_templateIndex.init({
				tplTreeUrl : '${ctx}/site/template/tree',
				queryUrl : '${ctx}/site/channel/template/query?channelId=${channelId}',
				editUrl : '${ctx}/site/channel/template/edit?channelId=${channelId}',
				deleteUrl : '${ctx}/site/channel/template/delete?channelId=${channelId}',
				importTplUrl : '${ctx}/site/channel/template/import?channelId=${channelId}',
				appChildUrl : '${ctx}/site/channel/appChild?channelId=${channelId}',
				forceUrl : '${ctx}/site/channel/forceRelease?channelId=${channelId}',
				previewUrl : '${ctx}/channel/template/preview',
				editContentUrl : '${ctx}/site/channel/template/editContent',
				historyUrl : '${ctx}/site/channel/template/history/index',
				verifyUrl : '${ctx}/site/channel/template/verify'
			});
		});
	</script>		
  </head>
  <body class="easyui-layout">
	<div data-options="region:'center',split:true" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="tb-add" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add',toggle:true">新增</a>
		  <a id="tb-edit" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit',toggle:true">修改</a>
 		  <a id="tb-remove" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove',toggle:true">删除</a>
 		  <a id="tb-import" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-zip-import',toggle:true">导入</a>
 		  <a id="tb-appchild" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-applied-child',toggle:true">应用栏目</a>
 		  <a id="tb-force" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-force-operate',toggle:true">强制发布</a>
		</div>
        <div  style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                      模板路径：<input type="text" name="LIKE_path" style="width:120px;"/>&nbsp;
                      模板类型：<form:select id="type" name="EQ_type" path="typeMap" cssClass="easyui-combobox">
			            <form:option value="" label="------请选择------"/>
			            <form:options items="${typeMap}" itemLabel="description"/>
			          </form:select>&nbsp;
                      说明：<input type="text" name="LIKE_describe" style="width:120px"/>&nbsp;
            <a id="tb-query" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',toggle:true">查询</a>
            <a id="tb-clear" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" onclick="javascript:document.forms[0].reset();$('#type').combobox('setValue','');">清除</a>
          </form>
        </div>
      </div>		
	</div>
    <div id="edit-window" class="easyui-window" closed="true" title="模板编辑">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
       </div>
     </div>	
     <div id="template-window" class="easyui-window" closed="true" style="overflow:auto;">
       <div class="easyui-layout" fit="true" >
         <div region="center" border="false">
           <ul id="tt2"></ul>
         </div>
         <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
           <a id="select-button" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
           <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#template-window').window('close');">关闭</a>
         </div>
       </div>
     </div>               
	 <div id="pop-window" class="easyui-window" title="弹出窗口" closed="true">
       <div class="easyui-layout" fit="true">
         <div region="center" border="false">
           <iframe id="editifr_pop"  name="editifr_pop" class="editifr" frameborder="0" width="100%" height="100%" scrolling="no"></iframe>
         </div>
       </div>
     </div>
     <div id="appchildren-window" class="easyui-window" closed="true" style="overflow:hidden;">
       <div class="easyui-layout" fit="true" >
         <div region="center" border="false" style="padding: 5px;">
           <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#99BBE8" style="border: #99BBE8 1px solid;">
             <tr align="center">
               <td height="30" width="30%">操作</td>
               <td height="30" width="70%">说明</td>
             </tr>
             <tr>
               <td height="40">&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="appchildrenRadio" name="appchildrenRadio" value="0" checked="checked"/>只新增不更新</td>
               <td height="40">&nbsp;只新增不存在的模板，并不更新存在的模板</td>
             </tr>
             <tr>
               <td height="40">&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="appchildrenRadio" name="appchildrenRadio" value="1"/>新增并覆盖</td>
               <td height="40">&nbsp;新增不存在的模板，并更新已存在的模板</td>
             </tr>
           </table>
         </div>
         <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
           <a id="appchild-button" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
           <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#appchildren-window').window('close');return false;">取消</a>
        </div>
      </div>
    </div>
    <div id="force-window" class="easyui-window" closed="true" style="overflow:hidden;">
      <div class="easyui-layout" fit="true" >
        <div region="center" border="false" style="padding: 5px;">
          <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#99BBE8" style="border: #99BBE8 1px solid;">
            <tr align="center">
              <td height="30" width="30%">操作</td>
              <td height="30" width="70%">说明</td>
            </tr>
            <tr>
              <td height="40">&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="channelRadio" name="channelRadio" value="0" checked="checked"/>本栏目</td>
              <td height="40">&nbsp;只对本栏目进行强制发布</td>
            </tr>
            <tr>
              <td height="40">&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="channelRadio" name="channelRadio" value="1"/>本栏目及子栏目</td>
              <td height="40">&nbsp;对本栏目及本栏目所属子栏目进行强制发布</td>
            </tr>
          </table>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="force-button" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#force-window').window('close');return false;">取消</a>
        </div>
      </div>
    </div>
  </body>
</html>