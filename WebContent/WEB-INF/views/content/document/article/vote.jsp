<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>调查投票列表</title>	
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/document/article/vote.js"></script>
	<script type="text/javascript">
		var _voteArticle = new VoteArticle('#tt','#tt2');
		$(function(){
			_voteArticle.init({
				queryUrl : '${ctx}/content/vote/questionnaire/query',
				treeUrl : '${ctx}/site/channel/tree'
			});
		});
    </script>
  </head>
  <body class="easyui-layout">
	<div region="west" title='<label id="treeload-button" style="cursor:pointer;"><img src="${ctx}/static/image/refresh.png" style="vertical-align: middle;">专栏库</label>' split="true" style="width:180px;">
	  <ul id="tt2"></ul>
	</div>
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt"  toolbar="#tb" fit="true"></table>
	  <div id="tb" style="padding:2px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
        </div>
        <div>
          <form id="queryform" style="padding:0;margin: 0;">
            <table class="formtable" width="100%">
              <tr>
                <td width="8%">编号：</td>
                <td width="17%"><input type="text" name="EQ_id" style="width:120px;"/></td>
                <td width="8%">标题：</td>
                <td width="17%"><input type="text" name="LIKE_title" style="width:120px;"/></td>
                <td width="8%">&nbsp;</td>
                <td width="17%">&nbsp;</td>
			　    <td width="25%" colspan="2">
                  <a id="tb-query" href="#" class="easyui-linkbutton" >查询</a>
                  <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton">清除</a>
                </td>
              </tr>
            </table>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>