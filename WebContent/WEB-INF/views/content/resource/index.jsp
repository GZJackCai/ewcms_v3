<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>资源管理</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <div class="easyui-tabs"  id="systemtab" fit="true" style="border: 0">
      <div title="图片资源"  style="padding: 2px;">
        <iframe id="imageifr" src="${ctx}/content/resource/manage/image" class="editifr" scrolling="no"></iframe>
      </div>
      <div title="Flash资源" style="padding:2px;">
        <iframe id="flashifr" src="" class="editifr" scrolling="no"></iframe>
      </div>
      <div title="影音资源" style="padding: 2px;">
        <iframe id="videoifr" src="" class="editifr" scrolling="no"></iframe>
      </div>
      <div title="文件资源" style="padding: 2px;">
        <iframe id="annexifr" src="" class="editifr" scrolling="no"></iframe>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/views/content/resource/index.js"></script>
    <script type="text/javascript">
    	var _m = new index('${ctx}/content/resource/manage');
        $(function(){
        	_m.init();
        });
    </script>
  </body>
</html>