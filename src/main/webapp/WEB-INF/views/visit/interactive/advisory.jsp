<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<html>
  <head>
	<title>网上咨询统计</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="north" style="height:40px" border="false">
	  <table width="100%" border="0" cellspacing="6" cellpadding="0"style="border-collapse: separate; border-spacing: 6px;">
		<tr>
		  <td>从 <input type="text" id="startDate" name="startDate" class="easyui-datebox" style="width:120px" editable="false" required="required"/> 至 <input type="text" id="endDate" name="endDate" class="easyui-datebox" style="width:120px" editable="false" required="required"/> <a class="easyui-linkbutton" href="javascript:void(0)" onclick="refresh();return false;">查看</a></td>
		</tr>
	  </table>
	</div>
	<div region="center">
	  <table id="tt" fit="true" border="false"></table>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript">
		$(function() {
			$('#tt').treegrid({
				rownumbers: true,  
	            collapsible: true,
	            striped : true,
				url : '${ctx}/visit/interactive/advisory/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
				idField:'id',
				treeField: 'text',
				columns:[[
						{field:'id',title:'组织编号',rowspan:2,width:60},
						{field:'text',title:'组织名称',rowspan:2,width:200},
			            {field:'tgCount',title:'已通过',width:60,
			            	formatter : function(val, rec){
			            		if (rec.data != null)
			            		return rec.data.tgCount;
			            	}	
			            },  
			            {field:'wtgCount',title:'未通过',width:60,
			            	formatter : function(val, rec){
			            		if (rec.data != null)
			            		return rec.data.wtgCount;
			            	}	
			            },
			            {field:'sumCount',title:'总数',rowspan:2,width:80,
			            	formatter : function(val, rec){
			            		if (rec.data != null)
			            		return rec.data.tgCount + rec.data.wtgCount;
			            	}	
			            }
				]]
			});
		});
		function refresh(){
			$('#tt').treegrid({
				url : '${ctx}/visit/interactive/advisory/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
			});
		}
	</script>
  </body>
</html>