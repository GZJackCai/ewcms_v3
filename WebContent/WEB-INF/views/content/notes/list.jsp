<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>备忘录列表</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;">  
	  <table id="tt" fit="true" toolbar="#tb"></table>
	  <div id="tb" style="padding:2px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="tb-edit" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-edit">修改</a>
		  <a id="tb-remove" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		  <a id="tb-close" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-cancel">关闭</a>
        </div>
        <div>
          <form id="queryform" style="padding:0;margin:0;">
            <table class="formtable" width="100%">
              <tr>
                <td width="6%">编号：</td>
                <td width="19%"><input type="text" name="EQ_id" style="width:120px;"/></td>
                <td width="6%">标题：</td>
                <td width="19%"><input type="text" name="LIKE_title" style="width:120px;"/></td>
                <td width="6%">日期：</td>
                <td width="19%"><input type="text" id="noteDate" name="GTED_noteDate" class="easyui-datebox" style="width:100px"/>&nbsp;至&nbsp;<input type="text" id="noteDate" name="LTED_noteDate" class="easyui-datebox" style="width:100px"/></td>
                <td width="25%" colspan="2">
                  <a id="tb-query" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                  <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
                  <a id="tb-more" href="javascript:void(0);" class="easyui-linkbutton"><span id="showHideLabel">更多...</span></a>
                </td>
              </tr>
              <tr>
                <td>提醒频率：</td>
                <td><form:select id="frequency" name="EQ_frequency" path="frequencyStatusMap">
			          <form:option value="" label="------请选择------"/>
			          <form:options items="${frequencyStatusMap}" itemLabel="description"/>
			        </form:select>
			    </td>
                <td>提醒频率：</td>
                <td><form:select id="before" name="EQ_before" path="beforeStatusMap">
			          <form:option value="" label="------请选择------"/>
			          <form:options items="${beforeStatusMap}" itemLabel="description"/>
			        </form:select>
			    </td>
                <td>是否提醒：</td>
                <td><form:select id="warn" name="BEQ_warn" path="bolMap">
			          <form:option value="" label="------请选择------"/>
			          <form:options items="${bolMap}"/>
			        </form:select>
                </td>
                <td width="6%">错过提醒：</td>
                <td width="19%">
                  <form:select id="missRemind" name="BEQ_missRemind" path="bolMap">
			        <form:option value="" label="------请选择------"/>
			        <form:options items="${bolMap}"/>
			      </form:select>
                </td>
              </tr>
            </table>
          </form>
        </div>
      </div>
	</div>
    <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="&nbsp;备忘录列表">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/notes/list.js"></script>
	<script type="text/javascript">
		var _notesList = new NotesList('#tt');
		$(function(){
			_notesList.init({
				queryUrl : '${ctx}/content/notes/query',
				editUrl : '${ctx}/content/notes/edit',
				deleteUrl : '${ctx}/content/notes/delete'
			});
		});
	</script>	
  </body>
</html>