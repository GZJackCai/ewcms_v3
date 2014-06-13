<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>出口分析</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/visit/dateutil.js"></script>
	<script type="text/javascript" src="${ctx}/static/fcf/js/FusionCharts.js"></script>
	<script type="text/javascript">
		$(function() {
			$('#startDate').datebox('setValue', dateTimeToString(new Date(new Date() - 15*24*60*60*1000)));
			$('#endDate').datebox('setValue', dateTimeToString(new Date()));
			$('#tt').datagrid({
				singleSelect : true,
				pagination : true,
				nowrap : true,
				striped : true,
				rownumbers : true,
				fitColumns : true,
				pageSize:30,
				url : '${ctx}/visit/totality/exit/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
			    columns:[[  
			            {field:'eeUrl',title:'出口URL',width:600,
			            	formatter : function(val, rec){
			            		if (val == null) return ''; 
			            		return '<a href="' + val + '" style="text-decoration: none" target="_blank">' + val + '</a>';
			            	}
			            },
			            {field:'eeCount',title:'出口次数',width:100},
			            {field:'eeRate',title:'比例',width:100},
			            {field:'trend',title:'时间趋势',width:70,
			            	formatter : function(val, rec){	
			            		return '<a href="javascript:void(0)" onclick="openTrend(\'' + rec.eeUrl + '\')" style="text-decoration: none">时间趋势</a>';
			            	}
			            }
			    ]]  
			});
		});
		function refresh(){
			$('#tt').datagrid({
				url : '${ctx}/visit/totality/exit/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
			});
		}
		function openTrend(value){
			var url = '${ctx}/visit/totality/exit/trend/index/' + value + '?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue');
			$.ewcms.openWindow({windowId:"#pop-window",iframeId:'#editifr_pop',src : url ,width:660,height:330,title:"时间趋势"});
		}
	</script>
  </head>
  <body class="easyui-layout">
	<div region="north" style="height:40px" border="false">
		<table width="100%" border="0" cellspacing="6" cellpadding="0"style="border-collapse: separate; border-spacing: 6px;">
			<tr>
				<td>
					从 <input type="text" id="startDate" name="startDate" class="easyui-datebox" style="width:120px" required/> 至 <input type="text" id="endDate" name="endDate" class="easyui-datebox" style="width:120px" required/> <a class="easyui-linkbutton" href="javascript:void(0)" onclick="refresh();return false;">查看</a>
				</td>
			</tr>
		</table>
	</div>
	<div region="center">
		<table id="tt" fit="true" border="false"></table>
	</div>
	<div id="pop-window" class="easyui-window" title="弹出窗口" icon="icon-visit-analysis" closed="true" style="display:none;">
           <div class="easyui-layout" fit="true">
               <div region="center" border="false">
               	<iframe id="editifr_pop"  name="editifr_pop" class="editifr" frameborder="0" width="100%" height="100%" scrolling="no"></iframe>
               </div>
           </div>
       </div>
  </body>
</html>