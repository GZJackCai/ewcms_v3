<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>问卷调查主题</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div id="subjectdiv" region="center" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
	</div>
	<div id="tb" style="padding:2px;height:auto;">
      <div class="toolbar" style="margin-bottom:2px">
        <a id="tb-add" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-add">新增</a>
		<a id="tb-edit" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-edit">修改</a>
		<a id="tb-remove" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-remove">删除</a>
		<a id="tb-up" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-up">上移</a>
		<a id="tb-down" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-down">下移</a>
      </div>
      <div>
        <form id="queryform" style="padding:0;margin: 0;">
          <table class="formtable" width="100%">
            <tr>
              <td width="6%">编号：</td>
              <td width="19%"><input type="text" name="EQ_id" style="width:120px;"/></td>
              <td width="6%">主题名称：</td>
              <td width="19%"><input type="text" name="LIKE_title" style="width:120px;"/></td>
              <td width="6%">选择方式：</td>
              <td width="19%"><input type="text" id="recordTimeStart" name="GTED_recordTime" class="easyui-datebox" style="width:100px"/>&nbsp;至&nbsp;<input type="text" id="recordTimeEnd" name="LTED_recordTime" class="easyui-datebox" style="width:100px"/></td>
              <td width="25%" colspan="2">
                  <a id="tb-query" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                  <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
              </td>
            </tr>
          </table>
        </form>
      </div>
    </div>
    <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="&nbsp;问卷调查主题" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/vote/subject/index.js"></script>
	<script type="text/javascript">
		var _subjectIndex = new SubjectIndex('#tt');
		$(function(){
			_subjectIndex.init({
				queryUrl : '${ctx}/content/vote/subject/query/${questionnaireId}',
				editUrl : '${ctx}/content/vote/subject/edit/${questionnaireId}',
				deleteUrl : '${ctx}/content/vote/subject/delete/${questionnaireId}',
				upUrl : '${ctx}/content/vote/subject/up/${questionnaireId}',
				downUrl : '${ctx}/content/vote/subject/down/${questionnaireId}',
				itemIndexUrl : '${ctx}/content/vote/subjectitem/index',
				itemEditUrl : '${ctx}/content/vote/subjectitem/editopt'
			});
		});
    </script>
  </body>
</html>