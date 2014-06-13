<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>文档回收站</title>	
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/document/recyclebin/index.js"></script>
	<script type="text/javascript" src="${ctx}/static/easyui/ext/datagrid-detailview.js"></script>
	<script type="text/javascript">
		var _recyclebinIndex = new RecyclebinIndex('#tt');
		$(function(){
			_recyclebinIndex.init({
				queryUrl : '${ctx}/content/document/recyclebin/query/${channelId}',
				trackUrl : '${ctx}/content/document/track/index',
				deleteUrl : '${ctx}/content/document/recyclebin/delete/${channelId}',
				restoreUrl : '${ctx}/content/document/recyclebin/restore/${channelId}'
			});
		});
	</script>
  </head>
  <body class="easyui-layout">
    <div region="center" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:2px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="tb-restore" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-resume">恢复文章</a>
		  <a id="tb-remove" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove">彻底删除</a>
		</div>
        <div>
          <form id="queryform" style="padding:0;margin: 0;">
            <table class="formtable" width="100%">
              <tr>
                <td width="5%">编号：</td>
                <td width="20%"><input type="text" name="EQ_id" style="width:120px;"/></td>
                <td width="5%">标题：</td>
                <td width="20%"><input type="text" name="LIKE_title" style="width:120px;"/></td>
                <td width="5%">状态：</td>
                <td width="20%"><form:select id="status" name="EQ_status" path="statusMap">
					  <form:option value="" label="-----请选择-----"/>
					  <form:options items="${statusMap}" itemLabel="description"/>
					</form:select>
				</td>
				<td width="25%" colspan="2">
				  <a href="#" id="toolbar-arrows" style="text-decoration:none">更多...</a>
                  <a id="tb-query" href="#" class="easyui-linkbutton" >查询</a>
                  <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton">清除</a>
                </td>
              </tr>
              <tr>
              	<td>类型：</td>
              	<td><form:select id="genre" name="EQ_genre" path="genreMap">
					  <form:option value="" label="-----请选择-----"/>
					  <form:options items="${genreMap}" itemLabel="description"/>
					</form:select>
              	</td>
              	<td>创建者：</td>
              	<td><input type="text" name="LIKE_created" style="width:120px;"/></td>
              	<td>发布时间：</td>
              	<td><input type="text" id="publishedStart" name="GTED_published" class="easyui-datebox" style="width:120px"/>&nbsp;至&nbsp;<input type="text" id="publishedEnd" name="LTED_published" class="easyui-datebox" style="width:120px"/></td>
              	<td width="5%">修改时间：</td>
              	<td width="20%"><input type="text" id="modifiedStart" name="GTED_modified" class="easyui-datebox" style="width:120px"/>&nbsp;至&nbsp;<input type="text" id="modifiedEnd" name="LTED_modified" class="easyui-datebox" style="width:120px"/></td>
              </tr>
            </table>
          </form>
        </div>
      </div>		
    </div>      	
  </body>
</html>