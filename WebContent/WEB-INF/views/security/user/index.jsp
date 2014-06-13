<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://shiro.apache.org/tags" prefix="shiro"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>用户管理</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/security/user/index.js"></script>	
	<script type="text/javascript" src="${ctx}/static/easyui/ext/datagrid-detailview.js"></script>
	<script type="text/javascript">
		var _userIndex = new UserIndex();
		$(function(){
			_userIndex.init({
				queryUrl:'${ctx}/security/user/query',
				detailUrl:'${ctx}/security/userdetail/index'
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
			<a id="tb-add" href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-add" plain="true">新增</a>
			<a id="tb-edit" href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-edit" plain="true">修改</a>
		  </shiro:hasPermission>
		  <shiro:hasPermission name="user:delete">
			<a id="tb-remove" href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-remove" plain="true">删除</a>
		  </shiro:hasPermission>
		</div>
        <div style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                     登录名：<input type="text" name="LIKE_loginName" style="width:80px;"/>&nbsp;
                     用户名：<input type="text" name="LIKE_realName" style="width:80px"/>&nbsp;
                     邮箱：<input type="text" name="LIKE_email" style="width:120px"/>&nbsp;
                     注册日期：<input type="text" id="registerDate" name="GTED_registerDate" class="easyui-datebox" style="width:120px"/>至<input type="text" id="registerDate" name="LTED_registerDate" class="easyui-datebox" style="width:120px"/>&nbsp;
            <a href="#" id="tb-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
            <a href="#" id="tb-clear" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" onclick="javascript:$('#registerDate').datebox('setValue','');document.forms[0].reset();">清除</a>
          </form>
        </div>
      </div>	
	</div>
    <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
      </div>
    </div>	
  </body>
</html>