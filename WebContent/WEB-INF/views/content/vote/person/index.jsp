<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>调查投票明细</title>	
	<%@ include file="../../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/vote/person/index.js"></script>
	<script type="text/javascript">
		var _personIndex = new PersionIndex('#tt');
		$(function(){
			_personIndex.init({
				queryUrl : '${ctx}/content/vote/person/query/${questionnaireId}',
				showUrl : '${ctx}/content/vote/record/index/${questionnaireId}'
			});
		});
	</script>
  </head>
  <body class="easyui-layout">
	<div region="center" style="padding:2px;" border="false">  
	  <table id="tt" toolbar="#tb" fit="true"></table>
	</div>
	<div id="tb" style="padding:2px;height:auto;">
      <div class="toolbar" style="margin-bottom:2px">
      </div>
      <div>
        <form id="queryform" style="padding:0;margin: 0;">
          <table class="formtable" width="100%">
            <tr>
              <td width="8%">编号：</td>
              <td width="17%"><input type="text" name="EQ_id" style="width:120px;"/></td>
              <td width="8%">IP地址：</td>
              <td width="17%"><input type="text" name="LIKE_ip" style="width:120px;"/></td>
              <td width="8%">投票时间：</td>
              <td width="17%"><input type="text" id="recordTimeStart" name="GTED_recordTime" class="easyui-datebox" style="width:100px"/>&nbsp;至&nbsp;<input type="text" id="recordTimeEnd" name="LTED_recordTime" class="easyui-datebox" style="width:100px"/></td>
			　<td width="25%" colspan="2">
			    <a href="#" id="toolbar-arrows" style="text-decoration:none">更多...</a>
                <a id="tb-query" href="#" class="easyui-linkbutton" >查询</a>
                <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton">清除</a>
              </td>
            </tr>
          </table>
        </form>
      </div>
    </div>
	<div id="record-window" class="easyui-window" icon="icon-votedetail" closed="true" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
       	  <iframe id="editifr_record"  name="editifr_record" frameborder="0" width="100%" height="100%" scrolling="auto" style="width:100%;height:100%;"></iframe>
        </div>
      </div>
    </div>
  </body>
</html>