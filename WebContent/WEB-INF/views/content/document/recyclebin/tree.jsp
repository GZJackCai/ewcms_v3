<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>文档编辑</title>	
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/document/recyclebin/tree.js"></script>
	<script type="text/javascript">
		var currentnode;
		var _recyclebinTree = new RecyclebinTree('#tt2');
		$(function(){
			_recyclebinTree.init({
				treeUrl : '${ctx}/site/channel/tree',
				indexUrl : '${ctx}/content/document/recyclebin/index'
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