<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>文档编辑</title>	
	<%@ include file="../../../taglibs.jsp"%>
	<script type="text/javascript" src="${ctx}/static/views/content/document/article/tree.js"></script>
	<script type="text/javascript">
		var rootnode,currentnode;
		var _articleTree = new ArticleTree('#tt2');
		$(function() {
			_articleTree.init({
				queryUrl : '${ctx}/site/channel/tree',
				articleUrl : '${ctx}/content/document/article/index',
				referUrl : '${ctx}/content/document/refer/index',
				workingbodyUrl : '${ctx}/plugin/online/workingbody/index',
				interactionUrl : '${ctx}/plugin/interaction/index',
				speakUrl : '${ctx}/plguin/interaction/speak',
				advisorUrl : '${ctx}/plugin/online/advisor/index',
				pbUrl : '${ctx}/particular/pb/index',
				paUrl : '${ctx}/particular/pa/index',
				ebUrl : '${ctx}/particular/eb/index',
				eaUrl : '${ctx}/particular/ea/index',
				mbUrl : '${ctx}/particular/mb/index',
				maUrl : '${ctx}/particular/ma/index',
			});
		});
	</script>
  </head>
  <body class="easyui-layout">
	<div region="west" title='<label id="treeload-button" style="cursor:pointer;"><img src="${ctx}/static/image/refresh.png" style="vertical-align: middle;">专栏库</label>' split="true" style="width:180px;">
	  <ul id="tt2"></ul>
	</div>
	<div region="center"  style="overflow:auto;">
	  <iframe id="editifr"  name="editifr" class="editifr"></iframe>
	</div>
  </body>
</html>