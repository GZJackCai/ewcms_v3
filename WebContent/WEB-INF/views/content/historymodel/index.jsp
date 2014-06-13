<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>历史记录</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/historymodel/index.js"></script>
	<script type="text/javascript">
		var _historyModelIndex = new HistoryModelIndex('#tt');
		$(function(){
			_historyModelIndex.init({
				queryUrl : '${ctx}/content/historymodel/query',
				deleteUrl : '${ctx}/content/historymodel/delete',
				downloadTemplateUrl : '${ctx}/content/historymodel/downloadTemplate'
			});
		});
	</script>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" fit="true" toolbar="#tb"></table>
	  <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
          <a id="tb-remove" href="#" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove',toggle:true">删除</a>
        </div>
        <div  style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                     保存时间：<input type="text" id="createDate" name="GTED_createDate" class="easyui-datebox" style="width:120px"/>至<input type="text" id="createDate"  name="LTED_createDate" class="easyui-datebox" style="width:120px"/>&nbsp;
            <a href="#" id="tb-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
            <a href="#" id="tb-clear" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" onclick="javascript:$('#createDate').datebox('setValue','');document.forms[0].reset();">清除</a>
          </form>
        </div>
      </div>
	</div>
  </body>
</html>