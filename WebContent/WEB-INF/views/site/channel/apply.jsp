<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>查看被其他栏目引用</title>
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/channel/apply.js"></script>
	<script>
	    var _channelApply = new ChannelApply('#tt');
		$(function(){
			_channelApply.init({
				queryUrl : '${ctx}/site/channel/apply/query/${channelId}',
				removeUrl : '${ctx}/site/channel/apply/delete/${channelId}'
			});
		});
	</script>
  </head>
  <body class="easyui-layout">
    <div data-options="region:'center',split:true" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
  	  <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px">
		  <a id="td-remove" href="#" class="easyui-linkbutton" plain="true" iconCls="icon-remove">移除</a>
		</div>
        <div  style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                     栏目编号：<input type="text" id="id" name="EQ_id" style="width:80px"/>
                     栏目名称：<input type="text" id="name" name="LIKE_name" style="width:80px"/>
                     栏目名称：<input type="text" id="absUrl" name="LIKE_absUrl" style="width:80px"/>
            <a id="tb-query" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search',toggle:true">查询</a>
            <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
          </form>
        </div>
      </div>
    </div>
  </body>
</html>