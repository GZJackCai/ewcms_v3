<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>文档编辑</title>	
    <%@ include file="../../../taglibs.jsp"%>
    <script type="text/javascript" src="${ctx}/static/easyui/ext/datagrid-detailview.js"></script>
    <script type="text/javascript" src="${ctx}/static/views/content/document/article/index.js"></script>
    <script type="text/javascript">
    	var _articleIndex = new ArticleIndex('#tt','#tt3');
    	$(function(){
    		_articleIndex.init({
    			queryUrl : '${ctx}/content/document/article/query/${channelId}',
    			editUrl : '${ctx}/content/document/article/edit/${channelId}',
    			deleteUrl : '${ctx}/content/document/article/delete/${channelId}',
    			reasonUrl : '${ctx}/content/document/article/reason',
    			treeUrl : '${ctx}/site/channel/tree',
    			operateTrackUrl : '${ctx}/content/document/operatetrack/index',
    			effectiveUrl : '${ctx}/content/document/article/reviewEffective/${channelId}',
    			previewUrl : '${ctx}/template/preview?channelId=${channelId}',
    			topUrl : '${ctx}/content/document/article/top/${channelId}',
    			shareUrl : '${ctx}/content/document/article/share/${channelId}',
    			sortUrl : '${ctx}/content/document/article/sort/${channelId}',
    			isSortUrl : '${ctx}/content/document/article/isSort/${channelId}',
    			clearSortUrl : '${ctx}/content/document/article/clearSort/${channelId}',
    			approveUrl : '${ctx}/content/document/article/approve/${channelId}',
    			approveArticleUrl : '${ctx}/content/document/article/approveArticle/${channelId}',
    			publishUrl : '${ctx}/content/document/article/pubblish/${channelId}',
    			associateUrl : '${ctx}/content/document/article/associateRelease/${channelId}',
    			breakUrl : '${ctx}/content/document/article/break/${channelId}',
    			moveArticleUrl : '${ctx}/content/document/article/move/${channelId}',
    			copyArticleUrl : '${ctx}/content/document/article/copy/${channelId}',
    			reviewArticleUrl : '${ctx}/content/document/article/reviewArticle/${channelId}'
    		});
    	});
    </script>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:2px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="menu-operate" href="#" class="easyui-menubutton" plain="true" iconCls="icon-article-preview" menu="#menu-operatesub">操作</a>
		  <a id="menu-preview" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-article-preview">预览</a>
 		  <a id="menu-top" href="#" class="easyui-menubutton" plain="true" iconCls="icon-top" menu="#menu-topsub">置顶</a>
 		  <a id="menu-share" href="#" class="easyui-menubutton" plain="true" iconCls="icon-share" menu="#menu-sharesub">共享</a>
 		  <a id="menu-sort" href="#" class="easyui-menubutton" plain="true" iconCls="icon-sort" menu="#menu-sortsub">排序</a>
 		  <a id="menu-approve" href="#" class="easyui-menubutton" plain="true" iconCls="icon-review" menu="#menu-approvesub">审核</a>
 		  <a id="menu-publish" href="#" class="easyui-menubutton" plain="true" iconCls="icon-publish" menu="#menu-publishsub">发布</a>
		</div>
		<div id="menu-operatesub" style="width:80px;">
            <div id="menu-operate-add" iconCls="icon-add">新增</div>
            <div id="menu-operate-upd" iconCls="icon-edit">修改</div>
            <div id="menu-operate-sep1" class="menu-sep"></div>
            <div id="menu-operate-remove" iconCls="icon-remove">删除</div>
            <div id="menu-operate-sep2" class="menu-sep"></div>
        	<div id="menu-operate-copy" iconCls="icon-copy">复制</div>
	        <div id="menu-operate-move" iconCls="icon-move">移动</div>
        </div>
        <div id="menu-topsub" style="width:80px;">
	    	<div id="menu-top-ok" iconCls="icon-top-set">确定</div>
	        <div id="menu-top-cancel" iconCls="icon-top-cancel">取消</div>
	    </div>
	    <div id="menu-sharesub" style="width:80px;">
	    	<div id="menu-share-ok" iconCls="icon-top-set">确定</div>
	        <div id="menu-share-cancel" iconCls="icon-top-cancel">取消</div>
	    </div>
        <div id="menu-sortsub" style="width:80px;">
        	<div id="menu-sort-ok" iconCls="icon-sortset">设置</div>
	        <div id="menu-sort-clear" iconCls="icon-sortclear">清除</div>
	    </div>
	    <div id="menu-approvesub" style="width:80px;">
	    	<div id="menu-approve-submit" iconCls="icon-reviewsubmit">提交</div>
	        <div id="menu-approve-ok" iconCls="icon-reviewprocess">确认</div>
	    </div>
	    <div id="menu-publishsub" style="width:80px;">
	    	<div id="menu-publish-independent" iconCls="icon-publishok">独立</div>
	    	<div id="menu-publish-relevance" iconCls="icon-publishrec">关联</div>
	    	<div id="menu-publish-sep1" class="menu-sep"></div>
	    	<div id="menu-publish-back" iconCls="icon-breakarticle">退回</div>
	    </div>
        <div>
          <form id="queryform" style="padding:0;margin: 0;">
            <table class="formtable" width="100%">
              <tr>
                <td width="5%">编号：</td>
                <td width="20%"><input type="text" name="EQ_article.id" style="width:120px;"/></td>
                <td width="5%">标题：</td>
                <td width="20%"><input type="text" name="LIKE_article.title" style="width:120px;"/></td>
                <td width="5%">状态：</td>
                <td width="20%"><form:select id="status" name="EQ_article.status" path="statusMap">
					  <form:option value="" label="-----请选择-----"/>
					  <form:options items="${statusMap}" itemLabel="description"/>
					</form:select>
				</td>
				<td width="25%" colspan="2">
				  <a href="#" id="toolbar-arrows" style="text-decoration:none">更多...</a>
                  <a id="tb-query" href="#" class="easyui-linkbutton" >查询</a>
                  <a id="tb-clear" href="#" class="easyui-linkbutton">清除</a>
                </td>
              </tr>
              <tr>
              	<td>类型：</td>
              	<td><form:select id="genre" name="EQ_article.genre" path="genreMap">
					  <form:option value="" label="-----请选择-----"/>
					  <form:options items="${genreMap}" itemLabel="description"/>
					</form:select>
              	</td>
              	<td>创建者：</td>
              	<td><input type="text" name="LIKE_article.created" style="width:120px;"/></td>
              	<td>发布时间：</td>
              	<td><input type="text" id="publishedStart" name="GTED_article.published" class="easyui-datebox" style="width:120px"/>&nbsp;至&nbsp;<input type="text" id="publishedEnd" name="LTED_article.published" class="easyui-datebox" style="width:120px"/></td>
              	<td width="5%">修改时间：</td>
              	<td width="20%"><input type="text" id="modifiedStart" name="GTED_article.modified" class="easyui-datebox" style="width:120px"/>&nbsp;至&nbsp;<input type="text" id="modifiedEnd" name="LTED_article.modified" class="easyui-datebox" style="width:120px"/></td>
              </tr>
            </table>
          </form>
        </div>
      </div>
    </div>
    <div id="moveorcopy-window" class="easyui-window" closed="true" style="display:none;overflow:hidden;">
      <div class="easyui-layout" fit="true" >
        <div region="center" border="false">
       	  <ul id="tt3"></ul>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="tb-move" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
          <a id="tb-copy" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#moveorcopy-window').window('close');">取消</a>
        </div>
      </div>
    </div>
    <div id="approve-window" class="easyui-window" closed="true" style="display:none;overflow:hidden;width:550px;height:230px;">
      <div class="easyui-layout" fit="true" >
        <div region="center" border="false" style="padding: 5px;">
       	  <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#99BBE8" style="border: #99BBE8 1px solid;">
            <tr align="center">
              <td height="30" width="20%">操作</td>
              <td height="30" width="80%">说明</td>
            </tr>
            <tr>
              <td height="40">&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="reviewRadio1" name="reviewRadio" value="0"/><label for="reviewRadio">通过</label></td>
              <td height="40">&nbsp;</td>
            </tr>
            <tr>
              <td height="40">&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="reviewRadio1" name="reviewRadio" value="1"/><label for="reviewRadio">不通过</label></td>
              <td height="40">&nbsp;<textarea cols="46" id="reason" name="reason"></textarea></td>
            </tr>
          </table>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="tb-approve" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#approve-window').window('close');return false;">取消</a>
        </div>
      </div>
    </div>
    <div id="sort-window" class="easyui-window" closed="true" style="display:none;overflow:hidden;">
      <div class="easyui-layout" fit="true" >
        <div region="center" border="false" style="padding: 5px;">
          <table width="100%" border="1" cellpadding="0" cellspacing="0" bordercolor="#99BBE8" style="border: #99BBE8 1px solid;">
            <tr align="center">
              <td height="30" width="20%">操作</td>
              <td height="30" width="80%">说明</td>
            </tr>
            <tr>
              <td height="40">&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="sortRadio1" name="sortRadio" value="0"/><label for="reviewRadio">插入</label></td>
              <td height="40">&nbsp;所选文章将插入到当前排序号</td>
            </tr>
            <tr>
              <td height="40">&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" id="sortRadio1" name="sortRadio" value="1"/><label for="reviewRadio">替换</label></td>
              <td height="40">&nbsp;所选文章将替换已有的文章的排序号</td>
            </tr>
          </table>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="tb-sort" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">确定</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="javascript:$('#sort-window').window('close');return false;">取消</a>
        </div>
      </div>
    </div>
  </body>
</html>