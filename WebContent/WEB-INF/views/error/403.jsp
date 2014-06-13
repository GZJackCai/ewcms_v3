<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>访问拒绝</title>
        <style type="text/css">
        h1 {font-size: 120%}
        p {font-size: 13px;font-weight: bold;}
        ul li {font-size: 12px}
        em {font-family: sans-serif;font-style: normal;color:red;}
        </style>
    </head>
    <body>
        <h1>访问拒绝</h1>
        <p>访问指定的资源被拒绝，拒绝原因可能如下：</p>
        <ul>
            <li>你的<em>权限</em>不能访问该资源，请与系统管理员联系；</li><p/>
            <li>长时间未使用本系统致使登录失效，请<a href="${ctx}/logout">重新登录</a>。</li>
        </ul>
</body>
</html>
