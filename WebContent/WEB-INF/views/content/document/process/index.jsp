<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>审批流程</title>	
	<%@ include file="../../../taglibs.jsp"%>
	<script type="text/javascript" src="${ctx}/static/views/content/document/process/index.js"></script>
	<script type="text/javascript">
		var _processIndex = new ProcessIndex('#tt');
		$(function(){
			_processIndex.init({
				queryUrl : '${ctx}/content/document/process/query/${channelId}',
				editUrl : '${ctx}/content/document/process/edit/${channelId}',
				deleteUrl : '${ctx}/content/document/process/delete/${channelId}',
				upUrl : '${ctx}/content/document/process/up/${channelId}',
				downUrl : '${ctx}/content/document/process/down/${channelId}'
			})
		});
	</script>		
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:2px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="tb-add" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增</a>
		  <a id="tb-edit" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-edit">修改</a>
		  <a id="tb-up" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-up">上移</a>
		  <a id="tb-down" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-down">下移</a>
		  <a id="tb-remove" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		</div>
        <div  style="padding-left:2px;">
        </div>
      </div>
    </div>
    <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="&nbsp;频道流程" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
      </div>
    </div>	
  </body>
</html>