<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>资源管理回收站</title>
	<%@ include file="../../taglibs.jsp" %>
    <script type="text/javascript" src="${ctx}/static/views/content/resource/recycle.js"></script>
    <script type="text/javascript">
	    var _r = new recycle('${ctx}');
        $(function(){
	        _r.init({
    	        queryUrl:'${ctx}/content/resource/recycle/query',
                deleteUrl:'${ctx}/content/resource/recycle/delete',
                clearUrl:'${ctx}/content/resource/recycle/clear',
                revertUrl:'${ctx}/content/resource/recycle/revert'
            });
        });
    </script>
  </head>
  <body>
    <table id="tt" toolbar="#tb"></table>
    <div id="tb" style="padding:5px;height:auto;display:none;">
      <div style="margin-bottom:5px">
        <a href="#" id="toolbar-revert" class="easyui-linkbutton" iconCls="icon-resume" plain="true">还原</a>
        <a href="#" id="toolbar-remove" class="easyui-linkbutton" iconCls="icon-remove" plain="true">永久删除</a>
        <a href="#" id="toolbar-clear" class="easyui-linkbutton" iconCls="icon-clear" plain="true">清空</a>
      </div>
      <div style="padding-left:5px;">
        <form id="queryform" style="padding: 0;margin: 0;">
          <table width="100%">
            <tr>
              <td width="100%">
		                  资源名称: <input type="text" name="LIKE_name" style="width:80px"/>&nbsp;
		                  描述: <input type="text" name="LIKE_description" style="width:120px"/>&nbsp;
		                  资源类型：<form:select id="type" name="EQ_type" path="typeMap" cssClass="easyui-combobox">
					            <form:option value="" label="------请选择------"/>
					            <form:options items="${typeMap}" itemLabel="description"/>
					        </form:select>&nbsp;
				  <a href="#" id="toolbar-arrows" style="text-decoration:none">更多...</a>
		          <a href="#" id="toolbar-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
		          <a href="#" id="toolbar-clear" class="easyui-linkbutton" data-options="iconCls:'icon-clear'" onclick="javascript:document.forms[0].reset();$('#createTime').datebox('setValue','');$('#type').combobox('setValue','');">清除</a>
		    </td>
          </tr>
          <tr>
            <td>
                          创建日期: <input type="text" id="createTime" name="GTED_createTime" class="easyui-datebox" style="width:120px"/>至<input type="text" id="createTime" name="LTED_createTime" class="easyui-datebox" style="width:120px"/>&nbsp;
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
  </body>
</html>