<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>权限管理</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/security/role/detail.js"></script>
	<script type="text/javascript">
		var _roleDetail = new RoleDetail();
		$(function(){
			_roleDetail.init({
				queryUrl : '${ctx}/security/roledetail/query?id=${id}',
				editUrl : '${ctx}/security/roledetail/editPermission?id=${id}',
				permissionUrl : '${ctx}/security/permission/query'
			});
		});
	</script>	
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
          <shiro:hasPermission name="user:edit">
			<a id="tb-add" href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true">添加</a>
		  </shiro:hasPermission>
		  <shiro:hasPermission name="user:delete">
			<a id="tb-remove" href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true">移除</a>
		  </shiro:hasPermission>
		</div>
		<!-- 
        <div style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                      名称：<input type="text" name="LIKE_realName" style="width:80px"/>&nbsp;
                      邮箱：<input type="text" name="LIKE_email" style="width:120px"/>&nbsp;
            <a href="javascript:void(0);" id="tb-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
          </form>
        </div>
         -->
      </div>	
	</div>
    <div id="edit-window" icon="icon-winedit" closed="true" class="easyui-window" title="" style="display:none;">
      <table id="permission-tt" toolbar="#auth-tb"></table>
      <div id="auth-tb" style="padding: 5px; height: auto; display: none;">
        <div style="padding-left: 5px;">
          <form id="auth-queryform" style="padding: 0; margin: 0;">
			名称: <input type="text" name="LIKE_name" style="width: 100px" />&nbsp; 
			描述: <input type="text" name="LIKE_remark" style="width: 150px" />&nbsp;
            <a href="javascript:void(0);" id="auth-toolbar-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
          </form>
        </div>
      </div>
      <div style="width:511px;height:16px;line-height:28px;text-align:center;background-color:#f6f6f6;position:absolute;height:28px;line-height:28px;bottom:7px;">
      	<a id="save-button" class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);">提交</a>
       	<a id="cancel-button" class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);">关闭</a>
      </div>
    </div>
    <input type="hidden" id="id" name="id" value="${id}"/>
  </body>
</html>