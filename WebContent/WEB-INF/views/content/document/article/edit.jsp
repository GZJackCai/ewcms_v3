<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>文档编辑：</title>	
    <%@ include file="../../../taglibs.jsp"%>
	<link rel="stylesheet" type="text/css" href="${ctx}/static/views/content/document/article/article.css"></link>
	<script type="text/javascript" src="${ctx}/static/views/jquery.cookies.js"></script>
	<script type="text/javascript" src="${ctx}/static/tiny_mce/tiny_mce_gzip.js"></script>
	<script type="text/javascript" src="${ctx}/static/tiny_mce/config_gzip.js"></script>
	<script type="text/javascript" src="${ctx}/static/tiny_mce/config.js"></script>
	<script type="text/javascript" src="${ctx}/static/views/content/document/article/edit.js"></script>
	<script type="text/javascript" src="${ctx}/static/views/content/document/article/article-toolbar.js"></script>
	<script type="text/javascript">
		var _articleEdit = new ArticleEdit({
			loginName : '<shiro:principal property="loginName"/>',
			pages : ${pages},
			currentPage : 1,
			mask : ${mask},
			noImage : '${ctx}/static/image/article/nopicture.jpg',
			categoryUrl : '${ctx}/content/document/category/all?articleId=${article.id}',
			insertResourceUrl : '${ctx}/content/resource/insert',
			voteUrl : '${ctx}/content/document/article/vote',
			treeUrl : '${ctx}/site/channel/tree',
			editUrl : '${ctx}/content/document/article/edit/${channelId}',
			saveUrl : '${ctx}/content/document/article/save',
			approveUrl : '${ctx}/content/document/article/approve/${channelId}',
			historyUrl : '${ctx}/content/document/article/history/index/${article.id}',
			keyWordUrl : '${ctx}/content/document/article/keyword',
			summaryUrl : '${ctx}/content/document/article/summary',
			relationUrl : '${ctx}/content/document/relation/index/${article.id}'
		});
		$(function(){
			_articleEdit.init();
		});
	</script>
  </head>
  <body>
    <%@ include file="../../../alertMessage.jsp" %>
  	<form:form id="articleSave" action="${ctx}/content/document/article/save" method="post" modelAttribute="article" class="form-horizontal">
	  <div id="wrapper" >
		<table id="buttonBarTable" width="100%" border="0" cellpadding="0" cellspacing="0" style="border: #B7D8ED 1px solid;">
		  <tr>
			<td>
			  <div>
				<table width="100%">
				  <tr>
					<td width="100%" style="border: 0px solid;">
					  <a id="tb-create" class="easyui-linkbutton" iconCls="icon-article-create" href="javascript:void(0)">新建</a>
					  <a id="tb-save" class="easyui-linkbutton" iconCls="icon-article-save" href="javascript:void(0)">保存</a>
					  <a id="tb-approve" class="easyui-linkbutton" iconCls="icon-article-review" href="javascript:void(0)">审核</a>
				      <a id="tb-keyword" class="easyui-linkbutton" iconCls="icon-article-keyword" href="javascript:void(0)">提取关键字</a>
					  <a id="tb-summary" class="easyui-linkbutton" iconCls="icon-article-summary" href="javascript:void(0)">提取摘要</a>
					  <a id="tb-history" class="easyui-linkbutton" iconCls="icon-article-history" href="javascript:void(0)">历史</a>
					  <a id="tb-relation" class="easyui-linkbutton" iconCls="icon-article-relation" href="javascript:void(0)">相关文章</a>
					  <a id="tb-cookies" class="easyui-linkbutton" iconCls="icon-article-cookies" href="javascript:void(0)">常用项</a>
					  <a id="tb-show" class="easyui-linkbutton" iconCls="icon-article-show" href="javascript:void(0)"><span id="showHideLabel">展开</span></a>
					  <a id="tb-cancel" class="easyui-linkbutton" iconCls="icon-cancel" href="javascript:void(0)">关闭</a>
					</td>
				  </tr>
				</table>
			  </div>
			</td>
		  </tr>
		</table>
		<table id="inputBarTable" width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#F6F9FD" style="border: #B7D8ED 1px solid;" class="formtable" >
		  <tr>
			<td width="7%">标题：</td>
			<td width="93%" colspan="3">
			  <table width="100%" style="border: 0px solid;">
			    <tr>
			      <td id="tdTitle" colspan="2" style="border: 0px solid; padding-left:0px;">
			        <input type="text" id="title" name="title" class="inputtext" style="width:320px;background:url(${ctx}/static/image/article/rule.gif) repeat-x left bottom;" maxlength="50" value="${article.title}"/>
			        <input type="hidden" id="titleStyle" name="titleStyle" value="${article.titleStyle}"/>
				  </td>
				  <td style="vertical-align: middle;border: 0px solid;">
					<table width="100%" style="border: 0px solid;">
					  <tr>
				      	<td style="border: 0px solid;">
				      	  <input type="checkbox" value="checkbox" id="ShowShortTitle" onclick="$('#trShortTitle').toggle()" style="vertical-align: middle;"/><label for="ShowShortTitle">&nbsp;短标题</label>&nbsp;&nbsp;
					      <input type="checkbox" value="checkbox" id="ShowSubTitle" onclick="$('#trSubTitle').toggle()" style="vertical-align: middle;"/><label for="ShowSubTitle">&nbsp;副标题</label>&nbsp;&nbsp;&nbsp;&nbsp;
					    </td>
				      </tr>
				    </table>
			      </td>
			    </tr>
			  </table>
			</td>
		  </tr>
		  <tr id="trShortTitle" style="display:none;">
			<td width="7%">短标题：</td>
			<td width="93%" id="tdShortTitle" colspan="3">
			  <input type="text" id="shortTitle" name="shortTitle" class="inputtext" style="width:300px;background:url(${ctx}/static/image/article/rule.gif) repeat-x left bottom;" maxlength="25" value="${article.shortTitle}"/>
			  <input type="hidden" id="shortTitleStyle" name="shortTitleStyle" value="${article.shortTitleStyle}"/>
			</td>
		  </tr>
		  <tr id="trSubTitle" style="display:none;">
			<td width="7%">副标题：</td>
			<td width="93%" id="tdSubTitle" colspan="3">
			  <input type="text" id="subTitle" name="subTitle" class="inputtext" style="width:320px;background:url(${ctx}/static/image/article/rule.gif) repeat-x left bottom;" maxlength="50" value="${article.subTitle}"/>
			  <input type="hidden" id="subTitleStyle" name="subTitleStyle" value="${article.subTitleStyle}"/>
			</td>
		  </tr>
		  <tr>
			<td width="7%">内部标题：</td>
			<td width="43%"><input type="checkbox" id="inside" name="inside" value="${article.inside}"/><label for="inside">&nbsp;&nbsp;</label></td>
			<td width="7%">频道选择：</td>
			<td width="43%"><select id="cc_channel" style="width:200px;" required="true"/></td>
		  </tr>
		  <tr id="trShowHide_1" style="display:none">
			<td width="7%">发布日期：</td>
			<td width="43%"><input type="text" id="published" name="published" class="easyui-datetimebox" style="width:150px;" value="${article.published}"/></td>
			<td width="7%">责任编辑：</td>
			<td width="43%"><input type="text" id="author" name="author" size="30" value="${article.author}"/></td>
		  </tr>
		  <tr id="trShowHide_2" style="display:none">
			<td>文章类型：</td>
			<td><form:select id="genre" path="genre" items="${genreMap}" itemLabel="description" cssClass="easyui-combobox"  panelHeight="auto" panelWidth="200px" style="width:200px"/></td>
			<td>来源：</td>
			<td><input type="text" id="origin" name="origin" size="60" value="${article.origin}"/></td>
		  </tr>
		  <tr id="trShowHide_3" style="display:none">
			<td>关键字：</td>
			<td><input type="text" id="keyword" name="keyword" size="60" value="${article.keyword}"/></td>
			<td>Tag：</td>
			<td><input type="text" id="tag" name="tag" size="60" value="${article.tag}"/></td>
		  </tr>
		  <tr id="trShowHide_4" style="display:none">
			<td style="height:30px;vertical-align: middle;">文章选项：</td>
			<td><input type="checkbox" id="isComment" name="isComment" value="${article.isComment}"/><label for="comment">允许评论</label></td>
			<td style="height:30px;vertical-align: middle;">分类属性：</td>
			<td><input id="cc_categories" name="categoryList" style="width:200px;"/></td>
		  </tr>
          <tr id="trShowHide_5" style="display:none">
            <td>引用图片：</td>
			<td>
			  <a id="tb-referImage" href="javascript:void(0);" style="text-decoration:none;">
			  <c:choose>
			  <c:when test="${not empty article.image && article.image != ''}">
			    <img id="referenceImage" name="referenceImage" width="120px" height="90px" src="${ctx}/${article.image}"/>
			  </c:when>
			  <c:otherwise>
			    <img id="referenceImage" name="referenceImage" width="120px" height="90px" src="${ctx}/static/image/article/nopicture.jpg"/>
			  </c:otherwise>
			  </c:choose>
			  </a>
			  <input type="text" id="image" name="image" style="display:none" value="${article.image}"/>
			  <a id="tb-clearimage" class="easyui-linkbutton" href="javascript:void(0)" style="vertical-align:bottom;">清除图片</a>
	        </td>
			<td>摘要：</td>
			<td><textarea cols="46" id="summary" name="summary">${article.summary}</textarea></td>
		  </tr>
		  <tr id="tr_url" style="display:none">
		    <td>链接地址：</td>
		    <td colspan="3"><input type="text" id="url" name="url" size="120" value="${article.url}"/></td>
		  </tr>
		</table>
		<c:if test="${not empty article.contents}">
		<table id="table_content" width="100%" border="0" cellpadding="0" cellspacing="0" bgcolor="#F6F9FD" style="border: #B7D8ED 1px solid;border-collapse:collapse">
		  <tr>
			<td valign='top'>
			  <div id="_DivContainer" style="text-align: center; overflow: auto; height: 466px; width: 100%; background-color: #666666; position: relative">
			    <table id="_Table1" width="1000" border="0" cellpadding="10" bgcolor="#FFFFFF" style="margin: 5px auto;">
			  	  <tr>
			  		<td valign="top">
			  		  <div id="DivContent" align="center">
			  			<table id="tableContent" width="100%" height="100%" cellpadding="0" cellspacing="0">
			  			<c:forEach items="${article.contents}" varStatus="contentsStauts" var="contentsVar">
			  			<c:choose>
			  			<c:when test="${contentsStauts.count==1}">
			  			  <tr id="trContent_${contentsStauts.count}">
		  					<td>
		  					  <form:textarea id="_Content_${contentsStauts.count}" path="contents[${contentsStauts.index}].detail" cssClass="mceEditor"/>
		  					  <form:hidden path="contents[${contentsStauts.index}].id"/>
		  					  <input type="hidden" id="content${contentsStauts.index}.page" name="contents[${contentsStauts.index}].page" value="${contentsStauts.count}"/>
			  				</td>
			  			  </tr>
			  			</c:when>
			  			<c:otherwise>
			  			  <tr id="trContent_${contentsStauts.count}" style="display:none">
		  					<td>
		  					  <form:textarea id="_Content_${contentsStauts.count}" path="contents[${contentsStauts.index}].detail" cssClass="mceEditor"/>
		  					  <form:hidden path="contents[${contentsStauts.index}].id"/>
		  					  <input type="hidden" id="content${contentsStauts.index}.page" name="contents[${contentsStauts.index}].page" value="${contentsStauts.count}"/>
			  				</td>
			  			  </tr>
			  			</c:otherwise>
			  			</c:choose>
			  			</c:forEach>
						</table>
			  		  </div>
			  		</td>
			  	  </tr>
			  	</table>
			  </div>
			</td>
		  </tr>
		</table>
		</c:if>
	  </div>
	  <div id="pageBarDiv" style="padding-right: 230px;">
		<table width="100%" id="pageBarTable_general">
		  <tr>
			<td id="td_pageBar" valign="middle" bgcolor="#F7F8FD" class="pagetab" height="20" width="80%">
			  <ul id="pageList">
			  <c:if test="${not empty article.contents}">
				<li onclick="ArticleEdit.changePage('p1')" onmouseover="ArticleEdit.onOverPage('p1')" onmouseout="ArticleEdit.onOutPage('p1')" class="current" id="p1" name="tabs"><b>页 1</b></li>
			  </c:if>
			  </ul>
			  <span class="add"><a onclick="ArticleEdit.addPage();" href="#" alt="在当前页后插入"><img src="${ctx}/static/image/article/icon_plus.gif" border="0"/></a></span>
			  <span class="add"><a onclick="ArticleEdit.delPage();" href="#" alt="删除当前页"><img src="${ctx}/static/image/article/icon_minus.gif" border="0"/></a></span>
			</td>
			<td width="20%" height="20" valign="middle" align="right" bgcolor="#F7F8FD" class="pagetab" style="vertical-align: middle;">最后保存时间:<span id="saveTime_general"><c:if test="${not empty article.modified}">${article.modified}</c:if></span></td>
		  </tr>
		</table>
		<table width="100%" id="pageBarTable_title">
		  <tr>
			<td width="100%" height="20" valign="middle" bgcolor="#F7F8FD" class="pagetab" align="left" style="vertical-align: middle;">最后保存时间:<span id="saveTime_title"><c:if test="${not empty article.modified}">${article.modified}</c:if></span></td>
		  </tr>
		</table>
	  </div>
	  <input type="hidden" id="created" name="created" value="${article.created}"/>
	  <input type="hidden" id="status" name="status" value="${article.status}"/>
	  <input type="hidden" id="channelId" name="channelId" value="${channelId}"/>
	  <input type="hidden" id="id" name="id" value="${article.id}"/>
	  <input type="hidden" id="articleMainId" name="articleMainId" value="${articleMainId}"/>
	  <input type="hidden" id="mask" name="mask" value="${mask}"/>
    </form:form>
	<div id="pop-window" class="easyui-window" title="弹出窗口" icon="icon-save" closed="true">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
       	  <iframe id="editifr_pop"  name="editifr_pop" class="editifr" frameborder="0" width="100%" height="100%" scrolling="no"></iframe>
        </div>
      </div>
    </div>
	<div id="insert-window" class="easyui-window" closed="true" icon="icon-save" title="插入">
	  <input type="hidden" id="refence_img"/>
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="uploadifr_insert_id"  name="uploadifr_insert" class="editifr" scrolling="no"></iframe>
        </div>
        <div region="south" border="false" style="text-align:right;height:30px;line-height:30px;padding:3px 6px;">
          <a id="tb-insertfile" class="easyui-linkbutton" icon="icon-save" href="javascript:void(0)">插入</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#insert-window').window('close');return false;">取消</a>
        </div>
      </div>
    </div>	
	<div id="vote-window" class="easyui-window" title="问卷调查" icon="icon-save" closed="true">
      <div class="easyui-layout" fit="true">
        <div region="center" border="false">
          <iframe id="editifr_vote"  name="editifr_vote" class="editifr" frameborder="0" width="100%" height="100%" scrolling="no"></iframe>
        </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a id="tb-insertvote" class="easyui-linkbutton" icon="icon-ok" href="javascript:void(0)">选择</a>
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)" onclick="$('#vote-window').window('close');return false;">取消</a>
        </div>
      </div>
    </div>
    <div id="ewcms-cookies" class="easyui-window" closed="true" style="overflow:hidden;" icon="icon-cookies">
      <div class="easyui-layout" fit="true" >
	    <div region="center" border="false">
	      <table width="100%" border="0" cellpadding="0" cellspacing="0" style="border: #B7D8ED 1px solid;">
	        <tr align="center">
		      <td>可以在这里设置常用项，选择后可保存2个星期</td>
	        </tr>
		    <tr>
		      <td><input type="checkbox" value="checkbox" id="ewcms_1" onclick="ArticleEdit.setCookies(this,'trShowHide_1');" style="vertical-align: middle;"/><label for="ewcms_1">&nbsp;第二行显示——<font color='red'>【发布日期、作者、审核人】</font></label></td>
		    </tr>
		    <tr>
		      <td><input type="checkbox" value="checkbox" id="ewcms_2" onclick="ArticleEdit.setCookies(this,'trShowHide_2');" style="vertical-align: middle;"/><label for="ewcms_2">&nbsp;第三行显示——<font color='red'>【文章类型、来源】</font></label></td>
		    </tr>
		    <tr>
		      <td><input type="checkbox" value="checkbox" id="ewcms_3" onclick="ArticleEdit.setCookies(this,'trShowHide_3');" style="vertical-align: middle;"/><label for="ewcms_3">&nbsp;第四行显示——<font color='red'>【关键了、Tag】</font></label></td>
		    </tr>
		    <tr>
		      <td><input type="checkbox" value="checkbox" id="ewcms_4" onclick="ArticleEdit.setCookies(this,'trShowHide_4');" style="vertical-align: middle;"/><label for="ewcms_4">&nbsp;第四行显示——<font color='red'>【文章选项、分类属性】</font></label></td>
		    </tr>
		    <tr>
		      <td><input type="checkbox" value="checkbox" id="ewcms_5" onclick="ArticleEdit.setCookies(this,'trShowHide_5');" style="vertical-align: middle;"/><label for="ewcms_5">&nbsp;第五行显示——<font color='red'>【引用图片、摘要】</font></label></td>
		    </tr>
		    <tr>
		      <td>&nbsp;</td>
		    </tr>
		  </table>
	    </div>
        <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
          <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0)"  onclick="$('#ewcms-cookies').window('close');return false;">关闭</a>
        </div>
      </div>
    </div>
  </body>
</html>