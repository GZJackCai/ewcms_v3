<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>全站点击量</title>    
    <%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
    <div region="north" style="height:310px" border="false">
      <table width="100%" border="0" cellspacing="6" cellpadding="0"style="border-collapse: separate; border-spacing: 6px;">
        <tr>
          <td>从 <input type="text" id="startDate" name="startDate" class="easyui-datebox" style="width:120px" required/> 至 <input type="text" id="endDate" name="endDate" class="easyui-datebox" style="width:120px" required/> <a class="easyui-linkbutton" href="javascript:void(0)" onclick="refresh();return false;">查看</a></td>
        </tr>
        <tr valign="top">
          <td>
            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="blockTable">
              <tr>
                <td style="padding:0px;">
                  <div style="height: 100%;margin:0px;">
                    <div id="divChart" style="width:640px;height:250px;background-color:white;"></div>
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
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/views/visit/dateutil.js"></script>
    <script type="text/javascript" src="${ctx}/static/fcf/js/FusionCharts.js"></script>
    <script type="text/javascript">
        var startDate = dateTimeToString(new Date(new Date() - 30*24*60*60*1000));
        var endDate = dateTimeToString(new Date());
        $(function() {
        	showChart();
        	
            $('#startDate').datebox('setValue', startDate);
            $('#endDate').datebox('setValue', endDate);
            $('#tt').datagrid({
                singleSelect : true,
                pagination : true,
                nowrap : true,
                striped : true, 
                rownumbers : true,
                pageSize:30,
                url : '${ctx}/visit/totality/siteclick/table?startDate=' + $('#startDate').datebox('getValue') + '&endDate=' + $('#endDate').datebox('getValue'),
                columns:[[  
                        {field:'visitDate',title:'日期',width:120},  
                        {field:'sumPv',title:'PV数量',width:100},  
                        {field:'countUv',title:'UV数量',width:100},
                        {field:'countIp',title:'IP数量',width:100},
                        {field:'countRv',title:'回头客人数',width:100},
                        {field:'avgTime',title:'页均停留时间',width:100}
                ]]  
            });
        });
        function showChart(){
            var parameter = {};
            parameter['startDate'] = startDate;
            parameter['endDate'] = endDate;
            parameter['labelCount'] = 8;
            $.post('${ctx}/visit/totality/siteclick/report', parameter, function(result) {
                  var myChart = new FusionCharts('${ctx}/static/fcf/swf/MSLine.swf?ChartNoDataText=无数据显示', 'myChartId', '640', '250','0','0');
                  myChart.setDataXML(result);      
                  myChart.render("divChart");
               });
        }
        function refresh(){
            startDate = $('#startDate').datebox('getValue');
            endDate = $('#endDate').datebox('getValue');
            showChart();
            $('#tt').datagrid({
                url:'${ctx}/visit/totality/siteclick/table?startDate=' + startDate + '&endDate=' + endDate
            });
        }
    </script>
    </body>
</html>