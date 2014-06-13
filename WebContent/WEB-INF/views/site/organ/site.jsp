<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>站点管理</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/organ/site.js"></script>
  </head>
  <c:choose>
  <c:when test="${not empty organ.parent and empty organ.parent.homeSiteId}">
  <body>
	上级机构还未创建主站，本机构目前还不能创建站点。
  </body>
  </c:when>
  <c:otherwise>
  <script type="text/javascript">
  		var _organSite = new OrganSite('#tt2');
		$(function(){
			  _organSite.init({
				 treeUrl : '${ctx}/site/setup/tree?siteId=${organ.parent.homeSiteId}&organId=${organ.id}',
				 addUrl : '${ctx}/site/setup/addSite?organId=${organ.id}',
				 setUrl : '${ctx}/site/setup/setSite?organId=${organ.id}',
				 renameUrl : '${ctx}/site/setup/renameSite',
				 delUrl : '${ctx}/site/setup/delSite',
				 moveUrl : '${ctx}/site/setup/moveSite',
				 configUrl : '${ctx}/site/setup/configSite/',
				 serverUrl : '${ctx}/site/setup/serverSite/'
			  });
		});
  </script>		
  <body class="easyui-layout">
	<div region="center"  style="overflow:hidden;">
	  <table class="formtable" align="center">
		<tr>
		  <td><u>${organ.name}</u>&nbsp;&nbsp;机构主站：<strong><label id="mainsitelabel">${site.siteName}</label></strong></td>
		</tr>
	    <tr>
	      <td><ul id="tt2"></ul></td>
	    </tr>
	  </table>		
	</div>
	<div id="treeopmenu" class="easyui-menu" style="width:100px; padding:4px 0 8px 0;">
	  <div id="add-button" data-options="iconCls:'icon-add'">创建站点</div>	
	  <div id="set-button" data-options="iconCls:'icon-redo'">设为主站</div>
	  <div id="config-button" data-options="iconCls:'icon-reload'">属性设置</div>
	  <div id="server-button" data-options="iconCls:'icon-reload'">发布设置</div>
	  <div id="rename-button" data-options="iconCls:'icon-undo'">重命名</div>	   	
	  <div id="del-button" data-options="iconCls:'icon-remove'">删除</div>
	  <div class="menu-sep"></div>								
	  <div id="cut-button" data-options="iconCls:'icon-cut'"><span id="cuttext">剪切</span></div>		
	  <div id="parse-button" data-options="iconCls:'icon-ok'" style="display:none;">粘贴</div>													   
	</div>
	<input type="hidden" id="homeSiteId" name="homeSiteId" value="${organ.homeSiteId}"/>	
    <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
      </div>
    </div>
  </body>
  </c:otherwise>
  </c:choose>
</html>