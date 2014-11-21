<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>缩略图</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/uploadify/uploadify.css"/>
  </head>
  <body class="easyui-layout">
    <div region="center" border="false">
      <div id="upload_queue" style="margin-left:2px;"></div>
    </div>
    <div region="south" border="false" style="height:38px;line-height:38px;padding:3px 6px;">
      <input type="file" name="upload" id="upload"/>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type='text/javascript' src="${ctx}/static/uploadify/jquery.uploadify.min.js"></script>
    <script type='text/javascript' src="${ctx}/static/uploadify/swfobject.js"></script>
    <script type='text/javascript' src="${ctx}/static/views/content/resource/thumb.js"></script>
    <script type="text/javascript">
        var _t = new thumb('${resourceId}',
            {uploaderUrl:'${ctx}/static/uploadify/uploadify.swf',
            expressInstallUrl:'${ctx}/static/uploadify/expressInstall.swf',
            scriptUrl: '${ctx}/content/resource/thumb/receive',
            jsessionid : '${pageContext.session.id}'});
        $(function() {
            _t.init();
        });
    </script>
  </body>
</html>
