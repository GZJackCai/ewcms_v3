<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>文章点击排行</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="north" style="height:40px" border="false">
	  <table width="100%" border="0" cellspacing="6" cellpadding="0"style="border-collapse: separate; border-spacing: 6px;">
		<tr>
		  <td>从 <input type="text" id="startDate" name="startDate" class="easyui-datebox" style="width:120px" required/> 至 <input type="text" id="endDate" name="endDate" class="easyui-datebox" style="width:120px" required/> <select id="cc_channel" style="width:200px;"></select>&nbsp;<a class="easyui-linkbutton" href="javascript:void(0)" onclick="refresh();return false;">查看</a></td>
		</tr>
	  </table>
	</div>
    <div region="center">
	  <table id="tt" fit="true" border="false"></table>
    </div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/visit/dateutil.js"></script>
	<script type="text/javascript" src="${ctx}/static/fcf/js/FusionCharts.js"></script>
	<script type="text/javascript">
		var startDate = dateTimeToString(new Date(new Date() - 30*24*60*60*1000));
		var endDate = dateTimeToString(new Date());
		var channelId = 0;
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
				url : '${ctx}/visit/traffic/article/table?startDate=' + startDate + '&endDate=' + endDate,
			    columns:[[  
			            {field:'channelName',title:'栏目名称',width:150}, 
			            {field:'articleTitle',title:'标题',width:300,
			            	formatter : function(val, rec){
			            		return '<a href="${ctx}/' + rec.url + '" style="text-decoration: none" target="_blank">' + val + '</a>';
			            	}	
			            },
			            {field:'created',title:'创建者',width:100},  
			            {field:'sumPv',title:'点击量',width:100},
			            {field:'avgSt',title:'页均停留时间',width:100}
			    ]]  
			});
			$('#cc_channel').combotree({  
			    url:'${ctx}/site/channel/tree',
			    onClick : function(node){
			    	var rootnode = $('#cc_channel').combotree('tree').tree('getRoot');
			    	if (node.id == rootnode.id){
			    		$('#cc_channel').combotree('setValue', '');
			    		channelId = 0;
			    	}else{
			    		channelId = node.id;
			    	}
			    }
			});
		});
		function refresh(){
			tartDate = $('#startDate').datebox('getValue');
			endDate = $('#endDate').datebox('getValue');
			var tableUrl = '${ctx}/visit/traffic/article/table?startDate=' + startDate + '&endDate=' + endDate;
			if (channelId != '0'){
				tableUrl += '&parentChannelId=' + channelId; 
			}
			$('#tt').datagrid({
				url:tableUrl
			});
		}
	</script>
  </body>
</html>