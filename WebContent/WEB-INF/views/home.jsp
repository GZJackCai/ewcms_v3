<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>EWCMS 站群内容管理平台</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
    <meta http-equiv="Cache-Control" content="no-store" />
    <meta http-equiv="Pragma" content="no-cache" />
    <meta http-equiv="Expires" content="0" />       
    <%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/views/home.css"/>
  </head>
  <body class="easyui-layout">
    <div data-options="region:'north', split:false" class="head">
      <table width="100%">
        <tr>
          <td width="189px" height="35" style="text-align: left">
            <table width="100%">
              <tr>
                <td><img src="${ctx}/static/image/top_bg_ewcms.gif" height="30px" border="0" style="border:0;padding-left:4px;padding-top:10px;"/></td>
              </tr>
              <tr><td align="center">网络站群内容管理系统V3.0</td></tr>
            </table>
          </td>
          <td>
            <div style="position:absolute;right:10px;text-align:right;top:10px;width:50%;">
              <span id="user-name" style="font-size:13px;font-weight: bold;"><shiro:principal property="realName"/></span>&nbsp;${siteName}&nbsp;欢迎你
               | 
              <span id="clock"></span>
              <img id="button-main" src="${ctx}/static/image/exit.png" width="17" height="17" style="border:0;cursor:pointer;cursor:hand;" align="top"/>
            </div>
            <div class="tabs-wrap" style="margin-top:36px;padding-left:58px;">
              <ul class="tabs">
                <li id="menu-index" class="tabs-selected"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">首页</span></a></li>
                <li id="menu-content"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">内容管理</span></a></li>
                <li id="menu-site"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">站点管理</span></a></li>
                <li id="menu-component"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">互动管理</span></a></li>
                <li id="menu-visit"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">统计分析</span></a></li>
                <li id="menu-report"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">报表管理</span></a></li>
                <li id="menu-crawler"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">采集管理</span></a></li>
                <li id="menu-scheduler"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">定时管理</span></a></li>
                <li id="menu-special"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">特殊管理</span></a></li>
                <li id="menu-security"><a href="javascript:void(0);" class="tabs-inner"><span class="tabs-title">权限管理</span></a></li>
              </ul>
            </div>
          </td>
          <td>
            <div style="position: absolute;right:70px;text-align:left;top:45px;width:15%;">
              <span id="tipMessage" style="color:red;font-size:13px;"></span>
            </div>
            <div class="bs" style="position: absolute;right: 30px;text-align: right;top:40px;width:10%;">
              <!-- <a class="styleswitch a5" style="cursor: pointer" title="浅灰色" rel="metro"></a> -->        
              <a class="styleswitch a4" style="cursor: pointer" title="灰色" rel="gray"></a>
              <a class="styleswitch a3" style="cursor: pointer" title="浅蓝色" rel="default"></a>        
              <!-- <a class="styleswitch a2" style="cursor: pointer" title="浅蓝色" rel="bootstrap"></a> -->
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
        <div id="west" region="west" split="false" title="子菜单项" style="width:165px;padding:1px;overflow:auto;">
          <div id="menusub-content" title="内容管理" style="overflow:auto;display:none;">
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'内容编辑',src:'${ctx}/content/document/article/tree'})">
                <img src="${ctx}/static/image/articleedit.png" style="border:0"/><br/>
                <span>内容编辑</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'回收站',src:'${ctx}/content/document/recyclebin/tree'})">
                <img src="${ctx}/static/image/recyclebin.png" style="border:0"/><br/>
                <span>回收站</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'共享库',src:'${ctx}/content/document/share/index'})">
                <img src="${ctx}/static/image/share.png" style="border:0"/><br/>
                <span>共享库</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'问卷调查',src:'${ctx}/content/vote/index'})">
                <img src="${ctx}/static/image/vote.png" style="border:0"/><br/>
                <span>问卷调查</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-content',title:'文章分类',src:'${ctx}/content/document/category/index'})">
                <img src="${ctx}/static/image/articlecategory.png" style="border:0"/><br/>
                <span>文章分类</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'备忘录',src:'${ctx}/content/notes/index'})">
                <img src="${ctx}/static/image/notes.png" style="border:0"/><br/>
                <span>备忘录</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-content', title:'个人消息',src:'${ctx}/content/message/index'})">
                <img src="${ctx}/static/image/message.png" style="border:0"/><br/>
                <span>个人消息</span>
              </a>
            </div>
          </div>
          <div id="menusub-site" title="站点管理" style="overflow:auto;display:none;">
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-site',title:'站点管理',src:'${ctx}/site/organ/index'})">
                <img src="${ctx}/static/image/site.png" style="border:0;"/><br/>
                <span>站点管理</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'专题与栏目',src:'${ctx}/site/channel/index'})">
                <img src="${ctx}/static/image/channel.png" style="border:0;"/><br/>
                <span>栏目管理</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'模板管理',src:'${ctx}/site/template/index'})">
                <img src="${ctx}/static/image/template.png" style="border:0;"/><br/>
                <span>模板管理</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'模板资源管理',src:'${ctx}/site/template/source/index'})">
                <img src="${ctx}/static/image/template_resources.png" style="border:0;"/><br/>
                <span>模板资源管理</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'资源管理',src:'${ctx}/content/resource/index'})">
                <img src="${ctx}/static/image/resources.png" style="border:0"/><br/>
                <span>资源管理</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-site', title:'资源回收站',src:'${ctx}/content/resource/recycle/index'})">
                <img src="${ctx}/static/image/recyclebin.png" style="border:0"/><br/>
                <span>资源回收站</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-site',title:'历史记录',src:'${ctx}/content/historymodel/index'})">
                <img src="${ctx}/static/image/historymodel.png" style="border: 0" /><br />
                <span>历史记录</span>
              </a>
            </div>
          </div>
          <div id="menusub-component" title="互动管理" style="overflow:auto;display:none;">
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-component', title:'政民互动',src:'${ctx}/plguin/interaction/index'})">
                <img src="${ctx}/static/image/interaction.png" style="border:0"/><br/>
                <span>政民互动</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-component', title:'留言审核',src:'${ctx}/plguin/interaction/speak'})">
                <img src="${ctx}/static/image/speak.png" style="border:0"/><br/>
                <span>留言审核</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-component', title:'网上咨询',src:'${ctx}/plugin/online/advisor/index'})">
                <img src="${ctx}/static/image/advisor.png" style="border:0"/><br/>
                <span>网上咨询</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-system',title:'公民信息',src:'${ctx}/plugin/citizen/index'})">
                <img src="${ctx}/static/image/citizen.png" style="border:0"/><br/>
                <span>公民信息</span>
              </a>
            </div>
          </div>
          <div id="menusub-visit" title="统计分析" style="overflow:auto;display:none;">
            <div region="west" title='统计分析' split="true">
              <ul id="tt-visit"></ul>  
            </div>
          </div>
          <div id="menusub-report" title="报表管理" style="overflow:auto;display:none;">
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-report', title:'文字报表',src:'${ctx}/plugin/report/text/index'})">
                <img src="${ctx}/static/image/report_text.png" style="border:0"/><br/>
                <span>文字报表</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-report', title:'图形报表',src:'${ctx}/plugin/report/chart/index'})">
                <img src="${ctx}/static/image/report_chart.png" style="border:0"/><br/>
                <span>图型报表</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-report', title:'报表分类',src:'${ctx}/plugin/report/category/index'})">
                <img src="${ctx}/static/image/report_category.png" style="border:0"/><br/>
                <span>报表分类</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-report', title:'报表存储',src:'${ctx}/plugin/report/repository/index'})">
                <img src="${ctx}/static/image/report_repository.png" style="border:0"/><br/>
                <span>报表存储</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-report', title:'报表集',src:'${ctx}/plugin/report/show/index'})">
                <img src="${ctx}/static/image/report_show.png" style="border:0"/><br/>
                <span>报表集</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-report',title:'外部数据源',src:'${ctx}/plugin/externalds/index'})">
                <img src="${ctx}/static/image/report_ds.png" style="border:0"/><br/>
                <span>外部数据源</span>
              </a>
            </div>  
          </div>
          <div id="menusub-crawler" title="采集管理" style="overflow:auto;display:none;">
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-crawler',title:'内容采集',src:'${ctx}/crawler/content/index'})">
                <img src="${ctx}/static/image/crawler_content.png" style="border:0"/><br/>
                <span>内容采集</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-crawler',title:'资源采集',src:'${ctx}/crawler/resource/index'})">
                <img src="${ctx}/static/image/crawler_resource.png" style="border:0"/><br/>
                <span>资源采集</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-crawler',title:'存储库',src:'${ctx}/crawler/storage/index'})">
                <img src="${ctx}/static/image/crawler_storage.png" style="border:0"/><br/>
                <span>存储库</span>
              </a>
            </div>
          </div>
          <div id="menusub-scheduler" title="定时管理" style="overflow:auto;display:none;">
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-scheduler',title:'任务设置',src:'${ctx}/scheduling/jobinfo/index'})"> 
                <img src="${ctx}/static/image/scheduling_job.png" style="border: 0" /><br/>
                <span>任务设置</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-scheduler',title:'作业设置',src:'${ctx}/scheduling/jobclass/index'})"> 
                <img src="${ctx}/static/image/scheduling_jobclass.png" style="border: 0" /><br/>
                <span>作业设置</span>
              </a>
            </div>
          </div>
          <div id="menusub-special" title="特殊管理" style="overflow:auto;display:none;">
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-special',title:'审批备案',src:'${ctx}/content/particular/approvalrecord/index'})">
                <img src="${ctx}/static/image/particular_ar.png" style="border:0"/><br/>
                <span>审批备案</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-special',title:'行业编码',src:'${ctx}/content/particular/industrycode/index'})">
                <img src="${ctx}/static/image/particular_ic.png" style="border:0"/><br/>
                <span>行业编码</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-special',title:'行政区划',src:'${ctx}/content/particular/zoningcode/index'})">
                <img src="${ctx}/static/image/particular_zc.png" style="border:0"/><br/>
                <span>行政区划</span>
              </a>
            </div>
          </div>
          <div id="menusub-security" title="权限管理" style="overflow:auto;display:none;">
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-security', title:'权限列表',src:'${ctx}/security/permission/index'})">
                <img src="${ctx}/static/image/role.png" style="border:0;"/><br/>
                <span>权限列表</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-security',title:'用户组管理',src:'${ctx}/security/role/index'})">
                <img src="${ctx}/static/image/group.png" style="border:0;"/><br/>
                <span>用户组管理</span>
              </a>
            </div>
            <div class="nav-item">
              <a href="javascript:$.ewcms.openTab({id:'#tab-security',title:'用户管理',src:'${ctx}/security/user/index'})">
                <img src="${ctx}/static/image/user.png" style="border:0;"/><br/>
                <span>用户管理</span>
              </a>
            </div> 
          </div>
        </div>
        <div region="center" border="false" style="overflow:auto;">
          <div class="easyui-tabs" id="tab-content" fit="true" border="false" style="display: none;"></div>
            <div class="easyui-tabs" id="tab-site" fit="true" border="false" style="display: none;"></div>
            <div class="easyui-tabs" id="tab-component" fit="true" border="false" style="display: none;"></div>
            <div class="easyui-tabs" id="tab-visit" fit="true" border="false" style="display: none;"></div>
            <div class="easyui-tabs" id="tab-report" fit="true" border="false" style="display: none;"></div>
            <div class="easyui-tabs" id="tab-crawler" fit="true" border="false" style="display: none;"></div>
            <div class="easyui-tabs" id="tab-scheduler" fit="true" border="false" style="display: none;"></div>
            <div class="easyui-tabs" id="tab-special" fit="true" border="false" style="display: none;"></div>
            <div class="easyui-tabs" id="tab-security" fit="true" border="false" style="display: none;"></div>
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
                      <form:select id="createArticleYear" path="yearMap" onchange="_home.showCreateArticleChart($('#createArticleYear').val());">
                        <form:options items="${yearMap}"/>
                      </form:select>
                      <a href="javascript:void(0)" class="icon-reload" onclick="_home.showCreateArticleChart($('#createArticleYear').val())"></a>
                    </div>
                  </div>
                  <div style="height: 200px; padding: 5px;" closable="true" collapsible="false" class="portal-p panel-body">
                    <div id="chartDiv" align="center">新增文章数</div>
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
                      <form:select id="publishArticleYear" path="yearMap" onchange="_home.showPublishArticleChart($('#publishArticleYear').val())">
                        <form:options items="${yearMap}"/>
                      </form:select>
                      <a href="javascript:void(0)" class="icon-reload" onclick="_home.showPublishArticleChart($('#publishArticleYear').val())"></a>
                    </div>
                  </div>
                  <div style="height: 200px; padding: 5px;" closable="true" collapsible="false" class="portal-p panel-body">
                    <div id="lineDiv" align="center">发布文章数</div>
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
                      <form:select id="publishPersonYear" path="yearMap" onchange="_home.showPublishPersonChart($('#publishPersonYear').val())">
                        <form:options items="${yearMap}"/>
                      </form:select>
                      <a href="javascript:void(0)" class="icon-reload" onclick="_home.showPublishPersonChart($('#publishPersonYear').val())"></a>
                    </div>
                  </div>
                  <div style="height: 200px; padding: 5px;" closable="true" collapsible="false" class="portal-p panel-body">
                    <div id="pieDiv" align="center">发布文章人员</div>
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
                      <a href="javascript:void(0);" onclick=""></a>
                    </div>
                  </div>
                  <div id="todo" style="height: 170px; padding: 5px;" closable="true" collapsible="false" class="portal-p panel-body"></div>
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
                      <a id="tb-message-more" href="javascript:void(0);" style="display:inline;">更多...</a>
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
                      <a id="tb-message-more" href="javascript:void(0);" style="display:inline;">更多...</a>
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
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/fcf/js/FusionCharts.js"></script>
    <script type="text/javascript" src="${ctx}/static/views/home.js"></script>
    <script type="text/javascript" src="${ctx}/static/views/clock.js"></script>
    <script type="text/javascript" src="${ctx}/static/views/polling.js"></script>
    <script type="text/javascript">
      var _home = new Home();
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
          visitUrl : '${ctx}/visit',
          messageMoreUrl : '${ctx}/content/message/index'
        });
              
        var pollingUrl = '${ctx}/content/message/polling/<shiro:principal property="loginName"/>';
        var messageDetailUrl = '${ctx}/content/message/detail/index/<shiro:principal property="loginName"/>';
        var dotoUrl = "${ctx}/content/document/article/tree";
        var poll = new Poll(pollingUrl, messageDetailUrl, dotoUrl);
      });
    </script>
  </body>
</html>