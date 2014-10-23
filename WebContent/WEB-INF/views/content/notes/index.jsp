<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>个人备忘</title>
    <%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/views/content/notes/notes.css"></link>
  </head>
  <body class="easyui-layout">
    <div region="center" style="padding: 2px;" border="false">
      <table width="100%" cellspacing="0" cellpadding="6" border="0" >
        <tr>
		  <td valign="middle" style="padding-bottom:0;">&nbsp;</td>
        </tr>
 		<tr>
		  <td style="padding:0;">
		    <table align="left" width="40%" cellspacing="0" cellpadding="0" border="0">
		      <tr align="left">
		        <td>&nbsp;</td>
		        <td valign="middle">
		        	<a href="javascript:void(0);" id="tb-today" iconCls="icon-notes-today" class="easyui-linkbutton">今天</a>
		        </td>
		        <td align="left">${toDayLunar}</td>
		      </tr>
		    </table>
		    <table align="left" width="30%" cellspacing="0" cellpadding="0" border="0">
		    	<tr>
		    		<td align="left" width="100%">
		    		  <a href="javascript:void(0);" id="tb-notedetial" iconCls="icon-notes-list" class="easyui-linkbutton">备忘录列表</a>
		    		</td>
		    	</tr>
		    </table>
		    <table align="right" width="30%" cellspacing="0" cellpadding="0" border="0">
		      <tr>
		        <td align="right" width="100%">
		        	<a id="tb-prevMonth" href="javascript:void(0);" iconCls="icon-notes-prev" class="easyui-linkbutton">上一个月</a>&nbsp;
					<input type="text" id="year" name="year" size="3" style="background-color:transparent;border:0;" readonly="readonly" value="${year}"/>年 <input type="text" id="month" name="month" size="1" style="background-color:transparent;border:0;" readonly="readonly" value="${month}"/>月&nbsp;
					<a id="tb-nextMonth" href="javascript:void(0);" iconCls="icon-notes-next" class="easyui-linkbutton">下一个月</a>
		        </td>
		        <td>&nbsp;&nbsp;</td>
			  </tr>
			</table>
		  </td>
		</tr>
        <tr>
          <td>
            <table id="result" width="100%" cellspacing="0" cellpadding="0" bordercolor="#aaccee" border="1">
              ${result}
	        </table>
	      </td>
        </tr>
      </table>
    </div>
    <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" title="&nbsp;备忘录">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr"  name="editifr" class="editifr" frameborder="0" scrolling="no"></iframe>
        </div>
      </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/views/content/notes/index.js"></script>
    <script type="text/javascript">
       var _notesIndex = new NotesIndex({
    	   addUrl : '${ctx}/content/notes/add',
    	   editUrl : '${ctx}/content/notes/edit'
       });
       $(function(){
    	  _notesIndex.init({
    		  dropUrl : '${ctx}/content/notes/drop',
    		  changeDateUrl : '${ctx}/content/notes/changeDate',
    		  toDayUrl : '${ctx}/content/notes/toDay/${currentYear}_${currentMonth}',
    		  noteDetailUrl : '${ctx}/content/notes/list'
    	  });
       });
    </script>
  </body>
</html>