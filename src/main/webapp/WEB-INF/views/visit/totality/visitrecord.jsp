<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<html>
  <head>
	<title>访问记录</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="center" split="true" style="padding:2px;" border="false">
	  <table id="tt" toolbar="#tb" fit="true"></table>
      <div id="tb" style="padding:5px;height:auto;">
        <div class="toolbar" style="margin-bottom:2px"></div>
        <div  style="padding-left:5px;">
          <form id="queryform"  style="padding: 0;margin: 0;">
                     访问日期：<input type="text" id="startDate" name="GTED_visitDate" class="easyui-datebox" style="width:120px"/>&nbsp;至&nbsp;<input type="text" id="endDate" name="LTED_visitDate" class="easyui-datebox" style="width:120px"/>&nbsp;
           <a id="tb-query" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',toggle:true">查询</a>
           <a id="tb-clear" href="javascript:document.forms[0].reset();" class="easyui-linkbutton" data-options="iconCls:'icon-clear'">清除</a>
          </form>
        </div>
      </div>		
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript" src="${ctx}/static/views/visit/dateutil.js"></script>
	<script type="text/javascript" src="${ctx}/static/fcf/js/FusionCharts.js"></script>
	<script type="text/javascript">
		$(function() {
			$('#tt').datagrid({
				singleSelect : true,
				pagination : true,
				nowrap : true,
				striped : true,
				rownumbers : true,
				fitColumns : true,
				pageSize:30,
				url : '${ctx}/visit/totality/visitrecord/table',
				columns:[[  
				        {field:'ip',title:'IP地址',width:100,
				            formatter : function(val, rec){
				            	return rec.visitPk.ip;
				            }	
				        },
				        {field:'remotePort',title:'源端口',width:100},
				        {field:'country',title:'地域',width:	150,
				            formatter : function(val, rec){
				            	var country = rec.country;
				            	var province = rec.province;
				            	var city = rec.city
				            	if (country == null) country = "";
				            	if (province == null) province = "";
				            	if (city == null) province = "";
				            	return country + " " + province + " " + city;
				            }	
				        },
				        {field:'url',title:'访问页面',width:350,
				            formatter : function(val, rec){
				            	if (rec.visitPk.url == null) return ''; 
				            	return '<a href="' + rec.visitPk.url + '" style="text-decoration: none" target="_blank">' + rec.visitPk.url + '</a>';
				            }	
				        },  
				        {field:'visitTime',title:'访问时间',width:145,
				            formatter : function(val, rec){
				            	return rec.visitPk.visitDate + ' ' + rec.visitTime;
				            }
				        },
				        {field:'referer',title:'来源URL',width:300,
				            formatter : function(val, rec){
				            	if (val == null) return ''; 
				            	return '<a href="' + val + '" style="text-decoration: none" target="_blank">' + val + '</a>';
				            }	
				        },
				        {field:'browser',title:'浏览器',width:80},
				        {field:'os',title:'操作系统',width:100},
				        {field:'screen',title:'屏幕大小',width:80},
				        {field:'language',title:'语言',width:100},
				        {field:'flashVersion',title:'Flash版本',width:70}
				    ]]  
				});
				
				$('#tb-query').bind('click', function(){
					$.ewcms.query({
			    		src : '${ctx}/visit/totality/table'
			    	});
				});
				
				$('#tb-clear').bind('click', function(){
					$('#queryform')[0].reset();
				  	$('#startDate').datebox('setValue','');
				   	$('#endDate').datebox('setValue','');
				});
			});
	</script>
  </body>
</html>