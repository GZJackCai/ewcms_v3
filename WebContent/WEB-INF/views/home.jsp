<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>EWCMS 站群内容管理平台</title>
		<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
		<meta http-equiv="Cache-Control" content="no-store" />
		<meta http-equiv="Pragma" content="no-cache" />
		<meta http-equiv="Expires" content="0" />       
		<%@ include file="taglibs.jsp" %>
        <link rel="stylesheet" type="text/css" href="${ctx}/static/views/home.css"/>
        <script type="text/javascript" src="${ctx}/static/views/home.js"></script>
        <script type="text/javascript" src="${ctx}/static/fcf/js/FusionCharts.js"></script>
        <script type="text/javascript" src="${ctx}/static/views/ewcms.pubsub.js"></script>
        <script type="text/javascript" src="${ctx}/static/views/polling.js"></script>
        <script type="text/javascript">
            var _home = new home();
            $(function(){
            	var clock = new Clock();
            	clock.display(document.getElementById('clock'));
            	        
            	_home.init({
                    user:'${ctx}/security/user/info/<shiro:principal property="loginName"/>',
                    password:'${ctx}/security/user/password',
                    exit:'${ctx}/logout',
                    siteswitch:'${ctx}/siteSwitchIndex',
                    progress:'${ctx}/progress',
                    hasSite:'${hasSite}',
                    visitTreeUrl : '${ctx}/visit/tree',
                    visitUrl : '${ctx}/visit'
                });
            	
            	var currentDate = new Date();
            	showPublishArticleChart(currentDate.getFullYear());
            	showCreateArticleChart(currentDate.getFullYear());
            	showPublishPersonChart(currentDate.getFullYear());
            	
        		var poll = new Poll('<shiro:principal property="loginName"/>');
            });
        </script>
    </head>
    <body class="easyui-layout">
      <div region="north" split="false" class="head">
        <table width="100%">
          <tr>
        	<td width="189px" height="35" style="text-align: left">
        	  <table width="100%">
        		<tr>
        		  <td>
        			<img src="${ctx}/static/image/top_bg_ewcms.gif" height="30px" border="0" style="border:0;padding-left:4px;padding-top:10px;"/>
        		  </td>
        		</tr>
        		<tr>
        		  <td align="center">网络站群内容管理系统V3.0</td>
        		</tr>
        	  </table>
        	</td>
        	<td>
        	  <div style="position: absolute;right: 10px;text-align: right;top: 5px;width: 50%;">
        		<span id="user-name" style="font-size:13px;font-weight: bold;"><shiro:principal property="realName"/></span>&nbsp;${siteName}&nbsp;欢迎你
        		| 
        		<span id="clock"></span>
        		<a id="button-main" href="javascript:void(0);" style="border:0;padding:0;"><img src="${ctx}/static/image/exit.png" width="17" height="17" style="border:0;"/></a>
        	  </div>
        	  <div class="tabs-wrap" style="margin-top:36px;padding-left:58px;">
        	    <ul class="tabs">
        	      <li id="menu-index" class="tabs-selected"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">首页</span></a></li>
        	      <li id="menu-content"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">内容管理</span></a></li>
				  <li id="menu-site"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">站点管理</span></a></li>
				  <li id="menu-component"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">互动管理</span></a></li>
				  <li id="menu-visit"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">统计分析</span></a></li>
				  <li id="menu-crawler"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">采集管理</span></a></li>
				  <li id="menu-system"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">系统管理</span></a></li>
        	    </ul>
			  </div>
   			</td>
   			<td>
   			  <div style="position: absolute;right: 70px;text-align: left;top:40px;width:15%;">
			    <span id="tipMessage" style="color:red;font-size:13px;"></span>
			  </div>
   			  <div class="bs" style="position: absolute;right: 30px;text-align: right;top:40px;width:10%;">
                <a class="styleswitch a5" style="cursor: pointer" title="灰色" rel="metro"></a>		
			    <a class="styleswitch a4" style="cursor: pointer" title="灰色" rel="gray"></a>
			    <a class="styleswitch a3" style="cursor: pointer" title="黑色" rel="default"></a>		
			    <a class="styleswitch a2" style="cursor: pointer" title="浅蓝色" rel="bootstrap"></a>
			    <a class="styleswitch a1" style="cursor: pointer" title="黑色" rel="black"></a>   				
			  </div>
   			</td>
          </tr>
        </table>
        <div id="mm" class="easyui-menu" style="width:120px;display:none;">
          <div  id="switch-menu" iconCls="icon-switch">站点切换</div>
          <div class="menu-sep"></div>
          <div id="user-menu" iconCls="icon-edit">修改用户信息</div>
          <div id="password-menu" iconCls="icon-password">修改密码</div>
          <div class="menu-sep"></div>
          <div id="progress-menu">发布进度</div>
          <div class="menu-sep"></div>
          <div id="exit-menu" iconCls="icon-exit">退出</div>
        </div>
      </div>
      <div region="center" border="true" style="overflow:auto;">
        <div id="center" class="easyui-layout" fit="true" style="display:none;">  
          <div id="west" region="west" split="false" title="子菜单项" style="width:130px;padding:1px;overflow:auto;">
	        <div id="menusub-content" title="内容管理" style="overflow:auto;display:none;">
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'内容编辑',src:'content/document/article/tree'})">
                  <img src="${ctx}/static/image/articleedit.png" style="border:0"/><br/>
                  <span>内容编辑</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'回收站',src:'content/document/recyclebin/tree'})">
                  <img src="${ctx}/static/image/recyclebin.png" style="border:0"/><br/>
                  <span>回收站</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'共享库',src:'content/document/share/index'})">
                  <img src="${ctx}/static/image/share.png" style="border:0"/><br/>
                  <span>共享库</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'问卷调查',src:'content/vote/index'})">
                  <img src="${ctx}/static/image/vote.png" style="border:0"/><br/>
                  <span>问卷调查</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'备忘录',src:'content/notes/index'})">
                  <img src="${ctx}/static/image/notes.png" style="border:0"/><br/>
                  <span>备忘录</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'个人消息',src:'content/message/index'})">
                  <img src="${ctx}/static/image/message.png" style="border:0"/><br/>
                  <span>个人消息</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'Comet',src:'chat/index'})">
                  <img src="${ctx}/static/image/message.png" style="border:0"/><br/>
                  <span>Comet</span>
                </a>
              </div>
	        </div>
	        <div id="menusub-site" title="站点管理" style="overflow:auto;display:none;">
	          <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-site',title:'站点管理',src:'site/organ/index'})">
                  <img src="${ctx}/static/image/site.png" style="border:0;"/><br/>
                  <span>站点管理</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'专题与栏目',src:'site/channel/index'})">
                  <img src="${ctx}/static/image/channel.png" style="border:0;"/><br/>
                  <span>栏目管理</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'模板管理',src:'site/template/index'})">
                  <img src="${ctx}/static/image/template.png" style="border:0;"/><br/>
                  <span>模板管理</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'模板资源管理',src:'site/template/source/index'})">
                  <img src="${ctx}/static/image/template_resources.png" style="border:0;"/><br/>
                  <span>模板资源管理</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'资源管理',src:'content/resource/index'})">
                  <img src="${ctx}/static/image/resources.png" style="border:0"/><br/>
                  <span>资源管理</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'资源回收站',src:'content/resource/recycle/index'})">
                  <img src="${ctx}/static/image/recyclebin.png" style="border:0"/><br/>
                  <span>资源回收站</span>
                </a>
              </div>
	        </div>
	        <div id="menusub-component" title="互动管理" style="overflow:auto;display:none;">
	          <div class="nav-item">
                <a href="javascript:_home.addTab('#tab-component','政民互动','plguin/interaction/index')">
                  <img src="${ctx}/static/image/interaction.png" style="border:0"/><br/>
                  <span>政民互动</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:_home.addTab('#tab-component','留言审核','plguin/interaction/speak')">
                  <img src="${ctx}/static/image/speak.png" style="border:0"/><br/>
                  <span>留言审核</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:_home.addTab('#tab-component','网上咨询','plugin/online/advisor/index')">
                  <img src="${ctx}/static/image/advisor.png" style="border:0"/><br/>
                  <span>网上咨询</span>
                </a>
              </div>
	        </div>
	        <div id="menusub-visit" title="统计分析" style="overflow:auto;display:none;">
	          <div region="west" title='统计分析' split="true">
		        <ul id="tt-visit"></ul>  
	          </div>
			</div>
	        <div id="menusub-crawler" title="采集管理" style="overflow:auto;display:none;">
	          <div class="nav-item">
                <a href="javascript:_home.addTab('#tab-crawler','内容采集','crawler/content/index')">
                  <img src="${ctx}/static/image/crawler_content.png" style="border:0"/><br/>
                  <span>内容采集</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:_home.addTab('#tab-crawler','资源采集','crawler/resource/index')">
                  <img src="${ctx}/static/image/crawler_resource.png" style="border:0"/><br/>
                  <span>资源采集</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:_home.addTab('#tab-crawler','存储库','crawler/storage/index')">
                  <img src="${ctx}/static/image/crawler_storage.png" style="border:0"/><br/>
                  <span>存储库</span>
                </a>
               </div>
	        </div>
	        <div id="menusub-system" title="系统管理" style="overflow:auto;display:none;">
	        <div id="mainmenu" class="easyui-accordion" style="overflow:auto;width:160px;height:auto;border: 0">
	          <div title="权限管理" style="overflow:auto">
	            <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system', title:'权限列表',src:'security/permission/index'})">
                    <img src="${ctx}/static/image/role.png" style="border:0;"/><br/>
                    <span>权限列表</span>
                  </a>
                </div>
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'用户组管理',src:'${ctx}/security/role/index'})">
                    <img src="${ctx}/static/image/group.png" style="border:0;"/><br/>
                    <span>用户组管理</span>
                  </a>
                </div>
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'用户管理',src:'${ctx}/security/user/index'})">
                    <img src="${ctx}/static/image/user.png" style="border:0;"/><br/>
                    <span>用户管理</span>
                  </a>
                </div> 
	          </div>
	          <div title="定时管理" style="overflow:auto">
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'任务设置',src:'${ctx}/scheduling/jobinfo/index'})"> 
                    <img src="${ctx}/static/image/scheduling_job.png" style="border: 0" /><br/>
                    <span>任务设置</span>
                  </a>
                </div>
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'作业设置',src:'${ctx}/scheduling/jobclass/index'})"> 
                    <img src="${ctx}/static/image/scheduling_jobclass.png" style="border: 0" /><br/>
                    <span>作业设置</span>
                  </a>
                </div>
              </div>
              <div title="基本信息" style="overflow:auto">
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'历史记录',src:'${ctx}/content/historymodel/index'})">
                    <img src="${ctx}/static/image/historymodel.png" style="border: 0" /><br />
                    <span>历史记录</span>
                  </a>
                </div>
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'文章分类',src:'${ctx}/content/document/category/index'})">
                    <img src="${ctx}/static/image/articlecategory.png" style="border:0"/><br/>
                    <span>文章分类</span>
                  </a>
                </div>
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'公民信息',src:'${ctx}/plugin/citizen/index'})">
                    <img src="${ctx}/static/image/citizen.png" style="border:0"/><br/>
                    <span>公民信息</span>
                  </a>
                </div>
              </div>
              <div title="报表管理" style="overflow:auto">
                <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-system', title:'文字报表',src:'${ctx}/plugin/report/text/index'})">
                  <img src="${ctx}/static/image/report_text.png" style="border:0"/><br/>
                  <span>文字报表</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-system', title:'图形报表',src:'${ctx}/plugin/report/chart/index'})">
                  <img src="${ctx}/static/image/report_chart.png" style="border:0"/><br/>
                  <span>图型报表</span>
                </a>
       	      </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-system', title:'报表分类',src:'${ctx}/plugin/report/category/index'})">
                  <img src="${ctx}/static/image/report_category.png" style="border:0"/><br/>
                  <span>报表分类</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-system', title:'报表存储',src:'${ctx}/plugin/report/repository/index'})">
                  <img src="${ctx}/static/image/report_repository.png" style="border:0"/><br/>
                  <span>报表存储</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-system', title:'报表集',src:'${ctx}/plugin/report/show/index'})">
                  <img src="${ctx}/static/image/report_show.png" style="border:0"/><br/>
                  <span>报表集</span>
                </a>
              </div>
              <div class="nav-item">
                <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'外部数据源',src:'${ctx}/plugin/externalds/index'})">
                  <img src="${ctx}/static/image/report_ds.png" style="border:0"/><br/>
                  <span>外部数据源</span>
                </a>
              </div>
              </div>
              <div title="特殊信息" style="overflow:auto">
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'审批备案',src:'${ctx}/content/particular/approvalrecord/index'})">
                    <img src="${ctx}/static/image/particular_ar.png" style="border:0"/><br/>
                    <span>审批备案</span>
                  </a>
                </div>
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'行业编码',src:'${ctx}/content/particular/industrycode/index'})">
                    <img src="${ctx}/static/image/particular_ic.png" style="border:0"/><br/>
                    <span>行业编码</span>
                  </a>
                </div>
                <div class="nav-item">
                  <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'行政区划',src:'${ctx}/content/particular/zoningcode/index'})">
                    <img src="${ctx}/static/image/particular_zc.png" style="border:0"/><br/>
                    <span>行政区划</span>
                   </a>
                 </div>
	           </div>
	         </div>
	        </div>
	      </div>
	      <div region="center" border="false" style="overflow:auto;">
	      	<div class="easyui-tabs" id="tab-content" fit="true" border="false" style="display: none;"></div>
	      	<div class="easyui-tabs" id="tab-site" fit="true" border="false" style="display: none;"></div>
	      	<div class="easyui-tabs" id="tab-component" fit="true" border="false" style="display: none;"></div>
	      	<div class="easyui-tabs" id="tab-visit" fit="true" border="false" style="display: none;"></div>
	      	<div class="easyui-tabs" id="tab-crawler" fit="true" border="false" style="display: none;"></div>
	      	<div class="easyui-tabs" id="tab-system" fit="true" border="false" style="display: none;"></div>
      	  </div>
      	</div>
      	<div id="tab-index" style="overflow:auto;">
      	  <div style="margin-top:9px;">
            <center><h2>欢迎使用EWCMS企业网站内容管理系统</h2></center>
          </div>
          <table cellspacing="0" cellpadding="0" border="0" width="100%">
           	<tr>
          	  <td class="portal-column-td" width="32%">
                <div style="overflow:hidden;padding:0 0 0 0">
	        	  <div class="panel" style="margin-bottom:2px;">
	        	    <div class="panel-header">
	        		  <div class="panel-title">新增文章数</div>
	        		  <div class="panel-tool">
					    <form:select id="createArticleYear" path="yearMap" onchange="showCreateArticleChart($('#createArticleYear').val());">
					    	<form:options items="${yearMap}"/>
					    </form:select>
					    <a href="javascript:void(0)" class="icon-reload" onclick="showCreateArticleChart($('#createArticleYear').val())"></a>
					  </div>
	        		</div>
	        		<div style="height: 200px; padding: 5px;" closable="true" collapsible="false" class="portal-p panel-body">
	        		  <div id="chartDiv" align="center">新增文章数</div>
					  <script type="text/javascript">
						function showCreateArticleChart(value){
						  $.post("${ctx}/fcf/createArticle/" + value, {}, function(result) {
						    var myChart = new FusionCharts("${ctx}/static/fcf/swf/Column3D.swf?ChartNoDataText=无数据显示", "myChartId", "400", "200");
						    myChart.setDataXML(result);      
  						    myChart.render("chartDiv");
	                      });  
						}
						//showCreateArticleChart($('#createArticleYear').val());
					  </script>
				    </div>
				  </div>
        	    </div>
        	  </td>
        	  <td width="1%"></td>
              <td class="portal-column-td" width="32%">
                <div style="overflow:hidden;padding:0 0 0 0">
	        	  <div class="panel" style="margin-bottom:2px;">
	        	    <div class="panel-header">
	        		  <div class="panel-title">发布文章数</div>
	        		  <div class="panel-tool">
					    <form:select id="publishArticleYear" path="yearMap" onchange="showPublishArticleChart($('#publishArticleYear').val())">
					    	<form:options items="${yearMap}"/>
					    </form:select>
					    <a href="javascript:void(0)" class="icon-reload" onclick="showPublishArticleChart($('#publishArticleYear').val())"></a>
					  </div>
	        		</div>
	        		<div style="height: 200px; padding: 5px;" closable="true" collapsible="false" class="portal-p panel-body">
	        		  <div id="lineDiv" align="center">发布文章数</div>
					  <script type="text/javascript">
						function showPublishArticleChart(value){
						  $.post("${ctx}/fcf/publishArticle/" + value, {}, function(result) {
						    var myChart = new FusionCharts("${ctx}/static/fcf/swf/Line.swf?ChartNoDataText=无数据显示", "myChartId", "400", "200");
						    myChart.setDataXML(result);      
						    myChart.render("lineDiv");
						  });  
						}
					  </script>
				    </div>
				  </div>
        		</div>
        	  </td>
        	  <td width="1%"></td>
        	  <td class="portal-column-td" width="32%">
                <div style="overflow:hidden;padding:0 0 0 0">
	        	  <div class="panel" style="margin-bottom:2px;">
	        		<div class="panel-header">
	        		  <div class="panel-title">发布文章人员</div>
	        		  <div class="panel-tool">
						<form:select id="publishPersonYear" path="yearMap" onchange="showPublishPersonChart($('#publishPersonYear').val())">
					    	<form:options items="${yearMap}"/>
					    </form:select>
					    <a href="javascript:void(0)" class="icon-reload" onclick="showPublishPersonChart($('#publishPersonYear').val())"></a>
					  </div>
	        		</div>
	        		<div style="height: 200px; padding: 5px;" closable="true" collapsible="false" class="portal-p panel-body">
	        		  <div id="pieDiv" align="center">发布文章人员</div>
					  <script type="text/javascript">
						function showPublishPersonChart(value){
						  $.post("${ctx}/fcf/publishPerson/" + value, {}, function(result) {
							var myChart = new FusionCharts("${ctx}/static/fcf/swf/Pie3D.swf?ChartNoDataText=无数据显示", "myChartId", "400", "200");
							myChart.setDataXML(result);
							myChart.render("pieDiv");
						  });  
						}
					  </script>
				    </div>
				  </div>
        		</div>
        	  </td>
        	</tr>
            <tr>
              <td class="portal-column-td" width="32%">
                <div style="overflow:hidden;padding:0 0 0 0">
	        	  <div class="panel" style="margin-bottom:2px;">
	        		<div class="panel-header">
	        		  <div class="panel-title">待办事栏</div>
	        		  <div class="panel-tool">
	        		  	<a href="javascript:void(0);" onclick="" style="text-decoration:none;"></a>
	        		  	<a href="javascript:void(0)" class="icon-reload" onclick=""></a>
	        		  </div>
	        		</div>
	        		<div id="todo" style="height: 170px; padding: 5px;" closable="true" collapsible="false" id="other" class="portal-p panel-body"></div>
				  </div>
        		</div>
        	  </td>
        	  <td width="1%"></td>
              <td class="portal-column-td" width="32%">
                <div style="overflow:hidden;padding:0 0 0 0">
	        	  <div class="panel" style="margin-bottom:2px;">
	        		<div class="panel-header">
	        		  <div class="panel-title">公告栏</div>
	        		  <div class="panel-tool">
	        		  	<a href="javascript:void(0);" onclick="javascript:_home.addTab('公告栏信息','message/more/index?type=NOTICE');return false;" style="text-decoration:none;display:inline;">更多...</a>
	        		  	<a href="javascript:void(0)" class="icon-reload" onclick=""></a>
	        		  </div>
	        		</div>
	        		<div style="height: 170px; padding: 5px;" closable="true" collapsible="false" title="" id="notice" class="portal-p panel-body"></div>
				  </div>
        		</div>
        	  </td>
        	  <td width="1%"></td>
              <td class="portal-column-td" width="32%">
                <div style="overflow:hidden;padding:0 0 0 0">
	        	  <div class="panel" style="margin-bottom:2px;">
	        		<div class="panel-header">
	        		  <div class="panel-title">订阅栏</div>
	        		  <div class="panel-tool">
	        		  	<a href="javascript:void(0);" onclick="javascript:_home.addTab('订阅栏信息','message/more/index?type=SUBSCRIPTION');return false;" style="text-decoration:none;display:inline;">更多...</a>
	        		  	<a href="javascript:void(0)" class="icon-reload" onclick=""></a>
	        		  </div>
	        		</div>
	        		<div style="height: 170px; padding: 5px;" closable="true" collapsible="false" id="subscription" class="portal-p panel-body"></div>
   				  </div>
        		</div>
        	  </td>
        	</tr>
	      </table>
      	</div>
	  </div>
      <div id="edit-window" class="easyui-window" closed="true" icon="icon-winedit" style="display:none;">
        <div class="easyui-layout" fit="true">
          <div region="center" border="false" style="padding: 2px;" fit="true">
            <iframe id="editifr" name="editifr" class="editifr" frameborder="0" scrolling="auto"></iframe>
          </div>
        </div>
      </div>
      <div id="detail-window" class="easyui-window" icon="" closed="true" style="display:none;">
        <div class="easyui-layout" fit="true">
          <div region="center" border="false">
           	<iframe id="editifr_detail"  name="editifr_detail" frameborder="0" width="100%" height="100%" scrolling="auto" style="width:100%;height:100%;"></iframe>
          </div>
        </div>
      </div>
    </body>
</html>