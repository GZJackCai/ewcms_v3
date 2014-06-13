<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<title>专栏编辑</title>
		<%@ include file="../../taglibs.jsp" %>
		<script type="text/javascript" src="${ctx}/static/views/site/channel/edit.js"></script>
		<script type="text/javascript">
			var _channelEdit = new ChannelEdit();
			$(function(){
				_channelEdit.init({
					templateUrl : '${ctx}/site/channel/template/index?channelId=${channel.id}',
					templateSourceUrl : '${ctx}/site/channel/templatesource/index?channelId=${channel.id}',
					aclUrl : '${ctx}/site/channel/acl/index/${channel.id}',
					schedulingUrl : '${ctx}/scheduling/job/channel/edit/${channel.id}',
					infoUrl : '${ctx}/site/channel/editInfo?channelId=${channel.id}',
					processUrl : '${ctx}/content/document/process/index/${channel.id}'
				})
			});	
		</script>					
	</head>
	<body>
	  <c:choose>
	    <c:when test="${empty channel}">
			专栏是对建设网站的栏目进行管理的，在这可以定制网站所需的栏目
			<br/>
			双击专栏或左边弹出菜单编辑可以对专栏进行设置
		</c:when>							
		<c:otherwise>
			<div class="easyui-tabs" id="systemtab" border="false" fit="true">
				<div title="基本设置" style="padding: 2px;">
					<iframe id="editinfoifr" name="editinfoifr" class="editifr" scrolling="no"></iframe>
				</div>			
				<div title="专栏模板">
					<iframe id="edittplifr" name="edittplifr" class="editifr" scrolling="no"></iframe>	
				</div>
				<div title="专栏资源">
					<iframe id="editsrcifr" name="editsrcifr" class="editifr" scrolling="no"></iframe>	
				</div>					
				<div title="访问控制">
				    <iframe id="editauthifr" name="editauthifr" class="editifr" scrolling="no"></iframe> 
				</div>
				<div title="发布设置" style="padding: 2px;">
					<iframe id="editquartzifr" name="editquartzifr" class="editifr" scrolling="no"></iframe>		
				</div>	
 				<div title="审核流程">
 					<iframe id="editprocessifr" name="editprocessifr" class="editifr" scrolling="no"></iframe>
 				</div>																		
			</div>	
		</c:otherwise>
	</c:choose>
	</body>
</html>