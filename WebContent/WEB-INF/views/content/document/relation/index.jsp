<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>相关文章</title>	
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/document/relation/index.js"></script>
	<script type="text/javascript">
		var _relationIndex = new RelationIndex('#tt');
		$(function(){
			_relationIndex.init({
				queryUrl : '${ctx}/content/document/relation/query/${articleId}',
				addUrl : '${ctx}/content/document/relation/add',
				deleteUrl : '${ctx}/content/document/relation/delete/${articleId}',
				upUrl : '${ctx}/content/document/relation/up/${articleId}',
				downUrl : '${ctx}/content/document/relation/down/${articleId}',
				saveUrl : '${ctx}/content/document/relation/save/${articleId}'
			});
		});
	</script>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="tb-add" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-add">添加</a>
		  <a id="tb-remove" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove">移除</a>
 		  <a id="tb-up" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-up">上移</a>
 		  <a id="tb-down" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-down">下移</a>
 		  <a id="tb-cancel" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" href="#">关闭</a>
		</div>
        <div  style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                      编号：<input type="text" name="EQ_relationArticle.id" style="width:120px;"/>&nbsp;
                      标题：<input type="text" name="LIKE_relationArticle.title" style="width:120px;"/>&nbsp;
            <a id="tb-query" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',toggle:true">查询</a>
            <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
          </form>
        </div>
      </div>
    </div>
    <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="&nbsp;相关文章" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
	      <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="tb-save" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">选择</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#edit-window').window('close');">取消</a>
        </div>
      </div>
    </div>	
  </body>
</html>