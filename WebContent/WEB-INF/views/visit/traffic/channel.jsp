<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>栏目点击排行</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="north" style="height:330px" border="false">
	  <table width="100%" border="0" cellspacing="6" cellpadding="0"style="border-collapse: separate; border-spacing: 6px;">
		<tr>
		  <td>当前报表：<span id="explain"></span> 下子栏目点击排行</td>
		</tr>
		<tr>
		  <td>从 <input type="text" id="startDate" name="startDate" class="easyui-datebox" style="width:120px" required/> 至 <input type="text" id="endDate" name="endDate" class="easyui-datebox" style="width:120px" required/> <a class="easyui-linkbutton" href="javascript:void(0)" onclick="refresh();return false;">查看</a></td>
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
		var startDate = dateTimeToString(new Date(new Date() - 30*24*60*60*1000));
		var endDate = dateTimeToString(new Date());
		var channelId = "0";
		var channelIds = [];
		$(function() {
			showChart();
			
			$('#startDate').datebox('setValue', dateTimeToString(new Date(new Date() - 15*24*60*60*1000)));
			$('#endDate').datebox('setValue', dateTimeToString(new Date()));
			channelIds.push(0);
			$('#explain').append('<span id="position_0"><a href="javascript:void(0);" style="text-decoration: none" onclick="positionSel(\'0\')">一级</a></span>');
			$('#tt').datagrid({
				singleSelect : true,
				pagination : false,
				nowrap : true,
				striped : true,
				url : '${ctx}/visit/traffic/channel/table?startDate=' + startDate + '&endDate=' + endDate,
			    columns:[[ 
			             {field:'channelId',title:'栏目编号',hidden:true},
			             {field:'channelName',title:'栏目名称',width:200,
			            	formatter : function(val, rec){
			            		if (val == null) return '';
			            		if (rec.isChildren){
			            			return '<a href="javascript:void(0);" style="text-decoration: none" onclick="channelChildren(\'' + rec.channelId + '\',\'' + rec.channelName + '\')">' + val + '</a>';
			            		}else{
			            			return val;
			            		}
			            	}
			            },
			            {field:'levelPageView',title:'本级PV量',width:100},
			            {field:'levelStickTime',title:'本级页均停留时间',width:110},
			            {field:'trend',title:'时间趋势',width:70,
			            	formatter : function(val, rec){	
			            		return '<a href="javascript:void(0)" style="text-decoration: none" onclick="openTrend(\'' + rec.channelName + '\',\'' + rec.channelId + '\')">时间趋势</a>';
			            	}
			            },
			            {field:'pageView',title:'子栏目PV量',width:100},
			            {field:'stickTime',title:'子栏页均停留时间',width:110}
			    ]]  
			});
		});
		function showChart(){
			var parameter = {};
			parameter['startDate'] = startDate;
			parameter['endDate'] = endDate;
			if (channelId != '0'){
				parameter['parentChannelId'] = channelId;
			}
			$.post('${ctx}/visit/traffic/channel/report', parameter, function(result) {
		  		var myChart = new FusionCharts('${ctx}/static/fcf/swf/Pie3D.swf?ChartNoDataText=无数据显示', 'myChartId', '640', '250','0','0');
	      		myChart.setDataXML(result);      
	      		myChart.render("divChart");
	   		});
		}
		function refresh(){
			startDate = $('#startDate').datebox('getValue');
			endDate = $('#endDate').datebox('getValue');
			var tableUrl = '${ctx}/visit/traffic/channel/table?startDate=' + startDate + '&endDate=' + endDate;
			if (channelId != '0'){
				tableUrl += '&parentChannelId=' + channelId; 
			}
			showChart();
			$('#tt').datagrid({
				url:tableUrl
			});
		}
		function openTrend(name, value){
			var url = '${ctx}/visit/traffic/channel/trend/index/' + value + '?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue');
			$.ewcms.openWindow({windowId:"#pop-window",ifarmeId:'#editifr_pop',src:url,width:660,height:330,title: name + " 时间趋势"});
		}
		function channelChildren(id, name){
			channelId = id;
			channelIds.push(id);
			$('#explain').append('<span id="position_' +  id + '"> >> <a href="javascript:void(0);" style="text-decoration: none" onclick="positionSel(\'' + id + '\',\'' + name + '\')">' + name + '</a></span>');
			startDate = $('#startDate').datebox('getValue');
			endDate = $('#endDate').datebox('getValue');
			var parameter = {};
			parameter['startDate'] = startDate;
			parameter['endDate'] = endDate;
			parameter['parentChannelId'] = id;
			$.post('${ctx}/visit/traffic/channel/report', parameter, function(result) {
		  		var myChart = new FusionCharts('${ctx}/static/fcf/swf/Pie3D.swf?ChartNoDataText=无数据显示', 'myChartId', '640', '250','0','0');
	      		myChart.setDataXML(result);      
	      		myChart.render("divChart");
	   		});
			$('#tt').datagrid({
				url:'${ctx}/visit/traffic/channel/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue') + "&parentChannelId=" + id
			});
		}
		function positionSel(index, name){
			if (channelId == index) return false;
			if (index == "0"){
				window.location.reload();
			}else{
				var position = $.inArray(index, channelIds);
				if ((position > -1) && (position != channelIds.length -1)){
					for (var i = position; i < channelIds.length; i++){
						$('#position_' + channelIds[i]).remove();
					}
					channelIds.splice(position + 1);
				}
				channelChildren(index, name);
			}
		}
	</script>
	</body>
</html>