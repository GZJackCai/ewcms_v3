<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>修改密码</title>
	<%@ include file="/WEB-INF/views/jspf/import-css.jspf" %>
    <link rel="stylesheet" type="text/css" href="${ctx}/static/views/security/account/password.css"/>
  </head>
  <body>
    <%@ include file="/WEB-INF/views/alertMessage.jsp" %>
    <form:form id="inputForm" action="${ctx}/security/user/savePassword" method="post" class="form-horizontal">
      <input type="hidden" id="loginName" name="loginName" value="<shiro:principal property="loginName"/>"/>
      <fieldset>
        <table class="formtable">
          <tr>
            <td width="20%">新密码:</td>
            <td width="80%">
              <input type="password" id="newPassword" name="newPassword" size="20" class="required" minlength="6" maxlength="16"/>
              <br/><span>6-16位字符，可以是半角字母、数字、“.”、“-”、“?”和下划线</span>
            </td>
          </tr>
          <tr>
            <td>确认密码:</td>
            <td><input type="password" id="passwordConfirm" name="passwordConfirm" size="20" equalTo="#newPassword"/></td>
          </tr>
        </table>
      </fieldset>
    </form:form>
	<div style="width:100%;height:16px;position:absolute;text-align:center;height:28px;line-height:28px;background-color:#f6f6f6;bottom:0px;left:0px;">
	  <a class="easyui-linkbutton" icon="icon-save" href="javascript:void(0);" onclick="javascript:$('#inputForm').submit();">提交</a>
	  <a class="easyui-linkbutton" icon="icon-cancel" href="javascript:void(0);" onclick="javascript:parent.$('#edit-window').window('close');">关闭</a>
	</div>
	<%@ include file="/WEB-INF/views/jspf/import-js.jspf" %>
    <script type="text/javascript" src="${ctx}/static/views/security/account/digitialspaghetti.password.min.js"></script>
    <script type="text/javascript">
        $(function(){
        	$('#newPassword').val('');
            $('input[name=newPassword]').pstrength({
                'displayMinChar': false,
                'minChar': 6,
                'colors': ["#f00", "#c06", "#f60", "#3c0", "#3f0"],
                'scores': [20, 30, 43, 50],
                'verdicts': ['弱', '一般', '较强', '强', '非常强']
            });
            $("#inputForm").validate({
    			messages: {
    				passwordConfirm: {
    		  			equalTo: "输入与上面相同的密码"
    				}
    	    	},
    	  		errorPlacement: function(error, element) {
    				error.insertAfter( element );
    	  		}
    		});
        });
    </script>
  </body>
</html>