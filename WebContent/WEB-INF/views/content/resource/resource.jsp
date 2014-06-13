<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>上传资源</title>
	<%@ include file="../../taglibs.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/uploadify/uploadify.css"/>
    <script type='text/javascript' src="${ctx}/static/uploadify/jquery.uploadify.min.js"></script>
    <script type='text/javascript' src="${ctx}/static/uploadify/swfobject.js"></script>
    <script type='text/javascript' src="${ctx}/static/views/content/resource/resource.js"></script>
    <script type="text/javascript">
    	var _u = new Upload('${ctx}','${ctx}/content/resource/save',
                {uploaderUrl:'${ctx}/static/uploadify/medium/uploadify.allglyphs.swf',
                expressInstallUrl:'${ctx}/static/uploadify/medium/expressInstall.swf',
                cancelImgUrl: '${ctx}/static/uploadify/image/cancel.png',
                scriptUrl: '${ctx}/content/resource/receive',
                thumbScriptUrl: '${ctx}/content/resource/thumb/receive',
                type: '${type}',
                multi:  '${multi}',
                fileDesc: '${fileDesc}',
                fileExt: '${fileExt}',
                resourceId: '${resourceId}',
                jsessionid : '${pageContext.session.id}'}
    	);
         $(function() {
         	_u.init();
         });
         function insert(callback,message){
            _u.insert(callback,message);
         }
    </script>
  </head>
  <body class="easyui-layout">
    <div region="center" border="false">
      <div id="upload_queue" style="margin-left:2px;display:none;"></div>
      <form:form  id="resource_infos" style="width:572px;height:340px; margin:2px;">
        <table id="tt" align="center" fit="true"></table>
      </form:form>
    </div>
    <div region="south" border=false style="height:38px;line-height:38px;padding:3px 6px;">
      <input type="file" name="upload" id="upload"/>
    </div>
  </body>
</html>
