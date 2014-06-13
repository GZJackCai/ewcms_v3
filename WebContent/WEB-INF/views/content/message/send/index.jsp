<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>发件箱</title>	
	<%@ include file="../../../taglibs.jsp"%>
	<script type="text/javascript" src="${ctx}/static/easyui/ext/datagrid-detailview.js"></script>
	<script type="text/javascript" src="${ctx}/static/views/content/message/send/index.js"></script>
	<script type="text/javascript">
		var _sendIndex = new SendIndex('#tt');
		$(function(){
			_sendIndex.init({
				queryUrl : '${ctx}/content/message/send/query',
				deleteUrl : '${ctx}/content/message/send/delete',
				contentUrl : '${ctx}/content/message/content/edit',
				contentDeleteUrl : '${ctx}/content/message/content/delete'
			});
		});
	</script>		
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
	  <div id="tb" style="padding:2px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="menu-add" href="#" class="easyui-menubutton" plain="true" iconCls="icon-add" menu="#menu-addsub">新增</a>
		  <a id="menu-remove" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		</div>
		<div id="menu-addsub" style="width:80px;">
            <div id="menu-add-message" iconCls="">信息</div>
            <div id="menu-add-subscription" iconCls="icon-subscription-add">订阅</div>
        </div>
        <div>
          <form id="queryform" style="padding:0;margin: 0;">
            <table class="formtable" width="100%">
              <tr>
                <td width="5%">编号：</td>
                <td width="20%"><input type="text" name="EQ_id" style="width:120px;"/></td>
                <td width="5%">名称：</td>
                <td width="20%"><input type="text" name="LIKE_name" style="width:120px;"/></td>
                <td width="5%">类型：</td>
                <td width="20%"><form:select id="type" name="EQ_type" path="sendTypeMap">
					  <form:option value="" label="-----请选择-----"/>
					  <form:options items="${sendTypeMap}" itemLabel="description"/>
					</form:select>
				</td>
				<td width="25%" colspan="2">
				  <a href="#" id="toolbar-arrows" style="text-decoration:none">更多...</a>
                  <a id="tb-query" href="#" class="easyui-linkbutton" >查询</a>
                  <a id="tb-clear" href="#" class="easyui-linkbutton">清除</a>
                </td>
              </tr>
              <tr>
              	<td>发送时间：</td>
              	<td><input type="text" id="sendTimeStart" name="GTED_sendTime" class="easyui-datebox" style="width:120px"/>&nbsp;至&nbsp;<input type="text" id="sendTimeEnd" name="LTED_sendTime" class="easyui-datebox" style="width:120px"/></td>
              </tr>
            </table>
          </form>
        </div>
      </div>
    </div>
    <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="&nbsp;消息" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
      </div>
    </div>	
  </body>
</html>