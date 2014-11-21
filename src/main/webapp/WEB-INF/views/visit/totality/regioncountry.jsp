<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<html>
  <head>
	<title>区域分布</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="north" style="height:310px" border="false">
	  <table width="100%" border="0" cellspacing="6" cellpadding="0" style="border-collapse: separate; border-spacing: 6px;">
		<tr>
		  <td>当前位置：<font color="red">全部</font></td>
		</tr>
		<tr>
		  <td>从 <input type="text" id="startDate" name="startDate" class="easyui-datebox" style="width:120px" editable="false" required="required"/> 至 <input type="text" id="endDate" name="endDate" class="easyui-datebox" style="width:120px" editable="false" required="required"/> <a class="easyui-linkbutton" href="javascript:void(0)" onclick="refresh();return false;">查看</a></td>
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
				url :  '${ctx}/visit/totality/region/country/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
				columns:[[  
				        {field:'country',title:'国家',width:120,
				            formatter : function(val, rec){
				            	return '<a style="text-decoration: none" href="javascript:void(0);" onclick="openLevel(\'' + rec.regionPk.country + '\')">' + rec.regionPk.country + '</a>';
				            }
				        },
				        {field:'rate',title:'PV比例',width:120},
				        {field:'pageViewSum',title:'PV数量',width:120},  
				        {field:'uniqueIdCount',title:'UV数量',width:120},
				        {field:'ipCount',title:'IP数量',width:120},
				        {field:'stickTimeAvg',title:'平均访问时长(秒)',width:120},
				        {field:'trend',title:'时间趋势',width:70,
				            formatter : function(val, rec){	
				            	return '<a style="text-decoration: none" href="javascript:void(0);" onclick="openTrend(\'' + rec.regionPk.country + '\')">时间趋势</a>';
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
			$.post('${ctx}/visit/totality/region/country/report', parameter, function(result) {
			  	var myChart = new FusionCharts('${ctx}/static/fcf/swf/Pie3D.swf?ChartNoDataText=无数据显示', 'myChartId', '640', '250','0','0');
		      	myChart.setDataXML(result);      
		      	myChart.render("divChart");
		   	});
		}
		function refresh(){
			$('#tt').datagrid({
				url : '${ctx}/visit/totality/region/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue')
			});
			showChart();
		}
		function openTrend(value){
			var url = '${ctx}/visit/totality/region/country/trend/index/' + value + '?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue');
			$.ewcms.openWindow({windowId:"#pop-window",iframeId:'#editifr_pop',src:url,width:660,height:330,title: value + " 时间趋势"});
		}
		function openLevel(name){
			window.location = '${ctx}/visit/totality/region/province/index/' + name + '?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'); 
		}
	</script>
  </body>
</html>