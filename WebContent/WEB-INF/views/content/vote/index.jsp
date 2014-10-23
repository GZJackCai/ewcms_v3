<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>调查投票列表</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body class="easyui-layout">
	<div region="west" title='<label id="treeload-button" style="cursor:pointer;"><img src="${ctx}/static/image/refresh.png" style="vertical-align: middle;">专栏库</label>' split="true" style="width:180px;">
	  <ul id="tt2"></ul>
    </div>  
	<div region="center" style="padding:2px;" border="false">
	  <iframe id="mainifr"  name="mainifr" class="editifr" frameborder="0" scrolling="no"></iframe>
    </div>
    <%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
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
	</body>
</html>