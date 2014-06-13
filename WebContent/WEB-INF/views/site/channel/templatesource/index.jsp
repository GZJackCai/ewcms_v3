<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>专栏资源管理</title>
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/channel/templatesource/index.js"></script>
	<script type="text/javascript" src="${ctx}/static/views/site/content.js"></script>
	<script>
		var _templateSourceIndex = new TemplateSourceIndex('#tt');
		$(function(){
			_templateSourceIndex.init({
				queryUrl : '${ctx}/site/channel/templatesource/query?channelId=${channelId}',
				editUrl : '${ctx}/site/channel/templatesource/edit?channelId=${channelId}',
				deleteUrl : '${ctx}/site/channel/templatesource/delete?channelId=${channelId}',
				editContentUrl : '${ctx}/site/channel/templatesource/editContent'
			});
		});
	</script>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="tb-add" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增</a>
		  <a id="tb-edit" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true">修改</a>
 		  <a id="tb-remove" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true">删除</a>
		</div>
        <div style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                      模板路径：<input type="text" name="LIKE_path" style="width:120px;"/>&nbsp;
                      说明：<input type="text" name="LIKE_describe" style="width:120px"/>&nbsp;
            <a id="tb-query" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
            <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
          </form>
        </div>
      </div>		
	</div>
    <div id="edit-window" class="easyui-window" closed="true" title="&nbsp;模板编辑">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
      </div>
    </div>	
  </body>
</html>