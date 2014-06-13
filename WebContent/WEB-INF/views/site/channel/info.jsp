<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>专栏信息设置</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/site/channel/info.js"></script>
	<script type="text/javascript"> 
	    var _channelInfo = new ChannelInfo();
	   	$(function() {
	   		<c:choose>
	   		  <c:when test="${empty channel.appChannel}">
	   		    $('#span-viewconnect').hide();
	   		  </c:when>
	   		  <c:otherwise>
	   		    $('#span-viewconnect').show();
	   		  </c:otherwise>
	   		</c:choose>
	   		
	   		_channelInfo.init({
	   			pinyinUrl : '${ctx}/site/channel/pinYin',
	   			referImageUrl : '${ctx}/resource/insert?type=image&multi=false',
	   			clearImageUrl : '${ctx}/static/image/article/nopicture.jpg',
	   			connectUrl : '${ctx}/site/channel/connect/${channel.id}',
	   			disConnectUrl : '${ctx}/site/channel/disConnect/${channel.id}',
	   			viewConnectUrl : '${ctx}/site/channel/apply/index/${channel.id}'
	   		});
	   	});
	</script>
  </head>
  <body>
    <%@ include file="../../alertMessage.jsp" %>
	<form:form action="${ctx}/site/channel/saveInfo" method="post" enctype="multipart/form-data" modelAttribute="channel" class="form-horizontal">
	  <fieldset>
	  <legend><span style="color:red;">栏目信息</span></legend>
	  <table  class="formtable" align="center">
		<tr>
		  <td width="20%">专栏(${channel.id})：</td>
		  <td width="80%"><form:checkbox id="publicenable" path="publicenable" value="${channel.publicenable}"/><label for="publicenable">是否发布</label></td>
		</tr>
		<c:choose>
		<c:when test="${empty channel.parent}">
		<tr>
		  <td>站点名称：</td>
		  <td>${channel.site.siteName}&nbsp;<a href="${channel.site.siteURL}" target="_blank"/>预览</a></td>
		</tr>
		<tr>
		  <td>站点目录名：</td>
		  <td>${channel.site.siteRoot}<input type="hidden" id="site.siteRoot" name="site.siteRoot" value="${channel.site.siteRoot}"/></td>				
		</tr>	
		<tr>
		  <td>站点URL：</td>
		  <td>${channel.site.siteURL}</td>				
		</tr>														
		</c:when>
		<c:otherwise>
		<tr>
		  <td>专栏类型：</td>
		  <td><form:select id="type" path="type"><form:options itemLabel="description"/></form:select></td>
		</tr>													
		<tr>
		  <td>专栏访问相对地址：</td>
		  <td>${channel.absUrl}</td>
		</tr>																
		<tr>
		  <td>专栏名称：</td>
		  <td>${channel.name}<input type="hidden" id="name" name="name" value="${channel.name}"/>&nbsp;
		  <shiro:hasPermission name="acl:updatec:${channel.id}">
		  <a class="easyui-linkbutton" id="pinyin" iconCls="icon-pinyin" href="javascript:void(0);" onclick="pinYin();">名称转拼音</a>
		  </shiro:hasPermission>
		  </td>
		</tr>
		<tr>
		  <td>专栏目录：</td>
		  <td><input type="text" id="dir" name="dir" value="${channel.dir}" size="60"/></td>
		</tr>	
		<tr>
		  <td>被其他栏目引用：</td>
		  <td>
		    <span id="span-connect"><c:choose><c:when test="${empty channel.appChannel}">已断开</c:when><c:otherwise>已建立</c:otherwise></c:choose></span>&nbsp;
		    <shiro:hasPermission name="acl:updatec:${channel.id}">
		    <a class="easyui-linkbutton" id="connect" icon="icon-connect" href="javascript:void(0);">重建</a>
		    <a class="easyui-linkbutton" id="disConnect" icon="icon-disconnect"  href="javascript:void(0);">断开</a>
		    </shiro:hasPermission>
		    <span id="span-viewconnect"><a class="easyui-linkbutton" id="viewConnect" href="javascript:void(0);">查看</a></span>
		  </td>
	    </tr>
		<tr>
		  <td>专栏URL：</td>
		  <td><input type="text" id="url" name="url" value="${channel.url}" size="60"/></td>
		</tr>		
		<tr>
		  <td>列表页最大文档数：</td>
		  <td><input type="text" id="listSize" name="listSize" size="10" value="${channel.listSize}"/></td>
		</tr>
		<tr>
		  <td>最大显示文档数：</td>
		  <td><input type="text" id="maxSize" name="maxSize" size="10" value="${channel.maxSize}"/></td>
		</tr>
		<tr>
		  <td>专栏介绍：</td>
		  <td><textarea cols="46" id="describe" name="describe">${channel.describe}</textarea></td>
		</tr>																																					
		</c:otherwise>
		</c:choose>
		<tr>
		  <td>引导图：</td>
		  <td>
		    <shiro:hasPermission name="acl:updatec:${channel.id}">
		      <a href="javascript:void(0);" id="referImage" style="text-decoration:none;">
		    </shiro:hasPermission>
			<input type="hidden" id="iconUrl" name="iconUrl" value="${channel.iconUrl}"/>
			<c:choose>
			<c:when test="${not empty channel.iconUrl && channel.iconUrl != ''}">
			<img id="viewImage" name="viewImage" width="120px" height="90px" src="${ctx}/${channel.iconUrl}"/>
			</c:when>
			<c:otherwise>
			<img id="viewImage" name="viewImage" width="120px" height="90px" src="${ctx}/static/image/article/nopicture.jpg"/>
			</c:otherwise>
			</c:choose>
			<shiro:hasPermission name="acl:updatec:${channel.id}">
			  </a>
			  <a class="easyui-linkbutton" id="clearImage" href="javascript:void(0)" icon="icon-clearimage" style="vertical-align:bottom;">清除图片</a>
			</shiro:hasPermission>
		  </td>				
		</tr>
		<shiro:hasPermission name="acl:updatec:${channel.id}">
		<tr>
		  <td colspan="2" style="padding:0;">
			<div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
			  <a class="easyui-linkbutton" icon="icon-save" href="javascript:document.forms[0].submit();">保存</a>
			  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:document.forms[0].reset();">重置</a>
			</div>								
		  </td>
		</tr>
		</shiro:hasPermission>																																																								
	  </table>
	  <input type="hidden" id="id" name="id" value="${channel.id}"/>
	  </fieldset>					
	</form:form>
	<div id="insert-window" class="easyui-window" closed="true" icon="icon-save" title="插入" style="display:none;">
      <div class="easyui-layout" fit="true">
      	<div region="center" border="false">
    	  <iframe src="" id="uploadifr_insert_id"  name="uploadifr_insert" class="editifr" scrolling="no"></iframe>
        </div>
        <div region="south" border="false" style="text-align:right;height:30px;line-height:30px;padding:3px 6px;">
          <a class="easyui-linkbutton" id="insertIcon" icon="icon-save" href="javascript:void(0)">插入</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#insert-window').window('close');return false;">取消</a>
        </div>
      </div>
    </div>
    <div id="connect-window" class="easyui-window" title="查看被引用栏目" icon="icon-save" closed="true" style="display:none;">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
       	  <iframe id="editifr_pop"  name="editifr_pop" class="editifr" frameborder="0" width="100%" height="100%" scrolling="no"></iframe>
        </div>
      </div>
    </div>	
  </body>
</html>