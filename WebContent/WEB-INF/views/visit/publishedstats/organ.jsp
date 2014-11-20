<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<html>
  <head>
	<title>组织机构发布统计</title>	
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
 	<script type="text/javascript" src="${ctx}/static/views/visit/dateutil.js"></script>
    <script type="text/javascript">
		$(function() {
			$('#startDate').datebox('setValue', dateTimeToString(new Date(new Date() - 30*24*60*60*1000)));
			$('#endDate').datebox('setValue', dateTimeToString(new Date()));
			$('#tt').treegrid({
				rownumbers: true,  
	            collapsible: true,
	            striped : true,
				url : '${ctx}/visit/publishedstats/organ/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
				idField:'id',
				treeField: 'text',
				columns:[[
				    {field:'id',title:'频道编号',width:60},
			        {field:'text',title:'频道名称',width:200},
			        {field:'draftCount',title:'初稿数',width:150,
			           	formatter : function(val, rec){
			            	if (rec.data != null)
			            	return rec.data.draftCount;
			            }	
			        },  
			        {field:'reeditCount',title:'重新编辑数',width:150,
			            formatter : function(val, rec){
			            	if (rec.data != null)
			            	return rec.data.reeditCount;
			            }	
			        },
			        {field:'reviewCount',title:'审核数',width:150,
			            formatter : function(val, rec){
			            	if (rec.data != null)
			            	return rec.data.reviewCount;
			            }	
			        },
		            {field:'releaseCount',title:'已发布数',width:150,
		            	formatter : function(val, rec){
		            		if (rec.data != null)
		            		return rec.data.releaseCount;
		            	}	
		            },
		            {field:'sumCount',title:'总数',width:150,
		            	formatter : function(val, rec){
		            		if (rec.data != null)
		            		return rec.data.draftCount + rec.data.reeditCount + rec.data.reviewCount + rec.data.releaseCount;
		            	}	
		            }
			        
			    ]]  
			});
		});
		function refresh(){
			$('#tt').treegrid({
				url : '${ctx}/visit/publishedstats/organ/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
			});
		}
	</script>
  </body>
</html>