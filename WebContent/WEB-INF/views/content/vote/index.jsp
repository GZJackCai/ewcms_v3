<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>调查投票列表</title>	
	<%@ include file="../../taglibs.jsp" %>
	<script type="text/javascript" src="${ctx}/static/views/content/vote/index.js"></script>
	<script type="text/javascript">
	    var _voteIndex = new VoteIndex('#tt2');
	    $(function(){
	    	_voteIndex.init({
	    		channelTreeUrl : '${ctx}/site/channel/tree',
	    		questionnaireUrl : '${ctx}/content/vote/questionnaire/index'
	    	});
	    })
		</script>
	</head>
	<body class="easyui-layout">
	    <div region="west" title='<label id="treeload-button" style="cursor:pointer;"><img src="${ctx}/static/image/refresh.png" style="vertical-align: middle;">专栏库</label>' split="true" style="width:180px;">
			<ul id="tt2"></ul>
		</div>  
	    <div region="center" style="padding:2px;" border="false">
	    	<iframe id="mainifr"  name="mainifr" class="editifr" frameborder="0" scrolling="no"></iframe>
	    	<!-- 
	    	<iframe id="mainifr" name="mainifr" class="mainifr" frameborder="0" scrolling="no" style="width:100%;height:47%;" style="padding:0px;"></iframe>
	    	<iframe id="subjectifr" name="subjectifr" class="subjectifr" frameborder="0" scrolling="no" style="width:100%;height:53%;" style="padding:0px;"></iframe>
	    	 -->
	    </div>
	</body>
</html>