<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<title>机构信息</title>	
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
	<form:form id="inputForm" action="${ctx}/site/organ/saveInfo" method="post" modelAttribute="organ" class="form-horizontal" enctype="multipart/form-data">
	  <fieldset>
		<table class="formtable">
		  <tr>
			<td width="20%">机构编号：</td>
			<td width="80%"><input type="text" id="id" name="id" readonly="readonly" value="${organ.id}"/></td>
		  </tr>
		  <tr>
			<td>机构名称：</td>
			<td><input type="text" id="name" name="name" readonly="readonly" value="${organ.name}" size="50"/></td>
		  </tr>
		  <tr>
			<td>创建时间：</td>
			<td><input type="text" id="createTime" name="createTime" readonly="readonly" value="${organ.createTime}" size="50"/></td>
		  </tr>
		  <tr>
			<td>修改时间：</td>
			<td><input type="text" id="updateTime" name="updateTime" readonly="readonly" value="${organ.updateTime}" size="50"/></td>
		  </tr>												
		  <tr>
			<td>机构引导图：</td>
			<td>
			  <input type="file" id="iconFile" name="iconFile" size="50"/>
			  <input type="button" value="查看" onclick="download()"/>
			</td>				
		  </tr>	
		  <tr>
			<td>机构描述：</td>
			<td><textarea cols="46" id="descripe" name="descripe">${organ.descripe}</textarea></td>				
		  </tr>
          <tr>
			<td>地址：</td>
			<td><input type="text" id="organInfo_address" name="organInfo.address" value="${organ.organInfo.address}" size="50"/></td>
		  </tr>
		  <tr>
			<td>邮编：</td>
			<td><input type="text" id="organInfo_postCode" name="organInfo.postCode" value="${organ.organInfo.postCode}" size="50"/></td>
		  </tr>
		  <tr>
			<td>交通方式：</td>
			<td><input type="text" id="organInfo_tranWay" name="organInfo.tranWay" value="${organ.organInfo.tranWay}" size="50"/></td>
		  </tr>
		  <tr>
			<td>联系电话：</td>
			<td><input type="text" id="organInfo_tel" name="organInfo.tel" value="${organ.organInfo.tel}" size="50"/></td>
		  </tr>	
		  <tr>
			<td>上班时间：</td>
		    <td><input type="text" id="organInfo_serviceTime" name="organInfo.serviceTime" value="${organ.organInfo.serviceTime}" size="50"/></td>
		  </tr>																							
		  <tr>
			<td>机构介绍：</td>
			<td><textarea cols="46" id="organInfo_describe" name="organInfo.describe">${organ.organInfo.describe}</textarea></td>				
		  </tr>			  <tr>
			<td colspan="2" style="padding:0;">
			  <div region="south" border="false" style="text-align:center;height:28px;line-height:28px;background-color:#f6f6f6">
				<a class="easyui-linkbutton" icon="icon-save" href="javascript:document.forms[0].submit();">保存</a>
				<a class="easyui-linkbutton" icon="icon-undo" href="javascript:document.forms[0].reset();">重置</a>
			  </div>								
			</td>
		  </tr>																													
		</table>	
	  </fieldset>
	  <input type="hidden" id="organInfo_id" name="organInfo.id" value="${organ.organInfo.id}"/>
	</form:form>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
	<script type="text/javascript">
		function download(){
			window.open('${ctx}/site/organ/download/${organ.id}');
		}
	</script>						
  </body>
</html>