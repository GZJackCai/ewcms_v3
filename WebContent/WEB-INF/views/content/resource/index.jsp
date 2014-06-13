<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>资源管理</title>
	<%@ include file="../../taglibs.jsp" %>
    <script type="text/javascript" src="${ctx}/static/views/content/resource/index.js"></script>
    <script type="text/javascript">
    	var _m = new index('${ctx}/content/resource/manage');
        $(function(){
        	_m.init();
        });
    </script>
  </head>
  <body>
    <div class="easyui-tabs"  id="systemtab" fit="true" style="border: 0">
      <div title="图片"  style="padding: 2px;">
        <iframe id="imageifr" src="${ctx}/content/resource/manage/image" class="editifr" scrolling="no"></iframe>
      </div>
      <div title="FLASH" style="padding:2px;">
        <iframe id="flashifr" src="" class="editifr" scrolling="no"></iframe>
      </div>
      <div title="视频和音频" style="padding: 2px;">
        <iframe id="videoifr" src="" class="editifr" scrolling="no"></iframe>
      </div>
      <div title="附件" style="padding: 2px;">
        <iframe id="annexifr" src="" class="editifr" scrolling="no"></iframe>
      </div>
    </div>
  </body>
</html>