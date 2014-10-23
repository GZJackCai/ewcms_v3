<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>上传资源</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/uploadify/uploadify.css"/>
  </head>
  <body class="easyui-layout">
    <div region="center" border="false">
      <div id="upload_queue" style="margin-left:2px"></div>
      <form:form  id="resource_infos" style="width:570px;height:280px; margin:2px;">
        <table id="tt" align="center" fit="true"></table>
      </form:form>
    </div>
    <div region="south" border="false" style="height:38px;line-height:38px;padding:3px 6px;">
      <input type="file" name="upload" id="upload"/>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type='text/javascript' src="${ctx}/static/uploadify/jquery.uploadify.min.js"></script>
    <script type='text/javascript' src="${ctx}/static/uploadify/swfobject.js"></script>
    <script type='text/javascript' src="${ctx}/static/views/content/resource/resource.js"></script>
    <script type="text/javascript">
    	var _upload = new Upload('${ctx}/content/resource/save',
                {objectName : '_upload',
    		    uploaderUrl:'${ctx}/static/uploadify/uploadify.swf',
                expressInstallUrl:'${ctx}/static/uploadify/expressInstall.swf',
                scriptUrl: '${ctx}/content/resource/receive',
                thumbScriptUrl: '${ctx}/content/resource/thumb/receive',
                type: '${type}',
                multi:  '${multi}',
                fileExt: '${fileExt}',
                resourceId: '${resourceId}',
                jsessionid : '${pageContext.session.id}'}
    	);
         $(function() {
        	 _upload.init();
         });
         function insert(callback,message){
        	 _upload.insert(callback,message);
         }
    </script>
  </body>
</html>
