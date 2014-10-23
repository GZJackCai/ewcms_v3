<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>数据源设置</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
	<div class="easyui-tabs" id="systemtab" border="false" fit="true">
	  <div title="JDBC数据源">
		<iframe id="editjdbcifr" name="editjdbcifr" class="editifr" scrolling="no"></iframe>
	  </div>			
	  <div title="JNDI数据源" >
		<iframe id="editjndiifr"  name="editjndiifr" class="editifr" scrolling="no"></iframe>	
	  </div>
	  <div title="BEAN数据源" >
		<iframe id="editbeanifr"  name="editbeanifr" class="editifr" scrolling="no"></iframe>	
	  </div>					
	  <div title="CUSTOM数据源" >
		<iframe id="editcustomifr"  name="editcustomifr" class="editifr" scrolling="no"></iframe> 
	  </div>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript">
		$(function(){
			$('#systemtab').tabs({
				onSelect:function(title){
					var jdbcurl='${ctx}/plugin/externalds/jdbc/index';
					var jndiurl='${ctx}/plugin/externalds/jndi/index';
					var beanurl='${ctx}/plugin/externalds/bean/index';
					var customurl='${ctx}/plugin/externalds/custom/index';
					if(title=="JDBC数据源"){
						$("#editjdbcifr").attr('src',jdbcurl);
					}
					if(title=="JNDI数据源"){
						$("#editjndiifr").attr('src',jndiurl);
					}
					if(title=="BEAN数据源"){
						$("#editbeanifr").attr('src',beanurl);
					}
					if(title=="CUSTOM数据源"){
						$("#editcustomifr").attr('src',customurl);
					}	
				}
			});
		});	
	</script>
  </body>
</html>