<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ page import="org.apache.shiro.authc.ExcessiveAttemptsException"%>
<%@ page import="org.apache.shiro.authc.IncorrectCredentialsException"%>
<%@ include file="/WEB-INF/views/jspf/taglibs.jspf" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
        <title>Ewcms用户登录</title>
        <link href="${ctx}/static/views/login.css"  type="text/css" rel="stylesheet"/>
	</head>
	<body id="userlogin_body">
    	<div></div>
		<div id="user_login">
			<dl>
		  		<dd id="user_top">
			  		<ul>
			    		<li class="user_top_l"></li>
			    		<li class="user_top_c"></li>
			    		<li class="user_top_r"></li>
			    	</ul>
		    	</dd>
				<form:form id="loginForm" action="login" method="post" class="form-horizontal">
			  		<dd id="user_main">
				  		<ul>
				    		<li class="user_main_l"></li>
				    		<li class="user_main_c">
				    			<div class="user_main_box">
				    				<ul>
				      					<li class="user_main_text">用户名：</li>
				      					<li class="user_main_input"><input type="text" id="username" name="username" value="${username}" class="txtusernamecssclass required span2" /></li>
				      				</ul>
				    				<ul>
				      					<li class="user_main_text">密&nbsp;&nbsp;&nbsp;&nbsp;码：</li>
				      					<li class="user_main_input"><input type="password" id="password"  name="password" class="txtpasswordcssclass required span2"/></li>
				      				</ul>
				    				<ul>
				      					<li class="user_main_text">验证码：</li>
				      					<li class="user_main_input">
				      						<input type="text" id="captcha" name="captcha" size="50" maxlength="4" class="txtvalidatecodecssclass required span2"/>
				        				</li>
	                            		<img id="id_checkcode" width="65px" height="20px" src="<c:url value='/checkcode.jpg'/>" alt="checkcode.jpg" title="看不清,换一张" style="padding-left: 5px;" />
				        			</ul>
				    				<ul>
				      					<li class="user_main_text"></li>
				      					<li class="user_main_input">
				      						<label class="checkbox inline" for="rememberMe"> <input type="checkbox" id="rememberMe" name="rememberMe"/> 记住我</label>
				        				</li>
				        			</ul>
									<ul>
										<li class="user_main_input">
												<%
												String error = (String) request.getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
												if(error != null){
												%>
												<span class="error">
												登录失败，请重试.
												</span>	
												<%
												}
												%>
		                            	</li>       			
	                            	</ul>			        			
			        			</div>
			        		</li>
			    		<li class="user_main_r">
			    			<input class="ibtnentercssclass" id="ibtnenter" style="border-top-width: 0px; border-left-width: 0px; border-bottom-width: 0px; border-right-width: 0px" type="image" src="${ctx}/static/image/login/user_botton.gif" name="ibtnenter"/> 
			    		</li>
			    	</ul>
		    	</dd>
		    	</form:form>
			    <dd id="user_bottom">
				  	<ul>
				    	<li class="user_bottom_l"></li>
				    	<li class="user_bottom_c"></li>
					    <li class="user_bottom_r"></li>
					</ul>
				</dd>
			</dl>
		</div>
        <script src="${ctx}/static/jquery/jquery-1.11.1.min.js" type="text/javascript"></script>
        <script src="${ctx}/static/views/login.js" type="text/javascript"></script>
		<script type="text/javascript">
			if(parent != self) {
				top.location = "<c:url value='/login'/>";
			}
			$(function() {
                var _login = new login("<c:url value = '/checkcode.jpg'/>");
                _login.init();
            });
		</script>
     </body>
</html>