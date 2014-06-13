<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>缩略图</title>
	<%@ include file="../../taglibs.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/uploadify/uploadify.css"/>
    <script type='text/javascript' src="${ctx}/static/uploadify/jquery.uploadify.min.js"></script>
    <script type='text/javascript' src="${ctx}/static/uploadify/swfobject.js"></script>
    <script type='text/javascript' src="${ctx}/static/views/content/resource/thumb.js"></script>
    <script type="text/javascript">
        var _t = new thumb('${resourceId}',
            {uploaderUrl:'${ctx}/static/uploadify/medium/uploadify.allglyphs.swf',
            expressInstallUrl:'${ctx}/static/uploadify/medium/expressInstall.swf',
            cancelImgUrl: '${ctx}/static/uploadify/image/cancel.png',
            scriptUrl: '${ctx}/content/resource/thumb/receive',
            jsessionid : '${pageContext.session.id}'});
        $(function() {
            _t.init();
        });
    </script>
  </head>
  <body>
    <div id="upload_queue" style="margin:20px 5px;height: 50px;"></div>
    <div align="right" style="text-align:right;height:38px;line-height:38px;padding:3px 6px;">
      <input type="file" name="upload" id="upload"/>
    </div>
  </body>
</html>
