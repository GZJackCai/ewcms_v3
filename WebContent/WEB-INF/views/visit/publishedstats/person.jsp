<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<html>
  <head>
	<title>人员发布统计</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="north" style="height:40px" border="false">
	  <table width="100%" border="0" cellspacing="6" cellpadding="0"style="border-collapse: separate; border-spacing: 6px;">
		<tr>
		  <td>栏目名称：<select id="cc_channel" style="width:200px;" editable="false" required="required"></select>&nbsp;&nbsp;从 <input type="text" id="startDate" name="startDate" class="easyui-datebox" style="width:120px" editable="false" required="required"/> 至 <input type="text" id="endDate" name="endDate" class="easyui-datebox" style="width:120px" editable="false" required="required"/> <a class="easyui-linkbutton" href="javascript:void(0)" onclick="refresh();return false;">查看</a></td>
	    </tr>
	  </table>
	</div>
	<div region="center">
	  <table id="tt" fit="true" border="false"></table>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/visit/dateutil.js"></script>
	<script type="text/javascript">
		$(function() {
			$('#startDate').datebox('setValue', dateTimeToString(new Date(new Date() - 15*24*60*60*1000)));
			$('#endDate').datebox('setValue', dateTimeToString(new Date()));
			$('#tt').datagrid({
				singleSelect : true,
				pagination : false,
				nowrap : true,
				striped : true,
				rownumbers : true,
				url : '${ctx}/visit/publishedstats/person/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
				columns:[[  
				     {field:'organName',title:'所属机构',width:200},
				     {field:'userName',title:'用户',width:150,
				         formatter : function(val, rec){
				         	return rec.realName + "(" + rec.loginName + ")";
				         }	
				      },
				      {field:'draftCount',title:'初稿数',width:150},  
				      {field:'reeditCount',title:'重新编辑数',width:150},
				      {field:'reviewCount',title:'审核数',width:150},
				      {field:'releaseCount',title:'已发布数',width:150},
				      {field:'sumCount',title:'总数',width:150,
				          formatter : function(val, rec){
				              return rec.draftCount + rec.reeditCount + rec.reviewCount + rec.releaseCount;
				          }	
				      }
				]]  
			});
			$('#cc_channel').combotree({  
				url:'${ctx}/site/channel/tree',
				required:false,
				onClick : function(node){
				   	var rootnode = $('#cc_channel').combotree('tree').tree('getRoot');
				    if (node.id == rootnode.id){
				    	$('#cc_channel').combotree('setValue', '');
						return;
				    }
				   }
			});
		});
		function refresh(){
			$('#tt').datagrid({
				url : '${ctx}/visit/publishedstats/person/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue') + '&channelId=' + $('#cc_channel').combotree('getValue')
			});
		}
	</script>
  </body>
</html>