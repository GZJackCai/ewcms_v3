<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>收件箱</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:2px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="menu-markread" href="javascript:void(0);" class="easyui-menubutton" plain="true" iconCls="icon-markread" menu="#menu-markreadsub">标记</a>
		  <a id="menu-remove" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		</div>
		<div id="menu-markreadsub" style="width:80px;">
            <div id="menu-mark-read" iconCls="">已读</div>
            <div id="menu-mark-unread" iconCls="">未读</div>
        </div>
        <div>
          <form id="queryform" style="padding:0;margin: 0;">
            <table class="formtable" width="100%">
              <tr>
                <td width="6%">编号：</td>
                <td width="19%"><input type="text" name="EQ_id" style="width:120px;"/></td>
                <td width="6%">名称：</td>
                <td width="19%"><input type="text" name="LIKE_name" style="width:120px;"/></td>
                <td width="6%">是否订阅：</td>
                <td width="19%">
                  <form:select id="subscription" name="BEQ_subscription" path="bolMap">
			        <form:option value="" label="------请选择------"/>
			        <form:options items="${bolMap}"/>
			      </form:select>
				</td>
				<td width="25%" colspan="2">
                  <a id="tb-query" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                  <a id="tb-clear" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
                  <a id="tb-more" href="javascript:void(0);" class="easyui-linkbutton"><span id="showHideLabel">更多...</span></a>
                </td>
              </tr>
              <tr>
              	<td>读取时间：</td>
              	<td><input type="text" id="readTimeStart" name="GTED_readTime" class="easyui-datebox" style="width:100px"/>&nbsp;至&nbsp;<input type="text" id="readTimeEnd" name="LTED_readTime" class="easyui-datebox" style="width:100px"/></td>
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
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/message/receive/index.js"></script>
	<script type="text/javascript">
		var _msgReceiveIndex = new MsgReceiveIndex('#tt');
		$(function(){
			_msgReceiveIndex.init({
				queryUrl : '${ctx}/content/message/receive/query',
				deleteUrl : '${ctx}/content/message/receive/delete/<shiro:principal property="loginName"/>',
				markReadUrl : '${ctx}/content/message/receive/markread/<shiro:principal property="loginName"/>',
				unReadUrl : '${ctx}/content/message/receive/unread/<shiro:principal property="loginName"/>',
				detailUrl : '${ctx}/content/message/detail/index'
			});
		});
	</script>	
  </body>
</html>