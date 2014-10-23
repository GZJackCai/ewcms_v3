<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>插入资源</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
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
              <table class="formtable" width="100%">
                <tr>
                  <td width="6%">文件名：</td>
                  <td width="19%"><input type="text" name="LIKE_name" style="width:80px"/></td>
                  <td width="6%">描述：</td>
                  <td width="19%"><input type="text" name="LIKE_description" style="width:120px"/></td>
                  <td width="6%">&nbsp;</td>
                  <td width="19%">&nbsp;</td>
                  <td width="25%" colspan="2">
                    <a href="javascript:void(0);" id="toolbar-query" class="easyui-linkbutton" iconCls="icon-search">查询</a>
                    <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
                  </td>
                </tr>
              </table>
            </form>
          </div>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/views/content/resource/insert.js"></script>
    <script type="text/javascript">
        var _insert = new ResourceInsert('#tt', {query:'${ctx}/content/resource/query/${type}'});
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
  </body>
</html>
