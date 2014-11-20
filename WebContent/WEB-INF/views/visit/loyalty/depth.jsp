<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<html>
  <head>
	<title>访问深度</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="north" style="height:310px" border="false">
	  <table width="100%" border="0" cellspacing="6" cellpadding="0" style="border-collapse: separate; border-spacing: 6px;">
		<tr>
		  <td>
			从 <input type="text" id="startDate" name="startDate" class="easyui-datebox" style="width:120px" editable="false" required="required"/> 至 <input type="text" id="endDate" name="endDate" class="easyui-datebox" style="width:120px" editable="false" required="required"/> <a class="easyui-linkbutton" href="javascript:void(0)" onclick="refresh();return false;">查看</a>
		  </td>
		</tr>
		<tr valign="top">
		  <td>
			<table width="100%" border="0" cellspacing="0" cellpadding="0" class="blockTable">
			  <tr>
				<td style="padding:0px;">
				  <div style="height: 100%;margin:0px;">
				    <div id="divChart" style="width:640px;height:250px;background-color:white"></div>
				  </div>
				</td>
			  </tr>
			</table>
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
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/visit/dateutil.js"></script>
	<script type="text/javascript" src="${ctx}/static/fcf/js/FusionCharts.js"></script>
	<script type="text/javascript">
		$(function() {
			$('#startDate').datebox('setValue', dateTimeToString(new Date(new Date() - 15*24*60*60*1000)));
			$('#endDate').datebox('setValue', dateTimeToString(new Date()));
			$('#tt').datagrid({
				singleSelect : true,
				pagination : false,
				nowrap : true,
				striped : true,
				url : '${ctx}/visit/loyalty/depth/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
			    columns:[[  
			            {field:'depth',title:'名称',width:150,
			            	formatter : function(val, rec){
			            		if (rec.accessDepthPk.depth>=30){
			            			return ">=30页";
			            		}else{
			            			return rec.accessDepthPk.depth + "页";	
			            		}
			            	}	
			            }, 
			            {field:'depthCount',title:'深度数量',width:120},
			            {field:'rate',title:'比例',width:120},  
			            {field:'trend',title:'时间趋势',width:70,
			            	formatter : function(val, rec){	
			            		return '<a href="javascript:void(0)" style="text-decoration: none" onclick="openTrend(\'' + rec.accessDepthPk.depth + '\')">时间趋势</a>';
			            	}
			            }
			    ]]  
			});
			showChart();
		});
		function showChart(){
			var parameter = {};
			parameter['startDate'] = $('#startDate').datebox('getValue');
			parameter['endDate'] = $('#endDate').datebox('getValue');
			$.post('${ctx}/visit/loyalty/depth/report', parameter, function(result) {
		  		var myChart = new FusionCharts('${ctx}/static/fcf/swf/Pie3D.swf?ChartNoDataText=无数据显示', 'myChartId', '640', '250','0','0');
	      		myChart.setDataXML(result);      
	      		myChart.render("divChart");
	   		});
		}
		function refresh(){
			$('#tt').datagrid({
				url:'${ctx}/visit/loyalty/depth/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue')
			});
			showChart();
		}
		function openTrend(value){
			var url = '${ctx}/visit/loyalty/depth/trend/index/' + value + '?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue');
			$.ewcms.openWindow({windowId:"#pop-window",iframe:'#editifr_pop',src:url,width:660,height:330,title:"时间趋势"});
		}
	</script>    
  </body>
</html>