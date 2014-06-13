<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>时间趋势</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/visit/dateutil.js"></script>
	<script type="text/javascript" src="${ctx}/static/fcf/js/FusionCharts.js"></script>
	<script type="text/javascript">
			var startDate = '${startDate}';
			var endDate = '${endDate}';
			$(function() {
			});
			function showChart(){
				var parameter = {};
				parameter['startDate'] = startDate;
				parameter['endDate'] = endDate;
				parameter['labelCount'] = 8;
				$.post('${ctx}/visit/totality/region/city/trend/report/${country}_${province}_${city}', parameter, function(result) {
			  		var myChart = new FusionCharts('${ctx}/static/fcf/swf/MSLine.swf?ChartNoDataText=无数据显示', 'myChartId', '640', '260','0','0');
		      		myChart.setDataXML(result);      
		      		myChart.render("divChart");
		   		});
			}
			function view(){
				startDate = $('#startDate').datebox('getValue');
				endDate = $('#endDate').datebox('getValue');
				showChart();
			}
		</script>
	</head>
	<body class="easyui-layout">
		 <div region="north" style="height:310px" border="false">
			<table width="100%" border="0" cellspacing="2" cellpadding="0"style="border-collapse: separate; border-spacing: 2px;">
				<tr>
					<td>
						从 <input type="text" id="startDate" name="startDate" class="easyui-datebox" style="width:120px" value="${startDate}" required/> 至 <input type="text" id="endDate" name="endDate" class="easyui-datebox" style="width:120px" value="${endDate}" required/> <a class="easyui-linkbutton" href="javascript:void(0)" onclick="view();return false;">查看</a>
						<a class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" href="javascript:void(0);" onclick="javascript:parent.$('#pop-window').window('close');">关闭</a>
					</td>
				</tr>
				<tr valign="top">
					<td>
					<table width="100%" border="0" cellspacing="0" cellpadding="0" class="blockTable">
						<tr>
							<td style="padding:0px;">
								<div style="height: 100%;margin:0px;">
									<div id="divChart" style="width:640px;height:260px;background-color:white"></div>
									<script type="text/javascript">
										showChart();
									</script>
								</div>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
		</div>
	</body>
</html>