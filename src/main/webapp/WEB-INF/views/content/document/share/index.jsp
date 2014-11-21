<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>文章共享库</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:2px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="tb-refence" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-refence">引用</a>
		  <a id="tb-copy" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-copy">复制</a>
 		  <a id="tb-preview" href="javascript:void(0);" class="easyui-linkbutton" plain="true" iconCls="icon-article-preview">预览</a>
		</div>
        <div>
          <form id="queryform" style="padding:0;margin:0;">
            <table class="formtable" width="100%">
              <tr>
                <td width="6%">编号：</td>
                <td width="19%"><input type="text" name="EQ_id" style="width:120px;"/></td>
                <td width="6%">标题：</td>
                <td width="19%"><input type="text" name="LIKE_title" style="width:120px;"/></td>
                <td width="6%">状态：</td>
                <td width="19%"><form:select id="status" name="EQ_status" path="statusMap">
					  <form:option value="" label="-----请选择-----"/>
					  <form:options items="${statusMap}" itemLabel="description"/>
					</form:select>
				</td>
				<td width="25%" colspan="2">
                  <a id="tb-query" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search'">查询</a>
                  <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
                  <a id="tb-more" href="javascript:void(0);" class="easyui-linkbutton"><span id="showHideLabel">更多...</span></a>
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
              	<td><input type="text" id="publishedStart" name="GTED_published" class="easyui-datebox" style="width:100px"/>&nbsp;至&nbsp;<input type="text" id="publishedEnd" name="LTED_published" class="easyui-datebox" style="width:100px"/></td>
              	<td width="6%">修改时间：</td>
              	<td width="19%"><input type="text" id="modifiedStart" name="GTED_modified" class="easyui-datebox" style="width:100px"/>&nbsp;至&nbsp;<input type="text" id="modifiedEnd" name="LTED_modified" class="easyui-datebox" style="width:100px"/></td>
              </tr>
            </table>
          </form>
        </div>
      </div>		
    </div>
    <div id="copy-window" class="easyui-window" closed="true" style="overflow:hidden;">
      <div class="easyui-layout" fit="true" >
        <div region="center" border="false">
       	  <ul id="tt3"></ul>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="tb-copyoperate" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)"  onclick="copyArticle('<s:url namespace='/document/share' action='copy'/>');">确定</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#copy-window').window('close');">取消</a>
        </div>
      </div>
    </div>
    <div id="refence-window" class="easyui-window" closed="true" style="overflow:hidden;">
      <div class="easyui-layout" fit="true" >
        <div region="center" border="false">
       	  <ul id="tt2"></ul>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="tb-refenceoperate" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)"  onclick="refenceArticle('<s:url namespace='/document/share' action='refence'/>');">确定</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#refence-window').window('close');">取消</a>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/content/document/share/index.js"></script>
	<script type="text/javascript">
		var _shareIndex = new ShareIndex('#tt','#tt2','#tt3');
		$(function(){
			_shareIndex.init({
				queryUrl : '${ctx}/content/document/share/query',
				treeUrl : '${ctx}/site/channel/tree',
				refenceUrl : '${ctx}/content/document/share/refence',
				copyUrl : '${ctx}/content/document/share/copy',
				previewUrl : '${ctx}/template/preview'
			});
		});
	</script>
  </body>
</html>