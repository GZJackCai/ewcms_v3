<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>插入资源</title>
	<%@ include file="../../taglibs.jsp" %>
    <script type="text/javascript" src="${ctx}/static/views/content/resource/insert.js"></script>
    <script type="text/javascript">
        var _insert = new ResourceInsert('#tt', '${ctx}', {query:'${ctx}/content/resource/query/${type}'});
    	$(function(){
        	_insert.init({
        		type:'${type}',
        		multi:'${multi}'
            });
        });
        var insert = function(callback,message){
           _insert.insert(callback,message);
        }
    </script>
  </head>
  <body>
    <div class="easyui-tabs" border="true" fit="true">
      <div title="上传"  style="padding: 2px;">
        <iframe id="resourceifr_id" name="resourceifr" src="${ctx}/content/resource/resource?type=${type}&multi=${muti}" class="editifr" scrolling="no"></iframe>
      </div>
      <div title="浏览"  style="padding: 2px;">
        <table id="tt" toolbar="#tb" fit="true"></table>
        <div id="tb" style="padding:2px;height:auto;display:none;">
          <div style="padding-left:2px;">
            <form id="queryform" style="padding: 0;margin: 0;">
                          文件名: <input type="text" name="LIKE_name" style="width:80px"/>&nbsp;
                          描述: <input type="text" name="LIKE_description" style="width:120px"/>&nbsp;
              <a href="#" id="toolbar-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
              <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
            </form>
          </div>
        </div>
      </div>
    </div>
  </body>
</html>
