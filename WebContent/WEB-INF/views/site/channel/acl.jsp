<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
   	<title>频道权限</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
   <div region="center" style="padding:2px;" border="false">
	 	<table id="tt" fit="true"></table>	
	</div>
    <div id="edit-window" class="easyui-window" closed="true" icon='icon-winedit'>
          <div class="easyui-layout" fit="true" >
                <div region="center" border="false" style="padding: 10px 5px;">
                <form id="permissionform">
                      <table class="formtable" >
                            <tr>
                                <td width="60px">名称：</td>
                                <td class="formFieldError" style="border: none;">
                                   <select id="cc" class="easyui-combobox" name="type" style="width:80px;" required="true">
                                        <option value="user">用户</option>
                                        <option value="role">角色</option>
                                    </select>
                                    <input type="text" id="input-name" name="name" style="width:150px;" style="margin-right: 5px;"/>
                                    <shiro:hasRole name="ROLE_ADMIN">
                                    <a id="button-query" class="easyui-linkbutton" icon="icon-levels" href="javascript:void(0)" plain="true"></a>
                                    </shiro:hasRole>
                                </td>
                           </tr>
                           <tr>
                               <td width="60px">权限：</td>
                               <td>
                                 <select id="cc_mask" class="easyui-combobox" name="mask" style="width:100px;" requried="true">
                                 	<option value="1">查看文章</option>
                                 	<option value="2">编辑文章</option>
                                 	<option value="3">删除文章</option>
                                 	<option value="4">审核文章</option>
                                 	<option value="5">发布文章</option>
                                 	<option value="6">查看栏目</option>
                                 	<option value="7">编辑栏目</option>
                                 	<option value="8">删除栏目</option>
                                 	<option value="9">管理员</option>
                                 </select>
                               </td>
                           </tr>
                        </table>
                        <input type="hidden" id="channelId" name="channelId" value="${channelId}"/>
                </form>
                </div>
                <div region="south" border="false" style="text-align:right;height:28px;line-height:28px;background-color:#f6f6f6">
                    <a id="button-save" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
                    <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#edit-window').window('close');return false;">取消</a>
                </div>
            </div>
      </div>
      <div id="query-window" icon="icon-winedit" closed="true">
        <div class="easyui-layout" fit="true" >
            <div region="center" border="false" style="padding: 10px 5px;">
                <table id="query-tt" toolbar="#query-tb"></table>
                <div id="query-tb" style="padding: 5px; height: auto; display: none;">
                   <div style="padding-left: 5px;">
                          <form id="auth-queryform" style="padding: 0; margin: 0;">
                                <span id="query-label-name">名称:</span><input id='query-input-name' type="text" name="EQ_name" style="width: 100px" />&nbsp; 
                                <span id="query-label-desc">描述:</span><input id='query-input-desc' type="text" name="EQ_remark" style="width: 150px" />&nbsp;
                              <a href="javascript:void(0);" id="auth-toolbar-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                          </form>
                   </div>
                </div>
            </div>
            <div region="south" border="false" style="text-align:right;height:28px;line-height:28px;background-color:#f6f6f6">
                <a id="button-selected" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
                <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#query-window').window('close');return false;">取消</a>
            </div>
         </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/views/site/channel/acl/index.js"></script>    
    <script type="text/javascript">
        var _acl = new ChannelAcl({
            queryUrl:'${ctx}/site/channel/acl/query/${channelId}',
            deleteUrl:'${ctx}/site/channel/deleteAcl/${channelId}',
            inheritUrl:'${ctx}/site/channel/inheritingAcl/${channelId}',
            saveUrl:'${ctx}/site/channel/saveAcl/${channelId}',
            userQueryUrl:'${ctx}/security/user/query',
            roleQueryUrl:'${ctx}/security/role/query',
        });
        
        $(function(){
            _acl.init({id:'${channelId}'});
        });
  	 </script>
  </body>
</html>