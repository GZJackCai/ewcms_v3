<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>操作明细</title>
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/document/operatetrack/index.js"></script>
	<script type="text/javascript">
		var _operateTrackIndex = new OperateTrackIndex('#tt');
		$(function(){
			_operateTrackIndex.init({
				queryUrl : '${ctx}/content/document/operatetrack/query/${articleMainId}',
				reasonUrl : '${ctx}/content/document/operatetrack/reason'
			});
		});
	</script>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">
	  <table id="tt" fit="true"></table>
	</div>
	<div id="reason-window" class="easyui-window" closed="true" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr_reason"  name="editifr_reason" frameborder="0" width="100%" height="100%" scrolling="auto" style="width:100%;height:100%;"></iframe>
       </div>
      </div>
    </div>
  </body>
</html>