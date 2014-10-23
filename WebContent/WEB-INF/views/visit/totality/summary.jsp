<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>文章分类</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="north" style="height:310px;padding: 8px" border="false">
	  <div id="divChart" style="width:700px;height:290px;background-color:white;"></div>
	</div>
	<div region="center" title='<strong>从 <font color="red">${firstAddDate}</font> 开始统计，总计 <font color="red">${visitDay}</font> 天 </strong>'>
	  <table id="tt" fit="true" border="false"></table>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/visit/dateutil.js"></script>
	<script type="text/javascript" src="${ctx}/static/fcf/js/FusionCharts.js"></script>
	<script type="text/javascript">
		var tableUrl = '${ctx}/visit/totality/summary/table';
		$(function() {
				$('#tt').datagrid({
					singleSelect : true,
					pagination : false,
					nowrap : true,
					striped : true,
					url : tableUrl,
				    columns:[[  
				            {field:'name',title:'名称',width:100},  
				            {field:'sumPv',title:'PV数量',width:120,
				            	formatter : function(val, rec){
				            		if (rec.name != '') return val;
				            		else return rec.betideIp;
				            	}
				            },  
				            {field:'countUv',title:'UV数量',width:120,
				            	formatter : function(val, rec){
				            		if (rec.name != '') return val;
				            		else return rec.betideIp;
				            	}
				            },
				            {field:'countIp',title:'IP数量',width:120,
				            	formatter : function(val, rec){
				            		if (rec.name != '') return val;
				            		else return rec.betideIp;
				            	}
				            },
				            {field:'rate',title:'回头率',width:80,
				            	formatter : function(val, rec){
				            		if (rec.name == '最高' || rec.name == '')
				            			return '';
				            		else
				            			return val;
				            	}
				            },
				            {field:'avgTime',title:'平均访问时长',width:100,
				            	formatter : function(val, rec){
				            		if (rec.name == '最高' || rec.name == '')
				            			return '';
				            		else
				            			return val;
				            	}
				            }
				    ]]  
				});
				
				showChart();
			});
			function showChart(){
				var startDate = dateTimeToString(new Date(new Date() - 30*24*60*60*1000));
				var endDate = dateTimeToString(new Date());
				var parameter = {};
				parameter['startDate'] = startDate;
				parameter['endDate'] = endDate;
				parameter['labelCount'] = 8;
				$.post('${ctx}/visit/totality/summary/report', parameter, function(result) {
			  		var myChart = new FusionCharts('${ctx}/static/fcf/swf/MSLine.swf?ChartNoDataText=无数据显示', 'myChartId', '700', '290','0','0');
		      		myChart.setDataXML(result);      
		      		myChart.render("divChart");
		   		});
			}
	</script>
  </body>
</html>